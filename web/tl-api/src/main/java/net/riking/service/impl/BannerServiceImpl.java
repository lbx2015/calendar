package net.riking.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.config.Const;
import net.riking.entity.model.Banner;
import net.riking.service.BannerService;
import net.riking.util.FileUtils;

@Service("bannerSerice")
@Transactional
public class BannerServiceImpl implements BannerService {
	private static final Logger logger = LogManager.getLogger("BannerService");

	@Override
	public void moveFile(Banner banner) {
		banner.setIsAduit("0");
		// 临时文件的图片转移路径
		String[] contentFileNames = new String[] { banner.getBannerURL() };
		if (contentFileNames.length != 0 && contentFileNames != null) {
			copyFile(contentFileNames);
		}
	}

	private void copyFile(String[] fileNames) {
		// TODO Auto-generated method stub
		for (int i = 0; i < fileNames.length; i++) {
			String fileName = fileNames[i].split(">")[0].replace("\"", "");
			String newPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_BANNER_PHOTO_PATH + fileName;
			String oldPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_TEMP_PHOTO_PATH + fileName;
			try {
				FileUtils.copyFile(oldPhotoUrl, newPhotoUrl);
			} catch (Exception e) {
				logger.error("文件复制异常" + e);
				throw new RuntimeException("文件复制异常" + e);
			}
			FileUtils.deleteFile(oldPhotoUrl);
		}
	}

}
