package com.imooc.flashsale.redis;

public abstract class BasePrefix implements KeyPrefix {

	private int expireSeconds;

	private String prefix;

	/**
	 * 默认0代表永不过期
	 */
	public BasePrefix(String prefix) {
		this(0, prefix);
	}

	public BasePrefix(int expireSeconds, String prefix) {
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	}

	public int getExpireSeconds() {
		return expireSeconds;
	}

	/**
	 * 生成一个key，避免key重复
	 */
	public String getPrefix() {
		String className = getClass().getSimpleName();
		return className + ":" + prefix;
	}

}
