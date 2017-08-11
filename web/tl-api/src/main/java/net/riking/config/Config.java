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
	private String accessKeyId;
	private String accessKeySecret;
	
	

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
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

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(1024L * 1024L);
		return factory.createMultipartConfig();
	}
}
