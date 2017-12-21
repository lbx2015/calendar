package net.riking.dao;

import java.util.List;

import net.riking.entity.model.AppUserResult;
import net.riking.entity.resp.OtherUserResp;

public interface AppUserDao {

	public List<AppUserResult> findUserMightKnow(String userId, String userIds, int begin, int end);

	public List<AppUserResult> userFollowUser(String userId, Integer pageBegin, Integer pageCount);

	public List<AppUserResult> findMyFans(String userId, Integer pageBegin, Integer pageCount);

	public OtherUserResp getOtherMes(String toUserId, String userId);
}
