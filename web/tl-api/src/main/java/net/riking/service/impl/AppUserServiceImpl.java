package net.riking.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.utils.UuidUtils;
import net.riking.dao.AppUserDao;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.AppUserResult;
import net.riking.entity.model.Email;
import net.riking.entity.resp.OtherUserResp;
import net.riking.service.AppUserService;
import net.riking.service.SysDataService;
import net.riking.util.EncryptionUtil;
import net.riking.util.FileUtils;
import net.riking.util.StringUtil;

@Service("appUserSerice")
@Transactional
public class AppUserServiceImpl implements AppUserService {
	private static final Logger logger = LogManager.getLogger("AppUserService");

	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Autowired
	SysDataService sysDataService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	AppUserDao appUserDao;

	public AppUser findByPhone(String phone) {
		return appUserRepo.findByPhone(phone);
	}

	public AppUser findByOpenId(String openId) {
		return appUserRepo.findByOpenId(openId);
	}

	@Transactional
	public AppUser register(AppUser user, AppUserDetail detail) {
		String uuid = UuidUtils.random();
		// Date date = new Date();
		user.setId(uuid);
		user.setPassWord(EncryptionUtil.MD5(user.getPhone()));
		user.setCreatedBy(uuid);
		// user.setCreatedTime(date);
		user.setModifiedBy(uuid);
		// user.setModifiedTime(date);
		user.setEnabled(Const.EFFECTIVE);
		user.setIsDeleted(Const.EFFECTIVE);

		/* 详细信息 */
		detail.setId(uuid);
		detail.setSex(Const.USER_SEX_MAN);
		// detail.setPhotoUrl(Const.DEFAULT_PHOTO_URL);// 默认头像Url
		detail.setIntegral(0);
		detail.setExperience(0);
		detail.setIsSubscribe(0);
		detail.setIsGuide(0);
		user.setDetail(detail);

		appUserRepo.save(user);
		appUserDetailRepo.save(detail);
		return user;
	}

	public AppUserDetail findDetailByOne(String id) {
		return appUserDetailRepo.findOne(id);
	}

	@Override
	public String savePhotoFile(MultipartFile mFile, String url) throws RuntimeException {
		// String suffix =
		// mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("."));
		String fileName = UuidUtils.random() + mFile.getOriginalFilename();
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = mFile.getInputStream();
			String path = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH + url;
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String photoUrl = path + fileName;
			fos = new FileOutputStream(photoUrl);
			int len = 0;
			byte[] buf = new byte[1024 * 1024];
			while ((len = is.read(buf)) > -1) {
				fos.write(buf, 0, len);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(CodeDef.EMP.GENERAL_ERR + "");
		} finally {
			try {
				fos.close();
				is.close();
			} catch (IOException e) {
				logger.error(e);
				throw new RuntimeException(CodeDef.EMP.GENERAL_ERR + "");
			}
		}
		return fileName;
	}

	@Override
	public String updUserPhotoUrl(MultipartFile mFile, String userId, String fileName) {

		AppUserDetail appUserDetail = appUserDetailRepo.findOne(userId);
		String oldFileName = appUserDetail.getPhotoUrl();
		String oleFilePath = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH + Const.TL_PHOTO_PATH
				+ oldFileName;
		// 删除服务器上文件
		ModelPropDict dict = sysDataService.getDict("T_ROOT_URL", "PHOTO_URL", "DEFAULT_URL");
		if (!dict.getValu().equals(oldFileName) && !mFile.getOriginalFilename().equals(oldFileName)) {
			FileUtils.deleteFile(oleFilePath);
		}
		// 数据库保存路径s
		appUserDetailRepo.updatePhoto(userId, fileName);
		return fileName;
	}

	/**
	 * 经验值计算等级
	 * @param experience
	 * @return
	 */
	public Integer transformExpToGrade(Integer experience) {
		List<ModelPropDict> propDicts = sysDataService.getDicts("T_APP_USER", "GRADE_RANGE");
		Integer begin = 0;
		Integer gradeOneMax = 0;
		Integer gradeTwoMax = 0;
		Integer gradeThrMax = 0;
		Integer gradeFourMax = 0;
		for (ModelPropDict modelPropDict : propDicts) {
			if ("V1".equals(modelPropDict.getKe())) {
				gradeOneMax = Integer.parseInt(modelPropDict.getValu());
			} else if ("V2".equals(modelPropDict.getKe())) {
				gradeTwoMax = Integer.parseInt(modelPropDict.getValu());
			} else if ("V3".equals(modelPropDict.getKe())) {
				gradeThrMax = Integer.parseInt(modelPropDict.getValu());
			} else if ("V4".equals(modelPropDict.getKe())) {
				gradeFourMax = Integer.parseInt(modelPropDict.getValu());
			}
		}
		if (begin <= experience && experience <= gradeOneMax) {
			return 1;
		} else if (gradeOneMax + 1 <= experience && experience <= gradeTwoMax) {
			return 2;
		} else if (gradeTwoMax + 1 <= experience && experience <= gradeThrMax) {
			return 3;
		} else if (gradeThrMax + 1 <= experience && experience <= gradeFourMax) {
			return 4;
		} else if (gradeFourMax + 1 <= experience) {
			return 5;
		} else {
			return 0;
		}
	}

	@Override
	public List<AppUserResult> findUserMightKnow(String userId, String userIds, int begin, int end) {
		return appUserDao.findUserMightKnow(userId, userIds, begin, end);
	}

	/**
	 * 获取用户头像路径
	 * @see net.riking.service.AppUserService#getPhotoUrlPath()
	 */
	@Override
	public String getPhotoUrlPath(String photoPath) {
		// 截取资源访问路径
		String projectPath = StringUtil.getProjectPath(request.getRequestURL().toString());
		String projectName = sysDataService.getDict("T_APP_USER", "PRO_NAME", "PRO_NAME").getValu();
		projectPath = projectPath + "/" + projectName + photoPath;
		return projectPath;
	}

	@Override
	public List<AppUserResult> userFollowUser(String userId, Integer pageBegin, Integer pageCount) {
		return appUserDao.userFollowUser(userId, pageBegin, pageCount);
	}

	@Override
	public List<AppUserResult> findMyFans(String userId, Integer pageBegin, Integer pageCount) {
		return appUserDao.findMyFans(userId, pageBegin, pageCount);
	}

	@Override
	public Email getMyEmail() {
		String mySmtpHost = sysDataService.getDict("T_EMAIL", "EMAIL", "MYSMTPHOST").getValu().trim();
		String myPassWord = sysDataService.getDict("T_EMAIL", "EMAIL", "MYPASSWORD").getValu().trim();
		String myAccount = sysDataService.getDict("T_EMAIL", "EMAIL", "MYACCOUNT").getValu().trim();
		String sender = sysDataService.getDict("T_EMAIL", "EMAIL", "SENDER").getValu().trim();
		Email email = new Email(myAccount, myPassWord, mySmtpHost, sender);
		return email;
	}

	@Override
	public OtherUserResp getOtherMes(String toUserId, String userId) {
		return appUserDao.getOtherMes(toUserId, userId);
	}

}
