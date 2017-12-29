package net.riking.entity.params;

import net.riking.entity.BaseEntity;

/**
 * 用户关注接收参数
 * 
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class UserFollowParams extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6952211991459875719L;

	// 用户Id
	private String userId;

	// 1-问题，2-话题，3-用户
	private Integer objType;

	// 目标对象的userId
	private String toUserId;

	// 1-评论；2-回答；3-提问
	private Integer optType;

	private Integer pindex; // 页数

	private Integer pcount;// 每页条数

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public Integer getOptType() {
		return optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public Integer getPindex() {
		return pindex;
	}

	public void setPindex(Integer pindex) {
		this.pindex = pindex;
	}

	public Integer getPcount() {
		return pcount;
	}

	public void setPcount(Integer pcount) {
		this.pcount = pcount;
	}

	public Integer getObjType() {
		return objType;
	}

	public void setObjType(Integer objType) {
		this.objType = objType;
	}

}
