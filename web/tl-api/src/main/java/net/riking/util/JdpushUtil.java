package net.riking.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import net.riking.entity.model.Jdpush;
@Component("jdpushUtil")
public class JdpushUtil {
	protected static final Logger LOG = LoggerFactory.getLogger(JdpushUtil.class);
	private static JPushClient jPushClient = new JPushClient("ae4b5cb2379495f2303019ff", "f7ac0692d540d2a7e15613bb");;
	/**
	 * 推送给指定用户
	 */
	public static int sendToRegistrationId(Jdpush jdpush) {
		int result = 0;
		try {
			if(StringUtils.isNotBlank(jdpush.getRegisrationId())){
				PushPayload pushPayload = JdpushUtil.buildPushObject_all_registrationId_alertWithTitle(jdpush);
				System.out.println(pushPayload);
				PushResult pushResult = jPushClient.sendPush(pushPayload);
				System.out.println(pushResult);
				if (pushResult.getResponseCode() == 200) {
					result = 1;
				}
			}
		} catch (APIConnectionException e) {
			e.printStackTrace();

		} catch (APIRequestException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 发送给所有安卓用户*/
	public static  int sendToAllAndroid(Jdpush jdpush) {
		int result = 0;
		try {
			PushPayload pushPayload = JdpushUtil.buildPushObject_android_all_alertWithTitle(jdpush);
			System.out.println(pushPayload);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			System.out.println(pushResult);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送给所有IOS用户
	 */
	public static  int sendToAllIos(Jdpush jdpush) {
		int result = 0;
		try {
			PushPayload pushPayload = JdpushUtil.buildPushObject_ios_all_alertWithTitle(jdpush);
			System.out.println(pushPayload);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			System.out.println(pushResult);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 发送给所有用户
	 */
	public static  int sendToAll(Jdpush jdpush) {
		int result = 0;
		try {
			PushPayload pushPayload = JdpushUtil.buildPushObject_android_and_ios(jdpush);
			System.out.println(pushPayload);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			System.out.println(pushResult);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public static PushPayload buildPushObject_android_and_ios(Jdpush jdpush) {
		return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.all())
				.setNotification(Notification.newBuilder().setAlert(jdpush.getNotificationTitle())
						.addPlatformNotification(AndroidNotification.newBuilder()
								.setAlert(jdpush.getNotificationTitle()).setTitle(jdpush.getNotificationTitle())
								// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
								.addExtra("androidNotification extras key", jdpush.getExtrasparam()).build())
						.addPlatformNotification(IosNotification.newBuilder()
								// 传一个IosAlert对象，指定apns title、title、subtitle等
								.setAlert(jdpush.getNotificationTitle())
								// 直接传alert
								// 此项是指定此推送的badge自动加1
								.incrBadge(1)
								// 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
								// 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
								.setSound("default")
								// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
								.addExtra("iosNotification extras key", jdpush.getExtrasparam())
								// 此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
								.setContentAvailable(true).build())
						.build())
				// Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
				// sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
				// [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
				.setMessage(Message.newBuilder().setMsgContent(jdpush.getMsgContent()).setTitle(jdpush.getMsgTitle())
						.addExtra("message extras key", jdpush.getExtrasparam()).build())

				.setOptions(Options.newBuilder()
						// 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
						.setApnsProduction(false)
						// 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
						.setSendno(1)
						// 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
						.setTimeToLive(86400).build())
				.build();
	}

	private static PushPayload buildPushObject_all_registrationId_alertWithTitle(Jdpush jdpush) {
		System.out.println("----------buildPushObject_all_all_alert");
		// 创建一个IosAlert对象，可指定APNs的alert、title等字段
		// IosAlert iosAlert = IosAlert.newBuilder().setTitleAndBody("title",
		// "alert body").build();
		return PushPayload.newBuilder()
				// 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
				.setPlatform(Platform.all())
				// 指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration
				// id
				.setAudience(Audience.registrationId(jdpush.getRegisrationId()))
				// jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
				.setNotification(Notification.newBuilder()
						// 指定当前推送的android通知
						.addPlatformNotification(AndroidNotification.newBuilder()

								.setAlert(jdpush.getNotificationTitle()).setTitle(jdpush.getNotificationTitle())
								// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
								.addExtra("androidNotification extras key", jdpush.getExtrasparam())

								.build())
						// 指定当前推送的iOS通知
						.addPlatformNotification(IosNotification.newBuilder()
								// 传一个IosAlert对象，指定apns title、title、subtitle等
								.setAlert(jdpush.getNotificationTitle())
								// 直接传alert
								// 此项是指定此推送的badge自动加1
								.incrBadge(1)
								// 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
								// 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
								.setSound("sound.caf")
								// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
								.addExtra("iosNotification extras key", jdpush.getExtrasparam())
								// 此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
								// 取消此注释，消息推送时ios将无法在锁屏情况接收
								// .setContentAvailable(true)

								.build())

						.build())
				// Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
				// sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
				// [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
				.setMessage(Message.newBuilder()

						.setMsgContent(jdpush.getMsgContent())

						.setTitle(jdpush.getMsgTitle())

						.addExtra("message extras key", jdpush.getExtrasparam())

						.build())

				.setOptions(Options.newBuilder()
						// 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
						.setApnsProduction(false)
						// 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
						.setSendno(1)
						// 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
						.setTimeToLive(86400)

						.build())

				.build();

	}

	private static PushPayload buildPushObject_android_all_alertWithTitle(Jdpush jdpush) {
		System.out.println("----------buildPushObject_android_registrationId_alertWithTitle");
		return PushPayload.newBuilder()
				// 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
				.setPlatform(Platform.android())
				// 指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration
				// id
				.setAudience(Audience.all())
				// jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
				.setNotification(Notification.newBuilder()
						// 指定当前推送的android通知
						.addPlatformNotification(AndroidNotification.newBuilder()
								.setAlert(jdpush.getNotificationTitle()).setTitle(jdpush.getNotificationTitle())
								// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
								.addExtra("androidNotification extras key", jdpush.getExtrasparam()).build())
						.build())
				// Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
				// sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
				// [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
				.setMessage(Message.newBuilder().setMsgContent(jdpush.getMsgContent()).setTitle(jdpush.getMsgTitle())
						.addExtra("message extras key", jdpush.getExtrasparam()).build())
				.setOptions(Options.newBuilder()
						// 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
						.setApnsProduction(false)
						// 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
						.setSendno(1)
						// 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
						.setTimeToLive(86400).build())
				.build();
	}

	private static PushPayload buildPushObject_ios_all_alertWithTitle(Jdpush jdpush) {
		System.out.println("----------buildPushObject_ios_registrationId_alertWithTitle");
		return PushPayload.newBuilder()
				// 指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
				.setPlatform(Platform.ios())
				// 指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration
				// id
				.setAudience(Audience.all())
				// jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
				.setNotification(Notification.newBuilder()
						// 指定当前推送的android通知
						.addPlatformNotification(IosNotification.newBuilder()
								// 传一个IosAlert对象，指定apns title、title、subtitle等
								.setAlert(jdpush.getNotificationTitle())
								// 直接传alert
								// 此项是指定此推送的badge自动加1
								.incrBadge(1)
								// 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
								// 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
								.setSound("sound.caf")
								// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
								.addExtra("iosNotification extras key", jdpush.getExtrasparam())
								// 此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
								// .setContentAvailable(true)

								.build())
						.build())
				// Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
				// sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
				// [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
				.setMessage(Message.newBuilder().setMsgContent(jdpush.getMsgContent()).setTitle(jdpush.getMsgTitle())
						.addExtra("message extras key", jdpush.getExtrasparam()).build())
				.setOptions(Options.newBuilder()
						// 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
						.setApnsProduction(false)
						// 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
						.setSendno(1)
						// 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
						.setTimeToLive(86400).build())
				.build();
	}
}
