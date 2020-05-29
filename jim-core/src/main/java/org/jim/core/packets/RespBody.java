/**
 * 
 */
package org.jim.core.packets;

import java.io.Serializable;
import java.util.Objects;

import org.jim.core.Status;
import org.jim.core.utils.JsonKit;
/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年7月26日 上午11:31:48
 */
public class RespBody implements Serializable{
	
	protected static final long serialVersionUID = 1L;
	/**
	 * 响应状态码;
	 */
	protected Integer code;
	/**
	 * 响应状态信息提示;
	 */
	protected String msg;
	/**
	 * 响应cmd命令码;
	 */
	protected Command command;
	/**
	 * 响应数据;
	 */
	protected Object data;

	public RespBody(){}

	public RespBody(Command command){
		this.command = command;
	}

	public RespBody(Command command,Object data){
		this(command);
		this.data = data;
	}

	public RespBody(Command command , Status status){
		this(status);
		this.command = command;
	}

	public RespBody(Status status){
		if(Objects.nonNull(status)){
			this.code = status.getCode();
			this.msg = status.getMsg();
		}
	}

	public RespBody(Integer code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public RespBody setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public RespBody setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Command getCommand() {
		return command;
	}

	public RespBody setCommand(Command command) {
		this.command = command;
		return this;
	}

	public Object getData() {
		return data;
	}

	public RespBody setData(Object data) {
		this.data = data;
		return this;
	}

	@Override
	public String toString() {
		return JsonKit.toJSONEnumNoUsingName(this);
	}
	
	public byte[] toByte(){
		return JsonKit.toJSONBytesEnumNoUsingName(this);
	}

}
