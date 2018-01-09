package net.riking.web.app;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.config.Const;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserFollowRelRepo;
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
import net.riking.entity.model.UserFollowRel;
import net.riking.entity.params.QAnswerParams;
import net.riking.entity.resp.FromUser;
import net.riking.entity.resp.ToUser;
import net.riking.service.AppUserService;
import net.riking.util.FileUtils;
import net.riking.util.MQProduceUtil;
import net.sf.json.JSONObject;

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
	HttpServletRequest request;

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

	@Autowired
	AppUserService appUserService;

	@Autowired
	AppUserFollowRelRepo userFollowRelRepo;

	@Autowired
	Config config;

//	@ApiOperation(value = "回答", notes = "POST")
//	@RequestMapping(value = "/answer", method = RequestMethod.POST)
//	public AppResp aboutApp(@RequestBody Map<String, Object> params) {
//		QAnswerParams qAnswerParams = Utils.map2Obj(params, QAnswerParams.class);
//		return new AppResp(config.getAppHtmlPath() + Const.TL_REPORT_INQUIRY_HTML5_PATH + "?userId="
//				+ qAnswerParams.getUserId() + "&questionId=" + qAnswerParams.getQuestionId(), CodeDef.SUCCESS);
//	}

	/**
	 * 问题回答详情
	 * @param params[userId,questAnswerId]
	 * @return
	 */
	@ApiOperation(value = "问题回答详情", notes = "POST")
	@RequestMapping(value = "/getQAnswer", method = RequestMethod.POST)
	public AppResp getQAnswer(@RequestBody QAnswerParams qAnswerParams) {
		QuestionAnswer questionAnswer = questionAnswerRepo.getById(qAnswerParams.getQuestAnswerId());
		if (questionAnswer != null) {
			// TODO 后面优化从redis中取
			Integer commentNum = qACommentRepo.commentCount(questionAnswer.getId());
			Integer agreeNum = qAnswerRelRepo.agreeCount(qAnswerParams.getQuestAnswerId(), 1);// 1-点赞
			questionAnswer.setCommentNum(commentNum);
			questionAnswer.setAgreeNum(agreeNum);
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
			questionAnswer.setIsFollow(0);// 未关注
			List<UserFollowRel> userFollowRels = userFollowRelRepo.findByUser(qAnswerParams.getUserId());
			for (UserFollowRel userFollowRel : userFollowRels) {
				if (userFollowRel.getFollowStatus() == 1
						&& userFollowRel.getToUserId().equals(questionAnswer.getUserId())) {
					questionAnswer.setIsFollow(1);// 已关注
				} else if (userFollowRel.getFollowStatus() == 2
						&& userFollowRel.getToUserId().equals(questionAnswer.getUserId())) {
					questionAnswer.setIsFollow(2);// 互相关注
				}
			}
			if (null != questionAnswer.getPhotoUrl()) {
//				questionAnswer.setPhotoUrl(
//						appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + questionAnswer.getPhotoUrl());
				questionAnswer.setPhotoUrl(
						FileUtils.getPhotoUrl(Const.TL_PHOTO_PATH, this.getClass()) + questionAnswer.getPhotoUrl());
			}
			// 等级
			if (null != questionAnswer.getExperience()) {
				questionAnswer.setGrade(appUserService.transformExpToGrade(questionAnswer.getExperience()));
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
			return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
		}
		qAnswerParams.setMqOptType(Const.MQ_OPT_QANSWER_COMMENT);
		JSONObject jsonArray = JSONObject.fromObject(qAnswerParams);
		MQProduceUtil.sendTextMessage(Const.SYS_OPT_QUEUE, jsonArray.toString());

		QAComment qaComment = new QAComment();
		qaComment.setUserId(qAnswerParams.getUserId());
		qaComment.setQuestionAnswerId(qAnswerParams.getQuestAnswerId());
		qaComment.setContent(qAnswerParams.getContent());
		qaComment.setCreatedBy(qAnswerParams.getUserId());
		qaComment.setModifiedBy(qAnswerParams.getUserId());
		qaComment.setIsAduit(0);// 未审核
		// qACommentRepo.save(qaComment);
		AppUser appUser = appUserRepo.findOne(qaComment.getUserId());
		AppUserDetail appUserDetail = appUserDetailRepo.findOne(qaComment.getUserId());
		if (null != appUser) {
			qaComment.setUserName(appUser.getUserName());
		}
		if (null != appUserDetail) {
			qaComment.setPhotoUrl(appUserDetail.getPhotoUrl());
		}
		qaComment.setIsAgree(0);// 0-未点赞
//		if (StringUtils.isNotBlank(qAnswerParams.getUserId())) {
//			List<String> qacIds = qACAgreeRelRepo.findByUser(qAnswerParams.getUserId(), 1);// 点赞
//			for (String qacId : qacIds) {
//				if (qaComment.getId().equals(qacId)) {
//					qaComment.setIsAgree(1);// 1-已点赞
//				}
//			}
//		}
		if (null != qaComment.getPhotoUrl()) {
//			qaComment.setPhotoUrl(appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + qaComment.getPhotoUrl());
			qaComment.setPhotoUrl(FileUtils.getPhotoUrl(Const.TL_PHOTO_PATH, this.getClass()) + qaComment.getPhotoUrl());
		}
		// 等级
		if (null != qaComment.getExperience()) {
			qaComment.setGrade(appUserService.transformExpToGrade(qaComment.getExperience()));
		}
		qaComment = qACommentRepo.save(qaComment);
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
		// 具体操作在mq队列里面
		qAnswerParams.setMqOptType(Const.MQ_OPT_QA_AGREEOR_COLLECT);
		JSONObject jsonArray = JSONObject.fromObject(qAnswerParams);
		MQProduceUtil.sendTextMessage(Const.SYS_OPT_QUEUE, jsonArray.toString());
		return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
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
		List<QAComment> questionAnswerList = qACommentRepo.findByQaId(qAnswerParams.getQuestAnswerId(),
				qAnswerParams.getUserId(), new PageRequest(0, 30));
		for (QAComment qAComment : questionAnswerList) {
			if (null != qAComment.getPhotoUrl()) {
//				qAComment.setPhotoUrl(appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + qAComment.getPhotoUrl());
				qAComment.setPhotoUrl(FileUtils.getPhotoUrl(Const.TL_PHOTO_PATH, this.getClass()) + qAComment.getPhotoUrl());
			}
			// 等级
			if (null != qAComment.getExperience()) {
				qAComment.setGrade(appUserService.transformExpToGrade(qAComment.getExperience()));
			}
			List<QACReply> qacReplies = qACReplyRepo.getByCommentId(qAComment.getId());
			for (QACReply qacReply : qacReplies) {
				FromUser fromUser = new FromUser();
				fromUser.setUserId(qacReply.getFromUserId());
				fromUser.setUserName(qacReply.getFromUserName());
				qacReply.setFromUser(fromUser);
				if (null != qacReply.getToUserId()) {
					ToUser toUser = new ToUser();
					toUser.setUserId(qacReply.getToUserId());
					toUser.setUserName(qacReply.getToUserName());
					qacReply.setToUser(toUser);
				}

			}
			qAComment.setQacReplyList(qacReplies);
			// TODO 统计数后面从redis中取点赞数
			Integer agreeNum = 0;
			agreeNum = qACAgreeRelRepo.agreeCount(qAComment.getId(), Const.OBJ_OPT_GREE);// 点赞
			qAComment.setAgreeNumber(agreeNum);
		}

		return new AppResp(questionAnswerList, CodeDef.SUCCESS);
	}
}
