package com.riking.calendar.pojo.synch;

import java.io.Serializable;

/**
 * 登录及注册参数
 *
 * @author james.you
 * @version crateTime：2017年11月29日 下午3:16:14
 * @used TODO
 */
public class LoginParams implements Serializable {
	public static final long serialVersionUID = 1L;
	
	//手机号
	public String phone;
	//微信openId
	public String openId;
	//输入的验证码
	public String verifyCode;
	//类型：1-手机号,2-微信号
	public int type=1;
	//客户端类型：1-IOS；2-Android
	public int clientType=2;
	//手机端deviceId
	public String phoneDeviceId;
	
}
