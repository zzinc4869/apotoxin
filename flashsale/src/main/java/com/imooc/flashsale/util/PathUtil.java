package com.imooc.flashsale.util;

public class PathUtil {

	// 注意获取当前操作系统的文件分隔符为“separator”
	private static String seperator = System.getProperty("file.separator");

	/**
	 * @return 图片存储根路径
	 */
	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String basePath;
		if (os.toLowerCase().startsWith("win")) {
			basePath = "E:/projectO2O/Pictures/";
		} else {
			basePath = "/home/xxx/imge";
		}
		// 根据操作系统的不同替换分隔符
		basePath = basePath.replace("/", seperator);
		return basePath;
	}

	/**
	 * @param shopId
	 * @return 返回图片存储子路径
	 */
	public static String getShopImgPath(long shopId) {
		String imagePath = "/upload/item/shop/" + shopId + "/";
		return imagePath.replace("/", seperator);
	}
}
