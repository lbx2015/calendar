package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_BASE_CHECKRESULT")
public class CheckResult {

	/**
	 * 系统id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue
	private Long id;

	/**
	 * 被校验的对象的id
	 */
	@Column(name = "checkId")
	private Long checkId;

	/**
	 * 被校验对象的类型,1是可疑,2是大额
	 */
	@Column(name = "checkType")
	private String type;

	/**
	 * 校验不通过的字段和原因组成的json
	 */
	@Column(name = "reason")
	private String reason;

	/**
	 * 校验日期
	 */
	@Column(name = "checkDate")
	private Date checkDate;

	/**
	 * 判断校验是否通过,1是通过,2是不通过
	 */
	@Column(name = "flag")
	private Integer flag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCheckId() {
		return checkId;
	}

	public void setCheckId(Long checkId) {
		this.checkId = checkId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "CheckResult [id=" + id + ", checkId=" + checkId + ", type=" + type + ", reason=" + reason
				+ ", checkDate=" + checkDate + ", flag=" + flag + "]";
	}

}
