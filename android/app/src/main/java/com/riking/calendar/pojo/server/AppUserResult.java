package com.riking.calendar.pojo.server;

public class AppUserResult {

    //	@Comment("物理主键")
//	@JsonProperty("userId")
    public String userId;

    //	@Comment("姓名")
    public String userName;

    //	@Comment("头像url")
    public String photoUrl;

    //	@Comment("经验值")
    public int experience;

    //	@Comment("回答数")
    public int answerNum;

    //	@Comment("点赞数")
    public int agreeNum;

    //	@Comment("是否已关注 0-未关注，1-已关注")
    public int isFollow;

    //	@Comment("是否已邀请 0-未邀请，1-已邀请")
    public int isInvited;

}
