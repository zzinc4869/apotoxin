package com.imooc.flashsale.vo;

import com.imooc.flashsale.domain.OrderInfo;

public class OrderDetailVo {
	private GoodsVo goodsVo;
	private OrderInfo orderInfo;

	public GoodsVo getGoodsVo() {
		return goodsVo;
	}

	public void setGoodsVo(GoodsVo goodsVo) {
		this.goodsVo = goodsVo;
	}

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

}
