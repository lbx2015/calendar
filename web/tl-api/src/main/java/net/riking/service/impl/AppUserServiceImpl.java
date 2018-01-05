package net.riking.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
import net.riking.entity.VO.AppUserVO;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.AppUserGrade;
import net.riking.entity.model.AppUserResult;
import net.riking.entity.model.Email;
import net.riking.entity.model.UserFollowCollect;
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
		String fileName = UuidUtils.random() + "." + mFile.getOriginalFilename().split("\\.")[1];
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = mFile.getInputStream();
			String spath = new String(this.getClass().getResource("/").getPath().getBytes("iso-8859-1"), "UTF-8");
			String path = spath + Const.TL_STATIC_PATH + url;
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
	 * 
	 * @param experience
	 * @return
	 */
	public Integer transformExpToGrade(Integer experience) {
		List<AppUserGrade> appUserGrades = sysDataService.getGrade(AppUserGrade.class.getName().toUpperCase());
		Integer maxGrade = 0;

		for (AppUserGrade appUserGrade : appUserGrades) {
			if (appUserGrade.getMinExp() <= experience && experience <= appUserGrade.getMaxExp()) {
				return appUserGrade.getGrade();
			}
			// 如果结束循环没找到所属等级，就把表里面的最大等级+1返回
			if (appUserGrade.getGrade() > maxGrade) {
				maxGrade = appUserGrade.getGrade();
			}
		}
		return maxGrade + 1;
	}

	@Override
	public List<AppUserResult> findUserMightKnow(String userId, String userIds, int begin, int end) {
		return appUserDao.findUserMightKnow(userId, userIds, begin, end);
	}

	/**
	 * 获取用户头像路径
	 * 
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

	/********************* WEB ***************/

	@Override
	public Page<AppUserVO> findAll(AppUserVO appUserVO, PageRequest pageable) {
		Specification<AppUser> bCondi = whereCondition(appUserVO);
		// 1.得到Page<AppUser>对象
		Page<AppUser> pageB = appUserRepo.findAll(bCondi, pageable);
		if (null != pageB) {
			// 2.得到AppUser对象集合
			List<AppUser> appUsers = pageB.getContent();
			List<AppUserVO> appUserVOs = getVos(appUsers);
			Page<AppUserVO> modulePage = new PageImpl<AppUserVO>(appUserVOs, pageable, pageB.getTotalElements());
			return modulePage;
		}
		return null;
	}

	private List<AppUserVO> getVos(List<AppUser> appUsers) {
		List<AppUserVO> appUserVOs = new ArrayList<AppUserVO>();
		for (AppUser appUser : appUsers) {
			String id = appUser.getId();
			AppUserDetail appUserDetail = appUserDetailRepo.findOne(id);
			AppUserVO appUserVO = new AppUserVO();
			appUserVO.setId(appUser.getId());
			appUserVO.setAppUser(appUser);
			appUserVO.setAppUserDetail(appUserDetail);
			appUserVOs.add(appUserVO);
		}
		return appUserVOs;
	}

	private Specification<AppUser> whereCondition(AppUserVO appUserVO) {
		return new Specification<AppUser>() {
			@Override
			public Predicate toPredicate(Root<AppUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				// 默认查询条件
				predicates.add(cb.equal(root.<String> get("isDeleted"), 1));

				if (null != appUserVO.getAppUser()) {
					if (StringUtils.isNotBlank(appUserVO.getAppUser().getId())) {
						predicates.add(cb.equal(root.<String> get("id"), appUserVO.getAppUser().getId()));
					}
					if (StringUtils.isNotBlank(appUserVO.getAppUser().getUserName())) {
						predicates.add(cb.like(root.<String> get("userName"),
								"%" + appUserVO.getAppUser().getUserName() + "%"));
					}
					if (StringUtils.isNotBlank(appUserVO.getAppUser().getEmail())) {
						predicates.add(
								cb.like(root.<String> get("email"), "%" + appUserVO.getAppUser().getEmail() + "%"));
					}
					if (StringUtils.isNotBlank(appUserVO.getAppUser().getPhone())) {
						predicates.add(
								cb.like(root.<String> get("phone"), "%" + appUserVO.getAppUser().getPhone() + "%"));
					}
				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		};
	}

	@Override
	public void updateModule(AppUserVO appUserVO) {
		if (StringUtils.isNotEmpty(appUserVO.getId())) {
			appUserVO.getAppUser().setId(appUserVO.getId());
			appUserVO.getAppUserDetail().setId(appUserVO.getId());
			appUserRepo.save(appUserVO.getAppUser());
			appUserDetailRepo.save(appUserVO.getAppUserDetail());
		}
	}

	@Override
	public void del(String id) {
		AppUser appUser = appUserRepo.findOne(id);
		appUser.setIsDeleted(0);
		appUserRepo.save(appUser);
	}

	@Override
	public List<UserFollowCollect> findByFolColByUserId(String userId, Integer pindex, Integer pcount) {

		return appUserDao.findByFolColByUserId(userId, pindex, pcount);
	}

	@Override
	public Integer countByFolColByUserId(String userId) {

		return appUserDao.countByFolColByUserId(userId);

	}

	/******************** WEB END ***********/
}
