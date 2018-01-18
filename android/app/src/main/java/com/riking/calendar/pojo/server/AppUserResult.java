package com.riking.calendar.pojo.server;

import com.riking.calendar.pojo.server.base.BaseUser;

public class AppUserResult extends BaseUser{

    //    @Comment("姓名")
    public String userName;

    //    @Comment("头像url")
    public String photoUrl;

    //phone
    public String phone;

    //    @Comment("经验值")
    public int experience;

    //    @Comment("用户描述")
    public String descript;

    //    @Comment("等级")
    public int grade;

    //    @Comment("回答数")
    public int answerNum;

    //    @Comment("点赞数")
    public int agreeNum;

    // @Comment("是否已邀请 0-未邀请，1-已邀请 2 互相关注")
    public int isInvited;

    public int fansNum;
}
