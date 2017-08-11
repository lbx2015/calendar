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

	private String accessKeyId;
	private String accessKeySecret;
	private String appHtmlPath;
	
	

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

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(1024L * 1024L);
		return factory.createMultipartConfig();
	}
}
