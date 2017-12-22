package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;
import net.riking.entity.MyDateFormat;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("用户等级表")
@Entity
@Table(name = "t_appuser_grade")
public class appUserGrade extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3940163346158751094L;

	@Comment("物理主键")
	@Id
	@Column(name = "grade")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer grade;

	@Comment("最小经验值")
	@Column(name = "min_exp")
	private Integer minExp;

	@Comment("最大经验值")
	@Column(name = "max_exp", length = 32)
	private Integer maxExp;

	@Comment("备注")
	@Column(name = "remark", length = 255)
	private String remark;// 备注

	@Comment("创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@MyDateFormat(pattern = "yyyyMMddHHmmssSSS")
	@org.hibernate.annotations.CreationTimestamp
	@Column(name = "created_time", insertable = false, updatable = false, nullable = false, columnDefinition = "datetime default now()")
	private Date createdTime;

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Integer getMinExp() {
		return minExp;
	}

	public void setMinExp(Integer minExp) {
		this.minExp = minExp;
	}

	public Integer getMaxExp() {
		return maxExp;
	}

	public void setMaxExp(Integer maxExp) {
		this.maxExp = maxExp;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
