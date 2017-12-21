package com.riking.calendar.pojo.resp;

import java.io.Serializable;

/**
 * APP用户信息返回
 */
public class AppUserResp implements Serializable {
	// 用户ID
	public String userId;

	// 用户名称
	public String userName;

	// 微信openid
	public String openId;

	// 用户邮箱
	public String email;

	// 手机号
	public String phone;

	// 真实姓名
	public String realName;

	// 公司名称
	public String companyName;

	// 用户性别:1-男,0-女
	public Integer sex;

	// 出身日期(yyyyMMdd)
	public String birthday;

	// 地址
	public String address;

	// 个性签名
	public String description;

	// 手机Macid
	public String phoneMacid;

	// 积分
	public Integer integral;

	// 经验值
	public Integer experience;

	// 用户头像（存放用户头像名称）
	public String photoUrl;

	// 全天提醒时间 HHmm时分
	public String remindTime;

	// 是否已订阅: 0-未订阅；1-已订阅
	public int isSubscribe;

	// 行业ID
	public int industryId;

	// 职位ID
	public int positionId;

	// 是否引导: 0-未引导；1-已引导
	public int isGuide;

}
