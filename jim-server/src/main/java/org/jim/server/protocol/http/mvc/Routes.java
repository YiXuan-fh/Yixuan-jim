package org.jim.server.protocol.http.mvc;

import cn.hutool.core.util.ArrayUtil;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.ClassAnnotationMatchProcessor;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.MethodAnnotationMatchProcessor;
import org.apache.commons.lang3.StringUtils;
import org.jim.server.protocol.http.annotation.RequestPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.utils.json.Json;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WChao 
 * 2017年7月1日 上午9:05:30
 */
public class Routes {
	private static Logger log = LoggerFactory.getLogger(Routes.class);
//	private HttpServerConfig httpServerConfig = null;

//	private String[] scanPackages = null;

	/**
	 * 路径和对象映射
	 * key: /user
	 * value: object
	 */
	public Map<String, Object> pathBeanMap = new HashMap<>();
	
	/**
	 * 路径和class映射
	 * 只是用来打印的
	 * key: /user
	 * value: Class
	 */
	public Map<String, Class<?>> pathClassMap = new HashMap<>();

	/**
	 * 路径和class映射
	 * key: class
	 * value: /user
	 */
	public Map<Class<?>, String> classPathMap = new HashMap<>();

	/**
	 * Method路径映射
	 * key: /user/update
	 * value: method
	 */
	public Map<String, Method> pathMethodMap = new HashMap<>();
	/**
	 * Method路径映射
	 * 只是用于打印日志
	 * key: /user/update
	 * value: method string
	 */
	public Map<String, String> pathMethodstrMap = new HashMap<>();

	/**
	 * 方法参数名映射
	 * key: method
	 * value: ["id", "name", "scanPackages"]
	 */
	public Map<Method, String[]> methodParamnameMap = new HashMap<>();

	/**
	 * 方法和对象映射
	 * key: method
	 * value: bean
	 */
	public Map<Method, Object> methodBeanMap = new HashMap<>();

	/**
	 * 
	 * @author: WChao
	 */
	public Routes(String[] scanPackages) {
//		this.scanPackages = scanPackages;
		
		if (scanPackages != null) {
			final FastClasspathScanner fastClasspathScanner = new FastClasspathScanner(scanPackages);
//			fastClasspathScanner.verbose();
			fastClasspathScanner.matchClassesWithAnnotation(RequestPath.class, new ClassAnnotationMatchProcessor() {
				@Override
				public void processMatch(Class<?> classWithAnnotation) {
					try {
						Object bean = classWithAnnotation.newInstance();
						RequestPath mapping = classWithAnnotation.getAnnotation(RequestPath.class);
						String beanPath = mapping.value();
//						if (!StringUtils.endsWith(beanUrl, "/")) {
//							beanUrl = beanUrl + "/";
//						}

						beanPath = formateBeanPath(beanPath);
						
						Object obj = pathBeanMap.get(beanPath);
						if (obj != null) {
							log.error("mapping[{}] already exists in class [{}]", beanPath, obj.getClass().getName());
						} else {
							pathBeanMap.put(beanPath, bean);
							pathClassMap.put(beanPath, classWithAnnotation);
							classPathMap.put(classWithAnnotation, beanPath);
						}
					} catch (Exception e) {
						
						log.error(e.toString(), e);
					}
				}
			});
			
			fastClasspathScanner.matchClassesWithMethodAnnotation(RequestPath.class, new MethodAnnotationMatchProcessor(){
				@Override
				public void processMatch(Class<?> matchingClass, Executable matchingMethodOrConstructor) {
//					log.error(matchingMethodOrConstructor + "");
					RequestPath mapping = matchingMethodOrConstructor.getAnnotation(RequestPath.class);
					
					String methodName = matchingMethodOrConstructor.getName();

					
					String methodPath = mapping.value();
					methodPath = formateMethodPath(methodPath);
					String beanPath = classPathMap.get(matchingClass);
					
					if (StringUtils.isBlank(beanPath)) {
						log.error("方法有注解，但类没注解, method:{}, class:{}", methodName, matchingClass);
						return;
					}
					
					Object bean = pathBeanMap.get(beanPath);
					String completeMethodPath = methodPath;
					if (beanPath != null) {
						completeMethodPath = beanPath + methodPath;
					}
					
					Class<?>[] parameterTypes = matchingMethodOrConstructor.getParameterTypes();
					Method method;
					try {
						method = matchingClass.getMethod(methodName, parameterTypes);
						
						Paranamer paranamer = new BytecodeReadingParanamer();
						String[] parameterNames = paranamer.lookupParameterNames(method, false); // will return null if not found
						
						Method checkMethod = pathMethodMap.get(completeMethodPath);
						if (checkMethod != null) {
							log.error("mapping[{}] already exists in method [{}]", completeMethodPath, checkMethod.getDeclaringClass() + "#" + checkMethod.getName());
							return;
						}
						
						pathMethodMap.put(completeMethodPath, method);
						pathMethodstrMap.put(completeMethodPath, matchingClass.getName() + "." + method.getName() + "(" + ArrayUtil.join(parameterNames, ",") + ")");
						methodParamnameMap.put(method, parameterNames);
						methodBeanMap.put(method, bean);
					} catch (Exception e) {
						log.error(e.toString(), e);
					} 
				}
			});
			
			fastClasspathScanner.scan();
			
			log.debug("class  mapping\r\n{}", Json.toFormatedJson(pathClassMap));
//			log.info("classPathMap scan result :\r\n {}\r\n", Json.toFormatedJson(classPathMap));
			log.debug("method mapping\r\n{}", Json.toFormatedJson(pathMethodstrMap));
//			log.info("methodParamnameMap scan result :\r\n {}\r\n", Json.toFormatedJson(methodParamnameMap));
		}
	}
	
	/**
	 * 格式化成"/user","/"这样的路径
	 * @param initPath
	 * @return
	 * @author: WChao
	 */
	private static String formateBeanPath(String initPath){
		if (StringUtils.isBlank(initPath)) {
			return "/";
		}
		initPath = StringUtils.replaceAll(initPath, "//", "/");
		if (!StringUtils.startsWith(initPath, "/")) {
			initPath = "/" + initPath;
		}
		
		if (StringUtils.endsWith(initPath, "/")) {
			initPath = initPath.substring(0, initPath.length() - 1);
		}
		return initPath;
	}
	
	private static String formateMethodPath(String initPath){
		if (StringUtils.isBlank(initPath)) {
			return "/";
		}
		initPath = StringUtils.replaceAll(initPath, "//", "/");
		if (!StringUtils.startsWith(initPath, "/")) {
			initPath = "/" + initPath;
		}
		
		return initPath;
	}

	/**
	 * @param args
	 * @author: WChao
	 */
	public static void main(String[] args) {

	}
}
