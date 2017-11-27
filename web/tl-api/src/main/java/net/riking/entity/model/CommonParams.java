package net.riking.entity.model;

import java.io.Serializable;

import javax.persistence.Transient;

public class CommonParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6952211991459875719L;

	// 用户Id
	@Transient
	private String userId;

	// 请求方向（up上，down下）
	@Transient
	private String direct;

	// 资讯信息id
	@Transient
	private String newsId;

	// (0-取消；1-收藏)
	@Transient
	private Integer enabled;

	// 请求上翻最新时间戳
	@Transient
	private String reqTimeStamp;

	// 评论内容
	@Transient
	private String content;

	// 屏蔽问题[1-问题；2-话题]
	@Transient
	private Integer objType;

	// 目标对象id
	@Transient
	private String objId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getObjType() {
		return objType;
	}

	public void setObjType(Integer objType) {
		this.objType = objType;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

	public String getReqTimeStamp() {
		return reqTimeStamp;
	}

	public void setReqTimeStamp(String reqTimeStamp) {
		this.reqTimeStamp = reqTimeStamp;
	}

}
