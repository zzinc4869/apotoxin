package com.imooc.flashsale.exception;

import com.imooc.flashsale.result.CodeMsg;

/**
 * 全局自定义异常
 */
public class GlobalException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private CodeMsg cm;

	/**
	 * 接收CodeMsg类型的异常，进行统一处理
	 */
	public GlobalException(CodeMsg cm) {
		super(cm.toString());
		this.cm = cm;
	}

	public CodeMsg getCm() {
		return cm;
	}

}
