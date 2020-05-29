/**
 * 
 */
package org.jim.core.cache.caffeine;

/**
 * @author mobo-pc
 *
 */
public class CaffeineConfig {
	
	private String cacheName;
	private Long  timeToLiveSeconds = 60L;
	private Long  timeToIdleSeconds = 60L;
	private Integer  maximumSize = 5000000;
	private Integer initialCapacity = 100;
	private boolean recordStats = false;
	
	public String getCacheName() {
		return cacheName;
	}
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	public Long getTimeToLiveSeconds() {
		return timeToLiveSeconds;
	}
	public void setTimeToLiveSeconds(Long timeToLiveSeconds) {
		this.timeToLiveSeconds = timeToLiveSeconds;
	}
	public Long getTimeToIdleSeconds() {
		return timeToIdleSeconds;
	}
	public void setTimeToIdleSeconds(Long timeToIdleSeconds) {
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
