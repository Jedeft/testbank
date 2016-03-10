package com.ncu.testbank.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class EmailUtils {

	private static Logger log = Logger.getLogger("testbankLog");

	private static String appid = null;
	private static String from = null;
	private static String subject = null;
	private static String signature = null;

	public static String sendEmail(String toEmail, String text) {
		try {
			if (appid == null || from == null || subject == null
					|| signature == null) {
				Properties properties = new Properties();
				InputStream in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("submail.properties");
				properties.load(in);

				appid = properties.getProperty("appid");
				from = properties.getProperty("from");
				subject = properties.getProperty("subject");
				signature = properties.getProperty("signature");
			}

			Map<String, String> params = new HashMap<>();
			params.put("appid", appid);
			params.put("from", from);
			params.put("to", toEmail);
			params.put("subject", subject);
			params.put("text", text);
			params.put("signature", signature);
			String result = HttpClientUtils.simplePostInvoke(
					"https://api.submail.cn/mail/send.json", params);
			return result;
		} catch (URISyntaxException | IOException e) {
			log.error("邮件发送失败：" + e);
		}
		return null;

	}

}
