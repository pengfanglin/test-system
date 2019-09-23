package com.project.utils;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 自定义yml配置文件属性
 */
@Component
public class ConfigUtils {
	public static String static_dir;//静态资源保存目录
	public static String wx_app_id;
	public static String wx_secret;
	public static String wx_merchants_id;//微信商户号
	public static String wx_app_key;//微信app_key,微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置\
	public static String zhu_tong_account;
	public static String zhu_tong_password;
	public ConfigUtils(Environment environment){
		static_dir=environment.getProperty("custom.static_dir");
		wx_app_id=environment.getProperty("custom.wx.app-id");
		wx_secret=environment.getProperty("custom.wx.secret");
		wx_merchants_id=environment.getProperty("custom.wx.merchants-id");
		wx_app_key=environment.getProperty("custom.wx.app-key");
		zhu_tong_account=environment.getProperty("custom.zhu_tong.account");
		zhu_tong_password=environment.getProperty("custom.zhu_tong.password");
	}
}
