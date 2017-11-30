package net.riking.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.config.Const;
import net.riking.core.utils.UuidUtils;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.service.AppUserService;
import net.riking.util.DateUtils;
import net.riking.util.EncryptionUtil;

@Service("appUserSerice")
@Transactional
public class AppUserServiceImpl implements AppUserService {
	@Autowired
	AppUserRepo appUserRepo;
	
	@Autowired
	AppUserDetailRepo appUserDetailRepo;
	
	public AppUser findByPhone(String phone){
		return appUserRepo.findByPhone(phone);
	}
	
	public AppUser findByOpenId(String openId){
		return appUserRepo.findByOpenId(openId);
	}
	
	@Transactional
	public AppUser register(AppUser user, AppUserDetail detail){
		String uuid = UuidUtils.random();
//		Date date = new Date();
		user.setId(uuid);
		user.setPassWord(EncryptionUtil.MD5(user.getPhone()));
		user.setCreatedBy(uuid);
//		user.setCreatedTime(date);
		user.setModifiedBy(uuid);
//		user.setModifiedTime(date);
		user.setEnabled(Const.EFFECTIVE);
		user.setIsDeleted(Const.EFFECTIVE);
		
		/*详细信息*/
		detail.setId(uuid);
		detail.setSex(Const.USER_SEX_MAN);
		detail.setIntegral(0);
		detail.setExperience(0);
		detail.setIsSubscribe(0);
		user.setDetail(detail);
		
		appUserRepo.save(user);
		appUserDetailRepo.save(detail);
		return user;
	}
	
	public AppUserDetail findDetailByOne(String id){
		return appUserDetailRepo.findOne(id);
	}
	
}
