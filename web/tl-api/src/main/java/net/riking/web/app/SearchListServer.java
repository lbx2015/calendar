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
import net.riking.config.Const;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.HotSearchRepo;
import net.riking.dao.repo.NewsRepo;
import net.riking.dao.repo.QAInviteRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.ReportSubscribeRelRepo;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.TopicRepo;
import net.riking.dao.repo.AppUserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUserResult;
import net.riking.entity.model.HotSearch;
import net.riking.entity.model.NewsResult;
import net.riking.entity.model.QuestResult;
import net.riking.entity.model.ReportResult;
import net.riking.entity.model.TopicResult;
import net.riking.entity.params.SearchParams;
import net.riking.service.AppUserService;
import net.riking.service.ReportService;

/**
 * 搜索接口
 * @author jc.tan
 * @version crateTime：2017年11月23日 上午10:46:40
 * @used 搜索列表（报表/话题/人脉/资讯/问题）结果
 */
@RestController
@RequestMapping(value = "/searchList")
public class SearchListServer {
	protected final transient Logger logger = LogManager.getLogger(getClass());

	@Autowired
	ReportService reportService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	AppUserService appUserService;

	@Autowired
	ReportSubscribeRelRepo reportSubscribeRelRepo;

	@Autowired
	TopicRepo topicRepo;

	@Autowired
	TopicRelRepo topicRelRepo;

	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	AppUserFollowRelRepo userFollowRelRepo;

	@Autowired
	QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	NewsRepo newsRepo;

	@Autowired
	TopicQuestionRepo topicQuestionRepo;

	@Autowired
	QAInviteRepo qAInviteRepo;

	@Autowired
	HotSearchRepo hotSearchRepo;

	@Autowired
	TQuestionRelRepo tQuestionRelRepo;

	/**
	 * 显示热门资讯（6条）
	 * @param params[userId]
	 * @return
	 */
	@ApiOperation(value = "显示热门搜索列表", notes = "POST")
	@RequestMapping(value = "/findHotSearchList", method = RequestMethod.POST)
	public AppResp findHotSearchList() {
		// 取前六条数据
		List<HotSearch> hotSearches = hotSearchRepo.findHotSearch(new PageRequest(0, 6));
		return new AppResp(hotSearches, CodeDef.SUCCESS);
	}

	/**
	 * 搜索列表[userId;showOptType(显示操作类型：0-不显示状态；1-显示关注/收藏、订阅状态；2-显示邀请状态)objType（1-报表；2-话题；3-人脉；4-资讯；5-
	 * 问题）; keyword:关键字；]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "搜索列表（报表/话题/人脉/资讯/问题）", notes = "POST")
	@RequestMapping(value = "/findSearchList", method = RequestMethod.POST)
	public AppResp findSearchList(@RequestBody SearchParams searchParams) {

		if (searchParams.getShowOptType() == null) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (StringUtils.isBlank(searchParams.getKeyWord())) {
			return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
		}
		if (StringUtils.isBlank(searchParams.getUserId())) {
			searchParams.setUserId("");
		}

		switch (searchParams.getObjType()) {
			// 报表
			case Const.OPJ_TYPE_REPORT:
				List<ReportResult> reportResults = findReportByKeyWord(searchParams);
				return new AppResp(reportResults, CodeDef.SUCCESS);
			// 话题
			case Const.OPJ_TYPE_TOPIC:
				List<TopicResult> topicResults = findTopicByKeyWord(searchParams);
				return new AppResp(topicResults, CodeDef.SUCCESS);
			// 人脉
			case Const.OPJ_TYPE_CONTACTS:
				List<AppUserResult> appUserResults = findUserByKeyWord(searchParams);
				return new AppResp(appUserResults, CodeDef.SUCCESS);
			// 资讯
			case Const.OPJ_TYPE_NEWS:
				List<NewsResult> newsResults = findNewsByKeyWord(searchParams);
				return new AppResp(newsResults, CodeDef.SUCCESS);
			// 问题
			case Const.OPJ_TYPE_QUEST:
				List<QuestResult> questResult = findQuestByKeyWord(searchParams);
				return new AppResp(questResult, CodeDef.SUCCESS);
			default:
				logger.error("参数异常：objType:" + searchParams.getObjType());
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}

	}

	/**
	 * 根据关键字搜索报表
	 * @param userId
	 * @param keyWord
	 * @return
	 */
	private List<ReportResult> findReportByKeyWord(SearchParams searchParams) {

		List<ReportResult> reportResultList = reportService.getReportResultByParam(searchParams.getKeyWord(),
				searchParams.getUserId());

		return reportResultList;
	}

