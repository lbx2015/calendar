package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

@Entity
@Table(name = "t_recommend")
public class Recommend extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8708349920299314670L;

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@JsonProperty("recommendId")
	private String id;

	// 报表id
	@Column(name = "report_id", length = 32)
	private String reportId;

	@Column(name = "industry_id")
	private Long industryId;

	@Transient
	private String reportName;

	public Recommend(String reportId, String reportName, Long industryId) {
		this.reportId = reportId;
		this.reportName = reportName;
		this.industryId = industryId;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public Long getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Long industryId) {
		this.industryId = industryId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
