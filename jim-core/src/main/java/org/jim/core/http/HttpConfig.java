package org.jim.core.http;

import org.jim.core.ImConst;
import org.jim.core.http.handler.IHttpRequestHandler;
import org.jim.core.http.listener.IHttpServerListener;
import org.jim.core.session.id.ISessionIdGenerator;
import org.tio.utils.cache.ICache;

/**
 * @author wchao
 * 2017年8月15日 下午1:21:14
 */
public class HttpConfig implements ImConst {
	/**
	 * IP地址
	 */
	protected String bindIp;
	/**
	 * 监听端口
	 */
	protected Integer bindPort = 80;
	/**
	 * 存放HttpSession对象的cacheName
	 */
	public static final String SESSION_CACHE_NAME = "jim-h-s";

	/**
	 * 存放sessionId的cookie name
	 */
	public static final String SESSION_COOKIE_NAME = "jimIxO";

	/**
	 * session默认的超时时间，单位：秒
	 */
	public static final long DEFAULT_SESSION_TIMEOUT = 30 * 60;

	/**
	 * 默认的静态资源缓存时间，单位：秒
	 */
	public static final int MAX_LIVE_TIME_OF_STATICS = 60 * 10;
	
	/**
	 * 文件上传时，boundary值的最大长度
	 */
	public static final int MAX_LENGTH_OF_BOUNDARY = 256;
	
	/**
	 * 文件上传时，头部的最大长度
	 */
	public static final int MAX_LENGTH_OF_MULTI_HEADER = 128;
	
	/**
	 * 文件上传时，体的最大长度
	 */
	public static final int MAX_LENGTH_OF_MULTI_BODY = 1024 * 1024 * 20;

	private String serverInfo = Http.SERVER_INFO;

	private String charset = Http.CHARSET_NAME;

	private ICache sessionStore = null;

	/**
	 * 存放HttpSession对象的cacheName

	 */
	private String sessionCacheName = SESSION_CACHE_NAME;

	/**
	 * session超时时间，单位：秒
	 */
	private Long sessionTimeout = DEFAULT_SESSION_TIMEOUT;

	private String sessionCookieName = SESSION_COOKIE_NAME;

	/**
	 * 静态资源缓存时间，如果小于等于0则不缓存，单位：秒
	 */
	private Integer maxLiveTimeOfStaticRes = MAX_LIVE_TIME_OF_STATICS;

	private String page404 = "/404.html";

	private String page500 = "/500.html";

	private ISessionIdGenerator sessionIdGenerator;
	
	private IHttpRequestHandler httpRequestHandler;
	
	private IHttpServerListener httpServerListener;

	/**
	 * 示例：
	 * 1、classpath中：page
	 * 2、绝对路径：/page
	 * //FileUtil.getAbsolutePath("page");//"/page";
	 */
	private String pageRoot = "page";
	/**
	 * mvc扫描包路径;
	 */
	private String[] scanPackages = null;

	public HttpConfig(IHttpRequestHandler httpRequestHandler, IHttpServerListener httpServerListener){
		setHttpRequestHandler(httpRequestHandler);
		setHttpServerListener(httpServerListener);
	}

	public static HttpConfig.Builder newBuilder(){
		return new HttpConfig.Builder();
	}

	public static class Builder{

		private String charset = Http.CHARSET_NAME;

		private ICache sessionStore;

		private Long sessionTimeout = DEFAULT_SESSION_TIMEOUT;

		/**
		 * 静态资源缓存时间，如果小于等于0则不缓存，单位：秒
		 */
		private Integer maxLiveTimeOfStaticRes = MAX_LIVE_TIME_OF_STATICS;

		private String page404 = "/404.html";

		private String page500 = "/500.html";

		private ISessionIdGenerator sessionIdGenerator;

		private IHttpRequestHandler httpRequestHandler;

		private IHttpServerListener httpServerListener;

		private String pageRoot = "page";

		private String[] scanPackages = null;

		public Builder charset(String charset){
			this.charset = charset;
			return this;
		}

		public Builder sessionStore(ICache sessionStore){
			this.sessionStore = sessionStore;
			return this;
		}

		public Builder sessionTimeout(Long sessionTimeout){
			this.sessionTimeout = sessionTimeout;
			return this;
		}

