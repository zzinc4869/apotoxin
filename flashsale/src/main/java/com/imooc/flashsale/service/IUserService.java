package com.imooc.flashsale.service;

import javax.servlet.http.HttpServletResponse;

import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.vo.LoginVo;
import com.imooc.flashsale.vo.RegisterVo;

public interface IUserService {

	public static final String COOKIE_NAME_TOKEN = "token";

	/**
	 * 登录检验
	 * 
	 * @return 注册id
	 */
	public String login(HttpServletResponse response, LoginVo loginVo);

	/**
	 * 注册验证
	 * 
	 * @return 注册id
	 */
	public int register(RegisterVo registerVo);

	/**
	 * 根据token从redis中获取一个user对象
	 * 
	 * @param token
	 * @return
	 */
	public User getByToken(HttpServletResponse response, String token);

}
