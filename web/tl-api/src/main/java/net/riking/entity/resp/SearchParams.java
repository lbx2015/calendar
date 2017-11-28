package net.riking.entity.resp;

import java.io.Serializable;

/**
 * 搜索列表类的接收参数
 * 
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class SearchParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6952211991459875719L;

	// 用户Id
	private String userId;

	// 显示操作类型：0-不显示状态；1-显示关注状态；2-显示邀请状态
	private String showOptType;

	// 搜索类型： 1-报表；2-话题；3-人脉；4-资讯；5-问题
	private String objType;

	// 关键字
	private String keyword;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getShowOptType() {
		return showOptType;
	}

	public void setShowOptType(String showOptType) {
		this.showOptType = showOptType;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
