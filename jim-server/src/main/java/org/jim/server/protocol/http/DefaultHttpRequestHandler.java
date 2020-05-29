package org.jim.server.protocol.http;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import org.apache.commons.lang3.StringUtils;
import org.jim.core.ImConst;
import org.jim.core.exception.ImException;
import org.jim.core.http.*;
import org.jim.core.http.handler.IHttpRequestHandler;
import org.jim.core.http.listener.IHttpServerListener;
import org.jim.core.http.session.HttpSession;
import org.jim.server.protocol.http.mvc.Routes;
import org.jim.server.util.ClassUtils;
import org.jim.server.util.HttpResps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.utils.cache.guava.GuavaCache;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author WChao
 *
 */
public class DefaultHttpRequestHandler implements IHttpRequestHandler,ImConst.Http {
	private static Logger log = LoggerFactory.getLogger(DefaultHttpRequestHandler.class);

	/**
	 * 静态资源的CacheName
	 * key:   path 譬如"/index.html"
	 * value: FileCache
	 */
	private static final String STATIC_RES_CONTENT_CACHENAME = "TIO_HTTP_STATIC_RES_CONTENT";

	protected HttpConfig httpConfig;

	protected Routes routes = null;

	private IHttpServerListener httpServerListener;

	private GuavaCache staticResCache;

	/**
	 *
	 * @param httpConfig
	 * @author WChao
	 */
	public DefaultHttpRequestHandler(HttpConfig httpConfig) {
		this.httpConfig = httpConfig;

		if (httpConfig.getMaxLiveTimeOfStaticRes() > 0) {
			staticResCache = GuavaCache.register(STATIC_RES_CONTENT_CACHENAME, (long) httpConfig.getMaxLiveTimeOfStaticRes(), null);
		}
		this.setHttpServerListener(httpConfig.getHttpServerListener());
	}

	/**
	 *
	 * @param httpConfig
	 * @param routes
	 * @author WChao
	 */
	public DefaultHttpRequestHandler(HttpConfig httpConfig, Routes routes) {
		this(httpConfig);
		this.routes = routes;
	}

	/**
	 * 创建httpSession
	 * @return
	 * @author WChao
	 */
	private HttpSession createSession() {
		String sessionId = httpConfig.getSessionIdGenerator().sessionId(httpConfig);
		HttpSession httpSession = new HttpSession(sessionId);
		return httpSession;
	}

	/**
	 * @return the httpConfig
	 */
	public HttpConfig getHttpConfig() {
		return httpConfig;
	}

	public IHttpServerListener getHttpServerListener() {
		return httpServerListener;
	}

	private Cookie getSessionCookie(HttpRequest request, HttpConfig httpConfig) throws ExecutionException {
		Cookie sessionCookie = request.getCookie(httpConfig.getSessionCookieName());
		return sessionCookie;
	}

	/**
	 * @return the staticResCache
	 */
	public GuavaCache getStaticResCache() {
		return staticResCache;
	}

