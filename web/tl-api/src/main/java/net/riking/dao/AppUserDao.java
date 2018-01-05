package net.riking.dao;

import java.util.List;

import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.AppUserResult;
import net.riking.entity.model.UserFollowCollect;
import net.riking.entity.resp.OtherUserResp;

public interface AppUserDao {

	/***
	 * 根据出生日期MMdd获取用户移动设备号
	 * @author james.you
	 * @version crateTime：2017年12月26日 下午6:29:22
	 * @used TODO
	 * @param brithDay MMdd
	 * @return
	 */
	List<AppUserDetail> findPhoneDeviceByBirthDay(String brithDay);

	public List<AppUserResult> findUserMightKnow(String userId, String userIds, int begin, int end);

	public List<AppUserResult> userFollowUser(String userId, Integer pageBegin, Integer pageCount);

	public List<AppUserResult> findMyFans(String userId, Integer pageBegin, Integer pageCount);

	public OtherUserResp getOtherMes(String toUserId, String userId);

	List<UserFollowCollect> findByFolColByUserId(String userId, String userName, Integer pindex, Integer pcount);

	/**
	 * 计算用户关注收藏管理总数
	 * @param userId
	 * @return
	 */
	Integer countByFolColByUserId(String userId, String userName);

}
