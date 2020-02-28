package com.imooc.flashsale.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.imooc.flashsale.access.AccessInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	UserArgumentResolver userArgumentResolver;

	@Autowired
	AccessInterceptor accessInterceptor;

	/**
	 * SpringMVC的此方法为Controller中方法的参数赋值，这里重写该方法根据分布式session获取对象
	 */
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		// 添加User对应的解析器
		resolvers.add(userArgumentResolver);
	}

	/**
	 * 注册对应的拦截器
	 */
	public void addInterceptors(InterceptorRegistry registry) {

		// 放行列表
		List<String> patterns = new ArrayList<String>();
		patterns.add("/bootstrap/**");
		patterns.add("/img/**");
		patterns.add("/jquery-validation/**");
		patterns.add("/js/**");
		patterns.add("/layer/**");
		patterns.add("/user/to_login");
		patterns.add("/user/to_register");
		// 进行拦截方法与放行方法
		// 作用：不登录和注册无法访问其他页面
		registry.addInterceptor(accessInterceptor).addPathPatterns("/**").excludePathPatterns(patterns);
	}

	/**
	 * 默认首页设置，当请求时项目地址的时候 返回login
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("login.html");
	}
}
