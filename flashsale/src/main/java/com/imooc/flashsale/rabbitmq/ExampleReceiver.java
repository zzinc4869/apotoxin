package com.imooc.flashsale.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.util.BeanUtil;

@Service
public class ExampleReceiver {

	private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

	/**************************** Four Exchanges Example *************************/
	@RabbitListener(queues = ExampleConfig.QUEUE)
	public void receive(String message) {
		User user = BeanUtil.stringToBean(message, User.class);
		logger.info("receive message: " + user.toString());
	}

	@RabbitListener(queues = ExampleConfig.QUEUE_A)
	public void receive1(String message) {
		String string = BeanUtil.stringToBean(message, String.class);
		logger.info("queue_a receive message: " + string);
	}

	@RabbitListener(queues = ExampleConfig.QUEUE_B)
	public void receive2(String message) {
		String string = BeanUtil.stringToBean(message, String.class);
		logger.info("queue_b receive message: " + string);
	}

	@RabbitListener(queues = ExampleConfig.HEADER_QUEUE)
	public void receiveHeader(byte[] message) {
		String string = new String(message);
		string = BeanUtil.stringToBean(string, String.class);
		logger.info("header queue receive message: " + string);
	}
}
