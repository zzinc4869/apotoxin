package com.imooc.flashsale.rabbitmq;

import com.imooc.flashsale.domain.User;

public class FlashSaleMessage {
	private User user;
	private Long goodsId;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	@Override
	public String toString() {
		return "FlashSaleMessage [user=" + user + ", goodsId=" + goodsId + "]";
	}
}
