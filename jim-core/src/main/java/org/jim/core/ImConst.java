package org.jim.core;

/**
 * 
 * @author wchao 
 *
 */
public interface ImConst
{

	interface Key{
		String IM_CHANNEL_CONTEXT_KEY = "im_channel_context_key";
		String IM_CHANNEL_SESSION_CONTEXT_KEY = "im_channel_session_context_key";
		/**
		 * 存放HttpConfig
		 */
		String HTTP_SERVER_CONFIG = "JIM_HTTP_SERVER_CONFIG";
	}

	interface Topic{
		String REDIS_CLUSTER_TOPIC_SUFFIX = "REDIS_";
		String JIM_CLUSTER_TOPIC = "JIM_CLUSTER";
	}

	interface Protocol{
		/**
		 * 心跳字节
		 */
		byte HEARTBEAT_BYTE = -128;

		/**
		 * 握手字节
		 */
		byte HANDSHAKE_BYTE = -127;

		/**
		 * 协议版本号
		 */
		byte VERSION = 0x01;

		String WEB_SOCKET = "ws";

		String HTTP = "http";

		String TCP = "tcp";

		String UNKNOWN = "unknown";

		String COOKIE_NAME_FOR_SESSION = "jim-s";
		/**
		 * 消息体最多为多少,只支持多少M数据
		 */
		int MAX_LENGTH_OF_BODY = (int) (1024 * 1024 * 2.1);

		/**
		 * 消息头最少为多少个字节,1+1+2+(2+4)
		 */
		int LEAST_HEADER_LENGTH = 4;

		/**
		 * 加密标识位mask，1为加密，否则不加密
		 */
		byte FIRST_BYTE_MASK_ENCRYPT = -128;

		/**
		 * 压缩标识位mask，1为压缩，否则不压缩
		 */
		byte FIRST_BYTE_MASK_COMPRESS = 0B01000000;

		/**
		 * 是否有同步序列号标识位mask，如果有同步序列号，则消息头会带有同步序列号，否则不带
		 */
		byte FIRST_BYTE_MASK_HAS_SYNSEQ = 0B00100000;

		/**
		 * 是否是用4字节来表示消息体的长度
		 */
		byte FIRST_BYTE_MASK_4_BYTE_LENGTH = 0B00010000;

		/**
		 * 版本号mask
		 */
		byte FIRST_BYTE_MASK_VERSION = 0B00001111;
	}

	interface Http{
		/**
		 * 请求体的格式
		 * @author wchao
		 * 2017年6月28日 上午10:03:12
		 */
		enum RequestBodyFormat {
			URLENCODED, MULTIPART, TEXT
		}

