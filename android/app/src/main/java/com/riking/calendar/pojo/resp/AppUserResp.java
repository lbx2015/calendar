package com.riking.calendar.pojo.resp;

import java.io.Serializable;

/**
 * APP用户信息返回
 */
public class AppUserResp {
    //1可以查看，0不可以
    public int checkMyDynamicState = 1;
    public int checkMyFollowState = 1;
    public int checkMyCollectState = 0;
    //微信昵称
    public String wechatNickName;
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
    public int sex;

    // 出身日期(yyyyMMdd)
    public String birthday;

    // 地址
    public String address;

    // 个性签名
    public String descript;

    // 手机Deviceid
    public String phoneDeviceid;

    // 积分
    public int integral;

    // 经验值
    public int experience;

    // 等级
    public int grade;

    // 用户头像（存放用户头像名称）
    public String photoUrl;

    // 全天提醒时间 HHmm时分
    public String remindTime;

    // 是否已订阅: 0-未订阅；1-已订阅
    public int isSubscribe;

    // 行业ID
    public String industryId;

    // 职位ID
    public String positionId;

    // 行业名字
    public String industryName;

    // 职位名字
    public String positionName;

    // 是否引导: 0-未引导；1-已引导
    public int isGuide;

    // 用户邮箱是否已认证： 0-未认证 1-已认证
    public int isIdentify;
}
