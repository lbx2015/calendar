package net.riking.entity.VO;

import java.util.Date;

import net.riking.entity.model.AppUser;
import net.riking.entity.model.TopicQuestion;

/**
 * WEB端：QuestionAnswer VO对象
 * 
 * @author fu.chen
 *
 */

public class QuestionAnswerVO {

	// 问题回答编号
	private String id;

	// 问题对象
	private TopicQuestion topicQuestion;

	// 回答人
	private AppUser appUser;

	// 回答时间
	private Date replyTime;

	// 评论数
	private String commentNum;

	// 评论审核数(未审核/不通过/已通过)
	private String isAduitNum;

	// 回复审核数(未审核/不通过/已通过)
	private String isAduitNumByReply;

	// 回答内容
	private String content;

	public TopicQuestion getTopicQuestion() {
		return topicQuestion;
	}

	public void setTopicQuestion(TopicQuestion topicQuestion) {
		this.topicQuestion = topicQuestion;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public String getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}

	public String getIsAduitNum() {
		return isAduitNum;
	}

	public void setIsAduitNum(String isAduitNum) {
		this.isAduitNum = isAduitNum;
	}

	public String getIsAduitNumByReply() {
		return isAduitNumByReply;
	}

	public void setIsAduitNumByReply(String isAduitNumByReply) {
		this.isAduitNumByReply = isAduitNumByReply;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
