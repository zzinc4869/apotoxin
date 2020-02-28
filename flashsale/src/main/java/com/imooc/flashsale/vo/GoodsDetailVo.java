package com.imooc.flashsale.vo;

import com.imooc.flashsale.domain.User;

public class GoodsDetailVo {
	private int saleStatus = 0;
	private int remainSeconds = 0;
	private GoodsVo goodsVo;
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getSaleStatus() {
		return saleStatus;
	}

	public void setSaleStatus(int saleStatus) {
		this.saleStatus = saleStatus;
	}

	public int getRemainSeconds() {
		return remainSeconds;
	}

	public void setRemainSeconds(int remainSeconds) {
		this.remainSeconds = remainSeconds;
	}

	public GoodsVo getGoodsVo() {
		return goodsVo;
	}

	public void setGoodsVo(GoodsVo goodsVo) {
		this.goodsVo = goodsVo;
	}

}
