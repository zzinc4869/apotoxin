package com.imooc.flashsale.domain;

import java.util.Date;

public class User {
	private Integer id;

	private Long mobile;

	private String nickname;

	private String password;

	private String salt;

	private String head;

	private Date registerDate;

	private Date lastLoginDate;

	private Integer loginCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname == null ? null : nickname.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt == null ? null : salt.trim();
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head == null ? null : head.trim();
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", mobile=" + mobile + ", nickname=" + nickname + ", password=" + password + ", salt="
				+ salt + ", head=" + head + ", registerDate=" + registerDate + ", lastLoginDate=" + lastLoginDate
				+ ", loginCount=" + loginCount + "]";
	}

}