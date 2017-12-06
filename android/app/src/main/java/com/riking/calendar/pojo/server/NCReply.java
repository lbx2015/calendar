package com.riking.calendar.pojo.server;

import com.riking.calendar.pojo.server.base.BaseAuditProp;

import java.util.Date;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//
//import net.riking.core.annos.Comment;
//import net.riking.entity.BaseAuditProp;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
//@Comment("行业资讯的评论回复表")
//@Entity
//@Table(name = "t_nc_reply")
public class NCReply extends BaseAuditProp {
	public String nCReplyId;
//	@Comment("操作人主键: fk t_app_user 发表回复的user_id")
//	@Column(name = "user_id", nullable = false)
	public String userId;

//	@Comment("被操作人主键: fk t_app_user 被评论人ID")
//	@Column(name = "to_user_id")
	public String toUserId;

//	@Comment("目标对象评论主键: fk t_news_comment 行业资讯的评论表")
//	@Column(name = "comment_id", nullable = false)
	public String commentId;

//	@Comment("目标对象评论回复主键: fk t_nc_reply 回复ID")
//	@Column(name = "reply_id")
	public String replyId;

//	@Comment("内容")
//	@Column(name = "content", nullable = false)
	public String content;

//	@Transient
	public String userName;

//	@Transient
	public String toUserName;
}
