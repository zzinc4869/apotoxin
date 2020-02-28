package com.imooc.flashsale.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.flashsale.domain.FlashSaleOrder;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.exception.GlobalException;
import com.imooc.flashsale.result.CodeMsg;
import com.imooc.flashsale.service.IFlashSaleService;
import com.imooc.flashsale.service.IGoodsService;
import com.imooc.flashsale.service.IOrderService;
import com.imooc.flashsale.util.BeanUtil;
import com.imooc.flashsale.vo.GoodsVo;

@Service
public class MQReceiver {

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private IFlashSaleService flashSaleService;

	@Autowired
	private IOrderService orderService;

	// private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

	@RabbitListener(queues = MQConfig.FLASHSALE_QUEUE)
	public void receiveFlashSaleMessage(String message) {
		// 获取消息
		FlashSaleMessage flashSaleMessage = BeanUtil.stringToBean(message, FlashSaleMessage.class);
		// logger.info("receive message: " + flashSaleMessage);
		User user = flashSaleMessage.getUser();
		long goodsId = flashSaleMessage.getGoodsId();

		System.out.println("<< " + user.getId() + " 出队");

		// 判断商品库存
		GoodsVo goods = goodsService.getGoodsVoById(goodsId);
		int stock = goods.getStockCount();
		if (stock <= 0) {
			throw new GlobalException(CodeMsg.FLASHSALE_OVER);
		}

		// 再次判断是否已经秒杀成功,防止重复秒杀
		FlashSaleOrder flashSaleOrder = orderService.getFlashSaleOrder(user.getId(), goodsId);
		if (flashSaleOrder != null) {
			throw new GlobalException(CodeMsg.REPEAT_FLASHSALE);
		}

		// 减库存 下订单 创建秒杀订单
		flashSaleService.doFlashSale(user, goods);
	}

}
