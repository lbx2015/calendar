package net.riking.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月11日 下午2:47:48
 * @used TODO
 */
@Component("config")
@ConfigurationProperties(prefix = "sys.config")
public class Config {

	/*############### 短信通道配置  #################*/
	private Integer isOpenSms;//短信通道开关：0-关闭,1-开启
	private String accessKeyId;
	private String accessKeySecret;
	private String signName;//模板签名
	private String commonTemplateCode;//通用模板编号
	/*############### 短信通道配置  #################*/
	
	private String appHtmlPath;
	private String appApiPath;
	
	public Integer getIsOpenSms() {
		return isOpenSms;
	}

	public void setIsOpenSms(Integer isOpenSms) {
		this.isOpenSms = isOpenSms;
	}

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

	public String getAppHtmlPath() {
		return appHtmlPath;
	}

	public void setAppHtmlPath(String appHtmlPath) {
		this.appHtmlPath = appHtmlPath;
	}
	
	public String getAppApiPath() {
		return appApiPath;
	}

	public void setAppApiPath(String appApiPath) {
		this.appApiPath = appApiPath;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getCommonTemplateCode() {
		return commonTemplateCode;
	}

	public void setCommonTemplateCode(String commonTemplateCode) {
		this.commonTemplateCode = commonTemplateCode;
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(1024L * 1024L);
		return factory.createMultipartConfig();
	}
}
