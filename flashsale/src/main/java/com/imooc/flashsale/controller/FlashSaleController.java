package com.imooc.flashsale.controller;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.flashsale.access.AccessLimit;
import com.imooc.flashsale.domain.FlashSaleOrder;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.exception.GlobalException;
import com.imooc.flashsale.rabbitmq.FlashSaleMessage;
import com.imooc.flashsale.rabbitmq.MQSender;
import com.imooc.flashsale.redis.GoodsKey;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.result.CodeMsg;
import com.imooc.flashsale.result.Result;
import com.imooc.flashsale.service.IFlashSaleService;
import com.imooc.flashsale.service.IGoodsService;
import com.imooc.flashsale.service.IOrderService;
import com.imooc.flashsale.vo.GoodsVo;

@RestController
@RequestMapping("/flashsale")
public class FlashSaleController implements InitializingBean {

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IFlashSaleService flashSaleService;

	@Autowired
	private MQSender mqSender;

	private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

	// 实现InitializingBean接口的此方法，用于在系统初始化时处理
	public void afterPropertiesSet() throws Exception {
		List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
		if (goodsVoList == null || goodsVoList.size() <= 0) {
			return;
		}
		for (GoodsVo goodsVo : goodsVoList) {
			// 初始化时将商品库存缓存
			redisService.set(GoodsKey.getFlashSaleGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
			// 标记商品未售罄
			localOverMap.put(goodsVo.getId(), false);
		}
	}

	/**
	 * 压测第一次：10*2000并发，QPS：63.9
	 */
	// 5秒访问5次，需要登录
	@AccessLimit(seconds = 5, maxVisit = 5, needLogin = true)
	@RequestMapping(value = "/{path}/do_flashsale", method = RequestMethod.POST)
	public Result<String> doFlashsale(User user, @PathVariable("path") String path,
			@RequestParam("goodsId") long goodsId) {
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		// 验证访问地址
		flashSaleService.checkFlashSalePath(user, goodsId, path);
		// 内存标记商品结束，避免继续访问redis查询
		boolean isOver = localOverMap.get(goodsId);
		if (isOver) {
			return Result.error(CodeMsg.FLASHSALE_OVER);
		}
		// 预减库存，判断库存,减少数据库访问
		long stock = redisService.decr(GoodsKey.getFlashSaleGoodsStock, "" + goodsId);
		if (stock < 0) {
			localOverMap.put(goodsId, true);
			return Result.error(CodeMsg.FLASHSALE_OVER);
		}

		// 判断是否已经秒杀成功,防止重复秒杀
		FlashSaleOrder flashSaleOrder = orderService.getFlashSaleOrder(user.getId(), goodsId);
		if (flashSaleOrder != null) {
			return Result.error(CodeMsg.REPEAT_FLASHSALE);
		}
		// 入队
		FlashSaleMessage message = new FlashSaleMessage();
		message.setUser(user);
		message.setGoodsId(goodsId);
		mqSender.sendFlashSaleMessge(message);
		return Result.success("排队中......");
	}

	/**
	 * 0:排队中 -1:秒杀失败 orderId:成功
	 */
	@RequestMapping(value = "/result", method = RequestMethod.GET)
	public Result<Long> getFlashSaleResult(User user, @RequestParam("goodsId") long goodsId) {
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		// 判断秒杀结果
		long result = flashSaleService.getFlashSaleResult(user.getId(), goodsId);
		return Result.success(result);
	}

	/**
	 * 安全验证：隐藏秒杀接口地址，验证是否为正常请求，防止通过url直接访问
	 * 
	 * @param user
	 * @param goodsId
	 * @return
	 */
	@AccessLimit(seconds = 5, maxVisit = 5, needLogin = true)
	@RequestMapping(value = "/path", method = RequestMethod.GET)
	public Result<String> getFlashSalePath(HttpServletRequest request, User user, @RequestParam("goodsId") long goodsId,
			@RequestParam(value = "verifyCode") int verifyCode) {
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		// 查询访问次数
		/*
		 * String url = request.getRequestURI(); String key = url + "_" + user.getId();
		 * Integer count = redisService.get(AccessKey.access, key, Integer.class); if
		 * (count == null) { redisService.set(AccessKey.access, key, 1); } else if
		 * (count < 5) { redisService.incr(AccessKey.access, key); } else { return
		 * Result.error(CodeMsg.ACCESS_LIMIT_REACHED); }
		 */

		// 校验验证码
		flashSaleService.checkVerifyCode(user, goodsId, verifyCode);
		// 创建秒杀地址
		String path = flashSaleService.createFlashSalePath(user, goodsId);
		return Result.success(path);
	}

	/**
	 * 生成图片验证码
	 * 
	 * @param user
	 * @param goodsId
	 * @return
	 */
	@AccessLimit(needLogin = true, maxVisit = 5, seconds = 5)
	@RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
	public Result<String> getVerifyCode(HttpServletResponse response, User user,
			@RequestParam("goodsId") long goodsId) {
		if (user == null) {
			throw new GlobalException(CodeMsg.SESSION_ERROR);
		}
		// 创建图片验证码
		BufferedImage image = flashSaleService.createVerifyCode(user, goodsId);
		try {
			OutputStream outputStream = response.getOutputStream();
			ImageIO.write(image, "JPEG", outputStream);
			outputStream.flush();
			outputStream.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
	}

}
