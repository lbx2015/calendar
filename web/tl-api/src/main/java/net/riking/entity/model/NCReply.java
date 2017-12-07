package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseAuditProp;
import net.riking.entity.resp.FromUser;
import net.riking.entity.resp.ToUser;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("行业资讯的评论回复表")
@Entity
@Table(name = "t_nc_reply")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class NCReply extends BaseAuditProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1461884746264578424L;

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@JsonProperty("nCReplyId")
	private String id;

	@Comment("操作人主键: fk t_app_user 发表回复的user_id")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Comment("被操作人主键: fk t_app_user 被评论人ID")
	@Column(name = "to_user_id")
	private String toUserId;

	@Comment("目标对象评论主键: fk t_news_comment 行业资讯的评论表")
	@Column(name = "comment_id", nullable = false)
	private String commentId;

	@Comment("目标对象评论回复主键: fk t_nc_reply 回复ID")
	@Column(name = "reply_id")
	private String replyId;

	@Comment("内容")
	@Column(name = "content", nullable = false)
	private String content;

	@Transient
	FromUser fromUser;

	@Transient
	ToUser toUser;

	@Transient
	private String userName;

	@Transient
	private String toUserName;

	public NCReply() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NCReply(String id, Date createdTime, Date modifiedTime, String userId, String toUserId, String commentId,
			String replyId, String content, String userName, String toUserName) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setModifiedTime(modifiedTime);
		this.userId = userId;
		this.toUserId = toUserId;
		this.commentId = commentId;
		this.replyId = replyId;
		this.content = content;
		this.userName = userName;
		this.toUserName = toUserName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public FromUser getFromUser() {
		return fromUser;
	}

	public void setFromUser(FromUser fromUser) {
		this.fromUser = fromUser;
	}

	public ToUser getToUser() {
		return toUser;
	}

	public void setToUser(ToUser toUser) {
		this.toUser = toUser;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

}