	@Override
	public HttpResponse handler(HttpRequest request, RequestLine requestLine) throws ImException {
		HttpResponse ret = null;
		try {
			processCookieBeforeHandler(request, requestLine);
			HttpSession httpSession = request.getHttpSession();

			if (httpServerListener != null) {
				ret = httpServerListener.doBeforeHandler(request, requestLine, ret);
				if (ret != null) {
					return ret;
				}
			}

			String path = requestLine.getPath();
			String initPath = path;

			Method method = routes.pathMethodMap.get(initPath);
			if (method != null) {
				String[] paramNames = routes.methodParamnameMap.get(method);
				Class<?>[] parameterTypes = method.getParameterTypes();

				Object bean = routes.methodBeanMap.get(method);
				Object obj = null;
				Map<String, Object[]> params = request.getParams();
				if (parameterTypes == null || parameterTypes.length == 0) {
					obj = method.invoke(bean);
				} else {
					//赋值这段代码待重构，先用上
					Object[] paramValues = new Object[parameterTypes.length];
					int i = 0;
					for (Class<?> paramType : parameterTypes) {
						try {
							if (paramType.isAssignableFrom(HttpRequest.class)) {
								paramValues[i] = request;
							} else if (paramType == HttpSession.class) {
								paramValues[i] = httpSession;
							} else if (paramType.isAssignableFrom(HttpConfig.class)) {
								paramValues[i] = httpConfig;
							} else if (paramType.isAssignableFrom(ChannelContext.class)) {
								paramValues[i] = request.getImChannelContext();
							} else {
								if (params != null) {
									if (ClassUtils.isSimpleTypeOrArray(paramType)) {
										Object[] value = params.get(paramNames[i]);
										if (value != null && value.length > 0) {
											if (paramType.isArray()) {
												paramValues[i] = Convert.convert(paramType, value);
											} else {
												paramValues[i] = Convert.convert(paramType, value[0]);
											}
										}
									} else {
										//BeanUtil.mapToBean(params, paramType, true);
										paramValues[i] = paramType.newInstance();
										Set<Entry<String, Object[]>> set = params.entrySet();
										label2: for (Entry<String, Object[]> entry : set) {
											String fieldName = entry.getKey();
											Object[] fieldValue = entry.getValue();

											PropertyDescriptor propertyDescriptor = BeanUtil.getPropertyDescriptor(paramType, fieldName, true);
											if (propertyDescriptor == null) {
												continue label2;
											} else {
												Method writeMethod = propertyDescriptor.getWriteMethod();
												if (writeMethod == null) {
													continue label2;
												}
												writeMethod = ClassUtil.setAccessible(writeMethod);
												Class<?>[] clazzes = writeMethod.getParameterTypes();
												if (clazzes == null || clazzes.length != 1) {
													log.info("方法的参数长度不为1，{}.{}", paramType.getName(), writeMethod.getName());
													continue label2;
												}
												Class<?> clazz = clazzes[0];

												if (ClassUtils.isSimpleTypeOrArray(clazz)) {
													if (fieldValue != null && fieldValue.length > 0) {
														if (clazz.isArray()) {
															writeMethod.invoke(paramValues[i], Convert.convert(clazz, fieldValue));
														} else {
															writeMethod.invoke(paramValues[i], Convert.convert(clazz, fieldValue[0]));
														}
													}
												}
											}
										}
									}
								}
							}
						} catch (Exception e) {
							log.error(e.toString(), e);
						} finally {
							i++;
						}
					}
					obj = method.invoke(bean, paramValues);
				}

				if (obj instanceof HttpResponse) {
					ret = (HttpResponse) obj;
					return ret;
				} else {
					throw new Exception(bean.getClass().getName() + "#" + method.getName() + "返回的对象不是" + HttpResponse.class.getName());
				}
			} else {
				GuavaCache contentCache = null;
				FileCache fileCache = null;
				if (httpConfig.getMaxLiveTimeOfStaticRes() > 0) {
					contentCache = GuavaCache.getCache(STATIC_RES_CONTENT_CACHENAME);
					fileCache = (FileCache) contentCache.get(initPath);
				}
				if (fileCache != null) {
					byte[] bodyBytes = fileCache.getData();
					Map<String, String> headers = fileCache.getHeaders();
					long lastModified = fileCache.getLastModified();
					log.info("从缓存获取:[{}], {}", path, bodyBytes.length);

					ret = HttpResps.try304(request, lastModified);
					if (ret != null) {
						ret.addHeader(ResponseHeaderKey.tio_from_cache, "true");

						return ret;
					}

					ret = new HttpResponse(request, httpConfig);
					ret.setBody(bodyBytes, request);
					ret.addHeaders(headers);
					return ret;
				} else {
					String root = FileUtil.getAbsolutePath(httpConfig.getPageRoot());
					File file = new File(root + path);
					if (!file.exists() || file.isDirectory()) {
						if (StringUtils.endsWith(path, "/")) {
							path = path + "index.html";
						} else {
							path = path + "/index.html";
						}
						file = new File(root, path);
					}

					if (file.exists()) {
						ret = HttpResps.file(request, file);
						ret.setStaticRes(true);

						if (contentCache != null && request.getIsSupportGzip()) {
							if (ret.getBody() != null && ret.getStatus() == HttpResponseStatus.C200) {
								String contentType = ret.getHeader(ResponseHeaderKey.Content_Type);
								String contentEncoding = ret.getHeader(ResponseHeaderKey.Content_Encoding);
								String lastModified = ret.getHeader(ResponseHeaderKey.Last_Modified);

								Map<String, String> headers = new HashMap<>();
								if (StringUtils.isNotBlank(contentType)) {
									headers.put(ResponseHeaderKey.Content_Type, contentType);
								}
								if (StringUtils.isNotBlank(contentEncoding)) {
									headers.put(ResponseHeaderKey.Content_Encoding, contentEncoding);
								}
								if (StringUtils.isNotBlank(lastModified)) {
									headers.put(ResponseHeaderKey.Last_Modified, lastModified);
								}
								headers.put(ResponseHeaderKey.tio_from_cache, "true");

								fileCache = new FileCache(headers, file.lastModified(), ret.getBody());
								contentCache.put(initPath, fileCache);
								log.info("放入缓存:[{}], {}", initPath, ret.getBody().length);
							}
						}

						return ret;
					}
				}
			}

			ret = resp404(request, requestLine);//Resps.html(request, "404--并没有找到你想要的内容", httpConfig.getCharset());
			return ret;
		} catch (Exception e) {
			logError(request, requestLine, e);
			ret = resp500(request, requestLine, e);//Resps.html(request, "500--服务器出了点故障", httpConfig.getCharset());
			return ret;
		} finally {
			if (ret != null) {
				try {
					processCookieAfterHandler(request, requestLine, ret);
					if (httpServerListener != null) {
						httpServerListener.doAfterHandler(request, requestLine, ret);
					}
				} catch (Exception e) {
					logError(request, requestLine, e);
				}
			}
		}
	}

