package net.riking.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("activeMqConfig")
@ConfigurationProperties(prefix = "sys.activeMq")
public class ActiveMqConfig {
	
	private String userName;
	
	private String passWord;
	
	private String url;
	
	public String getUserName() {
		if(StringUtils.isBlank(userName)){
			userName = Const.MQ_USER_NAME;
		}
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		if(StringUtils.isBlank(passWord)){
			passWord = Const.MQ_PASSWORD;
		}
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getUrl() {
		if(StringUtils.isBlank(url)){
			url = Const.MQ_BROKE_URL;
		}
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
