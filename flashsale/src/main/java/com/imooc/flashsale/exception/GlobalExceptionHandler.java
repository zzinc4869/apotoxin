package com.imooc.flashsale.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliyuncs.exceptions.ClientException;
import com.imooc.flashsale.result.CodeMsg;
import com.imooc.flashsale.result.Result;

/**
 * 全局异常处理
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

	/**
	 * 处理所有抛出为Exception的异常
	 */
	@ExceptionHandler(value = Exception.class)
	public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
		e.printStackTrace();
		// 如果是自定义异常
		if (e instanceof GlobalException) {
			GlobalException ex = (GlobalException) e;
			// 返回自定义错误信息
			return Result.error(ex.getCm());
		} else if (e instanceof BindException) {
			// 如果是绑定异常（出现在参数校验异常）
			BindException ex = (BindException) e;
			List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String msg = error.getDefaultMessage();
			return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
		} else if (e instanceof ClientException) {
			// 发生在短信发送时的异常
			return Result.error(CodeMsg.SEND_MESSAGE_ERROR);
		} else {
			return Result.error(CodeMsg.SERVER_ERROR);
		}
	}
}
