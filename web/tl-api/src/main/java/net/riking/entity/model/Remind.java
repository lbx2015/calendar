package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import net.riking.entity.BaseEntity;

@Entity
@Table(name = "t_remind")
public class Remind extends BaseEntity {
	
	/**
	 * @author Lucky.Liu on 2017/8/05.
	 */
	
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

	// 是否全天提醒(0-否；1-是)
	@Column(name = "is_allday", length = 1)
	private int isAllday;

	// 提前提醒时间（不选默认为0）
	@Column(name = "before_time", length = 2)
	private int beforeTime;

	// 结束时间：HHmm
	@Column(name = "end_time", length = 4)
	private String endTime;

	// 重复标识（0-不重复；1-法定工作日；2-法定节假日；3-其它）
	@Column(name = "repeat_flag", length = 8)
	private int repeatFlag;

	// 重复值(例如0,1,2,3,4,5,6)
	@Column(name = "repeat_value", length = 20)
	private String repeatValue;

	// 当前星期数（0,6）
	@Column(name = "curr_week", length = 1)
	private int currWeek;
	
	@Transient
	private int deleteState;

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

	public int getIsAllday() {
		return isAllday;
	}

	public void setIsAllday(int isAllday) {
		this.isAllday = isAllday;
	}

	public int getBeforeTime() {
		return beforeTime;
	}

	public void setBeforeTime(int beforeTime) {
		this.beforeTime = beforeTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getRepeatFlag() {
		return repeatFlag;
	}

	public void setRepeatFlag(int repeatFlag) {
		this.repeatFlag = repeatFlag;
	}

	public String getRepeatValue() {
		return repeatValue;
	}

	public void setRepeatValue(String repeatValue) {
		this.repeatValue = repeatValue;
	}

	public int getCurrWeek() {
		return currWeek;
	}

	public void setCurrWeek(int currWeek) {
		this.currWeek = currWeek;
	}

	public int getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(int deleteState) {
		this.deleteState = deleteState;
	}
	
	
}
