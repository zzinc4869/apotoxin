package com.imooc.flashsale.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPoolFactory {

	// 获取redis配置
	@Autowired
	private RedisConfig redisConfig;

	@Bean
	public JedisPool jedisPoolFactory() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
		jedisPoolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
		jedisPoolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait());
		JedisPool jPool = new JedisPool(jedisPoolConfig, redisConfig.getHost(), redisConfig.getPort(),
				redisConfig.getTimeout(), redisConfig.getPassword(), 0);
		return jPool;

	}
}
