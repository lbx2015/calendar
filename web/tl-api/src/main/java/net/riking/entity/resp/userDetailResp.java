package net.riking.entity.resp;

import net.riking.entity.BaseEntity;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class userDetailResp extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6491834829935023232L;

	public userDetailResp() {
		super();
	}

	private String userName;

	private String toUserName;

	// 用户头像Url
	private String photoUrl;

	// 经验值
	private Integer experience;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

}
