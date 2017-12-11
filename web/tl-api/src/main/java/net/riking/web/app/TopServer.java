package net.riking.web.app;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import net.riking.dao.repo.QACAgreeRelRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.TopicRepo;
import net.riking.dao.repo.UserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.QAExcellentResp;
import net.riking.entity.model.QuestResult;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.model.TQuestionResult;
import net.riking.entity.model.Topic;
import net.riking.entity.params.TopicParams;
import net.riking.service.QAnswerService;
import net.riking.service.TQuestionService;

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
	UserFollowRelRepo userFollowRelRepo;

	@Autowired
	TopicRepo topicRepo;

	@Autowired
	TQuestionService tQuestionService;

	@Autowired
	QAnswerService qAnswerService;

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
		return new AppResp(topic, CodeDef.SUCCESS);
	}

	/**
	 * TODO 精华问题回答列表[userId,topicId,optType（1-精华；2-问题；3-优秀回答者）,pindex(当前页数)]
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
		switch (topicParams.getOptType()) {
			// 精华
			case Const.TOP_OBJ_OPT_ESSENCE:
				List<TQuestionResult> tQuestionResults = tQuestionService.findEssenceByTid(topicParams.getTopicId(),
						topicParams.getPindex(), topicParams.getPindex() + 30);// 显示30条数据
				for (TQuestionResult tQuestionResult : tQuestionResults) {
					QuestionAnswer questionAnswer = qAnswerService.getAContentByOne(tQuestionResult.getTqId());
					tQuestionResult.setQaContent(questionAnswer.getContent());
					tQuestionResult.setQaAgreeNum(questionAnswer.getAgreeNum());
					tQuestionResult.setQaCommentNum(questionAnswer.getCommentNum());
				}
				Collections.sort(tQuestionResults, new Comparator<TQuestionResult>() {
					// TODO 根据评论数和点赞数排序，算法待确认
					@Override
					public int compare(TQuestionResult o1, TQuestionResult o2) {
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
						new PageRequest(topicParams.getPindex(), topicParams.getPindex() + 30));
				return new AppResp(questResults, CodeDef.SUCCESS);
			// 优秀回答者
			case Const.TOP_OBJ_OPT_EXRESP:
				List<QAExcellentResp> excellentResps = tQuestionService.findExcellentResp(topicParams.getTopicId(),
						topicParams.getPindex(), topicParams.getPindex() + 30);
				if (topicParams.getUserId() != null) {
					for (QAExcellentResp qaExcellentResp : excellentResps) {
						qaExcellentResp.setIsFollow(0);// 未关注
						List<String> toUserIds = userFollowRelRepo.findByUser(topicParams.getUserId());
						for (String toUserId : toUserIds) {
							if (qaExcellentResp.getUserId().equals(toUserId)) {
								qaExcellentResp.setIsFollow(1);// 已关注
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

}
