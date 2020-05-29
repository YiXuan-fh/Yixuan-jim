package org.jim.core.cache.caffeine;

import java.util.Properties;
/**
 * @author WChao
 * @date 2018年3月9日 上午1:09:03
 */
public class CaffeineConfiguration {
	
	private String cacheName;
	private Integer  timeToLiveSeconds = 1800;
	private Integer  timeToIdleSeconds = 1800;
	private Integer  maximumSize = 5000000;
	private Integer initialCapacity = 10;
	private boolean recordStats = false;
	
	public CaffeineConfiguration(){}
	
	public CaffeineConfiguration(String cacheName,Properties prop){
		this.cacheName = cacheName;
		String[] values = prop.getProperty(cacheName,"5000000,1800").split(",");
		this.maximumSize = Integer.valueOf(values[0]);
		if(values.length>1){
			this.timeToLiveSeconds = Integer.valueOf(values[1]);
			this.timeToIdleSeconds = Integer.valueOf(values[1]);
		}
	}
	public String getCacheName() {
		return cacheName;
	}
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	public Integer getTimeToLiveSeconds() {
		return timeToLiveSeconds;
	}
	public void setTimeToLiveSeconds(Integer timeToLiveSeconds) {
		this.timeToLiveSeconds = timeToLiveSeconds;
	}
	public Integer getTimeToIdleSeconds() {
		return timeToIdleSeconds;
	}
	public void setTimeToIdleSeconds(Integer timeToIdleSeconds) {
		this.timeToIdleSeconds = timeToIdleSeconds;
	}
	public Integer getMaximumSize() {
		return maximumSize;
	}
	public void setMaximumSize(Integer maximumSize) {
		this.maximumSize = maximumSize;
	}
	public Integer getInitialCapacity() {
		return initialCapacity;
	}
	public void setInitialCapacity(Integer initialCapacity) {
		this.initialCapacity = initialCapacity;
	}
	public boolean isRecordStats() {
		return recordStats;
	}
	public void setRecordStats(boolean recordStats) {
		this.recordStats = recordStats;
	}
}
