package com.riking.calendar.pojo.server;

import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.pojo.server.base.BaseAuditProp;

/**
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
//@Comment("行业资讯的评论回复表")
//@Entity
//@Table(name = "t_nc_reply")
public class NCReply extends BaseAuditProp {
    public AppUserResp toUser;
    public AppUserResp fromUser;
    public String qACReplyId;

    //	@Comment("目标对象评论主键: fk t_news_comment 行业资讯的评论表")
//	@Column(name = "comment_id", nullable = false)
    public String commentId;

    //	@Comment("目标对象评论回复主键: fk t_nc_reply 回复ID")
//	谁回复谁的@Column(name = "reply_id")
    public String replyId;

    //	@Comment("内容")
//	@Column(name = "content", nullable = false)
    public String content;
}
