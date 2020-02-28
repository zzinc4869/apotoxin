package com.imooc.flashsale.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.druid.util.StringUtils;

public class ValidatorUtil {

	// 正则表达式 以1开头的11位数字
	private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

	/**
	 * 验证手机号格式
	 */
	public static boolean isMobile(Long src) {
		String mobile = "" + src;
		if (StringUtils.isEmpty(mobile)) {
			return false;
		}
		Matcher matcher = mobile_pattern.matcher(mobile);
		return matcher.matches();
	}

	public static void main(String[] args) {
		System.out.println(isMobile(13659168934L));
		System.out.println(isMobile(23659168934L));
	}
}
