package net.riking.test;

import net.riking.entity.model.Jdpush;
import net.riking.util.JdpushUtil;

public class TestJG {

	public static void main(String[] args) {
		JdpushUtil jdpushUtil = new JdpushUtil();
		Jdpush jdpush = new Jdpush();
		jdpush.setNotificationTitle("您的一次性认证码为123456，您在登录金融台历手机客户端，切勿将短信内容告诉他人");
		jdpush.setMsgTitle("这个是主题");
		jdpush.setMsgContent("这个是内容");
		jdpush.setExtrasparam("这是空");
		jdpush.setRegisrationId("160a3797c831a2c5b7f");
		//160a3797c831a2c5b7f
		//18171adc0338eaeaf9e
	   int result = jdpushUtil.sendToRegistrationId(jdpush);
	   System.err.println(result);
		/*
		 * GetDateServiceImpl Test = new GetDateServiceImpl(); String map =
		 * GetDateServiceImpl.getDayOfWeekByDate("201708011");
		 */
	}

}
