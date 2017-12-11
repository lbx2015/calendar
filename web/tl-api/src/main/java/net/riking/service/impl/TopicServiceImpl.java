package net.riking.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.dao.TopicDao;
import net.riking.entity.model.TopicResult;
import net.riking.service.TQuestionService;
import net.riking.service.TopicService;

@Service("topicService")
@Transactional
public class TopicServiceImpl implements TopicService {
	private static final Logger logger = LogManager.getLogger(TQuestionService.class);

	@Autowired
	TopicDao topicDao;

	@Override
	public List<TopicResult> findTopicOfInterest(String userId, int begin, int end) {
		return topicDao.findTopicOfInterest(userId, begin, end);
	}

}
