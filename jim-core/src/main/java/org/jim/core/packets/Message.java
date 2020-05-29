/**
 * 
 */
package org.jim.core.packets;

import com.alibaba.fastjson.JSONObject;
import org.jim.core.utils.JsonKit;

import java.io.Serializable;
/**
 * 版本: [1.0]
 * 功能说明: 
 * @author : WChao 创建时间: 2017年7月26日 上午11:32:57
 */
public class Message implements Serializable{
	
	private static final long serialVersionUID = -6375331164604259933L;
	/**
	 * 消息创建时间
	 * new Date().getTime()
	 */
	protected Long createTime;
	/**
	 * 消息id，全局唯一
	 * UUIDSessionIdGenerator.instance.sessionId(null)
	 */
	protected String id ;
	/**
	 * 消息cmd命令码
	 */
	protected Integer cmd ;
	/**
	 * 扩展参数字段
	 */
	protected JSONObject extras;

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCmd() {
		return cmd;
	}

	public void setCmd(Integer cmd) {
		this.cmd = cmd;
	}

	public JSONObject getExtras() {
		return extras;
	}

	public void setExtras(JSONObject extras) {
		this.extras = extras;
	}

	public String toJsonString() {
		return JsonKit.toJSONString(this);
	}
	
	public byte[] toByte(){
		return JsonKit.toJsonBytes(this);
	}
	
	public abstract static class Builder<T extends Message , B extends Message.Builder<T,B>>{
		/**
		 * 消息创建时间
		 */
		protected Long createTime;
		/**
		 * 消息id，全局唯一
		 */
		protected String id ;
		/**
		 * 消息cmd命令;
		 */
		protected Integer cmd ;
		/**
		 * 扩展字段;
		 */
		protected JSONObject extras;

		private B theBuilder = this.getThis();

		/**
		 * 供子类获取自身builder抽象类;
		 * @return
		 */
		protected abstract B getThis();
		
		public B setCreateTime(Long createTime) {
			this.createTime = createTime;
			return theBuilder;
		}
		public B setId(String id) {
			this.id = id;
			return theBuilder;
		}
		public B setCmd(Integer cmd) {
			this.cmd = cmd;
			return theBuilder;
		}
		public B addExtra(String key , Object value) {
			 if (null == value) {
	                return theBuilder;
	         } else {
               if (null == extras) {
                   this.extras = new JSONObject();
               }
               this.extras.put(key, value);
               return theBuilder;
	         }
		}

		/**
		 * 供子类实现的抽象构建对象
		 * @return
		 */
		public abstract T build();
	}
}
