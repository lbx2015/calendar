package net.riking.entity.model;

import java.sql.Clob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

@Comment("报表信息")
@Entity
@Table(name = "T_Report_List")
public class ReportList extends BaseEntity {

	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	// 报表名称
	@Column(name = "reportName", length = 32)
	private String reportName;

	// 报表编号
	@Column(name = "reportCode", length = 32)
	private String reportCode;

	// 报表简介
	@Column(name = "reportBrief", length = 256)
	private String reportBrief;

	// 填报机构
	@Column(name = "reportOrganization", length = 32)
	private String reportOrganization;

	// 报送口径、频度及时间
	@Column(name = "reportFrequency", length = 32)
	private String reportFrequency;

	// 报送方式
	@Column(name = "reportStyle", length = 32)
	private String reportStyle;

	// 数据单位
	@Column(name = "reportUnit", length = 2)
	private String reportUnit;

	// 四舍五入要求
	@Column(name = "reportRound")
	private Integer reportRound;

	// 填报币种
	@Column(name = "reportCurrency", length = 32)
	private String reportCurrency;

	// 报表说明
	@Lob
	@Column(name = "reportNote")
	private Clob reportNote;

	// 报表规则
	@Lob
	@Column(name = "reportRule", length = 32)
	private Clob reportRule;

	// 报表所属模块，频度标识（外键）
	@Column(name = "module_Id", length = 3)
	private String moduleTypeId;

	// 报表模板下载地址
	@Column(name = "downloadUrl", length = 128)
	private String downloadUrl;

	// 删除状态    0删除 1显示
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

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getReportBrief() {
		return reportBrief;
	}

	public void setReportBrief(String reportBrief) {
		this.reportBrief = reportBrief;
	}

	public String getReportOrganization() {
		return reportOrganization;
	}

	public void setReportOrganization(String reportOrganization) {
		this.reportOrganization = reportOrganization;
	}

	public String getReportFrequency() {
		return reportFrequency;
	}

	public void setReportFrequency(String reportFrequency) {
		this.reportFrequency = reportFrequency;
	}

	public String getReportStyle() {
		return reportStyle;
	}

	public void setReportStyle(String reportStyle) {
		this.reportStyle = reportStyle;
	}

	public String getReportUnit() {
		return reportUnit;
	}

	public void setReportUnit(String reportUnit) {
		this.reportUnit = reportUnit;
	}

	public Integer getReportRound() {
		return reportRound;
	}

	public void setReportRound(Integer reportRound) {
		this.reportRound = reportRound;
	}

	public String getReportCurrency() {
		return reportCurrency;
	}

	public void setReportCurrency(String reportCurrency) {
		this.reportCurrency = reportCurrency;
	}

	public Clob getReportNote() {
		return reportNote;
	}

	public void setReportNote(Clob reportNote) {
		this.reportNote = reportNote;
	}

	public Clob getReportRule() {
		return reportRule;
	}

	public void setReportRule(Clob reportRule) {
		this.reportRule = reportRule;
	}

	public String getModuleTypeId() {
		return moduleTypeId;
	}

	public void setModuleTypeId(String moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	@Override
	public String toString() {
		return "ReportList [id=" + id + ", reportName=" + reportName + ", reportCode=" + reportCode + ", reportBrief="
				+ reportBrief + ", reportOrganization=" + reportOrganization + ", reportFrequency=" + reportFrequency
				+ ", reportStyle=" + reportStyle + ", reportUnit=" + reportUnit + ", reportRound=" + reportRound
				+ ", reportCurrency=" + reportCurrency + ", reportNote=" + reportNote + ", reportRule=" + reportRule
				+ ", moduleTypeId=" + moduleTypeId + ", downloadUrl=" + downloadUrl + "]";
	}

}
