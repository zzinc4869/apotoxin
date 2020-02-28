package com.imooc.flashsale.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {

	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random random = new Random();

	/**
	 * 生成文件存储路径
	 * 
	 * @param thumbnail  上传文件
	 * @param targetPath 目标路径
	 * @return 文件存储路径
	 */
	public static String generateThumbnail(InputStream thumbnailIpstm, String fileName, String targetPath) {

		// 随机生成不重名文件名
		String realFileName = getRandomFileName();
		// 获取文件扩展名
		String extension = getFileExtension(fileName);
		// 校验文件存储路径
		makeDirPath(targetPath);
		String relativePath = targetPath + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativePath);

		try {
			// 添加水印并压缩
			Thumbnails.of(thumbnailIpstm).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.png")), 0.25f)
					.outputQuality(0.8f).toFile(dest.getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativePath;
	}

	/**
	 * 创建目标路径所涉及到的目录
	 * 
	 * @param targetPath
	 */
	private static void makeDirPath(String targetPath) {
		String realFileNamePath = PathUtil.getImgBasePath() + targetPath;
		File dirPath = new File(realFileNamePath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 随机生成文件名 当前年月日小时分钟秒数+五位随机数
	 * 
	 * @return
	 */
	public static String getRandomFileName() {
		// 获取随机的五位数
		int rannum = random.nextInt(89999) + 10000;
		String nowTimeStr = dateformat.format(new Date());
		return nowTimeStr + rannum;
	}

	/**
	 * 判断storePath是文件路径还是目录路径 如果是文件路径， 则删除该文件 如果是目录路径， 则删除该目录下的所有文件
	 * 
	 * @param storePath
	 */
	public static void deleFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
		if (fileOrPath.exists()) {
			if (fileOrPath.isDirectory()) {
				File files[] = fileOrPath.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}
}
