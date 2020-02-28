package com.imooc.flashsale.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.flashsale.dao.FlashSaleOrderMapper;
import com.imooc.flashsale.dao.OrderInfoMapper;
import com.imooc.flashsale.domain.FlashSaleOrder;
import com.imooc.flashsale.domain.OrderInfo;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.enums.OrderInfoState;
import com.imooc.flashsale.exception.GlobalException;
import com.imooc.flashsale.redis.OrderKey;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.result.CodeMsg;
import com.imooc.flashsale.service.IGoodsService;
import com.imooc.flashsale.service.IOrderService;
import com.imooc.flashsale.vo.GoodsVo;
import com.imooc.flashsale.vo.OrderDetailVo;

@Service
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private FlashSaleOrderMapper orderDao;

	@Autowired
	private OrderInfoMapper orderInfoDao;

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private RedisService redisService;

	@Override
	public FlashSaleOrder getFlashSaleOrder(Integer userId, long goodsId) {
		FlashSaleOrder flashSaleOrder = redisService.get(OrderKey.getFlashSaleByUidGid, "" + userId + "_" + goodsId,
				FlashSaleOrder.class);
		if (flashSaleOrder == null) {
			flashSaleOrder = orderDao.selectByUserIdAndGoodsId(userId.longValue(), goodsId);
			if (flashSaleOrder != null) {
				redisService.set(OrderKey.getFlashSaleByUidGid, "" + userId + "_" + goodsId, flashSaleOrder);
			}
		}
		return flashSaleOrder;
	}

	@Transactional
	public OrderInfo createOrder(User user, GoodsVo goodsVo) {
		// 创建order
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setGoodsCount(1);
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsId(goodsVo.getId());
		orderInfo.setGoodsName(goodsVo.getGoodsName());
		orderInfo.setGoodsPrice(goodsVo.getFlashsalePrice());
		orderInfo.setOrderChannel(3);
		orderInfo.setStatus(OrderInfoState.CREATE_AND_UNPAID.getState());
		orderInfo.setUserId(user.getId().longValue());
		int effectedNums = orderInfoDao.insertSelective(orderInfo);
		if (effectedNums <= 0) {
			return null;
		}
		// System.out.println("创建订单：" + orderInfo);
		// 创建flashsale_order
		FlashSaleOrder flashSaleOrder = new FlashSaleOrder();
		flashSaleOrder.setGoodsId(goodsVo.getId());
		flashSaleOrder.setOrderId(orderInfo.getId());
		flashSaleOrder.setUserId(user.getId().longValue());
		effectedNums = orderDao.insertSelective(flashSaleOrder);
		if (effectedNums <= 0) {
			return null;
		}
		System.out.println(user.getId() + "创建秒杀订单：" + flashSaleOrder);
		// 写入redis缓存
		redisService.set(OrderKey.getFlashSaleByUidGid, "" + user.getId() + "_" + goodsVo.getId(), flashSaleOrder);
		return orderInfo;
	}

	@Override
	public OrderDetailVo getOrderDetail(long orderId) {
		OrderInfo orderInfo = orderInfoDao.selectByPrimaryKey(orderId);
		if (orderInfo == null) {
			throw new GlobalException(CodeMsg.ORDER_EMPTY);
		}
		GoodsVo goodsVo = goodsService.getGoodsVoById(orderInfo.getGoodsId());
		OrderDetailVo orderDetail = new OrderDetailVo();
		orderDetail.setGoodsVo(goodsVo);
		orderDetail.setOrderInfo(orderInfo);
		return orderDetail;
	}

}
