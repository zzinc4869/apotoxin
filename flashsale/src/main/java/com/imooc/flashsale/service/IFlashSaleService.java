package com.imooc.flashsale.service;

import java.awt.image.BufferedImage;

import com.imooc.flashsale.domain.OrderInfo;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.vo.GoodsVo;

public interface IFlashSaleService {

	/**
	 * 执行秒杀 减库存 下订单
	 */
	public OrderInfo doFlashSale(User user, GoodsVo goodsVo);

	/**
	 * 获得秒杀结果
	 */
	public long getFlashSaleResult(Integer userId, Long goodsId);

	/**
	 * 安全验证，是否为正常请求，防止通过url直接访问
	 */
	public void checkFlashSalePath(User user, long goodsId, String path);

	/**
	 * 创建秒杀地址
	 */
	public String createFlashSalePath(User user, long goodsId);

	/**
	 * 创建图片验证码
	 */
	public BufferedImage createVerifyCode(User user, long goodsId);

	/**
	 * 校验验证码
	 */
	public void checkVerifyCode(User user, long goodsId, int verifyCode);

}
