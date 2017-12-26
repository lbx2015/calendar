package net.riking.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.config.Const;
import net.riking.dao.NewsDao;
import net.riking.entity.model.News;
import net.riking.service.AppUserService;
import net.riking.service.NewsService;
import net.riking.service.TQuestionService;

@Service("newsService")
@Transactional
public class NewsServiceImpl implements NewsService {
	private static final Logger logger = LogManager.getLogger(TQuestionService.class);

	@Autowired
	NewsDao newsDao;

	@Autowired
	AppUserService appUserService;

	@Override
	public List<News> findCollectNews(String userId, int begin, int pageCount) {
		return newsDao.findCollectNews(userId, begin, pageCount);
	}

	@Override
	public String concatCoverUrls(String coverUrls) {
		String coverUrl = appUserService.getPhotoUrlPath(Const.TL_NEWS_PHOTO_PATH);
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

}
