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
    public Integer experience;

    //	@Comment("回答数")
    public Integer answerNum;

    //	@Comment("点赞数")
    public Integer agreeNum;

    //	@Comment("是否已关注 0-未关注，1-已关注")
    public Integer isFollow;

    //	@Comment("是否已邀请 0-未邀请，1-已邀请")
    public Integer isInvited;

}
