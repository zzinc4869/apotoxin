package com.imooc.flashsale.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;;

@Configuration
public class MQConfig {

	public static final String FLASHSALE_QUEUE = "flashsale.queue";

	/**
	 * Direct模式
	 */
	@Bean
	public Queue flashSaleQueue() {
		return new Queue(FLASHSALE_QUEUE, true);
	}

}
