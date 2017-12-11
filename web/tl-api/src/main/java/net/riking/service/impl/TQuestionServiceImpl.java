package net.riking.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.dao.TQuestionDao;
import net.riking.entity.model.QAExcellentResp;
import net.riking.entity.model.TQuestionResult;
import net.riking.service.TQuestionService;

@Service("tQuestionService")
@Transactional
public class TQuestionServiceImpl implements TQuestionService {
	private static final Logger logger = LogManager.getLogger(TQuestionService.class);

	@Autowired
	TQuestionDao tQuestionDao;

	@Override
	public List<TQuestionResult> findTopicHomeUp(String userId, Date reqTimeStamp, int start, int end) {

		return tQuestionDao.findTopicHomeUp(userId, reqTimeStamp, start, end);

	}

	@Override
	public List<TQuestionResult> findTopicHomeDown(String userId, Date reqTimeStamp, int start, int end) {
		return tQuestionDao.findTopicHomeDown(userId, reqTimeStamp, start, end);
	}

	@Override
	public List<TQuestionResult> findEssenceByTid(String topicId, int start, int end) {
		return tQuestionDao.findEssenceByTid(topicId, start, end);
	}

	@Override
	public List<QAExcellentResp> findExcellentResp(String topicId, int start, int end) {
		return tQuestionDao.findExcellentResp(topicId, start, end);
	}

}
