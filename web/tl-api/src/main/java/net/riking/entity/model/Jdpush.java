package net.riking.entity.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.riking.core.entity.BaseEntity;

/**
 * 极光推送参数
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月10日 上午9:55:50
 * @used TODO
 */
public class Jdpush extends BaseEntity {

	private String notificationTitle;// 通知内容标题

	private String msgTitle;// 消息内容标题

	private String msgContent;// 消息内容

	private String regisrationId;// 设备标识

	private String extrasparam;// 扩展字段
	
	private Map<String, String> extrasMap;

	public Jdpush() {

	}

	public Jdpush(String notificationTitle, String msgTitle, String msgContent, String regisrationId,
			String extrasparam) {
		this.notificationTitle = notificationTitle;
		this.msgTitle = msgTitle;
		this.msgContent = msgContent;
		this.regisrationId = regisrationId;
		this.extrasparam = extrasparam;
	}

	public String getNotificationTitle() {
		return notificationTitle;
	}

	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getRegisrationId() {
		return regisrationId;
	}

	public void setRegisrationId(String regisrationId) {
		this.regisrationId = regisrationId;
	}

	public String getExtrasparam() {
		if(StringUtils.isBlank(extrasparam)){
			return "";
		}
		return extrasparam;
	}

	public void setExtrasparam(String extrasparam) {
		this.extrasparam = extrasparam;
	}

	public Map<String, String> getExtrasMap() {
		if(extrasMap ==null){
			return  new HashMap<String,String>();
		}
		return extrasMap;
	}

	public void setExtrasMap(Map<String, String> extrasMap) {
		this.extrasMap = extrasMap;
	}
	
	

}
