package net.riking.test;

import java.lang.reflect.Field;

public class TestJG {

	public static void main(String[] args) throws Exception {
		/*JdpushUtil jdpushUtil = new JdpushUtil();
		Jdpush jdpush = new Jdpush();
		jdpush.setNotificationTitle("您的一次性认证码为123456，您在登录金融台历手机客户端，切勿将短信内容告诉他人");
		jdpush.setMsgTitle("这个是主题");
		jdpush.setMsgContent("这个是内容");
		jdpush.setExtrasparam("这是空");
		jdpush.setRegisrationId("160a3797c831a2c5b7f");
	   int result = jdpushUtil.sendToRegistrationId(jdpush);
	   System.err.println(result);*/
		Class c = Class.forName("net.riking.entity.model.Todo");
		/*Field[]  files = c.getDeclaredFields();
		StringBuffer sBuffer  = new StringBuffer();
		for (Field field : files) {
			sBuffer.append(field.getName()+" ");
			sBuffer.append(field.getType().getSimpleName()+" ");
			sBuffer.append(Modifier.toString(field.getModifiers())+";\n");
		}
		System.err.println(sBuffer);*/
		Field field = c.getDeclaredField("todoId");
		Object object = c.newInstance();
		field.setAccessible(true);
		field.set(object, "123qweasdzxc");
		System.err.println(field.get(object));
	}

}
