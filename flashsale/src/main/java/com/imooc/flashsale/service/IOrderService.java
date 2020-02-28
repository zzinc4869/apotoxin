package com.imooc.flashsale.service;

import com.imooc.flashsale.domain.FlashSaleOrder;
import com.imooc.flashsale.domain.OrderInfo;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.vo.GoodsVo;
import com.imooc.flashsale.vo.OrderDetailVo;

public interface IOrderService {

	/**
	 * 查询订单
	 * 
	 * @param userId
	 * @param goodsId
	 * @return
	 */
	FlashSaleOrder getFlashSaleOrder(Integer userId, long goodsId);

	/**
	 * 创建订单 order_info flashsale_order
	 */
	OrderInfo createOrder(User user, GoodsVo goodsVo);

	/**
	 * 获得订单详情
	 * 
	 * @param orderId
	 * @return
	 */
	OrderDetailVo getOrderDetail(long orderId);

}
