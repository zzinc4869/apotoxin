package com.imooc.flashsale.redis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.flashsale.util.BeanUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
 * 简单封装redis各项操作，不使用redis原生api
 *
 */
@Service
public class RedisService {

	// 从连接池中获取jedis
	@Autowired
	private JedisPool jedisPool;

	/**
	 * 根据key获取特定类型的value
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
		Jedis jedis = null;
		try {
			// 从连接池中获取jedis
			jedis = jedisPool.getResource();
			// 生成真正的key
			String realKey = prefix.getPrefix() + key;
			String valueStr = jedis.get(realKey);
			// 通过jedis获取的value为String类型，需要将其转换为object
			T t = BeanUtil.stringToBean(valueStr, clazz);
			return t;
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 设置key-value键值对
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public <T> boolean set(KeyPrefix prefix, String key, T value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String valueStr = BeanUtil.beanToString(value);
			if (valueStr == null || valueStr.length() <= 0) {
				return false;
			}
			// 生成真正的key
			String realKey = prefix.getPrefix() + key;
			int seconds = prefix.getExpireSeconds();
			if (seconds <= 0) {
				jedis.set(realKey, valueStr);
			} else {
				jedis.setex(realKey, seconds, valueStr);
			}
			return true;
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 判断key是否存在
	 */
	public <T> boolean exist(KeyPrefix prefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			// 生成真正的key
			String realKey = prefix.getPrefix() + key;
			return jedis.exists(realKey);
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * Redis Incr 命令将 key 中储存的数字值增一。 如果 key 不存在,那么 key 的值会先被初始化为 0 ,然后再执行 INCR
	 */
	public <T> Long incr(KeyPrefix prefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			// 生成真正的key
			String realKey = prefix.getPrefix() + key;
			return jedis.incr(realKey);
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * Redis Decr 命令将 key 中储存的数字值减一。如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
	 */
	public <T> Long decr(KeyPrefix prefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			// 生成真正的key
			String realKey = prefix.getPrefix() + key;
			return jedis.decr(realKey);
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 删除key
	 */
	public boolean delete(KeyPrefix prefix, String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			// 生成真正的key
			String realKey = prefix.getPrefix() + key;
			long ret = jedis.del(realKey);
			return ret > 0;
		} finally {
			returnToPool(jedis);
		}
	}

	public boolean delete(KeyPrefix prefix) {
		if (prefix == null) {
			return false;
		}
		List<String> keys = scanKeys(prefix.getPrefix());
		if (keys == null || keys.size() <= 0) {
			return true;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(keys.toArray(new String[0]));
			return true;
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public List<String> scanKeys(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> keys = new ArrayList<String>();
			String cursor = "0";
			ScanParams sp = new ScanParams();
			sp.match("*" + key + "*");
			sp.count(100);
			do {
				ScanResult<String> ret = jedis.scan(cursor, sp);
				List<String> result = ret.getResult();
				if (result != null && result.size() > 0) {
					keys.addAll(result);
				}
				// 再处理cursor
				cursor = ret.getStringCursor();
			} while (!cursor.equals("0"));
			return keys;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 返回连接池
	 * 
	 * @param jedis
	 */
	private void returnToPool(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

}
