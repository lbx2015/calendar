package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.riking.entity.BaseEntity;

@Entity
@Table(name = "t_remind_his")
public class RemindHis extends BaseEntity {
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	// 用户Id
	@Column(name = "user_id", length = 32)
	private String userId;

	// 提醒内容
	@Column(name = "content", length = 255)
	private String content;

	// 提醒时间
	@Column(name = "start_time", length = 4)
	private String startTime;

	// 提醒日期
	@Column(name = "str_date", length = 8)
	private String strDate;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	@Override
	public String toString() {
		return "RemindHis [id=" + id + ", userId=" + userId + ", content=" + content + ", startTime=" + startTime
				+ ", strDate=" + strDate + "]";
	}

}
