package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Transient;

import net.riking.entity.BaseEntity;


@Entity
@Table(name = "t_app_user_recommend")
public class AppUserRecommend extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8708349920299314670L;

	//行业/职位表
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	//报表id
	@Id
	@Column(name = "report_id", length = 32)
	private String reportId;
	
	@Id
	@Column(name = "industry_id")
	private Long industryId;
	
	@Transient
	private String reportName;
	
	public AppUserRecommend(String reportId,String reportName,Long industryId){
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
