package net.riking.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.AppUserResult;

public interface AppUserService {
	public AppUser findByPhone(String phone);

	public AppUser findByOpenId(String openId);

	public AppUser register(AppUser user, AppUserDetail detail);

	public AppUserDetail findDetailByOne(String id);

	public String uploadPhoto(MultipartFile mFile, String userId) throws RuntimeException;

	public List<AppUserResult> findUserMightKnow(String userId, int begin, int end);

}
