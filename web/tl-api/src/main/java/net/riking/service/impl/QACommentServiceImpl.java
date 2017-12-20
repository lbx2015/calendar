package net.riking.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.dao.QACommentDao;
import net.riking.entity.model.QACommentResult;
import net.riking.service.QACommentService;
import net.riking.service.TQuestionService;

@Service("qaCommentService")
@Transactional
public class QACommentServiceImpl implements QACommentService {
	private static final Logger logger = LogManager.getLogger(TQuestionService.class);

	@Autowired
	QACommentDao qaCommentDao;

	@Override
	public List<QACommentResult> findByUserId(String userId, Integer pageBegin, Integer pageCount) {
		return qaCommentDao.findByUserId(userId, pageBegin, pageCount);
	}

}
