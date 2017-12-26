package net.riking.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.config.Const;
import net.riking.dao.QACommentDao;
import net.riking.dao.repo.NCAgreeRelRepo;
import net.riking.dao.repo.QACAgreeRelRepo;
import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.NCAgreeRel;
import net.riking.entity.model.QACAgreeRel;
import net.riking.entity.model.QACommentResult;
import net.riking.entity.params.CommentParams;
import net.riking.service.QACommentService;
import net.riking.service.TQuestionService;
import net.riking.util.Utils;

@Service("qaCommentService")
@Transactional
public class QACommentServiceImpl implements QACommentService {
	private static final Logger logger = LogManager.getLogger(TQuestionService.class);

	@Autowired
	QACommentDao qaCommentDao;

	@Autowired
	NCAgreeRelRepo nCAgreeRelRepo;

	@Autowired
	QACAgreeRelRepo qACAgreeRelRepo;

	@Override
	public List<QACommentResult> findByUserId(String userId, Integer pageBegin, Integer pageCount) {
		return qaCommentDao.findByUserId(userId, pageBegin, pageCount);
	}

	@Override
	public void commentAgree(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException {
		CommentParams commentParams = new CommentParams();
		commentParams = (CommentParams) Utils.fromObjToObjValue(optCommon, commentParams);
		switch (commentParams.getObjType()) {
			// 回答类
			case Const.OBJ_TYPE_ANSWER:
				switch (commentParams.getEnabled()) {
					case Const.EFFECTIVE:
						QACAgreeRel rels = qACAgreeRelRepo.findByOne(commentParams.getUserId(),
								commentParams.getCommentId(), Const.OBJ_OPT_GREE);// 1-点赞
						if (null == rels) {
							// 如果传过来的参数是点赞，保存新的一条收藏记录
							QACAgreeRel qACAgreeRel = new QACAgreeRel();
							qACAgreeRel.setUserId(commentParams.getUserId());
							qACAgreeRel.setQacId(commentParams.getCommentId());
							qACAgreeRel.setDataType(Const.OBJ_OPT_GREE);// 点赞
							qACAgreeRelRepo.save(qACAgreeRel);
						}
						break;
					case Const.INVALID:
						// 如果传过来是取消点赞，把之前一条记录物理删除
						qACAgreeRelRepo.deleteByUIdAndQaId(commentParams.getUserId(), commentParams.getCommentId(),
								Const.OBJ_OPT_GREE);// 1-点赞
						break;
					default:
						logger.error("参数异常：enabled=" + commentParams.getEnabled());
						throw new RuntimeException("参数异常：enabled=" + commentParams.getEnabled());
				}
				break;
			// 资讯类
			case Const.OBJ_TYPE_NEWS:
				switch (commentParams.getEnabled()) {
					case Const.EFFECTIVE:
						NCAgreeRel rels = nCAgreeRelRepo.findByOne(commentParams.getUserId(),
								commentParams.getCommentId(), 1);// 1-点赞
						if (null == rels) {
							// 如果传过来的参数是点赞，保存新的一条收藏记录
							NCAgreeRel nCAgreeRel = new NCAgreeRel();
							nCAgreeRel.setUserId(commentParams.getUserId());
							nCAgreeRel.setNcId(commentParams.getCommentId());
							nCAgreeRel.setDataType(Const.OBJ_OPT_GREE);// 点赞
							nCAgreeRelRepo.save(nCAgreeRel);
						}
						break;
					case Const.INVALID:
						// 如果传过来是取消点赞，把之前一条记录物理删除
						nCAgreeRelRepo.deleteByUIdAndNcId(commentParams.getUserId(), commentParams.getCommentId(),
								Const.OBJ_OPT_GREE);// 1-点赞
						break;
					default:
						logger.error("参数异常：enabled=" + commentParams.getEnabled());
						throw new RuntimeException("参数异常：enabled=" + commentParams.getEnabled());
				}
				break;
			default:
				break;
		}

	}

}
