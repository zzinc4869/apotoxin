package com.imooc.flashsale.result;

/**
 * 错误信息统一处理
 */
public class CodeMsg {

	private int code;
	private String msg;

	// 通用异常 501xxx
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(50100, "服务端异常");
	public static CodeMsg BIND_ERROR = new CodeMsg(50101, "参数检验异常: %s");
	public static CodeMsg ILLEGAL_REQUEST = new CodeMsg(50102, "请求非法!");
	public static CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(50103, "访问太频繁!");

	// 注册登录模块 502xxx
	public static CodeMsg SESSION_ERROR = new CodeMsg(50210, "Session不存在或失效，请重新登陆");
	public static CodeMsg PASSWORD_EMPTY = new CodeMsg(50211, "密码不能为空！");
	public static CodeMsg MOBILE_EMPTY = new CodeMsg(50212, "手机号不能为空！");
	public static CodeMsg AVATAR_EMPTY = new CodeMsg(50213, "头像不能为空！");
	public static CodeMsg MOBILE_FORM_ERROR = new CodeMsg(50214, "手机号格式错误！");
	public static CodeMsg VERTIFYCODE_ERROR = new CodeMsg(50215, "验证码错误");
	public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(50216, "手机号未注册！");
	public static CodeMsg PASSWORD_ERROR = new CodeMsg(50217, "登录密码错误！");
	public static CodeMsg SEND_MESSAGE_ERROR = new CodeMsg(50218, "短信发送失败！");
	public static CodeMsg MOBILE_EXIST = new CodeMsg(50219, "该手机号已被注册！");
	public static CodeMsg NULL_USER = new CodeMsg(50220, "用户不存在！");
	public static CodeMsg FAIL_UPDATE = new CodeMsg(50221, "更新失败！");
	public static CodeMsg EMPTY_CODE = new CodeMsg(50222, "验证码为空！");

	// 商品模块 503xxx

	// 订单模块 504xxx
	public static CodeMsg ORDER_EMPTY = new CodeMsg(50410, "不存在该订单！");

	// 秒杀模块 505xxx
	public static CodeMsg FLASHSALE_OVER = new CodeMsg(50510, "秒杀商品已售罄！");
	public static CodeMsg REPEAT_FLASHSALE = new CodeMsg(50511, "不能重复秒杀该商品！");

	private CodeMsg() {

	}

	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	/**
	 * 填充参数 %s
	 */
	public CodeMsg fillArgs(Object... args) {
		int code = this.code;
		String message = String.format(this.msg, args);
		return new CodeMsg(code, message);
	}

	@Override
	public String toString() {
		return "CodeMsg [code=" + code + ", msg=" + msg + "]";
	}

}
