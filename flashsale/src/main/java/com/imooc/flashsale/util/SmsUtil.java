package com.imooc.flashsale.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 备注:基于阿里大于SmsDemo源码。Demo工程编码采用UTF-8 国际短信发送请勿参照此DEMO
 */
@Component
public class SmsUtil {

	// 产品名称:云通信短信API产品,开发者无需替换
	final static String product = "Dysmsapi";
	// 产品域名,开发者无需替换
	final static String domain = "dysmsapi.aliyuncs.com";

	private static final String TEMPLATE_CODE = "";
	private static final String SIGN_NAME = "";
	private static final int CODE_LENGTH = 6;

	// 读取属性文件
	@Autowired
	private Environment environment;

	@SuppressWarnings("deprecation")
	public String sendSms(String mobile) throws ClientException {

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 读取配置文件
		String accessKeyId = environment.getProperty("accessKeyId");
		String accessKeySecret = environment.getProperty("accessKeySecret");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(mobile);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(SIGN_NAME);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(TEMPLATE_CODE);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为

		// 生成一个随机数字串，并返回
		String code = getRandomCode(CODE_LENGTH);

		// request.setTemplateParam(param);
		request.setTemplateParam("{\"code\":\"" + code + "\"}");

		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		request.setOutId("yourOutId");

		// hint 此处可能会抛出异常，注意catch
		acsClient.getAcsResponse(request);

		return code;
	}

	@SuppressWarnings("deprecation")
	public QuerySendDetailsResponse querySendDetails(String mobile, String bizId) throws ClientException {

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 读取配置文件
		String accessKeyId = environment.getProperty("accessKeyId");
		String accessKeySecret = environment.getProperty("accessKeySecret");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象
		QuerySendDetailsRequest request = new QuerySendDetailsRequest();
		// 必填-号码
		request.setPhoneNumber(mobile);
		// 可选-流水号
		request.setBizId(bizId);
		// 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		request.setSendDate(ft.format(new Date()));
		// 必填-页大小
		request.setPageSize(10L);
		// 必填-当前页码从1开始计数
		request.setCurrentPage(1L);

		// hint 此处可能会抛出异常，注意catch
		QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

		return querySendDetailsResponse;
	}

	public static String getRandomCode(int length) {
		Random random = new Random();
		String result = "";
		for (int i = 0; i < length; i++) {
			result = result + random.nextInt(10);
		}
		return result;
	}

	public static void main(String[] args) {

	}
}
