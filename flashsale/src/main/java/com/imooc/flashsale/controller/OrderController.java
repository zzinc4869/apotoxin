package com.imooc.flashsale.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.result.CodeMsg;
import com.imooc.flashsale.result.Result;
import com.imooc.flashsale.service.IOrderService;
import com.imooc.flashsale.vo.OrderDetailVo;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private IOrderService orderService;

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public Result<OrderDetailVo> getOrderDetail(User user, @RequestParam("orderId") long orderId) {
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		OrderDetailVo orderDetail = orderService.getOrderDetail(orderId);
		return Result.success(orderDetail);
	}
}
