package net.riking.service;

import net.riking.entity.model.AppUser;

public interface AppUserService {
	public AppUser findByPhone(String phone);
	
	public AppUser findByOpenId(String openId);
	
}
