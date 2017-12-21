package net.riking.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import net.riking.entity.VO.AppUserVO;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.AppUserResult;
import net.riking.entity.model.Email;

public interface AppUserService {
	public AppUser findByPhone(String phone);

	public AppUser findByOpenId(String openId);

	public AppUser register(AppUser user, AppUserDetail detail);

	public AppUserDetail findDetailByOne(String id);

	public String updUserPhotoUrl(MultipartFile mFile, String userId, String fileName);

	public String savePhotoFile(MultipartFile mFile, String url) throws RuntimeException;

	public List<AppUserResult> findUserMightKnow(String userId, String userIds, int begin, int end);

	public Integer transformExpToGrade(Integer experience);

	public String getPhotoUrlPath(String photoPath);

	// 用户关注的人
	public List<AppUserResult> userFollowUser(String userId, Integer pageBegin, Integer pageCount);

	// 我的粉丝
	public List<AppUserResult> findMyFans(String userId, Integer pageBegin, Integer pageCount);

	public Email getMyEmail();

	/********************* WEB ***************/

	Page<AppUserVO> findAll(AppUserVO appUserVO, PageRequest pageable);

	void updateModule(AppUserVO appUserVO);

	void del(String id);

	/******************** WEB END ***********/
}
