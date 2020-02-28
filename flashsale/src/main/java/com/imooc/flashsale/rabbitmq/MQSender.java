package com.imooc.flashsale.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.flashsale.util.BeanUtil;

@Service
public class MQSender {

	@Autowired
	private AmqpTemplate amqpTemplate;

	private static Logger logger = LoggerFactory.getLogger(MQSender.class);

	public void sendFlashSaleMessge(FlashSaleMessage message) {
		String msg = BeanUtil.beanToString(message);
		logger.info("send flashsale message: " + msg);
		amqpTemplate.convertAndSend(MQConfig.FLASHSALE_QUEUE, msg);
	}

}