		/**
	 	*Accept-Language : zh-CN,zh;q=0.8
		 Sec-WebSocket-Version : 13
		 Sec-WebSocket-Extensions : permessage-deflate; client_max_window_bits
		 Upgrade : websocket
		 Host : t-io.org:9321
		 Accept-Encoding : gzip, deflate, sdch
		 User-Agent : Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36
		 Origin : http://www.t-io.org:9292
		 Sec-WebSocket-Key : kmCL2C7q9vtNSMyHpft7lw==
		 Connection : Upgrade
		 Cache-Control : no-cache
		 Pragma : no-cache
		 * @author wchao
		 * 2017年5月27日 下午2:11:57
		 */
		 interface RequestHeaderKey {
			String Cookie = "Cookie".toLowerCase();//Cookie: $Version=1; Skin=new;
			String Origin = "Origin".toLowerCase(); //http://127.0.0.1
			String Sec_WebSocket_Key = "Sec-WebSocket-Key".toLowerCase(); //2GFwqJ1Z37glm62YKKLUeA==
			String Cache_Control = "Cache-Control".toLowerCase(); //no-cache
			String Connection = "Connection".toLowerCase(); //Upgrade,  keep-alive
			String User_Agent = "User-Agent".toLowerCase(); //Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3088.3 Safari/537.36
			String Sec_WebSocket_Version = "Sec-WebSocket-Version".toLowerCase(); //13
			String Host = "Host".toLowerCase(); //127.0.0.1:9321
			String Pragma = "Pragma".toLowerCase(); //no-cache
			String Accept_Encoding = "Accept-Encoding".toLowerCase(); //gzip, deflate, br
			String Accept_Language = "Accept-Language".toLowerCase(); //zh-CN,zh;q=0.8,en;q=0.6
			String Upgrade = "Upgrade".toLowerCase(); //websocket
			String Sec_WebSocket_Extensions = "Sec-WebSocket-Extensions".toLowerCase(); //permessage-deflate; client_max_window_bits
			String Content_Length = "Content-Length".toLowerCase(); //65
			String Content_Type = "Content-Type".toLowerCase();// : 【application/x-www-form-urlencoded】【application/x-www-form-urlencoded; charset=UTF-8】【multipart/form-data; boundary=----WebKitFormBoundaryuwYcfA2AIgxqIxA0 】
			String If_Modified_Since = "If-Modified-Since".toLowerCase(); //与Last-Modified配合
			/**
			 * 值为XMLHttpRequest则为Ajax
			 */
			String X_Requested_With = "X-Requested-With".toLowerCase();//XMLHttpRequest
		}

		/**
		 *
		 * @author wchao
		 * 2017年6月27日 下午8:23:58
		 */
		 interface RequestHeaderValue {
			 interface Connection {
				String keep_alive = "keep-alive".toLowerCase();
				String Upgrade = "Upgrade".toLowerCase();
				String close = "close".toLowerCase();
			}

			//application/x-www-form-urlencoded、multipart/form-data、text/plain
			 interface Content_Type {
				/**
				 * 普通文本，一般会是json或是xml
				 */
				String text_plain = "text/plain".toLowerCase();
				/**
				 * 文件上传
				 */
				String multipart_form_data = "multipart/form-data".toLowerCase();
				/**
				 * 普通的key-value
				 */
				String application_x_www_form_urlencoded = "application/x-www-form-urlencoded".toLowerCase();
			}
		}

		 interface ResponseHeaderKey {
			String Set_Cookie = "Set-Cookie".toLowerCase(); //Set-Cookie: UserID=JohnDoe; Max-Age=3600; Version=1
			String Content_Length = "Content-Length".toLowerCase(); //65

			String Connection = "Connection".toLowerCase(); //Upgrade,  keep-alive
			String Keep_Alive = "Keep-Alive".toLowerCase(); //Keep-Alive:timeout=20
			String Sec_WebSocket_Accept = "Sec-WebSocket-Accept".toLowerCase();
			String Upgrade = "Upgrade".toLowerCase();

