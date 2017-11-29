package net.riking.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.dao.repo.AppUserRepo;
import net.riking.entity.model.AppUser;
import net.riking.service.AppUserService;

@Service("appUserSerice")
@Transactional
public class AppUserServiceImpl implements AppUserService {
	@Autowired
	AppUserRepo appUserRepo;
	
	public AppUser findByPhone(String phone){
		return appUserRepo.findByPhone(phone);
	}
	
	public AppUser findByOpenId(String openId){
		return appUserRepo.findByOpenId(openId);
	}
	
}
