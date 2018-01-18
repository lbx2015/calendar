package net.riking.web.app;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import net.riking.dao.repo.AppUserFollowRelRepo;
import net.riking.dao.repo.QACAgreeRelRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QAnswerRelRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.TopicRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.QAExcellentResp;
import net.riking.entity.model.QAnswerResult;
import net.riking.entity.model.QuestResult;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.model.Topic;
import net.riking.entity.model.UserFollowRel;
import net.riking.entity.params.TopicParams;
import net.riking.service.AppUserService;
import net.riking.service.QAnswerService;
import net.riking.service.TQuestionService;
import net.riking.util.FileUtils;

/**
 * 话题接口
 * @author jc.tan
 * @version crateTime：2017年11月23日 上午10:46:40
 * @used 话题接口
 */
@RestController
@RequestMapping(value = "/topic")
public class TopServer {
	protected final transient Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TopicQuestionRepo topicQuestionRepo;

	@Autowired
	HttpServletRequest request;

	@Autowired
	AppUserService appUserService;

	@Autowired
	TopicRelRepo topicRelRepo;

	@Autowired
	QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	TQuestionRelRepo tQuestionRelRepo;

	@Autowired
	QACommentRepo qACommentRepo;

	@Autowired
	QACAgreeRelRepo qACAgreeRelRepo;

	@Autowired
	AppUserFollowRelRepo userFollowRelRepo;

	@Autowired
	TopicRepo topicRepo;

	@Autowired
	TQuestionService tQuestionService;

	@Autowired
	QAnswerService qAnswerService;

	@Autowired
	QAnswerRelRepo qAnswerRelRepo;
	
