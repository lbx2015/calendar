package net.riking.web.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import net.riking.dao.repo.QACAgreeRelRepo;
import net.riking.dao.repo.QACommentRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.TopicRepo;
import net.riking.dao.repo.UserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.Topic;
import net.riking.entity.params.TopicParams;

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
	 * TODO 精华问题回答列表[tqId]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "精华问题回答列表", notes = "POST")
	@RequestMapping(value = "/essenceQAList", method = RequestMethod.POST)
	public AppResp essenceQAList_(@RequestBody TopicParams topicParams) {
		// 返回到前台的问题回答列表
		List<Map<String, Object>> questionAnswerMapList = new ArrayList<Map<String, Object>>();
		switch (topicParams.getOptType()) {
			// 精华
			case Const.TOP_OBJ_OPT_ESSENCE:

				break;
			// 问题
			case Const.TOP_OBJ_OPT_QUEST:

				break;
			// 优秀回答者
			case Const.TOP_OBJ_OPT_EXRESP:

				break;
			default:
				logger.error("参数异常：optType:" + topicParams.getOptType());
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}

		return new AppResp(questionAnswerMapList, CodeDef.SUCCESS);
	}

}
