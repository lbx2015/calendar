package net.riking.dao;

import java.util.Set;

import net.riking.entity.model.AppUserRecommend;

public interface AppUserCommendDao {

	Set<AppUserRecommend> findALL();
}
