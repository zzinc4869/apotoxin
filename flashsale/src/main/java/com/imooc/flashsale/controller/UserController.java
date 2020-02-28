package com.imooc.flashsale.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.result.Result;
import com.imooc.flashsale.service.IUserService;
import com.imooc.flashsale.util.CodeUtil;
import com.imooc.flashsale.vo.LoginVo;
import com.imooc.flashsale.vo.RegisterVo;

@Controller
@RequestMapping("/user")
public class UserController {

	/**
	 * 访问thymeleaf需要使用@Controller返回页面供解析，访问数据使用@RestController解析为json
	 */

	@Autowired
	private IUserService userService;

	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@RequestMapping("/to_login")
	public String login() {
		return "login";
	}

	@RequestMapping("/to_register")
	public String toLogin() {
		return "register";
	}

	/**
	 * 注册
	 */
	@RequestMapping("/do_register")
	@ResponseBody
	public Result<Boolean> doRegister(HttpServletRequest request, RegisterVo registerVo) {
		log.info(registerVo.toString());
		// System.out.println(registerVo);
		// 验证码校验
		CodeUtil.codeMessageVertify(request, registerVo.getCodeMsg());
		// 注册
		userService.register(registerVo);
		return Result.success(true);
	}

	/**
	 * 登录
	 */
	@RequestMapping("/do_login")
	@ResponseBody
	public Result<String> doLogin(HttpServletRequest request, HttpServletResponse response, @Valid LoginVo loginVo) {
		log.info(loginVo.toString());
		// 验证码校验
		CodeUtil.kaptchaVertify(request, loginVo.getVertifyCode());
		// 登录检验
		String token = userService.login(response, loginVo);

		return Result.success(token);
	}

	@RequestMapping("/testuser")
	@ResponseBody
	public Result<User> testUser(User user) {
		return Result.success(user);
	}

}
