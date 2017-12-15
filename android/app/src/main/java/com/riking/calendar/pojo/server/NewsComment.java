package com.riking.calendar.pojo.server;

import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.pojo.server.base.BaseAuditProp;

import java.util.List;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//
//import net.riking.core.annos.Comment;
//import net.riking.entity.BaseAuditProp;

/**
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
//@Comment("行业资讯的评论表")
//@Entity
//@Table(name = "t_news_comment")
public class NewsComment extends BaseAuditProp {
    public String newsCommentId;

    //	@Comment("操作人主键 : fk t_app_user 发表评论的用户id")
//	@Column(name = "user_id", nullable = false)
    public String userId;

    //	@Comment("目标对象主键: fk t_news 行业资讯id")
//	@Column(name = "news_id", nullable = false)
    public String newsId;

    //	@Comment("内容")
//	@Column(name = "content", nullable = false)
    public String content;

    // 用户名称
//	@Transient
    public String userName;

    // 点赞数
//	@Transient
    public Integer agreeNumber;

    // 用户头像Url
//	@Transient
    public String photoUrl;

    // 用户经验值
//	@Transient
    public Integer experience;

    // 评论的回复list
//	@Transient
    public List<NCReply> ncReplyList;

    //0 disagree 1 agree
    public int isAgree;

}
