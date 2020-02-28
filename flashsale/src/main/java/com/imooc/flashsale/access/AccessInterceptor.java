package com.imooc.flashsale.access;

import java.io.OutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.imooc.flashsale.domain.User;
import com.imooc.flashsale.redis.AccessKey;
import com.imooc.flashsale.redis.RedisService;
import com.imooc.flashsale.result.CodeMsg;
import com.imooc.flashsale.result.Result;
import com.imooc.flashsale.service.IUserService;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private IUserService userService;

	@Autowired
	private RedisService redisService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 如果得到一个方法，进行一下判断。若不加此判断，则将在所有方法运行前执行。
		if (handler instanceof HandlerMethod) {
			// 获取user并保存至当前线程
			User user = getUser(request, response);
			UserContext.setUser(user);
			HandlerMethod hm = (HandlerMethod) handler;
			AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
			// 如果注解为空，直接返回
			if (accessLimit == null) {
				return true;
			}
			int seconds = accessLimit.seconds();
			int maxVisit = accessLimit.maxVisit();
			boolean needLogin = accessLimit.needLogin();
			String key = request.getRequestURI();
			// 注解逻辑判断
			if (needLogin) {

				// 判断是否登录
				if (user == null) {
					// 提示错误
					// render(response, CodeMsg.SESSION_ERROR);
					// 重定向
					response.sendRedirect("/user/to_login");
					return false;
				}
				key += "_" + user.getId();
			}
			// 当前key失效时限
			AccessKey ak = AccessKey.withExpire(seconds);
			// 从缓存中获取访问次数
			Integer count = redisService.get(ak, key, Integer.class);
			// seconds秒访问maxVisit次数
			if (count == null) {
				redisService.set(ak, key, 1);
			} else if (count < maxVisit) {
				redisService.incr(ak, key);
			} else {
				render(response, CodeMsg.ACCESS_LIMIT_REACHED);
				return false;
			}
		}
		return true;
	}

	/**
	 * 渲染页面，提示错误信息
	 */
	private void render(HttpServletResponse response, CodeMsg cm) throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		String str = JSON.toJSONString(Result.error(cm));
		out.write(str.getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	/**
	 * 获取当前用户，同UserArgumentResolver
	 */
	private User getUser(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter(IUserService.COOKIE_NAME_TOKEN);
		String cookieToken = getCookieValue(request, IUserService.COOKIE_NAME_TOKEN);
		if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		return userService.getByToken(response, token);
	}

	/**
	 * 获取当前Cookie，同UserArgumentResolver
	 */
	private String getCookieValue(HttpServletRequest request, String cookiName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length <= 0) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookiName)) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
