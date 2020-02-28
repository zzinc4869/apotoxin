package com.imooc.flashsale.rabbitmq;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;

public class ExampleConfig {

	public static final String QUEUE = "queue";
	public static final String QUEUE_A = "queue.a";
	public static final String QUEUE_B = "queue.b";
	public static final String HEADER_QUEUE = "headerqueue";
	public static final String TOPIC_EXCHANGE = "topicExchange";
	public static final String FANOUT_EXCHANGE = "fanoutExchange";
	public static final String HEADERS_EXCHANGE = "headersExchange";

	/************************** Samples **************************************/
	/**
	 * Direct模式 交换机Exchange
	 */
	@Bean
	public Queue queue() {
		// 名称为QUEUE，且需要做持久化
		return new Queue(QUEUE, true);
	}

	@Bean
	public Queue queue_A() {
		return new Queue(QUEUE_A, true);
	}

	@Bean
	public Queue queue_B() {
		return new Queue(QUEUE_B, true);
	}

	/**
	 * Topic模式 交换机Exchange
	 */
	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(TOPIC_EXCHANGE);
	}

	@Bean
	public Binding topicBinding1() {
		// 匹配topic.key1
		return BindingBuilder.bind(queue_A()).to(topicExchange()).with("topic.key1");
	}

	@Bean
	public Binding topicBinding2() {
		// 支持通配符匹配 匹配任意或多个字符
		return BindingBuilder.bind(queue_B()).to(topicExchange()).with("topic.#");
	}

	/**
	 * Fanout模式 交换机Exchange：广播交换机
	 */
	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange(FANOUT_EXCHANGE);
	}

	@Bean
	public Binding fanoutBinding1() {
		return BindingBuilder.bind(queue_A()).to(fanoutExchange());
	}

	@Bean
	public Binding fanoutBinding2() {
		return BindingBuilder.bind(queue_B()).to(fanoutExchange());
	}

	/**
	 * Header模式 交换机Exchange
	 */
	@Bean
	public HeadersExchange headersExchange() {
		return new HeadersExchange(HEADERS_EXCHANGE);
	}

	@Bean
	public Queue headerQueue() {

		return new Queue(HEADER_QUEUE, true);
	}

	@Bean
	public Binding headerBinding() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key1", "value1");
		map.put("key2", "value2");
		return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(map).match();
	}
}
