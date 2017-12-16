package net.riking.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.dao.NewsDao;
import net.riking.entity.model.News;
import net.riking.service.NewsService;
import net.riking.service.TQuestionService;

@Service("newsService")
@Transactional
public class NewsServiceImpl implements NewsService {
	private static final Logger logger = LogManager.getLogger(TQuestionService.class);

	@Autowired
	NewsDao newsDao;

	@Override
	public List<News> findCollectNews(String userId, int begin, int pageCount) {
		return newsDao.findCollectNews(userId, begin, pageCount);
	}

}
