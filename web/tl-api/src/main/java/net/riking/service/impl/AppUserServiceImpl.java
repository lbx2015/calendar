package net.riking.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.service.AppUserService;
import net.riking.service.SysDataService;
import net.riking.util.EncryptionUtil;

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
		detail.setPhotoUrl(Const.DEFAULT_PHOTO_URL);// 默认头像Url
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
	public String uploadPhoto(MultipartFile mFile, String userId) throws RuntimeException {
		// String suffix =
		// mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("."));
		String fileName = UuidUtils.random() + mFile.getOriginalFilename();
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = mFile.getInputStream();
			String path = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH + Const.TL_PHOTO_PATH;
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
		AppUserDetail appUserDetail = appUserDetailRepo.findOne(userId);
		String oldFileName = appUserDetail.getPhotoUrl();
		String oleFilePath = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH + Const.TL_PHOTO_PATH
				+ oldFileName;
		// 删除服务器上文件
		ModelPropDict dict = sysDataService.getDict("T_ROOT_URL", "PHOTO_URL", "DEFAULT_URL");
		if (!dict.getValu().equals(oldFileName) || !oldFileName.equals(mFile.getOriginalFilename())) {
			this.deleteFile(oleFilePath);
		}
		// 数据库保存路径
		appUserRepo.updatePhoto(userId, fileName);
		return fileName;
	}

	/**
	 * 删除单个文件
	 *
	 * @param fileName 要删除的文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				logger.info("删除单个文件" + fileName + "成功！");
				return true;
			} else {
				logger.info("删除单个文件" + fileName + "失败！");
				return false;
			}
		} else {
			logger.info("删除单个文件失败：" + fileName + "不存在！");
			return false;
		}
	}
}
