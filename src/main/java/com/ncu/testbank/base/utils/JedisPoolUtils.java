package com.ncu.testbank.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtils {
	private static JedisPool pool = null;

	private static Logger log = Logger.getLogger("testBankLog");

	/**
	 * 构建redis连接池
	 * 
	 * @param ip
	 * @param port
	 * @return JedisPool
	 */
	public static JedisPool getPool() {
		Properties property = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("redis.properties");
		try {
			property.load(in);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxTotal个jedis实例，则此时pool的状态为exhausted(耗尽)。
			config.setMaxTotal(Integer.valueOf(property.getProperty("maxTotal")));
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(Integer.valueOf(property.getProperty("maxIdle")));
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(Long.valueOf(property
					.getProperty("maxWaitMillis")));
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			String host = property.getProperty("host");
			int port = Integer.valueOf(property.getProperty("port"));
			pool = new JedisPool(config, host, port);
		}
		return pool;
	}

	/**
	 * 返还到连接池
	 * 
	 * @param pool
	 * @param redis
	 */
	public static void returnResource(JedisPool pool, Jedis redis) {
		if (redis != null) {
			pool.returnResource(redis);
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		String value = null;

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}

		return value;
	}
}
