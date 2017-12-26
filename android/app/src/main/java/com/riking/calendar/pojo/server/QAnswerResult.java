package com.riking.calendar.pojo.server;

import java.util.Date;

public class QAnswerResult {
    //    @Comment("回答id")
    public String tqId;

    //    @Comment("问题回答id")
    public String qaId;

    // 用户id
//    @Transient
    public String userId;

    //    @Comment("标题")
    public String title;

    //    @Comment("内容")
    public String content;

    //    @Comment("创建时间")
//    @JsonFormat(pattern = "yyyyMMddHHmmssSSS")
    public Date createdTime;

    // 用户名
//    @Transient
    public String userName;

    // 用户头像Url
//    @Transient
    public String photoUrl;

    // 经验值
//    @Transient
    public int experience;

    // 等级
//    @Transient
    public int grade;

    // 封面url
//    @Transient
    public String coverUrl;

    // 问题回答点赞数
//    @Transient
    public int qaAgreeNum;

    // 问题回答评论数
//    @Transient
    public int qaCommentNum;

    // 是否已点赞 0-未点赞，1-已点赞
//    @Transient
    public int isAgree;
}
