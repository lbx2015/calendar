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

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

@Comment("WEb版本")
@Entity
@Table(name = "t_ewb_version")
public class WebVersion extends BaseEntity {
	
	/**
	 * @author Lucky.Liu on 2017/8/05.
	 */
	
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	// WEB版本号
	@Column(name = "version_number", length = 32)
	private String versionNumber;

	// 数据库脚本版本号
	@Column(name = "sql_version_number", length = 32)
	private String sqlVersionNumber;

	// 版本说明
	@Column(name = "version_note", length = 255)
	private String versionNote;

	// 更新日期
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "renewal_time")
	private Date renewalTime;
	
	// 删除状态 0删除 1显示
	@Column(name = "delete_state", length = 2)
	private String deleteState;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getRenewalTime() {
		return renewalTime;
	}

	public void setRenewalTime(Date renewalTime) {
		this.renewalTime = renewalTime;
	}

	public String getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(String deleteState) {
		this.deleteState = deleteState;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getSqlVersionNumber() {
		return sqlVersionNumber;
	}

	public void setSqlVersionNumber(String sqlVersionNumber) {
		this.sqlVersionNumber = sqlVersionNumber;
	}

	public String getVersionNote() {
		return versionNote;
	}

	public void setVersionNote(String versionNote) {
		this.versionNote = versionNote;
	}

	@Override
	public String toString() {
		return "WebVersion [id=" + id + ", versionNumber=" + versionNumber + ", sqlVersionNumber=" + sqlVersionNumber
				+ ", versionNote=" + versionNote + ", renewalTime=" + renewalTime + ", deleteState=" + deleteState
				+ "]";
	}

}
