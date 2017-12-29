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

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@JsonProperty("reportId")
	private String id;

	@Comment("报表名称")
	@Column(name = "title", length = 32)
	private String title;

	@Comment("报表代码")
	@Column(name = "code", length = 32)
	private String code;

	@Comment("报表类型（字典表：T_REPORT-REPORT_TYPE:PBOC、CBRC）")
	@Column(name = "report_type", length = 32)
	private String reportType;

	@Comment("报表种类（字典表：T_REPORT-REPORT_KIND:1-1104;2-大集中）")
	@Column(name = "report_kind", length = 32)
	private String reportKind;

	@Comment("报表所属模块（字典表：T_REPORT-MODLE_TYPE）")
	@Column(name = "module_type", length = 5)
	private String moduleType;

	@Comment("报表批次：（字典表：T_REPORT-REPORT_BATCH：0-4批，PBOC使用）")
	@Column(name = "report_batch", length = 1)
	private Integer reportBatch;

	@Comment("报表简介")
	@Column(name = "summary")
	private String summary;

	@Comment("报表说明")
	@Lob
	@Column(name = "fillin_note")
	private String fillinNote;

	@Comment("报表版本号")
	@Column(name = "version_no", length = 16)
	private String versionNo;

	@Comment("报表模板下载地址")
	@Column(name = "template_name", length = 128)
	private String templateName;

	@Column(name = "download_url", length = 128)
	private String downloadUrl;

	public Report() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportKind() {
		return reportKind;
	}

	public void setReportKind(String reportKind) {
		this.reportKind = reportKind;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public Integer getReportBatch() {
		return reportBatch;
	}

	public void setReportBatch(Integer reportBatch) {
		this.reportBatch = reportBatch;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getFillinNote() {
		return fillinNote;
	}

	public void setFillinNote(String fillinNote) {
		this.fillinNote = fillinNote;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

}
