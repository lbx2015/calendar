package net.riking.service;

import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;

public interface AppUserService {
	public AppUser findByPhone(String phone);
	
	public AppUser findByOpenId(String openId);
	
	public AppUser register(AppUser user, AppUserDetail detail);
	
	public AppUserDetail findDetailByOne(String id);
	
}
