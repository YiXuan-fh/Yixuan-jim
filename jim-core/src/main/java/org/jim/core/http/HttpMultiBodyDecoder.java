package org.jim.core.http;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jim.core.ImChannelContext;
import org.jim.core.exception.ImDecodeException;
import org.jim.core.utils.HttpParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.exception.LengthOverflowException;
import org.tio.core.utils.ByteBufferUtils;
import org.tio.utils.SystemTimer;

/**
 * @author wchao
 * 2017年7月26日 下午2:20:43
 */
public class HttpMultiBodyDecoder {
	public static class Header {
		private String contentDisposition = "form-data";
		private String name = null;
		private String filename = null;
		private String contentType = null;

		private Map<String, String> map = new HashMap<>();

		public String getContentDisposition() {
			return contentDisposition;
		}

		public String getContentType() {
			return contentType;
		}

		public String getFilename() {
			return filename;
		}

		public Map<String, String> getMap() {
			return map;
		}

		public String getName() {
			return name;
		}

		public void setContentDisposition(String contentDisposition) {
			this.contentDisposition = contentDisposition;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public void setMap(Map<String, String> map) {
			this.map = map;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 【
	 * Content-Disposition: form-data; name="uploadFile"; filename=""
	 * Content-Type: application/octet-stream
	 * 】
	 *
	 * 【
	 * Content-Disposition: form-data; name="end"
	 * 】
	 * @author wchao
	 * 2017年7月27日 上午10:18:01
	 */
	public  interface MultiBodyHeaderKey {
		String Content_Disposition = "Content-Disposition".toLowerCase();
		String Content_Type = "Content-Type".toLowerCase();
	}

	public  enum Step {
		BOUNDARY, HEADER, BODY, END
	}

	private static Logger log = LoggerFactory.getLogger(HttpMultiBodyDecoder.class);

	public static void decode(HttpRequest request, RequestLine firstLine, byte[] bodyBytes, String initBoundary, ImChannelContext channelContext) throws ImDecodeException {
		if (StringUtils.isBlank(initBoundary)) {
			throw new ImDecodeException("boundary is null");
		}

		long start = SystemTimer.currentTimeMillis();

		ByteBuffer buffer = ByteBuffer.wrap(bodyBytes);
		buffer.position(0);

		String boundary = "--" + initBoundary;
		String endBoundary = boundary + "--";
		Step step = Step.BOUNDARY;
		try {
			label1: while (true) {
				if (step == Step.BOUNDARY) {
					String line = ByteBufferUtils.readLine(buffer, request.getCharset(), HttpConfig.MAX_LENGTH_OF_BOUNDARY);
					if (boundary.equals(line)) {
						step = Step.HEADER;
						// 结束了
					} else if (endBoundary.equals(line))
					{
						break;
					} else {
						throw new ImDecodeException("line need:" + boundary + ", but is: " + line + "");
					}
				}

				Header multiBodyHeader = new Header();
				if (step == Step.HEADER) {
					List<String> lines = new ArrayList<>(2);
					label2: while (true) {
						String line = ByteBufferUtils.readLine(buffer, request.getCharset(), HttpConfig.MAX_LENGTH_OF_MULTI_HEADER);
						if ("".equals(line)) {
							break label2;
						} else {
							lines.add(line);
						}
					}

					parseHeader(lines, multiBodyHeader, channelContext);
					step = Step.BODY;
				}

				if (step == Step.BODY) {
					Step newParseStep = parseBody(multiBodyHeader, request, buffer, boundary, endBoundary, channelContext);
					step = newParseStep;

					if (step == Step.END) {
						break label1;
					}
				}

			}
		} catch (LengthOverflowException loe) {
			throw new ImDecodeException(loe);
		} catch (UnsupportedEncodingException e) {
			log.error(channelContext.toString(), e);
		} finally {
			long end = SystemTimer.currentTimeMillis();
			long iv = end - start;
			log.info("解析耗时:{}ms", iv);
		}

	}

	/**
	 * @param args
	 * @throws UnsupportedEncodingException
	 * @throws LengthOverflowException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException, LengthOverflowException {
		String testString = "hello\r\nddd\r\n";
		ByteBuffer buffer = ByteBuffer.wrap(testString.getBytes());

		String xString = ByteBufferUtils.readLine(buffer, "utf-8");
		System.out.println(xString);
		xString = ByteBufferUtils.readLine(buffer, "utf-8");
		System.out.println(xString);
	}

	/**
	 * 
	 * @param header
	 * @param request
	 * @param buffer
	 * @param boundary
	 * @param endBoundary
	 * @param channelContext
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws LengthOverflowException
	 * @author wchao
	 */
	public static Step parseBody(Header header, HttpRequest request, ByteBuffer buffer, String boundary, String endBoundary, ImChannelContext channelContext)
			throws UnsupportedEncodingException, LengthOverflowException {
		int initPosition = buffer.position();

		while (buffer.hasRemaining()) {
			String line = ByteBufferUtils.readLine(buffer, request.getCharset(), HttpConfig.MAX_LENGTH_OF_MULTI_BODY);
			boolean isEndBoundary = endBoundary.equals(line);
			boolean isBoundary = boundary.equals(line);
			if (isBoundary || isEndBoundary) {
				int startIndex = initPosition;
				int endIndex = buffer.position() - line.getBytes().length - 2 - 2;
				int length = endIndex - startIndex;
				byte[] dst = new byte[length];

				System.arraycopy(buffer.array(), startIndex, dst, 0, length);
				//该字段类型是file
				String filename = header.getFilename();
				if (filename != null)
				{
					if (!"".equals(filename)) {
						UploadFile uploadFile = new UploadFile();
						uploadFile.setName(filename);
						uploadFile.setData(dst);
						uploadFile.setSize(dst.length);
						request.addParam(header.getName(), uploadFile);
					}
				} else { //该字段是普通的key-value
					request.addParam(header.getName(), new String(dst, request.getCharset()));
				}
				if (isEndBoundary) {
					return Step.END;
				} else {
					return Step.HEADER;
				}
			}
		}
		return null;
	}

	/**
	 * 【
	 * Content-Disposition: form-data; name="uploadFile"; filename=""
	 * Content-Type: application/octet-stream
	 * 】
	 *
	 * 【
	 * Content-Disposition: form-data; name="end"
	 * 】
	 * @param lines
	 * @param header
	 * @author wchao
	 */
	public static void parseHeader(List<String> lines, Header header, ImChannelContext channelContext) throws ImDecodeException {
		if (lines == null || lines.size() == 0) {
			throw new ImDecodeException("multipart_form_data 格式不对，没有头部信息");
		}
		try {
			for (String line : lines) {
				String[] keyValue = StringUtils.split(line, ":");
				String key = StringUtils.lowerCase(StringUtils.trim(keyValue[0]));
				String value = StringUtils.trim(keyValue[1]);
				header.map.put(key, value);
			}

			String contentDisposition = header.map.get(MultiBodyHeaderKey.Content_Disposition);
			String name = HttpParseUtils.getPerprotyEqualValue(header.map, MultiBodyHeaderKey.Content_Disposition, "name");
			String filename = HttpParseUtils.getPerprotyEqualValue(header.map, MultiBodyHeaderKey.Content_Disposition, "filename");
			String contentType = header.map.get(MultiBodyHeaderKey.Content_Type);
			header.setContentDisposition(contentDisposition);
			header.setName(name);
			header.setFilename(filename);
			header.setContentType(contentType);

		} catch (Exception e) {
			log.error(channelContext.toString(), e);
			throw new ImDecodeException(e.toString());
		}
	}

	/**
	 *
	 */
	public HttpMultiBodyDecoder() {

	}

}
