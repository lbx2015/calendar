package net.riking.entity.params;

import java.io.Serializable;

/**
 * App版本获取接口参数
 * @author james.you
 * @version crateTime：2017年11月28日 下午2:39:26
 * @used TODO
 */
public class CommentParams implements Serializable {
	private static final long serialVersionUID = 1L;

	// 用户id
	private String userId;

	// 评论ID
	private String commentId;

	// 1-回答类；2-资讯类
	private Integer objType;

	// 内容
	private String content;

	// 0-取消；1-赞同
	private Integer enabled;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getObjType() {
		return objType;
	}

	public void setObjType(Integer objType) {
		this.objType = objType;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

}
