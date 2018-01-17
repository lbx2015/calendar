package com.riking.calendar.pojo.server;

import com.riking.calendar.pojo.server.base.BaseUser;

/**
 * 其他用户信息
 *
 * @author jc.tan 2017年12月21日
 * @see
 * @since 1.0
 */
public class OtherUserResp extends BaseUser{

    // 用户名称
    public String userName;

    // 用户性别:1-男,0-女
    public int sex;

    // 个性签名
    public String descript;

    // 等级
    public int grade;

    // 用户头像（存放用户头像名称）
    public String photoUrl;

    // 回答数
    public int answerNum;

    // 关注数
    public int followNum;

    // 粉丝
    public int fansNum;

    //1可以查看，0不可以
    public int checkMyDynamicState = 1;
    public int checkMyFollowState = 1;
    public int checkMyCollectState = 0;
    public String phone;

}
