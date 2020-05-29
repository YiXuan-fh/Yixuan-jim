package org.jim.core.cache;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author wchao
 * 2017年8月10日 上午11:38:26
 */
public interface ICache {

	/**
	 *
	 * 清空所有缓存
	 * @author wchao
	 */
	void clear();

	/**
	 * 根据key获取value
	 * @param key
	 * @return
	 * @author wchao
	 */
	public Serializable get(String key);
	
	/**
	 * 根据key获取value
	 * @param key
	 * @param clazz
	 * @return
	 * @author: wchao
	 */
	public <T> T get(String key, Class<T> clazz);

	/**
	 * 获取所有的key
	 * @return
	 * @author wchao
	 */
	Collection<String> keys();

	/**
	 * 将key value保存到缓存中
	 * @param key
	 * @param value
	 * @author wchao
	 */
	public void put(String key, Serializable value);

	/**
	 * 删除一个key
	 * @param key
	 * @return
	 * @author wchao
	 */
	public void remove(String key);

	/**
	 * 临时添加一个值，用于防止缓存穿透攻击
	 * @param key
	 * @param value
	 */
	void putTemporary(String key, Serializable value);
}
