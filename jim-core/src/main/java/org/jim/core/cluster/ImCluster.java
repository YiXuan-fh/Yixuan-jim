/**
 * 
 */
package org.jim.core.cluster;

/**
 *
 * @desc IM集群抽象类
 * @author WChao
 * @date 2020-04-29
 */
public abstract class ImCluster implements ICluster {
	/**
	 * IM集群配置
	 */
	protected ImClusterConfig clusterConfig;

	public ImCluster(ImClusterConfig clusterConfig){
		this.clusterConfig = clusterConfig;
	}
	public ImClusterConfig getClusterConfig() {
		return clusterConfig;
	}
}
