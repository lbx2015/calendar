package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import net.riking.entity.BaseEntity;

@Entity
@Table(name = "t_app_log_info")
public class AppLogInfo extends BaseEntity{
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	
	//app用户id
	@Column(name = "app_user_id", length = 32)
	private String appUserId;
	
	//创建时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 32)
	private Date createTime;
	
	//做的什么事情
	@Column(name = "do_thing", length = 32)
	private String doThing;
	
	//备注信息
	@Column(name = "remark", length = 500)
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDoThing() {
		return doThing;
	}

	public void setDoThing(String doThing) {
		this.doThing = doThing;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
