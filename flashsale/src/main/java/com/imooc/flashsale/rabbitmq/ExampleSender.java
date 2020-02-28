package com.imooc.flashsale.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;

import com.imooc.flashsale.util.BeanUtil;

@Service
public class ExampleSender {

	private AmqpTemplate amqpTemplate;

	private static Logger logger = LoggerFactory.getLogger(MQSender.class);

	/**************************** Four Exchanges Example *************************/
	public void send(Object message) {
		String msg = BeanUtil.beanToString(message);
		logger.info("send message: " + msg);
		amqpTemplate.convertAndSend(ExampleConfig.QUEUE, msg);
	}

	public void sendTopic(Object message) {
		String msg = BeanUtil.beanToString(message);
		logger.info("send topic message: " + msg);
		amqpTemplate.convertAndSend(ExampleConfig.TOPIC_EXCHANGE, "topic.key1", msg + 1);
		amqpTemplate.convertAndSend(ExampleConfig.TOPIC_EXCHANGE, "topic.key2", msg + 2);
	}

	public void sendFanout(Object message) {
		String msg = BeanUtil.beanToString(message);
		logger.info("send fanout message: " + msg);
		amqpTemplate.convertAndSend(ExampleConfig.FANOUT_EXCHANGE, "", msg);
	}

	public void sendHeader(Object message) {
		String msg = BeanUtil.beanToString(message);
		logger.info("send headers message: " + msg);
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader("key1", "value1");
		messageProperties.setHeader("key2", "value2");
		Message obj = new Message(msg.getBytes(), messageProperties);
		amqpTemplate.convertAndSend(ExampleConfig.HEADERS_EXCHANGE, "", obj);
	}

}
