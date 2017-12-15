package net.riking.dao;

import java.util.List;

import net.riking.entity.model.AppUserResult;

public interface AppUserDao {

	public List<AppUserResult> findUserMightKnow(String userId, int begin, int end);
}
