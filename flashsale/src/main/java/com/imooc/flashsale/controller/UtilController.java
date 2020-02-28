package com.imooc.flashsale.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliyuncs.exceptions.ClientException;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.imooc.flashsale.exception.GlobalException;
import com.imooc.flashsale.result.CodeMsg;
import com.imooc.flashsale.result.Result;
import com.imooc.flashsale.util.SmsUtil;
import com.imooc.flashsale.util.ValidatorUtil;

@Controller
@RequestMapping("/utils")
public class UtilController {

	@Autowired
	private DefaultKaptcha captchaProducer;

	@Autowired
	private SmsUtil smsUtil;

	/**
	 * 生成一个图片验证码
	 */
	@RequestMapping("/getKaptchaCode")
	public void getKaptchaCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

		byte[] captchaChallengeAsJpeg = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		try {
			// 生产验证码字符串并保存到session中
			String createText = captchaProducer.createText();
			request.getSession().setAttribute("vrifyCode", createText);

			// 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
			BufferedImage challenge = captchaProducer.createImage(createText);
			ImageIO.write(challenge, "jpg", jpegOutputStream);
		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(captchaChallengeAsJpeg);
		responseOutputStream.flush();
		responseOutputStream.close();
	}

	/**
	 * 使用阿里大于发送一条短信验证码
	 * 
	 * @throws ClientException
	 */
	@RequestMapping("/send_message")
	@ResponseBody
	public Result<Boolean> sendMessage(HttpServletRequest request, Long mobile) throws ClientException {
		// 验证手机号格式
		if (!ValidatorUtil.isMobile(mobile)) {
			throw new GlobalException(CodeMsg.MOBILE_FORM_ERROR);
		}
		// 发送短信并将code保存至session中
		String code = smsUtil.sendSms(mobile.toString());
		// String code = "123456";
		System.out.println("向" + mobile + "发送了一条短信，验证码为" + code);
		request.getSession().setAttribute("codeMessage", code);
		return Result.success(true);
	}

}
