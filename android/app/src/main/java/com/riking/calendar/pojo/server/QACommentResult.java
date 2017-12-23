package com.riking.calendar.pojo.server;

import java.util.Date;

/**
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
//@Comment("我的动态问题回答评论列表")
public class QACommentResult {
    //	@JsonProperty("qACommentId")
    public String qACommentId;

    //	@Comment("创建时间")
//	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
    public Date createdTime;

    //	@Comment("修改时间")
//	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
    public Date modifiedTime;

    //	@Comment("操作人主键  ")
    public String userId;

    //	@Comment("问题回答id")
    public String questionAnswerId;

    //	@Comment("内容")
    public String content;

    //	@Comment("问题回答内容")
    public String qaContent;

    //	@Comment("问题id")
    public String tqId;

    // 用户名
    public String userName;

    // 用户头像
    public String photoUrl;

    // 经验值
    public int experience;

    // 点赞数
    public int agreeNumber;

    // 等级
    public int grade;

    public int isAgree;

    //	@Comment("问题标题")
    public String tqTitle;

}