	private void logError(HttpRequest request, RequestLine requestLine, Exception e) {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n").append("remote  :").append(request.getRemote());
		sb.append("\r\n").append("request :").append(requestLine.getLine());
		log.error(sb.toString(), e);

	}

	private void processCookieAfterHandler(HttpRequest request, RequestLine requestLine, HttpResponse httpResponse) throws ExecutionException {
		HttpSession httpSession = request.getHttpSession();//(HttpSession) channelContext.getAttribute();//.getHttpSession();//not null
		Cookie cookie = getSessionCookie(request, httpConfig);
		String sessionId = null;

		if (cookie == null) {
			String domain = request.getHeader(RequestHeaderKey.Host);
			String name = httpConfig.getSessionCookieName();
			long maxAge = httpConfig.getSessionTimeout();
			sessionId = httpSession.getId();

			cookie = new Cookie(domain, name, sessionId, maxAge);
			httpResponse.addCookie(cookie);
			httpConfig.getSessionStore().put(sessionId, httpSession);
			if(log.isDebugEnabled()){
				log.info("{} 创建会话Cookie, {}", request.getImChannelContext(), cookie);
			}
		} else {
			sessionId = cookie.getValue();
			HttpSession httpSession1 = (HttpSession) httpConfig.getSessionStore().get(sessionId);

			if (httpSession1 == null) {//有cookie但是超时了
				sessionId = httpSession.getId();
				String domain = request.getHeader(RequestHeaderKey.Host);
				String name = httpConfig.getSessionCookieName();
				long maxAge = httpConfig.getSessionTimeout();
				//				maxAge = Long.MAX_VALUE; //把过期时间掌握在服务器端

				cookie = new Cookie(domain, name, sessionId, maxAge);
				httpResponse.addCookie(cookie);

				httpConfig.getSessionStore().put(sessionId, httpSession);
			}
		}
	}

	private void processCookieBeforeHandler(HttpRequest request, RequestLine requestLine) throws ExecutionException {
		Cookie cookie = getSessionCookie(request, httpConfig);
		HttpSession httpSession = null;
		if (cookie == null) {
			httpSession = createSession();
		} else {
			String sessionId = cookie.getValue();
			httpSession = (HttpSession) httpConfig.getSessionStore().get(sessionId);
			if (httpSession == null) {
				log.info("{} session【{}】超时", request.getImChannelContext(), sessionId);
				httpSession = createSession();
			}
		}
		request.setHttpSession(httpSession);
	}

	@Override
	public HttpResponse resp404(HttpRequest request, RequestLine requestLine) {
		String file404 = httpConfig.getPage404();
		String root = FileUtil.getAbsolutePath(httpConfig.getPageRoot());
		File file = new File(root + file404);
		if (file.exists()) {
			HttpResponse ret = HttpResps.redirect(request, file404 + "?tio_initpath=" + requestLine.getPathAndQuery());
			return ret;
		} else {
			HttpResponse ret = HttpResps.html(request, "404");
			return ret;
		}
	}

	@Override
	public HttpResponse resp500(HttpRequest request, RequestLine requestLine, Throwable throwable) {
		String file500 = httpConfig.getPage500();
		String root = FileUtil.getAbsolutePath(httpConfig.getPageRoot());
		File file = new File(root + file500);
		if (file.exists()) {
			HttpResponse ret = HttpResps.redirect(request, file500 + "?tio_initpath=" + requestLine.getPathAndQuery());
			return ret;
		} else {
			HttpResponse ret = HttpResps.html(request, "500");
			return ret;
		}
	}

	/**
	 * @param httpConfig the httpConfig to set
	 */
	public void setHttpConfig(HttpConfig httpConfig) {
		this.httpConfig = httpConfig;
	}

	public void setHttpServerListener(IHttpServerListener httpServerListener) {
		this.httpServerListener = httpServerListener;
	}

	/**
	 * @param staticResCache the staticResCache to set
	 */
	public void setStaticResCache(GuavaCache staticResCache) {
		this.staticResCache = staticResCache;
	}

	@Override
	public void clearStaticResCache(HttpRequest request) {
		if (staticResCache != null) {
			staticResCache.clear();
		}
	}

}