			/**
			 * Content-Disposition: attachment;filename=FileName.txt
			 * 文件下载
			 */
			String Content_disposition = "Content-disposition".toLowerCase();
			/**
			 * 文档的编码（Encode）方法。只有在解码之后才可以得到Content-Type头指定的内容类型。
			 * 利用gzip压缩文档能够显著地减少HTML文档的下载时间。
			 * Java的GZIPOutputStream可以很方便地进行gzip压缩，但只有Unix上的Netscape和Windows上的IE 4、IE 5才支持它。
			 * 因此，Servlet应该通过查看Accept-Encoding头（即request.getHeader("Accept-Encoding")）检查浏览器是否支持gzip，
			 * 为支持gzip的浏览器返回经gzip压缩的HTML页面，为其他浏览器返回普通页面。
			 */
			String Content_Encoding = "Content-Encoding".toLowerCase();
			/**
			 * 表示后面的文档属于什么MIME类型。Servlet默认为text/plain，但通常需要显式地指定为text/html。
			 * 由于经常要设置Content-Type，因此HttpServletResponse提供了一个专用的方法setContentType。
			 */
			String Content_Type = "Content-Type".toLowerCase();
			/**
			 * 当前的GMT时间。你可以用setDateHeader来设置这个头以避免转换时间格式的麻烦。
			 */
			String Date = "Date".toLowerCase();
			/**
			 * 应该在什么时候认为文档已经过期，从而不再缓存它？
			 */
			String Expires = "Expires".toLowerCase();
			/**
			 * 文档的最后改动时间。客户可以通过If-Modified-Since请求头提供一个日期，该请求将被视为一个条件GET，
			 * 只有改动时间迟于指定时间的文档才会返回，否则返回一个304（Not Modified）状态。Last-Modified也可用setDateHeader方法来设置。
			 */
			String Last_Modified = "Last-Modified".toLowerCase();
			/**
			 * 表示客户应当到哪里去提取文档。Location通常不是直接设置的，而是通过HttpServletResponse的sendRedirect方法，该方法同时设置状态代码为302。
			 */
			String Location = "Location".toLowerCase();
			/**
			 * 表示浏览器应该在多少时间之后刷新文档，以秒计。除了刷新当前文档之外，你还可以通过setHeader("Refresh", "5; URL=http://host/path")让浏览器读取指定的页面。
			 注意这种功能通常是通过设置HTML页面HEAD区的＜META HTTP-EQUIV="Refresh" CONTENT="5;URL=http://host/path"＞实现，这是因为，自动刷新或重定向对于那些不能使用CGI或Servlet的HTML编写者十分重要。但是，对于Servlet来说，直接设置Refresh头更加方便。

			 注意Refresh的意义是"N秒之后刷新本页面或访问指定页面"，而不是"每隔N秒刷新本页面或访问指定页面"。因此，连续刷新要求每次都发送一个Refresh头，而发送204状态代码则可以阻止浏览器继续刷新，不管是使用Refresh头还是＜META HTTP-EQUIV="Refresh" ...＞。

			 注意Refresh头不属于HTTP 1.1正式规范的一部分，而是一个扩展，但Netscape和IE都支持它。
			 */
			String Refresh = "Refresh".toLowerCase();
			/**
			 * 服务器名字。Servlet一般不设置这个值，而是由Web服务器自己设置。
			 */
			String Server = "Server".toLowerCase();

			/**
			 *
			 */
			String Access_Control_Allow_Origin = "Access-Control-Allow-Origin".toLowerCase(); //value: *

			/**
			 *
			 */
			String Access_Control_Allow_Headers = "Access-Control-Allow-Headers".toLowerCase(); //value: x-requested-with,content-type

			/**
			 * 是否是从缓存中获取的数据，tio-httpserver特有的头部信息
			 */
			String tio_from_cache = "tio-from-cache";
		}

		/**
		 *
		 * @author wchao
		 * 2017年6月27日 下午8:24:02
		 */
		 interface ResponseHeaderValue {
			 interface Connection {
				String keep_alive = "keep-alive".toLowerCase();
				String Upgrade = "Upgrade".toLowerCase();
				String close = "close".toLowerCase();
			}
		}

		/**
		 *
		 */
		String SERVER_INFO = "jim-http-server/0.0.1";

		/**
		 * 默认规定连接到本服务器的客户端统一用utf-8
		 */
		String CHARSET_NAME = "utf-8";
	}

	String AUTH_KEY = "authKey";

	int SERVER_PORT = 8888;
	
	String CHARSET = "utf-8";
	
	String TO = "to";
	
	String CHANNEL = "channel";
	
	String STATUS = "status";
	
	String HTTP_REQUEST = "httpRequest";

	String STORE = "store";
	
	String PUSH = "push";
	
	String GROUP = "group";
	
	String USER = "user";
	
	String TERMINAL = "terminal";
	
	String INFO = "info";
	
	String FRIENDS = "friends";
	
	String JIM = "J-IM";

}
