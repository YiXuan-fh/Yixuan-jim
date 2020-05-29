package org.jim.server.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.utils.lock.ListWithLock;
import cn.hutool.core.io.FileUtil;

/**
 * 美女图
 * @author tanyaowu
 * 2017年5月14日 上午9:48:03
 */
public class ImgMnService {
	private static Logger log = LoggerFactory.getLogger(ImgMnService.class);

	public static final ListWithLock<String> imgListWithLock = new ListWithLock<>(new ArrayList<String>());

	public static final String dftimg = "http://images.rednet.cn/articleimage/2013/01/23/1403536948.jpg";

	public static final String filepath = "/page/imgs/mn.txt";

	public static final int maxSize = 100000;

	static {
		start();
	}
	/**
	 *
	 * @author: tanyaowu
	 */
	public ImgMnService() {

	}

	static AtomicInteger imgIndex = new AtomicInteger();

	public static String nextImg() {

		Lock lock = imgListWithLock.getLock().readLock();
		try {
			lock.lock();
			List<String> list = imgListWithLock.getObj();

			if (list.size() == 0) {
				return dftimg;
			}

			int index = imgIndex.incrementAndGet() % list.size();// RandomUtil.randomInt(0, list.size() - 1);
			log.debug("图片index:" + index);
			String imgsrc = list.get(index);
			if (StringUtils.isNotBlank(imgsrc)) {
				return imgsrc;
			}
			return nextImg();

		} catch (Exception e1) {
			log.error(e1.toString(), e1);
			return dftimg;
		} finally {
			lock.unlock();
		}

	}

	public static void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					work();
					try {
						Thread.sleep(60*1000 * 60); //多长时间爬一次
					} catch (Exception e) {
						log.error(e.toString(), e);
					}
				}
			}
		}, "get img url").start();

	}

	static String[] pags = new String[] {
			"http://www.mmonly.cc/wmtp/wmxz/list_27_1.html",  //唯美写真
			/*"http://www.mmonly.cc/wmtp/wmxz/list_27_2.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_3.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_4.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_5.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_6.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_7.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_8.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_9.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_10.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_11.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_12.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_13.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_14.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_15.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_16.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_17.html",  //唯美写真
			"http://www.mmonly.cc/wmtp/wmxz/list_27_18.html",  //唯美写真

			"http://www.mmonly.cc/wmtp/qltp/list_22_2.html",   //情侣图片*/
	 };

	public static void work() {
		String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		File file = new File(basePath+filepath);
		if (file.exists()) {
			List<String> list = FileUtil.readLines(file, "utf-8");
			if (list.size() >= 1000) {
				imgListWithLock.getObj().addAll(list);
				return;
			}
		}else{
			FileUtil.touch(file);//创建文件
		}

		List<String> list = new ArrayList<>();
		list.addAll(imgListWithLock.getObj());

		long sleeptime = 500;
		boolean isfirst = false;
		if (list.size() == 0) {
			isfirst = true;
			sleeptime = 1;
		}

		L1: for (String pag : pags) {
			try {
				Document doc = null;
				//创建页面对象
				doc = Jsoup.connect(pag).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36")
						.timeout(10000).get();
				//根据标签和class id获取元素
				Elements div = doc.select("div.ABox");
				//根据标签获取元素
				Elements pages = div.select("a");



				int count = 0;
				int invalidCount = 0;
				for (Element e : pages) {
					String page = e.attr("href");
					try {
						Document imgdoc = Jsoup.connect(page)
								.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36").timeout(10000)
								.get();
						Elements div22 = imgdoc.select(".totalpage");
						String totalpageStr = div22.get(0).text();
						int totalpage = Integer.parseInt(totalpageStr);
						if (totalpage > 0) {
							for (int i = 1; i <= totalpage; i++) {
								String url = page.substring(0, page.length() - 5) + "_" + i + ".html";
								try {
									Document imgpage = Jsoup.connect(url)
											.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36")
											.timeout(10000).get();
									Elements div2 = imgpage.select("#big-pic");
									Elements p = div2.select("p");
									Elements img = p.select("img");
									String src = img.attr("src");

									if (StringUtils.isBlank(src) || !StringUtils.startsWith(src, "http")) {
										continue;
									}

									if (isfirst) {
										boolean f = savefile(src);
										if (!f) {
											break L1;
										}
									} else {
										list.add(src);
										log.error("{}、【{}】", list.size(), src);
										while (list.size() > maxSize) {
											break L1;
//											list.remove(0);
//											log.error("删除一个元素后:{}", list.size());
										}
									}
									invalidCount++;

									count++;
									i++;
									Thread.sleep(sleeptime);
								} catch (Exception e1) {
									//log.error("【"+url+"】爬取异常!");
								}
							}
						}
					} catch (IOException e1) {
						//log.error("【"+page+"】爬取异常!");
					}
				}
				if (!isfirst) {
					savefile(list);
				}

				log.debug("抓取图片地址，打完收工，本次共找到:{}, 其中有效数据:{}，", count, invalidCount);



			} catch (Exception e) {
				log.error(e.toString(), e);
			}

		}

		List<String> list1 = imgListWithLock.getObj();
		FileUtil.writeLines(list1,file.getPath(), "utf-8");
	}

	public static void savefile(List<String> srcs) {
		WriteLock lock = imgListWithLock.getLock().writeLock();
		try {
			lock.lock();
			List<String> list = imgListWithLock.getObj();
			list.addAll(srcs);
		} catch (Exception e1) {
			log.error(e1.toString(), e1);
		} finally {
			lock.unlock();
		}
	}

	public static boolean savefile(String src) {
		WriteLock lock = imgListWithLock.getLock().writeLock();
		try {
			lock.lock();
			List<String> list = imgListWithLock.getObj();
			list.add(src);
			log.debug("{}、【{}】", list.size(), src);
			while (list.size() > maxSize) {
				return false;
			}

		} catch (Exception e1) {
			log.error(e1.toString(), e1);
		} finally {
			lock.unlock();
		}
		return true;
	}

	/**
	 * @param args
	 * @author: tanyaowu
	 */
	public static void main(String[] args) {
		start();
	}
}