	/**
	 * 根据关键字搜索话题
	 * @param userId
	 * @param keyword
	 * @return
	 */
	private List<TopicResult> findTopicByKeyWord(SearchParams searchParams) {
		List<TopicResult> topicResults = topicRepo.getTopicByParam(searchParams.getKeyWord(), searchParams.getUserId());

		for (int i = 0; i < topicResults.size(); i++) {
			TopicResult topicResult = topicResults.get(i);
			// TODO 话题的关注数 后面从redis里面取
			Integer followNum = topicRelRepo.followCount(topicResult.getId(), 0);
			topicResult.setFollowNum(followNum);
			topicResult.setTopicUrl(appUserService.getPhotoUrlPath("/topic")+topicResult.getTopicUrl());
			// 不显示状态
			if (Const.OPT_TYPE_BLANK_STATUS == searchParams.getShowOptType()) {
				topicResult.setIsFollow(null);
			}
			topicResults.remove(i);
			topicResults.add(i, topicResult);
		}
		return topicResults;
	}

	/**
	 * 根据关键字搜索人脉
	 * @param userId
	 * @param keyWord
	 * @return
	 */
	private List<AppUserResult> findUserByKeyWord(SearchParams searchParams) {
		List<AppUserResult> appUserResults = appUserRepo.getUserByParam(searchParams.getKeyWord(),
				searchParams.getUserId());

		for (int i = 0; i < appUserResults.size(); i++) {
			AppUserResult appUserResult = appUserResults.get(i);
			if (null != appUserResult.getPhotoUrl()) {
				appUserResult
						.setPhotoUrl(appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + appUserResult.getPhotoUrl());
			}
			// 等级
			if (null != appUserResult.getExperience()) {
				appUserResult.setGrade(appUserService.transformExpToGrade(appUserResult.getExperience()));
			}
			// TODO 用户的回答数 后面从redis里面取
			Integer answerNum = questionAnswerRepo.answerCountByUserId(searchParams.getUserId());

			appUserResult.setAnswerNum(answerNum);// 回答数
			// 显示邀请状态
			if (Const.OPT_TYPE_INVITE_STATUS == searchParams.getShowOptType()) {
				appUserResult.setIsFollow(null);
				List<String> toUserIds = qAInviteRepo.findToIdByUIdAndQId(searchParams.getUserId(),
						searchParams.getTqId());
				appUserResult.setIsInvited(0);// 1-未邀请
				for (String toUserId : toUserIds) {
					if (appUserResult.getId().equals(toUserId)) {
						appUserResult.setIsInvited(1);// 1-已邀请
					}
				}
			}

			// 不显示状态
			if (Const.OPT_TYPE_BLANK_STATUS == searchParams.getShowOptType()) {
				appUserResult.setIsFollow(null);
				appUserResult.setIsInvited(null);
			}
			appUserResults.remove(i);
			appUserResults.add(i, appUserResult);
		}
		return appUserResults;
	}

	/**
	 * 根据关键字搜索资讯
	 * @param userId
	 * @param keyWord
	 * @return
	 */
	private List<NewsResult> findNewsByKeyWord(SearchParams searchParams) {
		List<NewsResult> newsResults = newsRepo.getNewsByParam(searchParams.getKeyWord());

		return newsResults;
	}

	/**
	 * 根据关键字搜索问题
	 * @param userId
	 * @param keyWord
	 * @return
	 */
	private List<QuestResult> findQuestByKeyWord(SearchParams searchParams) {
		List<QuestResult> questResults = topicQuestionRepo.getQuestByParam(searchParams.getKeyWord());
		for (QuestResult questResult : questResults) {
			Integer tqFollowNum = tQuestionRelRepo.followCount(questResult.getId(), Const.OBJ_OPT_GREE);
			Integer qanswerNum = questionAnswerRepo.answerCount(questResult.getId());
			// // TODO 后面从redis中取
			questResult.setTqFollowNum(tqFollowNum);
			questResult.setQanswerNum(qanswerNum);
		}
		return questResults;
	}
}
