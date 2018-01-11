package net.riking.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.dao.repo.QAInviteRepo;
import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.QAInvite;
import net.riking.entity.params.TQuestionParams;
import net.riking.service.QAInviteService;
import net.riking.util.Utils;

@Service("qaInviteService")
@Transactional
public class QAInviteServiceImpl implements QAInviteService {
	private static final Logger logger = LogManager.getLogger(QAInviteService.class);

	@Autowired
	QAInviteRepo qAInviteRepo;

	@Override
	public void saveQaInvite(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException {
		// 保存新的一条邀请记录
		TQuestionParams tQuestionParams = new TQuestionParams();
		tQuestionParams = (TQuestionParams) Utils.fromObjToObjValue(optCommon, tQuestionParams);
		QAInvite qaInvite = new QAInvite();
		qaInvite.setUserId(tQuestionParams.getUserId());
		qaInvite.setToUserId(tQuestionParams.getAttentObjId());
		qaInvite.setQuestionId(tQuestionParams.getTqId());
		qAInviteRepo.save(qaInvite);
	}

}