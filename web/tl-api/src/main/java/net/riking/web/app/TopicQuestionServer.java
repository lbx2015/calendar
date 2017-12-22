package net.riking.web.app;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

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
import net.riking.config.Config;
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
import net.riking.util.Utils;

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
	Config config;

	@Autowired
	AppUserService appUserService;

	@ApiOperation(value = "问题详情分享", notes = "POST")
	@RequestMapping(value = "/questionShare", method = RequestMethod.POST)
	public AppResp questionShare_() {
		return new AppResp(config.getAppHtmlPath() + Const.TL_QUESTIONSHARE_HTML5_PATH, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "提问", notes = "POST")
	@RequestMapping(value = "/inquiry", method = RequestMethod.POST)
	public AppResp aboutApp(@RequestBody Map<String, Object> params) {
		TQuestionParams tQuestionParams = Utils.map2Obj(params, TQuestionParams.class);
		String title = "";
		try {
			title = java.net.URLEncoder.encode(java.net.URLEncoder.encode(tQuestionParams.getTitle(), "utf-8"),
					"utf-8");
		} catch (UnsupportedEncodingException e) {
			return new AppResp(CodeDef.EMP.GENERAL_ERR, CodeDef.EMP.GENERAL_ERR_DESC);
		}
		return new AppResp(config.getAppHtmlPath() + Const.TL_REPORT_INQUIRY_HTML5_PATH + "?userId="
				+ tQuestionParams.getUserId() + "&title=" + title + "&topicId=" + tQuestionParams.getTopicId(),
				CodeDef.SUCCESS);
	}

	/**
	 * 问题的详情[userId,tqId]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "问题的详情", notes = "POST")
	@RequestMapping(value = "/getTopicQuestion", method = RequestMethod.POST)
	public AppResp getTopicQuestion(@RequestBody TQuestionParams tQuestionParams) {
		TopicQuestion topicQuestion = topicQuestionRepo.getById(tQuestionParams.getTqId(), tQuestionParams.getUserId());
		// TODO 问题的关注数 后面从redis里面取
		Integer followNum = tQuestionRelRepo.followCount(tQuestionParams.getTqId(), 0);// 0-关注
		topicQuestion.setFollowNum(followNum);
		// TODO 问题的回答数 后面从redis里面取
		Integer answerNum = questionAnswerRepo.answerCount(tQuestionParams.getTqId());
		topicQuestion.setAnswerNum(answerNum);
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

		return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
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
