package com.riking.calendar.pojo.params;

/**
 * 更新用户信息参数接收
 * @author james.you
 * @version crateTime：2017年11月29日 下午3:16:14
 * @used TODO
 */
public class UpdUserParams extends BaseParams {
	// 用户名称
	public String userName;
	// 行业名字
	public String industryName;

	// 职位名字
	public String positionName;

	// 微信openid
	public String openId;

	// 用户邮箱
	public String email;

	// 手机号
	public String phone;

	// 用户状态 0-禁用 1-启用
	public String enabled;

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

	// 手机Deviceid
	public String phoneDeviceid;

	// 积分
	public Integer integral;

	// 经验值
	public Integer experience;

	// 用户头像（存放用户头像名称）
	public String photoUrl;

	// 全天提醒时间 HHmm时分
	public String remindTime;

	// 是否已订阅: 0-未订阅；1-已订阅
	public Integer isSubscribe;

	// 行业ID
	public Integer industryId;

	// 职位ID
	public Integer positionId;

	// 是否引导: 0-未引导；1-已引导
	public Integer isGuide;

}
