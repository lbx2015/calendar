package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.entity.BasePageQueryProp;

@Comment("报表详情表")
@Entity
@Table(name = "t_report")
public class Report extends BasePageQueryProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4087711136918056749L;

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@JsonProperty("reportId")
	private String id;

	@Comment("报表标题")
	@Column(name = "title", length = 32)
	private String title;

	@Comment("报表代码")
	@Column(name = "code", length = 32)
	private String code;

	@Comment("报表代码")
	@Column(name = "report_no", length = 32)
	private String reportNo;

	@Comment("报表简介")
	@Column(name = "summary")
	private String summary;

	@Comment("填报机构")
	@Column(name = "fillin_org", length = 512)
	private String fillinOrg;

	@Comment("报送口径、频度及时间")
	@Column(name = "submit_frequency", length = 256)
	private String submitFrequency;

	@Comment("报送方式")
	@Column(name = "submit_mode", length = 32)
	private String submitMode;

	@Comment("数据单位")
	@Column(name = "submit_unit", length = 32)
	private String submitUnit;

	@Comment("四舍五入要求")
	@Column(name = "submit_round", length = 32)
	private String submitRound;

	@Comment("填报币种")
	@Column(name = "fillin_currency", length = 3)
	private String fillinCurrency;

	@Comment("报表说明")
	@Lob
	@Column(name = "note")
	private String note;

	@Comment("报表规则")
	@Lob
	@Column(name = "report_rule", length = 32)
	private String reportRule;

	@Comment("报表所属模块，频度标识")
	@Column(name = "module_type", length = 3)
	private String moduleType;

	@Comment("报表模板下载地址")
	@Column(name = "download_url", length = 128)
	private String downloadUrl;

	public Report() {
	}

	@Comment("报表版本号")
	@Column(name = "version_no", length = 128)
	private String versionNo;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReportNo() {
		return reportNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getFillinOrg() {
		return fillinOrg;
	}

	public void setFillinOrg(String fillinOrg) {
		this.fillinOrg = fillinOrg;
	}

	public String getSubmitFrequency() {
		return submitFrequency;
	}

	public void setSubmitFrequency(String submitFrequency) {
		this.submitFrequency = submitFrequency;
	}

	public String getSubmitMode() {
		return submitMode;
	}

	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
	}

	public String getSubmitUnit() {
		return submitUnit;
	}

	public void setSubmitUnit(String submitUnit) {
		this.submitUnit = submitUnit;
	}

	public String getSubmitRound() {
		return submitRound;
	}

	public void setSubmitRound(String submitRound) {
		this.submitRound = submitRound;
	}

	public String getFillinCurrency() {
		return fillinCurrency;
	}

	public void setFillinCurrency(String fillinCurrency) {
		this.fillinCurrency = fillinCurrency;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

}
