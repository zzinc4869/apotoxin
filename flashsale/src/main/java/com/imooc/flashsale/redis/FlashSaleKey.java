package com.imooc.flashsale.redis;

public class FlashSaleKey extends BasePrefix {

	private FlashSaleKey(String prefix) {
		super(prefix);
	}

	private FlashSaleKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	public static FlashSaleKey isGoodsOver = new FlashSaleKey("go");
	public static FlashSaleKey getFlashSalePath = new FlashSaleKey(60, "gfsp");
	public static FlashSaleKey getFlashSaleVerifyCode = new FlashSaleKey(240, "gfsvc");

}
