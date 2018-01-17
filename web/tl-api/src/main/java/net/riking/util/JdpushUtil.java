package net.riking.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
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
			if(StringUtils.isBlank(jdpush.getRegisrationId())){
				throw new Exception("发送给指定用户 时，Jdpush的regisrationId 值不能为空");
			}
			PushPayload pushPayload = JdpushUtil.buildPushObject_android_and_ios(jdpush);
			System.err.println(pushPayload);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			System.err.println(pushResult);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
			
		} catch (Exception e) {
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
	public static int sendToAll(Jdpush jdpush) {
		int result = 0;
		try {
			if(StringUtils.isNotBlank(jdpush.getRegisrationId())){
				throw new Exception("发送给所有用户 时，Jdpush不能指定regisrationId");
			}
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
	
	/**
	 * 发给所有用户  或   指定的某个用户
	 * @used TODO
	 * @param jdpush
	 * @return
	 */
    public static PushPayload buildPushObject_android_and_ios(Jdpush jdpush) {
    	//默认所有用户
    	Audience audience = Audience.all();
    	//指定某个用户
    	if(StringUtils.isNotBlank(jdpush.getRegisrationId())){
    		audience = Audience.registrationId(jdpush.getRegisrationId());
    	}
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(audience)
                .setNotification(Notification.newBuilder()
                		.setAlert(jdpush.getJpushContent())
                		//android
                		.addPlatformNotification(AndroidNotification.newBuilder()
                				.setTitle(jdpush.getJpushTitle())
                				.addExtras(jdpush.getExtrasMap()).build())
                		//ios
                		.addPlatformNotification(IosNotification.newBuilder()
                				.setAlert(IosAlert.newBuilder()
                						.setTitleAndBody(jdpush.getJpushTitle(), "",jdpush.getJpushContent())
                						.build())
                				.setSound("sound.caf")
                				.incrBadge(1)
                				.addExtras(jdpush.getExtrasMap()).build())
                		.build())
                .build();
    }
    
	/**
	 * 所有android用户
	 * @used TODO
	 * @param jdpush
	 * @return
	 */
    public static PushPayload buildPushObject_android_all_alertWithTitle(Jdpush jdpush) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                		.setAlert(jdpush.getJpushContent())
                		//android
                		.addPlatformNotification(AndroidNotification.newBuilder()
                				.setTitle(jdpush.getJpushTitle())
                				.addExtras(jdpush.getExtrasMap()).build())
                		//ios
                		.addPlatformNotification(IosNotification.newBuilder()
                				.setAlert(IosAlert.newBuilder()
                						.setTitleAndBody(jdpush.getJpushTitle(), "",jdpush.getJpushContent())
                						.build())
                				.setSound("sound.caf")
                				.incrBadge(1)
                				.addExtras(jdpush.getExtrasMap()).build())
                		.build())
                .build();
    }
    
	/**
	 * 所有IOS用户
	 * @used TODO
	 * @param jdpush
	 * @return
	 */
    public static PushPayload buildPushObject_ios_all_alertWithTitle(Jdpush jdpush) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                		.setAlert(jdpush.getJpushContent())
                		//android
                		.addPlatformNotification(AndroidNotification.newBuilder()
                				.setTitle(jdpush.getJpushTitle())
                				.addExtras(jdpush.getExtrasMap()).build())
                		//ios
                		.addPlatformNotification(IosNotification.newBuilder()
                				.setAlert(IosAlert.newBuilder()
                						.setTitleAndBody(jdpush.getJpushTitle(), "",jdpush.getJpushContent())
                						.build())
                				.setSound("sound.caf")
                				.incrBadge(1)
                				.addExtras(jdpush.getExtrasMap()).build())
                		.build())
                .build();
    }


/*	public static PushPayload buildPushObject_android_and_ios(Jdpush jdpush) {
		return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.all())
				.setNotification(Notification.newBuilder().setAlert(jdpush.getNotificationTitle())
						.addPlatformNotification(AndroidNotification.newBuilder()
//								.setAlert(StringUtils.isNotBlank(jdpush.getMsgContent())? jdpush.getMsgContent().substring(0, jdpush.getMsgContent().length()>=50?50:jdpush.getMsgContent().length()):"")
								.setAlert(new Alert(jdpush.getMsgTitle(), jdpush.getMsgContent()).toJSON())
//								.setTitle(jdpush.getNotificationTitle())
								// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
								.addExtras(jdpush.getExtrasMap())
								.addExtra("androidNotification extras key", jdpush.getExtrasparam()).build())
						.addPlatformNotification(IosNotification.newBuilder()
								// 传一个IosAlert对象，指定apns title、title、subtitle等
//								.setAlert(jdpush.getMsgTitle()+ (StringUtils.isNotBlank(jdpush.getMsgContent())? "\n"+jdpush.getMsgTitle()+(StringUtils.isNotBlank(jdpush.getMsgContent())? "\n"+jdpush.getMsgContent().substring(0, jdpush.getMsgContent().length()>=50?50:jdpush.getMsgContent().length()):""):""))
								.setAlert(new Alert(jdpush.getMsgTitle(), jdpush.getMsgContent()).toJSON())
								// 直接传alert
								// 此项是指定此推送的badge自动加1
								.incrBadge(1)
								// 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
								// 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
								.setSound("default")
								.addExtras(jdpush.getExtrasMap())
								// 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
								.addExtra("iosNotification extras key", jdpush.getExtrasparam())
								// 此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
								.setContentAvailable(true).build())
						.build())
				// Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
				// sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
				// [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
				.setMessage(Message.newBuilder().setMsgContent(jdpush.getMsgContent())
						.setTitle(jdpush.getMsgTitle())
						.addExtras(jdpush.getExtrasMap())
						.addExtra("message extras key", jdpush.getExtrasparam()).build())

				.setOptions(Options.newBuilder()
						// 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
						.setApnsProduction(false)
						// 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
						.setSendno(1)
						// 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
						.setTimeToLive(86400).build())
				.build();
	}*/

}
