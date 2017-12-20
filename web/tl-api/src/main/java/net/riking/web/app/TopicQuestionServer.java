package net.riking.web.app;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QAInviteRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.UserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.QAInvite;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.model.TopicQuestion;
import net.riking.entity.params.TQuestionParams;
import net.riking.service.AppUserService;

/**
 * 问题接口
 * @author jc.tan
 * @version crateTime：2017年11月23日 上午10:46:40
 * @used 问题接口
 */
@RestController
@RequestMapping(value = "/topicQuestion")
public class TopicQuestionServer {
	protected final transient Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TopicQuestionRepo topicQuestionRepo;

	@Autowired
	TopicRelRepo topicRelRepo;

	@Autowired
	QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	HttpServletRequest request;

	@Autowired
	TQuestionRelRepo tQuestionRelRepo;

	@Autowired
	QACommentRepo qACommentRepo;

	@Autowired
	QAnswerRelRepo qAnswerRelRepo;

	@Autowired
	UserFollowRelRepo userFollowRelRepo;

	@Autowired
	QAInviteRepo qAInviteRepo;

	@Autowired
	AppUserService appUserService;

	/**
	 * 问题的详情[userId,tqId]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "问题的详情", notes = "POST")
	@RequestMapping(value = "/getTopicQuestion", method = RequestMethod.POST)
	public AppResp getTopicQuestion(@RequestBody TQuestionParams tQuestionParams) {
		TopicQuestion topicQuestion = topicQuestionRepo.getById(tQuestionParams.getTqId());
		// TODO 问题的关注数 后面从redis里面取
		Integer followNum = tQuestionRelRepo.followCount(tQuestionParams.getTqId(), 0);// 0-关注
		topicQuestion.setFollowNum(followNum);
		// TODO 问题的回答数 后面从redis里面取
		Integer answerNum = questionAnswerRepo.answerCount(tQuestionParams.getTqId());
		topicQuestion.setAnswerNum(answerNum);
		topicQuestion.setIsFollow(0);// 0-未关注
		List<String> questIds = tQuestionRelRepo.findByUser(tQuestionParams.getUserId(), 0);// 0-关注
		for (String tqId : questIds) {
			if (topicQuestion.getId().equals(tqId)) {
				topicQuestion.setIsFollow(1);// 1-已关注
			}
		}
		topicQuestion.setQuestionAnswers(findAnswerList(tQuestionParams));
		if (null != topicQuestion.getPhotoUrl()) {
			topicQuestion
					.setPhotoUrl(appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + topicQuestion.getPhotoUrl());
		}
		// 等级
		if (null != topicQuestion.getExperience()) {
			topicQuestion.setGrade(appUserService.transformExpToGrade(topicQuestion.getExperience()));
		}
		return new AppResp(topicQuestion, CodeDef.SUCCESS);
	}

	/**
	 * 问题回答列表[userId,tqId]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "问题回答列表", notes = "POST")
	@RequestMapping(value = "/questionAnswerList", method = RequestMethod.POST)
	public AppResp questionAnswerList(@RequestBody TQuestionParams tQuestionParams) {

		return new AppResp(findAnswerList(tQuestionParams), CodeDef.SUCCESS);
	}

	/**
	 * 邀请回答的邀请
	 * @param params[userId;attentObjId;tqId;]
	 * @return
	 */
	@ApiOperation(value = "邀请回答的邀请", notes = "POST")
	@RequestMapping(value = "/answerInvite", method = RequestMethod.POST)
	public AppResp answerInvite_(@RequestBody TQuestionParams tQuestionParams) {
		if (StringUtils.isBlank(tQuestionParams.getUserId())) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (StringUtils.isBlank(tQuestionParams.getAttentObjId())) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (StringUtils.isBlank(tQuestionParams.getTqId())) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		QAInvite qaInvite = qAInviteRepo.findByOne(tQuestionParams.getUserId(), tQuestionParams.getAttentObjId(),
				tQuestionParams.getTqId());
		if (null == qaInvite) {
			// 如果传过来的参数是收藏，保存新的一条收藏记录
			QAInvite qaInviteNew = new QAInvite();
			qaInviteNew.setUserId(tQuestionParams.getUserId());
			qaInviteNew.setToUserId(tQuestionParams.getAttentObjId());
			qaInviteNew.setQuestionId(tQuestionParams.getTqId());
			qAInviteRepo.save(qaInviteNew);
		}

		return new AppResp(CodeDef.SUCCESS);
	}

	private List<QuestionAnswer> findAnswerList(TQuestionParams tQuestionParams) {
		List<QuestionAnswer> questionAnswerList = questionAnswerRepo.findByTqId(tQuestionParams.getTqId(),
				tQuestionParams.getUserId());
		for (QuestionAnswer questionAnswer : questionAnswerList) {
			if (null != questionAnswer.getPhotoUrl()) {
				questionAnswer.setPhotoUrl(
						appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + questionAnswer.getPhotoUrl());
			}
			// 等级
			if (null != questionAnswer.getExperience()) {
				questionAnswer.setGrade(appUserService.transformExpToGrade(questionAnswer.getExperience()));
			}
			// TODO 统计数后面从redis中取回答的评论数
			Integer commentNum = qACommentRepo.commentCount(questionAnswer.getId());
			questionAnswer.setCommentNum(commentNum);
			// TODO 统计数后面从redis中取点赞数
			Integer agreeNum = qAnswerRelRepo.agreeCount(questionAnswer.getId(), 1);// 1-点赞
			questionAnswer.setAgreeNum(agreeNum);
		}
		return questionAnswerList;
	}

}
