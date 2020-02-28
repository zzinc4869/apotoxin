package com.imooc.flashsale.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.rabbitmq.ExampleReceiver;
import com.imooc.flashsale.rabbitmq.ExampleSender;

@RestController
@RequestMapping("/demo")
public class DemoController {

	@Autowired
	ExampleSender mqSender;

	@Autowired
	ExampleReceiver mqReceiver;

	@RequestMapping("/send")
	public void sendAndReceive() {
		User user = new User();
		user.setNickname("hack");
		user.setPassword("fyguhijolnkjbtfygu");
		user.setMobile(13659168934L);
		user.setRegisterDate(new Date());
		mqSender.send(user);
	}

	@RequestMapping("/send/topic")
	public String sendTopic() {
		String string = "hello,immoc";
		mqSender.sendTopic(string);
		return string;
	}

	@RequestMapping("/send/fanout")
	public String sendFanout() {
		String string = "hello,immoc";
		mqSender.sendFanout(string);
		return string;
	}

	@RequestMapping("/send/header")
	public String sendHeader() {
		String string = "hello,immoc";
		mqSender.sendHeader(string);
		return string;
	}
}
