package net.riking.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.dao.QAnswerDao;
import net.riking.entity.model.QAnswerResult;
import net.riking.entity.model.QuestionAnswer;
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
	public QuestionAnswer getAContentByOne(String questionId) {

		return qAnswerDao.getAContentByOne(questionId);

	}

	@Override
	public List<QAnswerResult> findCollectQAnswer(String userId, int start, int pageCount) {
		return qAnswerDao.findCollectQAnswer(userId, start, pageCount);
	}
}
