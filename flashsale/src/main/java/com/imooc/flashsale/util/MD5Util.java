package com.imooc.flashsale.util;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

	private static final String saltG = "1a2b3c4d";

	private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };

	public static String md5(String src) {

		return DigestUtils.md5Hex(src);
	}

	/**
	 * 将真正的密码加密用于服务器间传输
	 */
	public static String inputPassToFormPass(String inputPass) {
		String str = "" + saltG.charAt(0) + saltG.charAt(2) + inputPass + saltG.charAt(7) + saltG.charAt(5);
		return md5(str);
	}

	/**
	 * 将一次加密的密码用随机盐值加密用于数据库存储
	 */
	public static String formPassToDBPass(String formPass, String salt) {
		return md5(formPass + salt);
	}

	/**
	 * 将用户密码转换为数据库存储的密码
	 */
	public static String inputPassToDBPass(String inputPass, String salt) {
		String formPass = inputPassToFormPass(inputPass);
		String dbPass = formPassToDBPass(formPass, salt);
		return dbPass;
	}

	public static String getRandomSalt() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}

	public static void main(String[] args) {
		System.out.println(getRandomSalt());
	}
}
