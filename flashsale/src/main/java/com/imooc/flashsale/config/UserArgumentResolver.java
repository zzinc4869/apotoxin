package com.imooc.flashsale.config;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.imooc.flashsale.access.UserContext;
import com.imooc.flashsale.domain.User;

/**
 * 解析Controller中方法的User对象
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// 返回参数支持的对象类型
		Class<?> clazz = parameter.getParameterType();
		return clazz == User.class;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		return UserContext.getUser();
	}

}
