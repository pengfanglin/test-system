package com.project.utils;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 极光推送
 */
@Component
public class JPushUtils {

	private static JPushClient jPushClient;
	private static Logger logger=LoggerFactory.getLogger(JSONUtils.class);
	@Autowired
	public JPushUtils(JPushClient jPushClient){
		JPushUtils.jPushClient=jPushClient;
	}

	public static void push(String content, String registrationId, String type) {
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.android_ios())
				.setAudience(Audience.alias(registrationId))
				.setMessage(Message.newBuilder().setMsgContent(content).addExtra("from", "JPush").addExtra("type", type==null?"":type).build())
				.setNotification(Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
						.setAlert(content).setBadge(1).setSound(type==null||"message".equals(type)||"boss_worker_bind".equals(type)?"default":"grab_work_order".equals(type)?"2.caf":"1.caf").addExtra("from", "JPush").addExtra("type", type==null?"":type).build()).build())
				.setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
				.build();
		try {
			jPushClient.sendPush(payload);
			logger.info("推送成功");
		} catch (APIConnectionException e) {
			logger.error("连接极光服务器失败");
		} catch (APIRequestException e) {
			logger.error(e.getStatus()+" "+e.getErrorCode()+" "+e.getErrorMessage());
		}
	}
	public static void push(String content, String registrationId) {
		push(content,registrationId,null);
	}
}
