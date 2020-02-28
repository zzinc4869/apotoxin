package com.imooc.flashsale.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.flashsale.domain.FlashSaleOrder;
import com.imooc.flashsale.domain.OrderInfo;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.exception.GlobalException;
import com.imooc.flashsale.redis.FlashSaleKey;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.result.CodeMsg;
import com.imooc.flashsale.service.IFlashSaleService;
import com.imooc.flashsale.service.IGoodsService;
import com.imooc.flashsale.service.IOrderService;
import com.imooc.flashsale.util.MD5Util;
import com.imooc.flashsale.util.UUIDUtil;
import com.imooc.flashsale.vo.GoodsVo;

@Service
public class FlashSaleServiceImpl implements IFlashSaleService {

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private RedisService redisService;

	@Transactional
	public OrderInfo doFlashSale(User user, GoodsVo goodsVo) {
		// 减少库存
		boolean success = goodsService.reduceStock(goodsVo);
		// 减库存成功，创建订单
		if (success) {
			// order_info flashsale_order
			return orderService.createOrder(user, goodsVo);
		} else {
			setGoodsOver(goodsVo.getId());
			return null;
		}
	}

	@Override
	public long getFlashSaleResult(Integer userId, Long goodsId) {
		FlashSaleOrder flashSaleOrder = orderService.getFlashSaleOrder(userId, goodsId);
		if (flashSaleOrder != null) {
			// 秒杀成功
			return flashSaleOrder.getOrderId();
		}
		boolean isOver = getGoodsOver(goodsId);
		if (isOver) {
			// 商品售罄
			return -1;
		} else {
			// 仍在排队中
			return 0;
		}
	}

	private void setGoodsOver(Long goodsId) {
		redisService.set(FlashSaleKey.isGoodsOver, "" + goodsId, true);

	}

	private boolean getGoodsOver(Long goodsId) {
		return redisService.exist(FlashSaleKey.isGoodsOver, "" + goodsId);
	}

	@Override
	public void checkFlashSalePath(User user, long goodsId, String path) {
		String expectedPath = redisService.get(FlashSaleKey.getFlashSalePath, "" + user.getId() + "_" + goodsId,
				String.class);
		if (path == null || !path.equals(expectedPath)) {
			throw new GlobalException(CodeMsg.ILLEGAL_REQUEST);
		}
	}

	@Override
	public String createFlashSalePath(User user, long goodsId) {
		// 创建随机地址
		String randomStr = MD5Util.md5(UUIDUtil.getRandomUUID() + "flashsale");
		// 存入缓存
		redisService.set(FlashSaleKey.getFlashSalePath, "" + user.getId() + "_" + goodsId, randomStr);
		return randomStr;
	}

	@Override
	public BufferedImage createVerifyCode(User user, long goodsId) {
		if (user == null || goodsId <= 0) {
			return null;
		}
		int width = 80;
		int height = 32;
		// create the image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		// set the background color
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		// draw the border
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);
		// create a random instance to generate the codes
		Random rdm = new Random();
		// make some confusion
		for (int i = 0; i < 50; i++) {
			int x = rdm.nextInt(width);
			int y = rdm.nextInt(height);
			g.drawOval(x, y, 0, 0);
		}
		// generate a random code
		String expressions = generateVerifyCode(rdm);
		g.setColor(new Color(0, 100, 0));
		g.setFont(new Font("Candara", Font.BOLD, 24));
		g.drawString(expressions, 8, 24);
		g.dispose();
		// 把验证码存到redis中
		int rnd = calc(expressions);
		redisService.set(FlashSaleKey.getFlashSaleVerifyCode, user.getId() + "," + goodsId, rnd);
		// 输出图片
		return image;
	}

	private static char[] ops = new char[] { '+', '-', '*' };

	/**
	 * 生成表达式 + - *
	 */
	private String generateVerifyCode(Random rdm) {
		int num1 = rdm.nextInt(9) + 1;
		int num2 = rdm.nextInt(9) + 1;
		int num3 = rdm.nextInt(9) + 1;
		char op1 = ops[rdm.nextInt(3)];
		char op2 = ops[rdm.nextInt(3)];
		String exp = "" + num1 + op1 + num2 + op2 + num3;
		return exp;
	}

	private int calc(String expressions) {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			return (Integer) engine.eval(expressions);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public void checkVerifyCode(User user, long goodsId, int verifyCode) {
		if (user == null || goodsId <= 0) {
			throw new GlobalException(CodeMsg.ILLEGAL_REQUEST);
		}
		Integer codeExpected = redisService.get(FlashSaleKey.getFlashSaleVerifyCode, user.getId() + "," + goodsId,
				Integer.class);
		if (codeExpected == null || codeExpected - verifyCode != 0) {
			throw new GlobalException(CodeMsg.VERTIFYCODE_ERROR);
		}
		redisService.delete(FlashSaleKey.getFlashSaleVerifyCode, user.getId() + "," + goodsId);
	}

}
