package net.riking.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.config.Const;
import net.riking.dao.QAnswerDao;
import net.riking.dao.repo.NCReplyRepo;
import net.riking.dao.repo.QACReplyRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.NCReply;
import net.riking.entity.model.QACReply;
import net.riking.entity.model.QAComment;
import net.riking.entity.model.QAnswerRel;
import net.riking.entity.model.QAnswerResult;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.params.CommentParams;
import net.riking.entity.params.QAnswerParams;
import net.riking.service.QAnswerService;
import net.riking.util.Utils;

@Service("QAnswerService")
@Transactional
public class QAnswerServiceImpl implements QAnswerService {
	private static final Logger logger = LogManager.getLogger(QAnswerService.class);

	@Autowired
	QAnswerDao qAnswerDao;

	@Autowired
	QAnswerRelRepo qAnswerRelRepo;

	@Autowired
	QACommentRepo qACommentRepo;

	@Autowired
	QACReplyRepo qACReplyRepo;

	@Autowired
	NCReplyRepo nCReplyRepo;

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

	@Override
	public void QaAgreeCollect(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException {
		QAnswerParams qAnswerParams = new QAnswerParams();
		qAnswerParams = (QAnswerParams) Utils.fromObjToObjValue(optCommon, qAnswerParams);
		switch (qAnswerParams.getOptType()) {
			// 1-点赞
			case Const.OBJ_OPT_GREE:
				if (Const.EFFECTIVE == qAnswerParams.getEnabled()) {
					QAnswerRel rels = qAnswerRelRepo.findByOne(qAnswerParams.getUserId(),
							qAnswerParams.getQuestAnswerId(), Const.OBJ_OPT_GREE);// 1-点赞
					if (null == rels) {
						// 如果传过来的参数是点赞，保存新的一条关注记录
						QAnswerRel qAnswerRel = new QAnswerRel();
						qAnswerRel.setUserId(qAnswerParams.getUserId());
						qAnswerRel.setQaId(qAnswerParams.getQuestAnswerId());
						qAnswerRel.setDataType(Const.OBJ_OPT_GREE);// 点赞
						qAnswerRelRepo.save(qAnswerRel);
					}
				} else if (Const.INVALID == qAnswerParams.getEnabled()) {
					// 如果传过来是取消点赞，把之前一条记录物理删除
					qAnswerRelRepo.deleteByUIdAndQaId(qAnswerParams.getUserId(), qAnswerParams.getQuestAnswerId(),
							Const.OBJ_OPT_GREE);// 点赞
				} else {
					logger.error("问题回答点赞参数异常：enabled=" + qAnswerParams.getEnabled());
					throw new RuntimeException("问题回答点赞参数异常：enabled=" + qAnswerParams.getEnabled());
				}
				break;
			// 2-收藏
			case Const.OBJ_OPT_COLLECT:
				if (Const.EFFECTIVE == qAnswerParams.getEnabled()) {
					QAnswerRel rels = qAnswerRelRepo.findByOne(qAnswerParams.getUserId(),
							qAnswerParams.getQuestAnswerId(), Const.OBJ_OPT_COLLECT);// 1-收藏
					if (null == rels) {
						// 如果传过来的参数是收藏，保存新的一条关注记录
						QAnswerRel qAnswerRel = new QAnswerRel();
						qAnswerRel.setUserId(qAnswerParams.getUserId());
						qAnswerRel.setQaId(qAnswerParams.getQuestAnswerId());
						qAnswerRel.setDataType(Const.OBJ_OPT_COLLECT);// 收藏
						qAnswerRelRepo.save(qAnswerRel);
					}
				} else if (Const.INVALID == qAnswerParams.getEnabled()) {
					// 如果传过来是取消收藏，把之前一条记录物理删除
					qAnswerRelRepo.deleteByUIdAndQaId(qAnswerParams.getUserId(), qAnswerParams.getQuestAnswerId(),
							Const.OBJ_OPT_COLLECT);// 收藏
				} else {
					logger.error("参数异常：enabled=" + qAnswerParams.getEnabled());
					throw new RuntimeException("问题回答收藏参数异常：enabled=" + qAnswerParams.getEnabled());
				}
				break;
			default:
				logger.error("参数异常：objType=" + qAnswerParams.getOptType());
				throw new RuntimeException("问题回答收藏参数异常：obtType=" + qAnswerParams.getOptType());
		}
	}

	@Override
	public void qACommentPub(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException {
		QAnswerParams qAnswerParams = new QAnswerParams();
		qAnswerParams = (QAnswerParams) Utils.fromObjToObjValue(optCommon, qAnswerParams);
		QAComment qaComment = new QAComment();
		qaComment.setUserId(qAnswerParams.getUserId());
		qaComment.setQuestionAnswerId(qAnswerParams.getQuestAnswerId());
		qaComment.setContent(qAnswerParams.getContent());
		qaComment.setCreatedBy(qAnswerParams.getUserId());
		qaComment.setModifiedBy(qAnswerParams.getUserId());
		qaComment.setIsAduit(0);// 未审核
		qACommentRepo.save(qaComment);
	}

	@Override
	public void commentReply(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException {
		CommentParams commentParams = new CommentParams();
		commentParams = (CommentParams) Utils.fromObjToObjValue(optCommon, commentParams);
		switch (commentParams.getObjType()) {
			// 回答类
			case Const.OBJ_TYPE_ANSWER:
				QACReply qACReply = new QACReply();
				qACReply.setFromUserId(commentParams.getUserId());
				qACReply.setCreatedBy(commentParams.getUserId());
				qACReply.setModifiedBy(commentParams.getUserId());
				qACReply.setCommentId(commentParams.getCommentId());
				qACReply.setContent(commentParams.getContent());
				qACReply.setReplyId(commentParams.getReplyId());
				qACReply.setToUserId(commentParams.getToUserId());
				qACReply.setIsAduit(0);// 是否审核： 0-未审核，1-已审核,2-不通过
				qACReply = qACReplyRepo.save(qACReply);
				break;
			// 资讯类
			case Const.OBJ_TYPE_NEWS:
				NCReply ncReply = new NCReply();
				ncReply.setFromUserId(commentParams.getUserId());
				ncReply.setCreatedBy(commentParams.getUserId());
				ncReply.setModifiedBy(commentParams.getUserId());
				ncReply.setCommentId(commentParams.getCommentId());
				ncReply.setContent(commentParams.getContent());
				ncReply.setReplyId(commentParams.getReplyId());
				ncReply.setToUserId(commentParams.getToUserId());
				ncReply.setIsAduit(0);// 是否审核： 0-未审核，1-已审核,2-不通过
				ncReply = nCReplyRepo.save(ncReply);
				break;
			default:
				throw new RuntimeException("objType参数异常:" + commentParams.getObjType());
		}
	}
}
