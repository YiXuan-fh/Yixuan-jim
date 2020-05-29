/**
 * 
 */
package org.jim.core.cache.j2cache;

import java.io.Serializable;
import java.util.Collection;

import org.jim.core.cache.ICache;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;

/**
 * 红薯大大的j2cache
 * @author WChao
 */
public class J2Cache implements ICache {

	private String cacheName = null;

	/**
	 * 
	 */
	public J2Cache(String cacheName) {
		this.cacheName = cacheName;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	private static CacheChannel getChannel() {
		CacheChannel cache = net.oschina.j2cache.J2Cache.getChannel();
		return cache;
	}

	@Override
	public void clear() {
		CacheChannel cache = getChannel();
		cache.clear(cacheName);
	}

	@Override
	public Serializable get(String key) {
		CacheChannel cache = getChannel();
		CacheObject cacheObject = cache.get(cacheName, key);
		if (cacheObject != null) {
			return (Serializable) cacheObject.getValue();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key, Class<T> clazz) {
		Serializable ret = get(key);
		return (T) ret;
	}

	@Override
	public Collection<String> keys() {
		CacheChannel cache = getChannel();
		return cache.keys(cacheName);
	}

	@Override
	public void put(String key, Serializable value) {
		CacheChannel cache = getChannel();
		cache.set(cacheName, key, value);
	}

	@Override
	public void remove(String key) {
		CacheChannel cache = getChannel();
		cache.evict(cacheName, key);
	}

	@Override
	public void putTemporary(String key, Serializable value) {
		throw new RuntimeException("不支持防缓存穿透");
	}

	public long ttl(String key) {
		throw new RuntimeException("不支持ttl");
	}

}
