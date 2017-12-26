package net.riking.entity.params;

import java.util.Date;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.riking.entity.BaseEntity;

/**
 * 主页的接收参数
 * 
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class HomeParams extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3255489408075879850L;

	// 用户Id
	private String userId;

	// 请求方向（up上，down下）
	private String direct;

	// 请求上翻最新时间戳
	@DateTimeFormat(pattern = "yyyyMMddHHmmssSSS")
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	private Date reqTimeStamp;

	// 1-问题；2-话题
	private Integer objType;

	// 目标对象id
	private String objId;

	// 0-屏蔽；1-显示
	private Integer enabled;

	@Transient
	// mq操作类型(消费者根据此类型判断mq操作)
	private Integer mqOptType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getMqOptType() {
		return mqOptType;
	}

	public void setMqOptType(Integer mqOptType) {
		this.mqOptType = mqOptType;
	}

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

	public Date getReqTimeStamp() {
		return reqTimeStamp;
	}

	public void setReqTimeStamp(Date reqTimeStamp) {
		this.reqTimeStamp = reqTimeStamp;
	}

	public Integer getObjType() {
		return objType;
	}

	public void setObjType(Integer objType) {
		this.objType = objType;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

}