	/**
	 * 话题的详情[topicId,userId]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "话题的详情", notes = "POST")
	@RequestMapping(value = "/getTopic", method = RequestMethod.POST)
	public AppResp getTopic_(@RequestBody TopicParams topicParams) {
		Topic topic = topicRepo.getById(topicParams.getTopicId());

		// TODO 话题的关注数 后面从redis里面取
		Integer followNum = topicRelRepo.followCount(topic.getId(), 0);// 0-关注
		topic.setFollowNum(followNum);
		topic.setIsFollow(0);// 0-未关注
		List<String> topicIds = topicRelRepo.findByUser(topicParams.getUserId(), 0);// 0-关注
		for (String topicId : topicIds) {
			if (topic.getId().equals(topicId)) {
				topic.setIsFollow(1);// 1-已关注
			}
		}
		if (null != topic.getPhotoUrl()) {
//			topic.setPhotoUrl(appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + topic.getPhotoUrl());
			topic.setPhotoUrl(FileUtils.getPhotoUrl(Const.TL_TOPIC_PHOTO_PATH, this.getClass()) + topic.getPhotoUrl());
		}
		// 等级
		if (null != topic.getExperience()) {
			topic.setGrade(appUserService.transformExpToGrade(topic.getExperience()));
		}
		return new AppResp(topic, CodeDef.SUCCESS);
	}

	/**
	 * TODO 精华问题回答列表[userId,topicId,optType（1-精华；2-问题；3-优秀回答者）,pindex(当前页数)，pcount(每页条数)]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "精华问题回答列表", notes = "POST")
	@RequestMapping(value = "/essenceQAList", method = RequestMethod.POST)
	public AppResp essenceQAList_(@RequestBody TopicParams topicParams) {
		if (topicParams.getPindex() == null) {
			topicParams.setPindex(0);
		}
		if (topicParams.getPindex() != 0 && topicParams.getPindex() != null) {
			topicParams.setPindex(topicParams.getPindex() - 1);
		}
		if (topicParams.getOptType() == null) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		int pageCount = (topicParams.getPcount() == null || topicParams.getPcount() == 0) ? 30
				: topicParams.getPcount();

		int pageBegin = topicParams.getPindex() * pageCount;

		switch (topicParams.getOptType()) {
			// 精华
			case Const.TOP_OBJ_OPT_ESSENCE:
				List<QAnswerResult> tQuestionResults = tQuestionService.findEssenceByTid(topicParams.getTopicId(),
						topicParams.getUserId(), pageBegin, pageCount);
				for (QAnswerResult tQuestionResult : tQuestionResults) {
					if (null != tQuestionResult.getPhotoUrl()) {
//						tQuestionResult.setPhotoUrl(
//								appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + tQuestionResult.getPhotoUrl());
						tQuestionResult.setPhotoUrl(
								FileUtils.getPhotoUrl(Const.TL_PHOTO_PATH, this.getClass()) + tQuestionResult.getPhotoUrl());
					}
					// 等级
					if (null != tQuestionResult.getExperience()) {
						tQuestionResult.setGrade(appUserService.transformExpToGrade(tQuestionResult.getExperience()));
					}
					QuestionAnswer questionAnswer = qAnswerService.getAContentByOne(tQuestionResult.getTqId());
					tQuestionResult.setContent(questionAnswer.getContent());
					tQuestionResult.setQaAgreeNum(questionAnswer.getAgreeNum());
					tQuestionResult.setQaCommentNum(questionAnswer.getCommentNum());
				}
				Collections.sort(tQuestionResults, new Comparator<QAnswerResult>() {
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
				return new AppResp(tQuestionResults, CodeDef.SUCCESS);
			// 问题
			case Const.TOP_OBJ_OPT_QUEST:
				List<QuestResult> questResults = topicQuestionRepo.findByTid(topicParams.getTopicId(),
						new PageRequest(pageBegin, pageCount));
				for (QuestResult questResult : questResults) {
					if (null != questResult.getPhotoUrl()) {
//						questResult.setPhotoUrl(
//								appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + questResult.getPhotoUrl());
						questResult.setPhotoUrl(
								FileUtils.getPhotoUrl(Const.TL_PHOTO_PATH, this.getClass()) + questResult.getPhotoUrl());
					}
					// 等级
					if (null != questResult.getExperience()) {
						questResult.setGrade(appUserService.transformExpToGrade(questResult.getExperience()));
					}
					questResult.setIsFollow(0);// 0-未关注
					if (null != topicParams.getUserId()) {
						List<String> tqIds = tQuestionRelRepo.findByUser(topicParams.getUserId(), 0);// 0-关注
						for (String tqId : tqIds) {
							if (tqId.equals(questResult.getId())) {
								questResult.setIsFollow(1);// 1-已关注
							}
						}
					}
				}
				return new AppResp(questResults, CodeDef.SUCCESS);
			// 优秀回答者
			case Const.TOP_OBJ_OPT_EXRESP:
				List<QAExcellentResp> excellentResps = tQuestionService.findExcellentResp(topicParams.getTopicId(),
						pageBegin, pageCount);
				for (QAExcellentResp qaExcellentResp : excellentResps) {
					if (null != qaExcellentResp.getPhotoUrl()) {
//						qaExcellentResp.setPhotoUrl(
//								appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + qaExcellentResp.getPhotoUrl());
						qaExcellentResp.setPhotoUrl(
								FileUtils.getPhotoUrl(Const.TL_PHOTO_PATH, this.getClass()) + qaExcellentResp.getPhotoUrl());
					}
					// 等级
					if (null != qaExcellentResp.getExperience()) {
						qaExcellentResp.setGrade(appUserService.transformExpToGrade(qaExcellentResp.getExperience()));
					}
					qaExcellentResp.setIsFollow(0);// 未关注
					if (topicParams.getUserId() != null) {
						List<UserFollowRel> userFollowRels = userFollowRelRepo.findByUser(topicParams.getUserId());
						for (UserFollowRel userFollowRel : userFollowRels) {
							if (userFollowRel.getFollowStatus() == 1
									&& userFollowRel.getToUserId().equals(qaExcellentResp.getUserId())) {
								qaExcellentResp.setIsFollow(1);// 已关注
							} else if (userFollowRel.getFollowStatus() == 2
									&& userFollowRel.getToUserId().equals(qaExcellentResp.getUserId())) {
								qaExcellentResp.setIsFollow(2);// 互相关注
							}
						}
					}
				}
				return new AppResp(excellentResps, CodeDef.SUCCESS);
			default:
				logger.error("参数异常：optType:" + topicParams.getOptType());
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}

	}
	
	/**
	 * 话题下优秀回答者   回答数[topicId,userId]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "话题下优秀回答者   回答的问题列表", notes = "POST")
	@RequestMapping(value = "/getQAnswerSize", method = RequestMethod.POST)
	public AppResp getQAnswerSize(@RequestBody TopicParams topicParams) {
		Long count= qAnswerService.findQACountByTopicIdAndUserId(topicParams.getTopicId(), topicParams.getUserId());
		return new AppResp(count, CodeDef.SUCCESS);
	}
	
	/**
	 * 话题下优秀回答者   回答的问题列表[topicId,userId]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "话题下优秀回答者   回答的问题列表", notes = "POST")
	@RequestMapping(value = "/getQAnswerResults", method = RequestMethod.POST)
	public AppResp getQAnswerResults(@RequestBody TopicParams topicParams) {
		List<QAnswerResult> list = qAnswerService.findQAByTopicIdAndUserId(topicParams.getTopicId(), topicParams.getUserId(), topicParams.getPindex(), topicParams.getPcount());
		return new AppResp(list, CodeDef.SUCCESS);
	}
	

}
