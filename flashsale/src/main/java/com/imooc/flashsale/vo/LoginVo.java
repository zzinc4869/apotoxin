package com.imooc.flashsale.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.imooc.flashsale.validator.IsMobile;

public class LoginVo {

	@NotNull
	@IsMobile
	private Long mobile;

	@NotNull
	@Length(min = 32)
	private String password;

	private String vertifyCode;

	@Override
	public String toString() {
		return "LoginVo [mobile=" + mobile + ", password=" + password + ", vertifyCode=" + vertifyCode + "]";
	}

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVertifyCode() {
		return vertifyCode;
	}

	public void setVertifyCode(String vertifyCode) {
		this.vertifyCode = vertifyCode;
	}

}
