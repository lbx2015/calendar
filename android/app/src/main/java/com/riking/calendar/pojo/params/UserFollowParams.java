package com.riking.calendar.pojo.params;


/**
 * 用户关注接收参数
 * 
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class UserFollowParams extends BaseParams {

	// 1-问题，2-话题，3-用户
	public int objType;

	// 目标对象的userId
	public String toUserId;

	// 1-评论；2-回答；3-提问
	public int optType;

	public int pindex; // 页数

	public int pcount=30;// 每页条数

}
