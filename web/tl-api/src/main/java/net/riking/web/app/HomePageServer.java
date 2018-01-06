package net.riking.web.app;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.AppUserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUserResult;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.model.TQuestionResult;
import net.riking.entity.model.TopicResult;
import net.riking.entity.model.UserFollowRel;
import net.riking.entity.params.HomeParams;
import net.riking.service.AppUserService;
import net.riking.service.QAnswerService;
import net.riking.service.TQuestionService;
import net.riking.service.TopicService;
import net.riking.util.DateUtils;
import net.riking.util.MQProduceUtil;
import net.sf.json.JSONObject;

/**
 * 首页接口
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/homePage")
public class HomePageServer {
	protected final transient Logger logger = LogManager.getLogger(getClass());

	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	TQuestionRelRepo tQuestionRelRepo;

	@Autowired
	TopicRelRepo topicRelRepo;

	@Autowired
	QAnswerRelRepo qAnswerRelRepo;

	@Autowired
	HttpServletRequest request;

	@Autowired
	TopicQuestionServer topicQuestionServer;

	@Autowired
	TQuestionService tQuestionService;

	@Autowired
	QAnswerService qAnswerService;

	@Autowired
	TopicService topicService;

	@Autowired
	AppUserService appUserService;

	@Autowired
	AppUserFollowRelRepo userFollowRelRepo;

	/**
	 *
	 * @param params[userId,direct,reqTimeStamp]
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "显示首页数据", notes = "POST")
	@RequestMapping(value = "/findHomePageData", method = RequestMethod.POST)
	public AppResp findHomePageData(@RequestBody HomeParams homeParams) throws ParseException {
		Date date = null;
		if (homeParams == null) {
			homeParams = new HomeParams();
			homeParams.setReqTimeStamp(new Date());
			homeParams.setDirect(Const.DIRECT_UP);
		}
		if (homeParams.getReqTimeStamp() == null || "".equals(homeParams.getReqTimeStamp())) {
			date = DateUtils.StringFormatMS(DateUtils.DateFormatMS(new Date(), "yyyyMMddHHmmssSSS"),
					"yyyyMMddHHmmssSSS");
			homeParams.setReqTimeStamp(date);
			homeParams.setDirect(Const.DIRECT_UP);
		}
		if (homeParams.getDirect() == null) {
			homeParams.setDirect(Const.DIRECT_UP);
		}
		String tquestIds = "";
		// 查询用户屏蔽的问题
		{
			StringBuilder stringBuilder = new StringBuilder();
			if (homeParams.getUserId() != null) {
				List<String> tqIds = tQuestionRelRepo.findByUser(homeParams.getUserId(), 3);// 3-屏蔽
				for (int i = 0; i < tqIds.size(); i++) {
					stringBuilder.append(tqIds.get(i) + ",");
					if (i == tqIds.size() - 1) {
						stringBuilder.append(tqIds.get(i));
					}
				}
			}
			tquestIds = stringBuilder.toString();
		}
		// 分页数据
		List<TQuestionResult> tQuestionList = new ArrayList<TQuestionResult>();
		switch (homeParams.getDirect()) {
			// 如果操作方向是向上：根据时间戳是上一页最后一条数据时间返回下一页数据
			case Const.DIRECT_UP:
				// 查询查出前30条数据
				if(date.compareTo(homeParams.getReqTimeStamp())==0){//若是系统时间， 表示用户刚刚登陆，加载上一次登陆数据
					tQuestionList = tQuestionService.findTopicHomeUp("", homeParams.getReqTimeStamp(), tquestIds, 0,
							30);
				}else{
					tQuestionList = tQuestionService.findTopicHomeUp(homeParams.getUserId(), homeParams.getReqTimeStamp(),
							tquestIds, 0, 30);
				}
				// 如果查不到数据返回未登录时候数据
//				if (tQuestionList.size() == 0) {
//					tQuestionList = tQuestionService.findTopicHomeUp("", homeParams.getReqTimeStamp(), tquestIds, 0,
//							30);
//				}
				break;
			// 如果操作方向是向上：根据时间戳是第一页第一条数据时间刷新第一页的数据）
			case Const.DIRECT_DOWN:
				// 查询查出前30条数据
				tQuestionList = tQuestionService.findTopicHomeDown(homeParams.getUserId(), homeParams.getReqTimeStamp(),
						tquestIds, 0, 30);
				// 如果查不到数据返回未登录时候数据
				/*if (tQuestionList.size() == 0) {
					tQuestionList = tQuestionService.findTopicHomeUp("", homeParams.getReqTimeStamp(), tquestIds, 0,
							30);
				}*/
				break;
			default:
				logger.error("请求方向参数异常：direct:" + homeParams.getDirect());
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}

		for (TQuestionResult tQuestionResult : tQuestionList) {
			// TODO 如果等于1拼接话题图片资源路径，否则拼接用户图片资源路径
			if (null != tQuestionResult.getFromImgUrl()) {
				if (tQuestionResult.getPushType() == 1) {
					// TODO
					tQuestionResult.setFromImgUrl(tQuestionResult.getFromImgUrl());
				} else {
					tQuestionResult.setFromImgUrl(
							appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + tQuestionResult.getFromImgUrl());
				}
			}
			// 等级
			if (tQuestionResult.getExperience() != null) {
				tQuestionResult.setGrade(appUserService.transformExpToGrade(tQuestionResult.getExperience()));
			}
			/* 1-根据用户关注的话题推送问题 */
			if (tQuestionResult.getPushType() == 1) {
				QuestionAnswer questionAnswer = qAnswerService.getAContentByOne(tQuestionResult.getTqId());
				tQuestionResult.setQaContent(questionAnswer.getContent());
				tQuestionResult.setQaId(questionAnswer.getId());
				tQuestionResult.setQaAgreeNum(questionAnswer.getAgreeNum());
				tQuestionResult.setQaCommentNum(questionAnswer.getCommentNum());
				tQuestionResult.setCoverUrl(questionAnswer.getCoverUrl());
			}
			// 加点赞，关注，收藏状态
			appendStatusByPushType(tQuestionResult, homeParams);
		}
		// 把查出来的数据按倒序重新排列
		Collections.sort(tQuestionList, new Comparator<TQuestionResult>() {
			@Override
			public int compare(TQuestionResult o1, TQuestionResult o2) {
				if (o1.getCreatedTime().after(o2.getCreatedTime())) {
					return -1;
				} else {
					return 1;
				}
			}
		});
		
		if(tQuestionList.isEmpty()){
			return new AppResp(tQuestionList, CodeDef.SUCCESS);
		}
		// 可能感兴趣的话题
		// 除去用户已关注的话题
		List<String> topIds = topicRelRepo.findByUser(homeParams.getUserId(), Const.OBJ_OPT_FOLLOW);
		String topicIds = "";
		{
			StringBuilder stringBuilder = new StringBuilder();
			if (homeParams.getUserId() != null) {
				for (int i = 0; i < topIds.size(); i++) {
					stringBuilder.append(topIds.get(i) + ",");
					if (i == topIds.size() - 1) {
						stringBuilder.append(topIds.get(i));
					}
				}
			}
			topicIds = stringBuilder.toString();
		}
		List<TopicResult> topics = topicService.findTopicOfInterest(homeParams.getUserId(), topicIds, 0, 8);// 取出0-8条数据
		for (TopicResult topic : topics) {
			topic.setIsFollow(0);// 0-未关注
			for (String topId : topIds) {
				if (topId.equals(topic.getId())) {
					topic.setIsFollow(1);// 1-已关注
				}
			}
		}
		if (topics.size() != 0)

		{
			TQuestionResult tQuestionResultTopic = new TQuestionResult();
			tQuestionResultTopic.setPushType(6);
			if (tQuestionList.size() != 0) {
				tQuestionResultTopic.setCreatedTime(tQuestionList.get(tQuestionList.size() - 1).getCreatedTime());
			}
			tQuestionResultTopic.setTopicResults(topics);
			tQuestionList.add(tQuestionResultTopic);
		}

		// 可能感兴趣的人
		// 除去用户已关注的话题
		List<UserFollowRel> userFollowRels = userFollowRelRepo.findByUser(homeParams.getUserId());
		String userIds = "";
		{
			StringBuilder stringBuilder = new StringBuilder();
			if (homeParams.getUserId() != null) {
				for (int i = 0; i < userFollowRels.size(); i++) {
					stringBuilder.append(userFollowRels.get(i).getToUserId() + ",");
					if (i == userFollowRels.size() - 1) {
						stringBuilder.append(userFollowRels.get(i).getToUserId());
					}
				}
			}
			userIds = stringBuilder.toString();
		}
		List<AppUserResult> userResults = appUserService.findUserMightKnow(homeParams.getUserId(), userIds, 0, 8);// 取出0-8条数据
		for (AppUserResult userResult : userResults) {
			userResult.setIsFollow(0);// 0-未关注
			// 等级
			if (userResult.getExperience() != null) {
				userResult.setGrade(appUserService.transformExpToGrade(userResult.getExperience()));
			}
			// 截取资源访问路径
			if (null != userResult.getPhotoUrl()) {
				userResult.setPhotoUrl(appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + userResult.getPhotoUrl());
			}
			for (UserFollowRel userFollowRel : userFollowRels) {
				if (userFollowRel.getFollowStatus() == 1 && userFollowRel.getToUserId().equals(userResult.getId())) {
					userResult.setIsFollow(1);// 已关注
				} else if (userFollowRel.getFollowStatus() == 2
						&& userFollowRel.getToUserId().equals(userResult.getId())) {
					userResult.setIsFollow(2);// 互相关注
				}
			}
		}
		if (userResults.size() != 0)

		{
			TQuestionResult tQuestionResultUser = new TQuestionResult();
			tQuestionResultUser.setPushType(7);
			if (tQuestionList.size() != 0) {
				tQuestionResultUser.setCreatedTime(tQuestionList.get(tQuestionList.size() - 1).getCreatedTime());
			}
			tQuestionResultUser.setAppUserResults(userResults);
			tQuestionList.add(tQuestionResultUser);
		}
		return new AppResp(tQuestionList, CodeDef.SUCCESS);

	}

	/**
	 * 屏蔽问题
	 * @param params[userId,objType,objId,enabled]
	 * @return
	 */
	@ApiOperation(value = "屏蔽问题", notes = "POST")
	@RequestMapping(value = "/shield", method = RequestMethod.POST)
	public AppResp shield_(@RequestBody HomeParams homeParams) {

		homeParams.setMqOptType(Const.MQ_OPT_SHIELD_QUEST);
		JSONObject jsonArray = JSONObject.fromObject(homeParams);
		MQProduceUtil.sendTextMessage(Const.SYS_OPT_QUEUE, jsonArray.toString());
		return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
	}

	// 加点赞，关注，收藏状态
	private void appendStatusByPushType(TQuestionResult tQuestionResult, HomeParams homeParams) {
		if (tQuestionResult.getPushType() == 1 || tQuestionResult.getPushType() == 2
				|| tQuestionResult.getPushType() == 4) {
			tQuestionResult.setStatus(0);// 未点赞
			if (homeParams.getUserId() != null) {
				List<String> qaIds = qAnswerRelRepo.findByUser(homeParams.getUserId(), Const.OBJ_OPT_GREE);
				for (String qaId : qaIds) {
					if (qaId.equals(tQuestionResult.getQaId())) {
						tQuestionResult.setStatus(1);// 已点赞
					}

				}
			}
		}
		if (tQuestionResult.getPushType() == 3) {
			List<String> tqIds = tQuestionRelRepo.findByUser(homeParams.getUserId(), Const.OBJ_OPT_FOLLOW);
			tQuestionResult.setStatus(0);// 未关注
			if (homeParams.getUserId() != null) {
				for (String tqId : tqIds) {
					if (tqId.equals(tQuestionResult.getTqId())) {
						tQuestionResult.setStatus(1);// 已关注
					}

				}
			}
		}
		if (tQuestionResult.getPushType() == 7) {
			tQuestionResult.setStatus(0);// 未收藏
			if (homeParams.getUserId() != null) {
				List<String> qaIds = qAnswerRelRepo.findByUser(homeParams.getUserId(), Const.OBJ_OPT_COLLECT);
				for (String qaId : qaIds) {
					if (qaId.equals(tQuestionResult.getQaId())) {
						tQuestionResult.setStatus(1);// 已收藏
					}

				}
			}
		}
	}
}
