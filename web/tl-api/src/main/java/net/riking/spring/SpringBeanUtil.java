package net.riking.spring;

import org.springframework.web.context.WebApplicationContext;

public class SpringBeanUtil {
	 
	private SpringBeanUtil(){}
	private WebApplicationContext wac;
	
	public WebApplicationContext getWac() {
		return wac;
	}
	public void setWac(WebApplicationContext wac) {
		this.wac = wac;
	}
	private static SpringBeanUtil springBeanUtil = new SpringBeanUtil();
	public static SpringBeanUtil  getInstance(){
		return springBeanUtil;
	}	 
	public Object getBean(String bean){
		Object obj = wac.getBean(bean);
    	return obj;
    }
	 
}
