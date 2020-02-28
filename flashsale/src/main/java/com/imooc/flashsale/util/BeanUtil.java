package com.imooc.flashsale.util;

import com.alibaba.fastjson.JSON;

public class BeanUtil {
	/**
	 * 将特定类型的bean转换为string
	 * 
	 * @param value
	 * @return
	 */
	public static <T> String beanToString(T value) {
		if (value == null) {
			return null;
		}
		Class<?> clazz = value.getClass();
		if (clazz == int.class || clazz == Integer.class) {
			return "" + value;
		} else if (clazz == String.class) {
			return (String) value;
		} else if (clazz == long.class || clazz == Long.class) {
			return "" + value;
		} else {
			return JSON.toJSONString(value);
		}
	}

	/**
	 * 将String转换为一个特定类型的bean
	 * 
	 * @param str   带转换的字符串
	 * @param clazz 转换的类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T stringToBean(String str, Class<T> clazz) {
		if (str == null || str.length() <= 0 || clazz == null) {
			return null;
		}
		if (clazz == int.class || clazz == Integer.class) {
			return (T) Integer.valueOf(str);
		} else if (clazz == String.class) {
			return (T) str;
		} else if (clazz == long.class || clazz == Long.class) {
			return (T) Long.valueOf(str);
		} else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}
}
