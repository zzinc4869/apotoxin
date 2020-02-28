package com.imooc.flashsale.redis;

public class UserKey extends BasePrefix {

	private static final int TOKEN_EXPIRE_SECONDS = 3600 * 24 * 2;

	private UserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	public static UserKey token = new UserKey(TOKEN_EXPIRE_SECONDS, "token");
	public static UserKey getById = new UserKey(0, "id");

}
