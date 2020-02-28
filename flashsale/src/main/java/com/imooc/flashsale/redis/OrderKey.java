package com.imooc.flashsale.redis;

public class OrderKey extends BasePrefix {

	private OrderKey(String prefix) {
		super(prefix);
	}

	// 页面缓存时间较短
	public static OrderKey getFlashSaleByUidGid = new OrderKey("fsug");

}
