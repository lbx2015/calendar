package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;
import net.riking.entity.PageQuery;

@Comment("报表信息")
@Entity
@Table(name = "T_report_list")
public class ReportList extends PageQuery {

	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	// 报表名称
	@Column(name = "report_name", length = 32)
	private String reportName;

	// 报表编号
	@Column(name = "report_code", length = 32)
	private String reportCode;

	// 报表简介
	@Column(name = "report_brief", length = 256)
	private String reportBrief;

	// 填报机构
	@Column(name = "report_organization", length = 32)
	private String reportOrganization;

	// 报送口径、频度及时间
	@Column(name = "report_frequency", length = 32)
	private String reportFrequency;

	// 报送方式
	@Column(name = "report_style", length = 32)
	private String reportStyle;

	// 数据单位
	@Column(name = "report_unit", length = 2)
	private String reportUnit;

	// 四舍五入要求
	@Column(name = "report_round", length = 32)
	private String reportRound;

	// 填报币种
	@Column(name = "report_currency", length = 32)
	private String reportCurrency;

	// 报表说明
	@Lob
	@Column(name = "report_note")
	private String reportNote;

	// 报表规则
	@Lob
	@Column(name = "report_rule", length = 32)
	private String reportRule;

	// 报表所属模块，频度标识
	@Column(name = "module_type", length = 3)
	private String moduleType;

	// 报表模板下载地址
	@Column(name = "download_url", length = 128)
	private String downloadUrl;

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

	public String getReportRound() {
		return reportRound;
	}

	public void setReportRound(String reportRound) {
		this.reportRound = reportRound;
	}

	public String getReportCurrency() {
		return reportCurrency;
	}

	public void setReportCurrency(String reportCurrency) {
		this.reportCurrency = reportCurrency;
	}

	public String getReportNote() {
		return reportNote;
	}

	public void setReportNote(String reportNote) {
		this.reportNote = reportNote;
	}

	public String getReportRule() {
		return reportRule;
	}

	public void setReportRule(String reportRule) {
		this.reportRule = reportRule;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
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
				+ ", moduleType=" + moduleType + ", downloadUrl=" + downloadUrl + ", deleteState=" + deleteState + "]";
	}

}
