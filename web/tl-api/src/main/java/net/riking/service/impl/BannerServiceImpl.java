package net.riking.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
	public Page<Banner> findAllToPage(Banner banner, PageRequest pageable) {
		// TODO Auto-generated method stub
		Specification<Banner> bCondi = whereCondition(banner);
		return null;
	}

	private Specification<Banner> whereCondition(Banner banner) {
		return new Specification<Banner>() {
			@Override
			public Predicate toPredicate(Root<Banner> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				// 默认查询条件
				predicates.add(cb.equal(root.<String> get("isDeleted"), 1));

				// if (null != banner) {
				// if (StringUtils.isNotBlank(banner.get)) {
				// predicates.add(cb.like(root.<String> get("userName"),
				// "%" + appUserVO.getAppUser().getUserName() + "%"));
				// }
				// if (StringUtils.isNotBlank(appUserVO.getAppUser().getEmail())) {
				// predicates.add(
				// cb.like(root.<String> get("email"), "%" + appUserVO.getAppUser().getEmail() +
				// "%"));
				// }
				// if (StringUtils.isNotBlank(appUserVO.getAppUser().getPhone())) {
				// predicates.add(
				// cb.like(root.<String> get("phone"), "%" + appUserVO.getAppUser().getPhone() +
				// "%"));
				// }
				// }
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		};
	}

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
