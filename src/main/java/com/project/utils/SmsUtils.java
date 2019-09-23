package com.project.utils;

import com.project.bean.others.CodeBean;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 短信通知
 */
public class SmsUtils {

	/**
	 * 网信通
	 */
	public static void sms(String phone,String content){
		// 产生随机验证码
				try {
					String format = "yyyy-MM-dd HH:mm:ss";
					String time = TimeUtils.getCurrentTime(format);
					Date date = TimeUtils.getDateFromTime(format, time);
					String url = "http://www.api.zthysms.com/sendSms.do";
					String tkey = TimeUtils.getCurrentTime("yyyyMMddHHmmss");
					Map<String,Object> params=new HashMap<>();
					params.put("username",ConfigUtils.zhu_tong_account);
					params.put("password", EncodeUtils.MD5Encode(EncodeUtils.MD5Encode(ConfigUtils.zhu_tong_password).toLowerCase()+tkey).toLowerCase());
					params.put("mobile",phone);
					params.put("content",content);
					params.put("tkey",tkey);
					params.put("xh","");
					String ret = HttpUtils.post(url, params);
					String result = "";
					switch (ret.split(",")[0]) {
						case "-1":
							result = "用户名或者密码不正确或用户禁用或者是管理账户";
							break;
						case "1":
							result = "发送短信成功";
							break;
						case "0":
							result = "发送短信失败";
							break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
}
