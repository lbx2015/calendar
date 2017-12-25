package com.riking.calendar.pojo.server;

import com.riking.calendar.pojo.server.base.BaseAuditProp;

import java.util.List;

//@Comment("问题回答的评论 表")
//@Entity
//@Table(name = "t_qa_comment")
public class QAComment extends BaseAuditProp {

//    @Comment("物理主键")
//    @Id
//    @Column(name = "id", length = 32)
//    @GenericGenerator(name = "system-uuid", strategy = "uuid")
//    @GeneratedValue(generator = "system-uuid")
//    @JsonProperty("qACommentId")
    public String qACommentId;

//    @Comment("操作人主键  ")
//    @Column(name = "user_id", nullable = false)
    public String userId;

//    @Comment("目标对象主键")
//    @Column(name = "question_answer_id", nullable = false)
    public String questionAnswerId;

//    @Comment("内容")
//    @Column(name = "content", nullable = false)
    public String content;

    // 用户名
//    @Transient
    public String userName;

    // 用户头像
//    @Transient
    public String photoUrl;

    // 点赞数
//    @Transient
    public Integer agreeNumber;

    // 经验值
//    @Transient
    public Integer experience;

    // 等级
//    @Transient
    public Integer grade;

//    @Transient
//    @Comment("是否已点赞 0-未点赞，1-已点赞")
    public Integer isAgree;

    // 问题回答评论的回复list
//	@Transient
    public List<NCReply> qacReplyList;
}
