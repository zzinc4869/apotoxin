package com.imooc.flashsale.util;

import javax.servlet.http.HttpServletRequest;

import com.imooc.flashsale.exception.GlobalException;
import com.imooc.flashsale.result.CodeMsg;

public class CodeUtil {

	/**
	 * 校验验证码
	 */
	public static boolean kaptchaVertify(HttpServletRequest request, String verifyCodeActual) {
		// 从session中获取验证码正确值
		String verifyCodeExcepted = (String) request.getSession().getAttribute("vrifyCode");
		if (verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExcepted)) {
			throw new GlobalException(CodeMsg.VERTIFYCODE_ERROR);
		}
		return true;
	}

	/**
	 * 校验短信验证码
	 */
	public static boolean codeMessageVertify(HttpServletRequest request, String verifyCodeActual) {
		// 从session中获取验证码正确值
		String verifyCodeExcepted = (String) request.getSession().getAttribute("codeMessage");
		if (verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExcepted)) {
			throw new GlobalException(CodeMsg.VERTIFYCODE_ERROR);
		}
		return true;
	}
}
