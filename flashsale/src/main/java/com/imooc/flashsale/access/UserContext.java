package com.imooc.flashsale.access;

import com.imooc.flashsale.domain.User;

public class UserContext {

	// 绑定当前线程，在多线程的情况下属于线程安全
	private static ThreadLocal<User> userHolder = new ThreadLocal<User>();

	public static void setUser(User user) {
		userHolder.set(user);
	}

	public static User getUser() {
		return userHolder.get();
	}

}
