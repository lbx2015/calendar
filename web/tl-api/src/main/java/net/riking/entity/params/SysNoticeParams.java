package net.riking.entity.params;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.riking.entity.BaseEntity;

/**
 * 消息通知接收参数
 * @author james.you
 * @version crateTime：2018年1月3日 下午8:01:15
 * @used TODO
 */
public class SysNoticeParams extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6952211991459875719L;

	// 用户Id
	private String userId;

	//多个消息id，以逗号分隔
	//private String noticeIds;
	
	
	private List<String> noticeIds;
	
	//单个消息id
	private String noticeId;
	
	//删除内容是否有系统消息：1-有；0-无
	private Integer haveSysInfo;
	
	//翻页时间戳
	@DateTimeFormat(pattern = "yyyyMMddHHmmssSSS")
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	private Date reqTimeStamp;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public List<String> getNoticeIds() {
		return noticeIds;
	}

	public void setNoticeIds(List<String> noticeIds) {
		this.noticeIds = noticeIds;
	}

	public Integer getHaveSysInfo() {
		return haveSysInfo;
	}

	public void setHaveSysInfo(Integer haveSysInfo) {
		this.haveSysInfo = haveSysInfo;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public Date getReqTimeStamp() {
		return reqTimeStamp;
	}

	public void setReqTimeStamp(Date reqTimeStamp) {
		this.reqTimeStamp = reqTimeStamp;
	}
	
}
