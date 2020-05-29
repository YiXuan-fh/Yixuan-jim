package org.jim.core.ws;

import org.tio.utils.hutool.Snowflake;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author WChao
 * 2017年6月5日 上午10:44:26
 */
public class WsUuid {
	private Snowflake snowflake;

	public WsUuid() {
		snowflake = new Snowflake(ThreadLocalRandom.current().nextInt(1, 30), ThreadLocalRandom.current().nextInt(1, 30));
	}

	public WsUuid(long workerId, long dataCenterId) {
		snowflake = new Snowflake(workerId, dataCenterId);
	}

	/**
	 * @return
	 * @author wchao
	 */
	public String uuid() {
		return snowflake.nextId() + "";
	}

}
