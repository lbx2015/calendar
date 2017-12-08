package com.riking.calendar.pojo.server;

import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.pojo.server.base.BaseAuditProp;


/**
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
//@Comment("问题回答评论回复信息 表")
//@Entity
//@Table(name = "t_qac_reply")
public class QACReply extends BaseAuditProp {

    public String qACReplyId;
    //	@Transient
    public AppUserResp fromUser;
    //	@Transient
    public AppUserResp toUser;
    //	@Comment("操作人主键 : fk t_app_user 发表回复的user_id")
//	@Column(name = "from_user_id", nullable = false)
    public String fromUserId;
    //	@Comment("被操作人主键: fk t_app_user 被评论人ID")
//	@Column(name = "to_user_id")
    public String toUserId;
    //	@Comment("目标对象评论主键: fk t_qa_comment")
//	@Column(name = "comment_id", nullable = false)
    public String commentId;
    //	@Comment("目标对象评论回复主键: fk t_qac_reply 回复ID")
//	@Column(name = "reply_id")
    public String replyId;
    //	@Comment("内容")
//	@Column(name = "content", nullable = false)
    public String content;

}
