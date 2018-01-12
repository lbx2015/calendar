package net.riking.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.config.Const;
import net.riking.dao.NewsDao;
import net.riking.dao.repo.NewsCommentRepo;
import net.riking.dao.repo.NewsRelRepo;
import net.riking.dao.repo.NewsRepo;
import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.News;
import net.riking.entity.model.NewsComment;
import net.riking.entity.model.NewsRel;
import net.riking.entity.params.NewsParams;
import net.riking.service.NewsService;
import net.riking.service.TQuestionService;
import net.riking.util.FileUtils;
import net.riking.util.Utils;

@Service("newsService")
@Transactional
public class NewsServiceImpl implements NewsService {
	private static final Logger logger = LogManager.getLogger(TQuestionService.class);

	@Autowired
	NewsDao newsDao;

	// @Autowired
	// AppUserService appUserService;

	@Autowired
	NewsRelRepo newsRelRepo;

	@Autowired
	NewsCommentRepo newsCommentRepo;

	@Autowired
	NewsRepo newsRepo;

	@Override
	public List<News> findCollectNews(String userId, int begin, int pageCount) {
		return newsDao.findCollectNews(userId, begin, pageCount);
	}

	@Override
	public String concatCoverUrls(String coverUrls) {
		// String coverUrl = appUserService.getPhotoUrlPath(Const.TL_NEWS_PHOTO_PATH);
		String coverUrl = FileUtils.getAbsolutePathByProject(Const.TL_NEWS_CONTENT_PATH);
		if (null != coverUrls) {
			String[] strings = coverUrls.split(",");
			if (null != strings) {
				for (int i = 0; i < strings.length; i++) {
					if (strings.length - 1 != i) {
						coverUrls += coverUrl + strings[i] + ",";
					} else {
						coverUrls += coverUrl + strings[i];
					}
				}
			} else {
				coverUrls = coverUrl + coverUrls;
			}
		}
		return coverUrls;
	}

	@Override
	public void newsCollect(MQOptCommon common) throws IllegalArgumentException, IllegalAccessException {
		NewsParams newsParams = new NewsParams();
		newsParams = (NewsParams) Utils.fromObjToObjValue(common, newsParams);
		switch (newsParams.getEnabled()) {
			case Const.EFFECTIVE:
				NewsRel rels = newsRelRepo.findByOne(newsParams.getUserId(), newsParams.getNewsId(),
						Const.OBJ_OPT_COLLECT);// 2-收藏
				if (null == rels) {
					// 如果传过来的参数是收藏，保存新的一条收藏记录
					NewsRel newsRel = new NewsRel();
					newsRel.setUserId(newsParams.getUserId());
					newsRel.setNewsId(newsParams.getNewsId());
					newsRel.setDataType(Const.OBJ_OPT_COLLECT);
					newsRelRepo.save(newsRel);
				}
				break;
			case Const.INVALID:
				// 如果传过来是取消收藏，把之前一条记录物理删除
				newsRelRepo.deleteByUIdAndNId(newsParams.getUserId(), newsParams.getNewsId(), Const.OBJ_OPT_COLLECT);// 2-收藏
				break;
			default:
				logger.error("参数异常：enabled=" + newsParams.getEnabled());
				throw new RuntimeException("参数异常：enabled=" + newsParams.getEnabled());
		}
	}

	@Override
	public void newsCommentPub(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException {
		NewsParams newsParams = new NewsParams();
		newsParams = (NewsParams) Utils.fromObjToObjValue(optCommon, newsParams);
		NewsComment newsCommentInfo = new NewsComment();
		newsCommentInfo.setUserId(newsParams.getUserId());
		newsCommentInfo.setNewsId(newsParams.getNewsId());
		newsCommentInfo.setContent(newsParams.getContent());
		newsCommentInfo.setIsAduit(0);// 0-未审核，1-已审核,2-不通过
		newsCommentInfo = newsCommentRepo.save(newsCommentInfo);
	}

	@Override
	public void moveFile(News news) {
		if (news.getIsAduit() == 2) {
			news.setIsAduit(0);
		}
		// 临时文件的图片转移路径
		String[] contentFileNames = news.getContent().split("alt=");
		if (contentFileNames.length != 0 && contentFileNames != null) {
			copyFile(contentFileNames);
		}
		// 临时文件的图片转移路径
		String[] coverFileNames = new String[] { news.getCoverUrls() };
		if (coverFileNames.length != 0 && coverFileNames != null) {
			copyFile2CoverUrl(coverFileNames);
		}
		news.setContent(news.getContent().replace("temp", "news/content/"));
		// 新增加载内容图片时访问不到默认显示的图片
		news.setContent(news.getContent().replace("<img", "<img onerror=\"this.src='images/img_default.jpg'\" "));
		newsRepo.save(news);
	}

	private void copyFile(String[] fileNames) {
		for (int i = 1; i < fileNames.length; i++) {
			String fileName = fileNames[i].split(">")[0].replace("\"", "");
			String newPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_NEWS_CONTENT_PATH + fileName;
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

	private void copyFile2CoverUrl(String[] fileNames) {
		for (int i = 0; i < fileNames.length; i++) {
			String fileName = fileNames[i];
			String newPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_NEWS_COVER_PATH + fileName;
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