		public Builder maxLiveTimeOfStaticRes(int maxLiveTimeOfStaticRes){
			this.maxLiveTimeOfStaticRes = maxLiveTimeOfStaticRes;
			return this;
		}

		public Builder page404(String page404){
			this.page404 = page404;
			return this;
		}

		public Builder page500(String page500){
			this.page500 = page500;
			return this;
		}

		public Builder sessionIdGenerator(ISessionIdGenerator sessionIdGenerator){
			this.sessionIdGenerator = sessionIdGenerator;
			return this;
		}

		public Builder httpRequestHandler(IHttpRequestHandler httpRequestHandler){
			this.httpRequestHandler = httpRequestHandler;
			return this;
		}

		public Builder httpServerListener(IHttpServerListener httpServerListener){
			this.httpServerListener = httpServerListener;
			return this;
		}

		public Builder pageRoot(String pageRoot){
			this.pageRoot = pageRoot;
			return this;
		}

		public Builder scanPackages(String[] scanPackages){
			this.scanPackages = scanPackages;
			return this;
		}

		public HttpConfig build(){
			HttpConfig httpConfig = new HttpConfig(this.httpRequestHandler, this.httpServerListener);
			httpConfig.setCharset(this.charset);
			httpConfig.setSessionStore(this.sessionStore);
			httpConfig.setSessionTimeout(this.sessionTimeout);
			httpConfig.setMaxLiveTimeOfStaticRes(maxLiveTimeOfStaticRes);
			httpConfig.setPage404(page404);
			httpConfig.setPage500(page500);
			httpConfig.setSessionIdGenerator(sessionIdGenerator);
			httpConfig.setPageRoot(pageRoot);
			httpConfig.setScanPackages(scanPackages);
			return httpConfig;
		}
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public ICache getSessionStore() {
		return sessionStore;
	}

	public void setSessionStore(ICache sessionStore) {
		this.sessionStore = sessionStore;
	}

	public Long getSessionTimeout() {
		return sessionTimeout;
	}

	public int getMaxLiveTimeOfStaticRes() {
		return maxLiveTimeOfStaticRes;
	}

	public String getPage404() {
		return page404;
	}

	public void setPage404(String page404) {
		this.page404 = page404;
	}

	public String getPage500() {
		return page500;
	}

	public void setPage500(String page500) {
		this.page500 = page500;
	}

	public ISessionIdGenerator getSessionIdGenerator() {
		return sessionIdGenerator;
	}

	public void setSessionIdGenerator(ISessionIdGenerator sessionIdGenerator) {
		this.sessionIdGenerator = sessionIdGenerator;
	}

	public IHttpRequestHandler getHttpRequestHandler() {
		return httpRequestHandler;
	}

	public void setHttpRequestHandler(IHttpRequestHandler httpRequestHandler) {
		this.httpRequestHandler = httpRequestHandler;
	}

	public IHttpServerListener getHttpServerListener() {
		return httpServerListener;
	}

	public void setHttpServerListener(IHttpServerListener httpServerListener) {
		this.httpServerListener = httpServerListener;
	}

	public String getPageRoot() {
		return pageRoot;
	}

	public void setPageRoot(String pageRoot) {
		this.pageRoot = pageRoot;
	}

	public String[] getScanPackages() {
		return scanPackages;
	}

	public void setScanPackages(String[] scanPackages) {
		this.scanPackages = scanPackages;
	}

	public String getServerInfo() {
		return serverInfo;
	}

	public String getSessionCacheName() {
		return sessionCacheName;
	}

	public String getSessionCookieName() {
		return sessionCookieName;
	}

	public String getBindIp() {
		return bindIp;
	}

	public void setBindIp(String bindIp) {
		this.bindIp = bindIp;
	}

	public Integer getBindPort() {
		return bindPort;
	}

	public void setBindPort(Integer bindPort) {
		this.bindPort = bindPort;
	}

	public void setSessionTimeout(Long sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public void setMaxLiveTimeOfStaticRes(Integer maxLiveTimeOfStaticRes) {
		this.maxLiveTimeOfStaticRes = maxLiveTimeOfStaticRes;
	}
}
