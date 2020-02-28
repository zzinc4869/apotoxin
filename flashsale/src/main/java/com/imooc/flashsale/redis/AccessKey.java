package com.imooc.flashsale.redis;

public class AccessKey extends BasePrefix {

	private AccessKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	public static AccessKey access = new AccessKey(5, "AKaccess");

	public static AccessKey withExpire(int expireSeconds) {
		return new AccessKey(expireSeconds, "access");
	}

}