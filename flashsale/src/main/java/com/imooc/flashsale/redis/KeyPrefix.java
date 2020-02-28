package com.imooc.flashsale.redis;

public interface KeyPrefix {

	/**
	 * 获得过期时间
	 * 
	 * @return
	 */
	public int getExpireSeconds();

	/**
	 * 获得key的前缀，避免key重复
	 * 
	 * @return
	 */
	public String getPrefix();
}
