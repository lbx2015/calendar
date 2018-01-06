package net.riking.web.app;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.config.Const;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.AppUserFollowRelRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QAInviteRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.QAInvite;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.model.Topic;
import net.riking.entity.model.TopicQuestion;
import net.riking.entity.params.TQuestionParams;
import net.riking.service.AppUserService;
import net.riking.service.QuestionKeyWordService;
import net.riking.util.FileUtils;
import net.riking.util.MQProduceUtil;
import net.riking.util.Utils;
import net.sf.json.JSONObject;

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
	AppUserFollowRelRepo userFollowRelRepo;

	@Autowired
	QAInviteRepo qAInviteRepo;

	@Autowired
	Config config;

	@Autowired
	AppUserService appUserService;
	
	@Autowired
	QuestionKeyWordService questionKeyWordService;

	@ApiOperation(value = "问题详情分享", notes = "POST")
	@RequestMapping(value = "/questionShare", method = RequestMethod.POST)
	public AppResp questionShare_() {
		return new AppResp(config.getAppHtmlPath() + Const.TL_QUESTIONSHARE_HTML5_PATH, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "提问", notes = "POST")
	@RequestMapping(value = "/inquiry", method = RequestMethod.POST)
	public AppResp inquiry(@RequestBody Map<String, Object> params) {
		TQuestionParams tQuestionParams = Utils.map2Obj(params, TQuestionParams.class);
		String title = "";
		try {
		    if (!tQuestionParams.getTitle().endsWith("?")) {
		    	tQuestionParams.setTitle(tQuestionParams.getTitle()+ "?");
            }
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
			// 如果传过来的参数是邀请，保存新的一条邀请记录
			tQuestionParams.setMqOptType(Const.MQ_OPT_ANSWERINVITE);
			JSONObject jsonArray = JSONObject.fromObject(tQuestionParams);
			MQProduceUtil.sendTextMessage(Const.SYS_OPT_QUEUE, jsonArray.toString());
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

	
	/**
	 * 根据问题title找出话题列表
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "话题列表", notes = "POST")
	@RequestMapping(value = "/getTopicByQuest", method = RequestMethod.POST)
	public AppResp getTopicByQuest_(@RequestBody TQuestionParams Params) {
		List<Topic> list = questionKeyWordService.getTopicByQuestion(Params.getTitle());
		return new AppResp(list, CodeDef.SUCCESS);
	}
	
	@RequestMapping(value = "/questionSave", method = RequestMethod.GET)
	public Resp questionSave_(@RequestParam HashMap<String, Object> params) {
		TopicQuestion topicQuestion = Utils.map2Obj(params, TopicQuestion.class);
		try {
			topicQuestion.setTitle(URLDecoder.decode(topicQuestion.getTitle(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return new Resp(CodeDef.ERROR);
		}
		String[] fileNames = topicQuestion.getContent().split("alt=");
		for (int i = 1; i < fileNames.length; i++) {
			String fileName = fileNames[i].split(">")[0].replace("\"", "");
			String newPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_QUESTION_PHOTO_PATH + fileName;
			String oldPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_TEMP_PHOTO_PATH + fileName;
			try {
				FileUtils.copyFile(oldPhotoUrl, newPhotoUrl);
			} catch (Exception e) {
				logger.error("文件复制异常" + e);
				return new Resp(CodeDef.ERROR);
			}
			FileUtils.deleteFile(oldPhotoUrl);
		}
		topicQuestion.setCreatedBy(topicQuestion.getUserId());
		topicQuestion.setModifiedBy(topicQuestion.getUserId());
		topicQuestion.setIsAduit(0);
		topicQuestion.setContent(topicQuestion.getContent().replace("temp", "question"));
		topicQuestionRepo.save(topicQuestion);

		return new Resp(topicQuestion, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "回答h5页面跳转", notes = "POST")
	@RequestMapping(value = "/answerHtml", method = RequestMethod.POST)
	public AppResp qAnswer(@RequestBody Map<String, Object> params) {
		TQuestionParams tQuestionParams = Utils.map2Obj(params, TQuestionParams.class);
		return new AppResp(config.getAppHtmlPath() /*+ Const.TL_QUESTION_ANSWER_HTML5_PATH*/ + "?userId="
				+ tQuestionParams.getUserId() + "&questionId=" + tQuestionParams.getTqId(),
				CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "回答保存", notes = "GET")
	@RequestMapping(value = "/answerSave", method = RequestMethod.GET)
	public Resp answerSave_(@RequestParam HashMap<String, Object> params) {
		QuestionAnswer questionAnswer = Utils.map2Obj(params, QuestionAnswer.class);
		String[] fileNames = questionAnswer.getContent().split("alt=");
		for (int i = 1; i < fileNames.length; i++) {
			String fileName = fileNames[i].split(">")[0].replace("\"", "");
			String newPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_ANSWER_PHOTO_PATH + fileName;
			String oldPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
					+ Const.TL_TEMP_PHOTO_PATH + fileName;
			try {
				FileUtils.copyFile(oldPhotoUrl, newPhotoUrl);
			} catch (Exception e) {
				logger.error("文件复制异常" + e);
				return new Resp(CodeDef.ERROR);
			}
			FileUtils.deleteFile(oldPhotoUrl);
		}
//		Pattern pattern = Pattern.compile("(?<=alt\\=\")(.+?)(?=\")");
//		Matcher matcher = pattern.matcher(questionAnswer.getContent());
//        while(matcher.find()){
//        	String fileName = matcher.group();
//        	if(StringUtils.isBlank(questionAnswer.getCoverUrl())){
//        		questionAnswer.setCoverUrl(fileName);
//        	}
//        	String newPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
//					+ Const.TL_ANSWER_PHOTO_PATH + fileName;
//			String oldPhotoUrl = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH
//					+ Const.TL_TEMP_PHOTO_PATH + fileName;
//			try {
//				FileUtils.copyFile(oldPhotoUrl, newPhotoUrl);
//			} catch (Exception e) {
//				logger.error("文件复制异常" + e);
//				return new Resp(CodeDef.ERROR);
//			}
//			FileUtils.deleteFile(oldPhotoUrl);
//        }
		questionAnswer.setCreatedBy(questionAnswer.getUserId());
		questionAnswer.setModifiedBy(questionAnswer.getUserId());
		questionAnswer.setIsAduit(0);
		questionAnswer.setCoverUrl(questionAnswer.getContent().split("alt=")[1].split(">")[0].replace("\"", ""));
		questionAnswer.setContent(questionAnswer.getContent().replace("temp", "answer"));
		questionAnswerRepo.save(questionAnswer);
		return new Resp(questionAnswer, CodeDef.SUCCESS);
	}
}
