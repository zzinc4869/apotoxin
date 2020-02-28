package com.imooc.flashsale.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.imooc.flashsale.validator.IsMobile;

public class RegisterVo {

	@NotNull
	@Length(min = 5)
	private String nickname;

	@NotNull
	private String password;

	@NotNull
	@IsMobile
	private Long mobile;

	@NotNull
	private String codeMsg;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public String getCodeMsg() {
		return codeMsg;
	}

	public void setCodeMsg(String codeMsg) {
		this.codeMsg = codeMsg;
	}

	@Override
	public String toString() {
		return "RegisterVo [nickname=" + nickname + ", password=" + password + ", mobile=" + mobile + ", codeMsg="
				+ codeMsg + "]";
	}

}
