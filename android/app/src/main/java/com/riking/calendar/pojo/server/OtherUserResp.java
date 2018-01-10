package com.riking.calendar.pojo.server;

/**
 * 其他用户信息
 *
 * @author jc.tan 2017年12月21日
 * @see
 * @since 1.0
 */
public class OtherUserResp {
    // 其他用户ID
    public String userId;

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

    // 是否已关注
    public int isFollow;

}
