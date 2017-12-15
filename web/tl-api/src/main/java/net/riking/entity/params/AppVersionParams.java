package net.riking.entity.params;

import net.riking.entity.BaseEntity;

/**
 * App版本获取接口参数
 * @author james.you
 * @version crateTime：2017年11月28日 下午2:39:26
 * @used TODO
 */
public class AppVersionParams extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1066487596381289421L;

	// 版本号
	private String versionNo;

	// 移动端类型:1-IOS;2-Android
	private Integer clientType;

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

}
