package net.riking.dao;

import java.util.List;

import net.riking.entity.model.AppUserResult;

public interface AppUserDao {

	public List<AppUserResult> findUserMightKnow(String userId, String userIds, int begin, int end);

	public List<AppUserResult> userFollowUser(String userId, Integer pageBegin, Integer pageCount);

	public List<AppUserResult> findMyFans(String userId, Integer pageBegin, Integer pageCount);
}
