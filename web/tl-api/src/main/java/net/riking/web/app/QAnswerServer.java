package net.riking.web.app;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.NCAgreeRelRepo;
import net.riking.dao.repo.NCReplyRepo;
import net.riking.dao.repo.NewsCommentRepo;
import net.riking.dao.repo.NewsRelRepo;
import net.riking.dao.repo.QACAgreeRelRepo;
import net.riking.dao.repo.QACReplyRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.QACReply;
import net.riking.entity.model.QAComment;
import net.riking.entity.model.QAnswerRel;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.params.QAnswerParams;
import net.riking.entity.resp.FromUser;
import net.riking.entity.resp.ToUser;

/**
 * 
 * 问题的回答接口
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/qAnswer")
public class QAnswerServer {
	protected final transient Logger logger = LogManager.getLogger(getClass());

	@Autowired
	QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	NewsCommentRepo newsCommentRepo;

	@Autowired
	NCReplyRepo nCReplyRepo;

	@Autowired
	NCAgreeRelRepo nCAgreeRelRepo;

	@Autowired
	NewsRelRepo newsRelRepo;

	@Autowired
	QACommentRepo qACommentRepo;

	@Autowired
	QAnswerRelRepo qAnswerRelRepo;

	@Autowired
	QACReplyRepo qACReplyRepo;

	@Autowired
	QACAgreeRelRepo qACAgreeRelRepo;

	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	/**
	 * 问题回答详情
	 * @param params[userId,questAnswerId]
	 * @return
	 */
	@ApiOperation(value = "问题回答详情", notes = "POST")
	@RequestMapping(value = "/getQAnswer", method = RequestMethod.POST)
	public AppResp getQAnswer(@RequestBody QAnswerParams qAnswerParams) {
		QuestionAnswer questionAnswer = questionAnswerRepo.getById(qAnswerParams.getQuestAnswerId());
		questionAnswer.setIsAgree(0);// 0-未点赞
		questionAnswer.setIsCollect(0);// 0-未收藏
		if (StringUtils.isNotBlank(qAnswerParams.getUserId())) {
			List<QAnswerRel> rels = qAnswerRelRepo.findByUser(qAnswerParams.getUserId());
			for (QAnswerRel rel : rels) {
				if (Const.OBJ_OPT_GREE == rel.getDataType()) {
					if (questionAnswer.getId().equals(rel.getQaId())) {
						questionAnswer.setIsAgree(1);// 1-已点赞
					}
				} else if (Const.OBJ_OPT_COLLECT == rel.getDataType()) {
					if (questionAnswer.getId().equals(rel.getQaId())) {
						questionAnswer.setIsCollect(1);// 1-已收藏
					}
				}
			}
		}
		return new AppResp(questionAnswer, CodeDef.SUCCESS);
	}

	/**
	 * 问题回答的评论
	 * @param params[userId,questAnswerId,content]
	 * @return
	 */
	@ApiOperation(value = "问题回答的评论", notes = "POST")
	@RequestMapping(value = "/qACommentPub", method = RequestMethod.POST)
	public AppResp qACommentPub(@RequestBody QAnswerParams qAnswerParams) {
		if (StringUtils.isBlank(qAnswerParams.getUserId())) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (StringUtils.isBlank(qAnswerParams.getQuestAnswerId())) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (StringUtils.isBlank(qAnswerParams.getContent())) {
			return new AppResp("", CodeDef.SUCCESS);
		}
		QAComment qaComment = new QAComment();
		qaComment.setUserId(qAnswerParams.getUserId());
		qaComment.setQuestionAnswerId(qAnswerParams.getQuestAnswerId());
		qaComment.setContent(qAnswerParams.getContent());
		qaComment.setCreatedBy(qAnswerParams.getUserId());
		qaComment.setModifiedBy(qAnswerParams.getUserId());
		qaComment.setIsAudit(0);// 未审核
		qACommentRepo.save(qaComment);
		AppUser appUser = appUserRepo.findOne(qaComment.getUserId());
		AppUserDetail appUserDetail = appUserDetailRepo.findOne(qaComment.getUserId());
		if (null != appUser) {
			qaComment.setUserName(appUser.getUserName());
		}
		if (null != appUserDetail) {
			qaComment.setPhotoUrl(appUserDetail.getPhotoUrl());
		}
		qaComment.setIsAgree(0);// 0-未点赞
		if (StringUtils.isNotBlank(qAnswerParams.getUserId())) {
			List<String> qacIds = qACAgreeRelRepo.findByUser(qAnswerParams.getUserId(), 1);// 点赞
			for (String qacId : qacIds) {
				if (qaComment.getId().equals(qacId)) {
					qaComment.setIsAgree(1);// 1-已点赞
				}
			}
		}
		return new AppResp(qaComment, CodeDef.SUCCESS);
	}

	/**
	 * 问题回答的点赞/收藏
	 * @param params[userId,questAnswerId,optType,enabled]
	 * @return
	 */
	@ApiOperation(value = "问题回答的点赞/收藏", notes = "POST")
	@RequestMapping(value = "/agreeOrCollect", method = RequestMethod.POST)
	public AppResp QAnswerAgree(@RequestBody QAnswerParams qAnswerParams) {
		switch (qAnswerParams.getOptType()) {
			// 1-点赞
			case 1:
				if (Const.EFFECTIVE == qAnswerParams.getEnabled()) {
					QAnswerRel rels = qAnswerRelRepo.findByOne(qAnswerParams.getUserId(),
							qAnswerParams.getQuestAnswerId(), 1);// 1-点赞
					if (null == rels) {
						// 如果传过来的参数是点赞，保存新的一条关注记录
						QAnswerRel qAnswerRel = new QAnswerRel();
						qAnswerRel.setUserId(qAnswerParams.getUserId());
						qAnswerRel.setQaId(qAnswerParams.getQuestAnswerId());
						qAnswerRel.setDataType(1);// 点赞
						qAnswerRelRepo.save(qAnswerRel);
					}
				} else if (Const.INVALID == qAnswerParams.getEnabled()) {
					// 如果传过来是取消点赞，把之前一条记录物理删除
					qAnswerRelRepo.deleteByUIdAndQaId(qAnswerParams.getUserId(), qAnswerParams.getQuestAnswerId(),
							Const.OBJ_OPT_GREE);// 点赞
				} else {
					logger.error("参数异常：enabled=" + qAnswerParams.getEnabled());
					return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			// 2-收藏
			case 2:
				if (Const.EFFECTIVE == qAnswerParams.getEnabled()) {
					QAnswerRel rels = qAnswerRelRepo.findByOne(qAnswerParams.getUserId(),
							qAnswerParams.getQuestAnswerId(), 2);// 1-收藏
					if (null == rels) {
						// 如果传过来的参数是收藏，保存新的一条关注记录
						QAnswerRel qAnswerRel = new QAnswerRel();
						qAnswerRel.setUserId(qAnswerParams.getUserId());
						qAnswerRel.setQaId(qAnswerParams.getQuestAnswerId());
						qAnswerRel.setDataType(2);// 收藏
						qAnswerRelRepo.save(qAnswerRel);
					}
				} else if (Const.INVALID == qAnswerParams.getEnabled()) {
					// 如果传过来是取消收藏，把之前一条记录物理删除
					qAnswerRelRepo.deleteByUIdAndQaId(qAnswerParams.getUserId(), qAnswerParams.getQuestAnswerId(),
							Const.OBJ_OPT_COLLECT);// 收藏
				} else {
					logger.error("参数异常：enabled=" + qAnswerParams.getEnabled());
					return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			default:
				logger.error("参数异常：objType=" + qAnswerParams.getOptType());
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		return new AppResp("", CodeDef.SUCCESS);
	}

	/**
	 * TODO 问题回答评论列表[userId,tqId，questAnswerId]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "问题回答评论列表", notes = "POST")
	@RequestMapping(value = "/qACommentList", method = RequestMethod.POST)
	public AppResp qACommentList(@RequestBody QAnswerParams qAnswerParams) {
		// 返回到前台的问题回答列表
		List<QAComment> questionAnswerList = qACommentRepo.findByQaId(qAnswerParams.getQuestAnswerId());
		for (QAComment qAComment : questionAnswerList) {
			List<QACReply> qacReplies = qACReplyRepo.getByCommentId(qAComment.getId());
			for (QACReply qacReply : qacReplies) {
				AppUser appUser = appUserRepo.findOne(qacReply.getFromUserId());
				if (null != appUser) {
					FromUser fromUser = new FromUser();
					fromUser.setUserId(qacReply.getFromUserId());
					fromUser.setUserName(appUser.getUserName());
					qacReply.setFromUser(fromUser);
				}
				if (null != qacReply.getToUserId()) {
					AppUser apptoUser = appUserRepo.findOne(qacReply.getToUserId());
					if (null != apptoUser) {
						ToUser toUser = new ToUser();
						toUser.setUserId(qacReply.getToUserId());
						toUser.setUserName(apptoUser.getUserName());
						qacReply.setToUser(toUser);
					}
				}

			}
			qAComment.setQacReplyList(qacReplies);
			// TODO 统计数后面从redis中取点赞数
			Integer agreeNum = 0;
			agreeNum = qACAgreeRelRepo.agreeCount(qAComment.getId(), Const.OBJ_OPT_GREE);// 点赞
			qAComment.setAgreeNumber(agreeNum);
			qAComment.setIsAgree(0);// 0-未点赞
			if (StringUtils.isNotBlank(qAnswerParams.getUserId())) {
				List<String> qacIds = qACAgreeRelRepo.findByUser(qAnswerParams.getUserId(), 1);// 点赞
				for (String qacId : qacIds) {
					if (qAComment.getId().equals(qacId)) {
						qAComment.setIsAgree(1);// 1-已点赞
					}

				}
			}
		}

		return new AppResp(questionAnswerList, CodeDef.SUCCESS);
	}
}
