package com.riking.calendar.pojo.server;

import java.util.Date;
import java.util.List;

public class TQuestionResult {
    //回答是否点赞
    public byte isAgree;
    //话题的ID
    public String topicId;
    //	@Comment("问题主键")
    public String tqId;

    //	@Comment("问题回答主键")
    public String qaId;

    //	@Comment("问题标题")
    public String tqTitle;

    //	@Comment("话题的标题")
    public String topicTitle;

    // 创建时间
//	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
    public Date createdTime;

    //	@Comment("问题的回答内容")
    public String qaContent;

    //	@Comment("用户Id")
    public String userId;

    //	@Comment("用户名")
    public String userName;

    //	@Comment("用户/话题头像")
    public String fromImgUrl;

    //	@Comment("经验值")
    public int experience;

    //	@Comment("问题回答的评论数")
    public int qaCommentNum;

    //	@Comment("问题回答的点赞数")
    public int qaAgreeNum;

    //	@Comment("问题的关注数")
    public int qfollowNum;

    //	@Comment("话题关注数")
    public int tfollowNum;

    //	@Comment("问题的回答数")
    public int qanswerNum;

    //	@Comment("回答封面url")
    public String coverUrl;

    /**
     * 1-根据用户关注的话题推送问题
     * 2-查询关注的用户点赞的回答
     * 3-关注的用户关注的问题
     * 4-关注的用户回答的问题
     * 5-可能感兴趣的话题
     * 6-可能感兴趣的人
     * 7-查询关注的用户收藏的回答
     */
    public int pushType;

    //	@Comment("可能感兴趣的话题")
    public List<TopicResult> topicResults;

    //	@Comment("可能感兴趣的人")
    public List<AppUserResult> appUserResults;

}
