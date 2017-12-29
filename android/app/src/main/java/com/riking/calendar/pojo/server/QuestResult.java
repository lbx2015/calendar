package com.riking.calendar.pojo.server;

import java.util.Date;

public class QuestResult {
//    @Comment("物理主键")
//    @JsonProperty("questionId")
    public String questionId;

//    @Comment("标题")
    public String title;

//    @Comment("内容")
    public String content;

//    @Comment("创建时间")
//    @JsonFormat(pattern = "yyyyMMddHHmmssSSS")
    public Date createdTime;

    // 问题关注数
//    @Transient
    public int tqFollowNum;

    // 是否关注 1-已关注，0-未关注
//    @Transient
    public int isFollow;

    // 问题回答数
//    @Transient
    public int qanswerNum;

    // 用户名
//    @Transient
    public String userName;

    // 经验值
//    @Transient
    public int experience;

    // 经验值
//    @Transient
    public int grade;

    // 用户头像Url
//    @Transient
    public String photoUrl;

}
