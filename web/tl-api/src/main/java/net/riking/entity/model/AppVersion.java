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

import net.riking.entity.BaseEntity;

/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月5日 下午5:38:07
 * @used TODO
 */
@Entity
@Table(name = "t_app_version")
public class AppVersion extends BaseEntity {
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	// app版本号
	@Column(name = "version_number", length = 32)
	private String versionNumber;

	// 版本说明
	@Column(name = "version_note", length = 255)
	private String versionNote;

	// 更新日期
	@Temporal(TemporalType.DATE)
	@Column(name = "renewal_time")
	private Date renewalTime;

	// 删除状态 0删除 1显示
	@Column(name = "delete_state", length = 2)
	private String deleteState;

	@Column(name = "forces", length = 2)
	private String forces;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(String deleteState) {
		this.deleteState = deleteState;
	}

	public String getForces() {
		return forces;
	}

	public void setForces(String forces) {
		this.forces = forces;
	}

	public Date getRenewalTime() {
		return renewalTime;
	}

	public void setRenewalTime(Date renewalTime) {
		this.renewalTime = renewalTime;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getVersionNote() {
		return versionNote;
	}

	public void setVersionNote(String versionNote) {
		this.versionNote = versionNote;
	}

	@Override
	public String toString() {
		return "AppVersion [id=" + id + ", versionNumber=" + versionNumber + ", versionNote=" + versionNote
				+ ", renewalTime=" + renewalTime + ", deleteState=" + deleteState + ", forces=" + forces + "]";
	}

}
