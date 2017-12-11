package net.riking.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.dao.QAnswerDao;
import net.riking.service.QAnswerService;

@Service("QAnswerService")
@Transactional
public class QAnswerServiceImpl implements QAnswerService {
	private static final Logger logger = LogManager.getLogger(QAnswerService.class);

	@Autowired
	QAnswerDao qAnswerDao;

	/**
	 * 根据问题的id根据评论数，点赞数排序查询第一条回答
	 * @param questionId
	 * @return
	 */
	@Override
	public String getAContentByOne(String questionId) {

		return qAnswerDao.getAContentByOne(questionId);

	}

}
