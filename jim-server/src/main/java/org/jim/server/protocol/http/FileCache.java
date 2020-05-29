package org.jim.server.protocol.http;

import java.util.Map;

/**
 * @author WChao
 * 2017年8月15日 下午5:44:52
 */
public class FileCache implements java.io.Serializable {

	private static final long serialVersionUID = 6517890350387789902L;

	/**
	 * @param args
	 * @author WChao
	 */
	public static void main(String[] args) {

	}

	//this.addHeader(HttpConst.ResponseHeaderKey.Content_Encoding, "gzip");
	private Map<String, String> headers = null;
	private long lastModified;

	private byte[] data;

	/**
	 *
	 * @author WChao
	 */
	public FileCache() {
	}

	public FileCache(Map<String, String> headers, long lastModified, byte[] data) {
		super();
		this.setHeaders(headers);
		this.lastModified = lastModified;
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

}
