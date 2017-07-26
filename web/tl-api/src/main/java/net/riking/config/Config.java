package net.riking.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 自动读config配置，只要声明一个属性就会自动注入
 * 
 * @author kai.cheng
 *
 */
@Component("config")
@ConfigurationProperties(prefix = "sys.config")
public class Config {

	private String company;
	private String amlWorkId;
	private String baseInfoWorkId;
	private String receipt;
	private String summary;
	//正确回执
	private String correctReceipt;
	//补正回执
	private String modifyReceipt;
	//错误回执
	private String wrongReceipt;
	//大额新增补正
	private String bigAmountxzbz;
	//可疑新增修改
	private String supxzbz;

	private String attachmentdir;
	
	//只导出大额
	private String exportBig;
	//只导出可疑
	private String exportSusp;
	//都导出
	private String exportBoth;
	//是否同步基础信息到原始表
	private String snyc;
	
	
	public String getSnyc() {
		return snyc;
	}

	public void setSnyc(String snyc) {
		this.snyc = snyc;
	}

	public String getAttachmentdir() {
		return attachmentdir;
	}

	public void setAttachmentdir(String attachmentdir) {
		this.attachmentdir = attachmentdir;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBaseInfoWorkId() {
		return baseInfoWorkId;
	}

	public void setBaseInfoWorkId(String baseInfoWorkId) {
		this.baseInfoWorkId = baseInfoWorkId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getCorrectReceipt() {
		return correctReceipt;
	}

	public void setCorrectReceipt(String correctReceipt) {
		this.correctReceipt = correctReceipt;
	}

	public String getModifyReceipt() {
		return modifyReceipt;
	}

	public void setModifyReceipt(String modifyReceipt) {
		this.modifyReceipt = modifyReceipt;
	}

	public String getWrongReceipt() {
		return wrongReceipt;
	}

	public void setWrongReceipt(String wrongReceipt) {
		this.wrongReceipt = wrongReceipt;
	}

	public String getBigAmountxzbz() {
		return bigAmountxzbz;
	}

	public void setBigAmountxzbz(String bigAmountxzbz) {
		this.bigAmountxzbz = bigAmountxzbz;
	}

	public String getSupxzbz() {
		return supxzbz;
	}

	public void setSupxzbz(String supxzbz) {
		this.supxzbz = supxzbz;
	}
	
	

	public String getExportBig() {
		return exportBig;
	}

	public void setExportBig(String exportBig) {
		this.exportBig = exportBig;
	}

	public String getExportSusp() {
		return exportSusp;
	}

	public void setExportSusp(String exportSusp) {
		this.exportSusp = exportSusp;
	}

	public String getExportBoth() {
		return exportBoth;
	}

	public void setExportBoth(String exportBoth) {
		this.exportBoth = exportBoth;
	}

	/**
	 * 大额可疑工作流id
	 * @return
	 */
	public String getAmlWorkId() {
		return amlWorkId;
	}

	public void setAmlWorkId(String amlWorkId) {
		this.amlWorkId = amlWorkId;
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(1024L * 1024L);
		return factory.createMultipartConfig();
	}
}
