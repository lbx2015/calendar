package net.riking.web.app;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.NewsCommentRepo;
import net.riking.dao.repo.QACAgreeRelRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.SignInRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.UserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.News;
import net.riking.entity.model.QACommentResult;
import net.riking.entity.model.QAnswerResult;
import net.riking.entity.model.QuestResult;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.params.UserFollowParams;
import net.riking.service.AppUserService;
import net.riking.service.NewsService;
import net.riking.service.QACommentService;
import net.riking.service.QAnswerService;
import net.riking.service.SysDataService;
import net.riking.service.TQuestionService;
import net.riking.service.TopicService;

/**
 * 我的动态接口
 * @author jc.tan 2017年11月30日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/userDynamic")
public class AppUserDynamicServer {
	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Autowired
	HttpServletRequest request;

	@Autowired
	QACommentRepo qACommentRepo;

	@Autowired
	SysDataService sysDataservice;

	@Autowired
	AppUserService appUserService;

	@Autowired
	SignInRepo signInRepo;

	@Autowired
	QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	UserFollowRelRepo userFollowRelRepo;

	@Autowired
	TQuestionService tQuestionService;

	@Autowired
	TopicService topicService;

	@Autowired
	TopicRelRepo topicRelRepo;

	@Autowired
	QAnswerRelRepo qAnswerRelRepo;

	@Autowired
	TopicQuestionRepo topicQuestionRepo;

	@Autowired
	QAnswerService qAnswerService;

	@Autowired
	NewsService newsService;

	@Autowired
	NewsCommentRepo newsCommentRepo;

	@Autowired
	QACommentService qaCommentService;

	@Autowired
	QACAgreeRelRepo qACAgreeRelRepo;

	/**
	 * userId;optType(1-评论；2-回答；3-提问)
	 * @param userParams
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@ApiOperation(value = "我的动态（评论，回答，提问列表）", notes = "POST")
	@RequestMapping(value = "/myDynamic", method = RequestMethod.POST)
	public AppResp myDynamic_(@RequestBody UserFollowParams userFollowParams)
			throws IllegalArgumentException, IllegalAccessException {
		if (userFollowParams == null || StringUtils.isBlank(userFollowParams.getUserId())
				|| userFollowParams.getOptType() == null) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (userFollowParams.getPindex() == null) {
			userFollowParams.setPindex(0);
		}
		if (userFollowParams.getPindex() != 0 && userFollowParams.getPindex() != null) {
			userFollowParams.setPindex(userFollowParams.getPindex() - 1);
		}
		// 如果toUserId不为空，userId和toUserId互换，userId是对方，toUserId是当前登录用户
		if (StringUtils.isNotBlank(userFollowParams.getToUserId())) {
			String userId = "";
			userId = userFollowParams.getUserId();
			userFollowParams.setUserId(userFollowParams.getToUserId());
			userFollowParams.setToUserId(userId);
		}
		// 分页计算
		int pageCount = (userFollowParams.getPcount() == null || userFollowParams.getPcount() == 0) ? 30
				: userFollowParams.getPcount();

		int pageBegin = userFollowParams.getPindex() * pageCount;

		switch (userFollowParams.getOptType()) {
			// 评论
			case Const.OBJ_OPT_COMMENT:
				List<QACommentResult> qaCommentResults = qaCommentService.findByUserId(userFollowParams.getUserId(),
						pageBegin, pageCount);
				List<String> qacIds = null;
				if (StringUtils.isNotBlank(userFollowParams.getToUserId())) {
					qacIds = qACAgreeRelRepo.findByUser(userFollowParams.getToUserId(), Const.OBJ_OPT_GREE);
				} else {
					qacIds = qACAgreeRelRepo.findByUser(userFollowParams.getUserId(), Const.OBJ_OPT_GREE);
				}
				for (QACommentResult qaCommentResult : qaCommentResults) {
					qaCommentResult.setIsAgree(0);
					if (qacIds.contains(qaCommentResult.getId())) {
						qaCommentResult.setIsAgree(1);
					}
					// 点赞数 TODO 后面从redis中取
					Integer agreeNumber = qACommentRepo.commentCount(qaCommentResult.getQuestionAnswerId());
					qaCommentResult.setAgreeNumber(agreeNumber);
					// 截取资源访问路径
					if (null != qaCommentResult.getPhotoUrl()) {
						qaCommentResult.setPhotoUrl(
								appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + qaCommentResult.getPhotoUrl());
					}
					// 等级
					if (null != qaCommentResult.getExperience()) {
						qaCommentResult.setGrade(appUserService.transformExpToGrade(qaCommentResult.getExperience()));
					}
				}
				return new AppResp(qaCommentResults, CodeDef.SUCCESS);
			// 回答
			case Const.OBJ_OPT_ANSWER:
				// 回答
				List<QAnswerResult> qAnswerResults = questionAnswerRepo
						.findQAnswerByUserId(userFollowParams.getUserId(), new PageRequest(pageBegin, pageCount));
				List<String> qaIds = null;
				if (StringUtils.isNotBlank(userFollowParams.getToUserId())) {
					qaIds = qAnswerRelRepo.findByUser(userFollowParams.getToUserId(), Const.OBJ_OPT_GREE);
				} else {
					qaIds = qAnswerRelRepo.findByUser(userFollowParams.getUserId(), Const.OBJ_OPT_GREE);
				}
				for (QAnswerResult qAnswerResult : qAnswerResults) {
					qAnswerResult.setIsAgree(0);
					if (qaIds.contains(qAnswerResult.getQaId())) {
						qAnswerResult.setIsAgree(1);
					}
					// TODO 获取评论数，点赞数后面从redis中获取
					qAnswerResult.setQaCommentNum(qACommentRepo.commentCount(qAnswerResult.getQaId()));
					qAnswerResult
							.setQaCommentNum(qAnswerRelRepo.agreeCount(qAnswerResult.getQaId(), Const.OBJ_OPT_GREE));
				}

				return new AppResp(qAnswerResults, CodeDef.SUCCESS);
			// 提问
			case Const.OBJ_OPT_INQUIRY:
				List<QuestResult> questResults = topicQuestionRepo.findByUserId(userFollowParams.getUserId(),
						new PageRequest(pageBegin, pageCount));

				return new AppResp(questResults, CodeDef.SUCCESS);
			default:
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
	}

	/**
	 * userId;objType(1-问题回答，2-行业资讯)toUserId
	 * @param userFollowParams
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@ApiOperation(value = "我的收藏", notes = "POST")
	@RequestMapping(value = "/myCollection", method = RequestMethod.POST)
	public AppResp myCollection_(@RequestBody UserFollowParams userFollowParams)
			throws IllegalArgumentException, IllegalAccessException {
		if (userFollowParams == null || StringUtils.isBlank(userFollowParams.getUserId())
				|| userFollowParams.getObjType() == null) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (userFollowParams.getPindex() == null) {
			userFollowParams.setPindex(0);
		}
		if (userFollowParams.getPindex() != 0 && userFollowParams.getPindex() != null) {
			userFollowParams.setPindex(userFollowParams.getPindex() - 1);
		}
		// 如果toUserId不为空，userId和toUserId互换，userId是对方，toUserId是当前登录用户
		if (StringUtils.isNotBlank(userFollowParams.getToUserId())) {
			String userId = "";
			userId = userFollowParams.getUserId();
			userFollowParams.setUserId(userFollowParams.getToUserId());
			userFollowParams.setToUserId(userId);
		}
		// 分页计算
		int pageCount = (userFollowParams.getPcount() == null || userFollowParams.getPcount() == 0) ? 30
				: userFollowParams.getPcount();

		int pageBegin = userFollowParams.getPindex() * pageCount;

		switch (userFollowParams.getObjType()) {
			// 问题回答
			case Const.OBJ_TYPE_ANSWER:
				List<QAnswerResult> qAnswerResults = qAnswerService.findCollectQAnswer(userFollowParams.getUserId(),
						pageBegin, pageCount);
				List<String> list = null;
				if (StringUtils.isNotBlank(userFollowParams.getToUserId())) {
					list = qAnswerRelRepo.findByUser(userFollowParams.getToUserId(), Const.OBJ_OPT_GREE);
				}
				for (QAnswerResult qAnswerResult : qAnswerResults) {
					if (null != qAnswerResult.getPhotoUrl()) {
						qAnswerResult.setPhotoUrl(
								appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + qAnswerResult.getPhotoUrl());
					}
					// 等级
					if (null != qAnswerResult.getExperience()) {
						qAnswerResult.setGrade(appUserService.transformExpToGrade(qAnswerResult.getExperience()));
					}
					QuestionAnswer questionAnswer = qAnswerService.getAContentByOne(qAnswerResult.getTqId());
					qAnswerResult.setContent(questionAnswer.getContent());
					qAnswerResult.setQaAgreeNum(questionAnswer.getAgreeNum());
					qAnswerResult.setQaCommentNum(questionAnswer.getCommentNum());
					if (StringUtils.isNotBlank(userFollowParams.getToUserId())) {
						qAnswerResult.setIsAgree(0);// 0-未点赞
						if (list.contains(qAnswerResult.getQaId())) {
							qAnswerResult.setIsAgree(1);// 1-已点赞
						}
					}
				}
				Collections.sort(qAnswerResults, new Comparator<QAnswerResult>() {
					// TODO 根据评论数和点赞数排序，算法待确认
					@Override
					public int compare(QAnswerResult o1, QAnswerResult o2) {
						if (o1.getQaCommentNum() > o2.getQaCommentNum()) {
							return -1;
						} else if (o1.getQaAgreeNum() > o2.getQaAgreeNum()) {
							return -1;
						} else {
							return 0;
						}
					}

				});
				return new AppResp(qAnswerResults, CodeDef.SUCCESS);
			// 行业资讯
			case Const.OBJ_TYPE_NEWS:
				List<News> newsInfoList = newsService.findCollectNews(userFollowParams.getUserId(), pageBegin,
						pageCount);
				for (News newsInfo : newsInfoList) {
					// TODO 从数据库查询评论数插到资讯表,后面从redis里面找
					int count = 0;
					count = newsCommentRepo.commentCount(newsInfo.getId());
					newsInfo.setCommentNumber(count);
					// 截取资源访问路径
					if (null != newsInfo.getPhotoUrl()) {
						newsInfo.setPhotoUrl(
								appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + newsInfo.getPhotoUrl());
					}
					// 等级
					if (null != newsInfo.getExperience()) {
						newsInfo.setGrade(appUserService.transformExpToGrade(newsInfo.getExperience()));
					}
				}
				return new AppResp(newsInfoList, CodeDef.SUCCESS);
			default:
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
	}
}
