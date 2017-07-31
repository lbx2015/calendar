package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

@Comment("WEb版本")
@Entity
@Table(name = "T_WEB_Version")
public class WebVersion extends BaseEntity {
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	// WEB版本号
	@Column(name = "versionNumber", length = 32)
	private String versionNumber;

	// 数据库脚本版本号
	@Column(name = "sqlVersionNumber", length = 32)
	private String sqlVersionNumber;

	// 版本说明
	@Column(name = "versionNote", length = 255)
	private String versionNote;

	// 删除状态 0删除 1显示
	@Column(name = "delete_state", length = 2)
	private String deleteState;

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
		return "WEBVersion [id=" + id + ", versionNumber=" + versionNumber + ", sqlVersionNumber=" + sqlVersionNumber
				+ ", versionNote=" + versionNote + "]";
	}

}