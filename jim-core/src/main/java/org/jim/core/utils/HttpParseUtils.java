package org.jim.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 本类部分代码参考了voovan项目，不过为了适应需要，作了部分改动，感谢作者的贡献
 * 项目地址： http://www.voovan.org/
 * @author wchao
 * 2017年7月27日 上午10:09:19
 */
public class HttpParseUtils {
	//	private static Logger log = LoggerFactory.getLogger(HttpParseUtils.class);

	/**
	 * 【------------------------------------------------------------------------------------
	 * 以下代码参考了voovan项目，不过为了适应需要，作了部分改动，感谢作者的贡献
	 * 项目地址： http://www.voovan.org/
	 * ------  start  ------
	 */
	private static ConcurrentHashMap<Integer, Pattern> regexPattern = new ConcurrentHashMap<>();

	private static Pattern getCachedPattern(String regex) {
		Pattern pattern = null;
		if (regexPattern.containsKey(regex.hashCode())) {
			pattern = regexPattern.get(regex.hashCode());
		} else {
			pattern = Pattern.compile(regex);
			regexPattern.put(regex.hashCode(), pattern);
		}
		return pattern;
	}

	/**
	 * 解析字符串中的所有等号表达式成 Map
	 * @param str
	 *              等式表达式
	 * @return 等号表达式 Map
	 */
	public static Map<String, String> getEqualMap(String str) {
		Map<String, String> equalMap = new HashMap<>();
		String[] searchedStrings = searchByRegex(str, "([^ ;,]+=[^ ;,]+)");
		for (String groupString : searchedStrings) {
			//这里不用 split 的原因是有可能等号后的值字符串中出现等号
			String[] equalStrings = new String[2];
			int equalCharIndex = groupString.indexOf("=");
			equalStrings[0] = groupString.substring(0, equalCharIndex);
			equalStrings[1] = groupString.substring(equalCharIndex + 1, groupString.length());
			if (equalStrings.length == 2) {
				String key = equalStrings[0];
				String value = equalStrings[1];
				if (value.startsWith("\"") && value.endsWith("\"")) {
					value = value.substring(1, value.length() - 1);
				}
				equalMap.put(key, value);
			}
		}
		return equalMap;
	}

	/**
	 * <pre>
	 * 获取HTTP 头属性里等式的值
	 * 	可以从字符串 Content-Type: multipart/form-data; boundary=ujjLiiJBznFt70fG1F4EUCkIupn7H4tzm
	 * 	直接解析出boundary的值.
	 * 	使用方法:getPerprotyEqualValue(packetMap,"Content-Type","boundary")获得ujjLiiJBznFt70fG1F4EUCkIupn7H4tzm
	 * </pre>
	 * @param propertyName   属性名
	 * @param valueName      属性值
	 * @return
	 */
	public static String getPerprotyEqualValue(Map<String, String> packetMap, String propertyName, String valueName) {
		String propertyValueObj = packetMap.get(propertyName);
		if (propertyValueObj == null) {
			return null;
		}
		String propertyValue = propertyValueObj.toString();
		Map<String, String> equalMap = getEqualMap(propertyValue);
		return equalMap.get(valueName);
	}

	/**
	 * ------------------------------------------------------------------------------------】
	 * 以上代码参考了voovan项目，不过为了适应需要，作了部分改动，感谢作者的贡献
	 * 项目地址： http://www.voovan.org/
	 * ------  end  ------
	 */
	/**
	 * @param args
	 * @author wchao
	 */
	public static void main(String[] args) {

	}

	/**
	 * 正则表达式查找,匹配的被提取出来做数组
	 * @param source 目标字符串
	 * @param regex 正则表达式
	 * @return  匹配的字符串数组
	 */
	public static String[] searchByRegex(String source, String regex) {
		if (source == null) {
			return null;
		}

		Pattern pattern = getCachedPattern(regex);
		Matcher matcher = pattern.matcher(source);
		ArrayList<String> result = new ArrayList<>();
		while (matcher.find()) {
			result.add(matcher.group());
		}
		return result.toArray(new String[0]);
	}

	/**
	 *
	 * @author wchao
	 */
	public HttpParseUtils() {
	}
}
