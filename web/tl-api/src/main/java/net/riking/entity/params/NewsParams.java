package net.riking.entity.params;

import java.io.Serializable;
import java.util.Date;

import net.riking.entity.MyDateFormat;

/**
 * 资讯类的接收参数
 * 
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class NewsParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6952211991459875719L;

	// 用户Id
	private String userId;

	// 请求方向（up上，down下）
	private String direct;

	// 资讯信息id
	private String newsId;

	// (0-取消；1-收藏)
	private Integer enabled;

	// 请求上翻最新时间戳
	@MyDateFormat(pattern = "yyyyMMddHHmmssSSS")
	private Date reqTimeStamp;

	// 评论内容
	private String content;

	// 屏蔽问题[1-问题;2-话题;3-屏蔽]
	private Integer objType;

	// 目标对象id
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

	public Date getReqTimeStamp() {
		return reqTimeStamp;
	}

	public void setReqTimeStamp(Date reqTimeStamp) {
		this.reqTimeStamp = reqTimeStamp;
	}

}
