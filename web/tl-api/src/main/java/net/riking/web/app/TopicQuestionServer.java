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
import net.riking.dao.repo.UserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.QuestionAnswer;
import net.riking.entity.model.TQuestionRel;
import net.riking.entity.model.TopicQuestion;
import net.riking.entity.model.TopicRel;
import net.riking.entity.model.UserFollowRel;
import net.riking.entity.params.TQuestionParams;
import net.riking.util.DateUtils;
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
	TQuestionRelRepo tQuestionRelRepo;

	@Autowired
	QACommentRepo qACommentRepo;

	@Autowired
	QACAgreeRelRepo qACAgreeRelRepo;

	@Autowired
	UserFollowRelRepo userFollowRelRepo;

	/**
	 * 问题的详情[userId,tqId]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "问题的详情", notes = "POST")
	@RequestMapping(value = "/getTopicQuestion", method = RequestMethod.POST)
	public AppResp getTopicQuestion(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		TQuestionParams tQuestionParams = Utils.map2Obj(params, TQuestionParams.class);
		TopicQuestion topicQuestion = topicQuestionRepo.getById(tQuestionParams.getTqId());
		// TODO 问题的关注数 后面从redis里面取
		Integer followNum = tQuestionRelRepo.followCount(tQuestionParams.getTqId(), 0);// 0-关注
		topicQuestion.setFollowNum(followNum);
		// TODO 问题的回答数 后面从redis里面取
		Integer answerNum = questionAnswerRepo.answerCount(tQuestionParams.getTqId());
		topicQuestion.setAnswerNum(answerNum);
		// 将对象转换成map
		Map<String, Object> topicQuestionMap = Utils.objProps2Map(topicQuestion, true);
		String pattern = "yyyyMMddHHmmssSSS";
		topicQuestionMap.put("createdTime", DateUtils.DateFormatMS(topicQuestion.getCreatedTime(), pattern));
		topicQuestionMap.put("modifiedTime", DateUtils.DateFormatMS(topicQuestion.getModifiedTime(), pattern));
		return new AppResp(topicQuestionMap, CodeDef.SUCCESS);
	}

	/**
	 * 问题，话题，用户的关注[userId,objType(1-问题；2-话题；3-用户),attentObjId（关注类型ID）,enabled（1-关注；0-取消）]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "问题，话题，用户的关注", notes = "POST")
	@RequestMapping(value = "/tQUAgree", method = RequestMethod.POST)
	public AppResp tQUAgree(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		TQuestionParams tQuestionParams = Utils.map2Obj(params, TQuestionParams.class);
		switch (tQuestionParams.getObjType()) {
			// 问题关注
			case Const.OBJ_TYPE_1:
				if (Const.EFFECTIVE == tQuestionParams.getEnabled()) {
					// 如果传过来的参数是关注，保存新的一条关注记录
					TQuestionRel topQuestionRel = new TQuestionRel();
					topQuestionRel.setUserId(tQuestionParams.getUserId());
					topQuestionRel.setTqId(tQuestionParams.getAttentObjId());
					topQuestionRel.setDataType(0);// 关注
					tQuestionRelRepo.save(topQuestionRel);
				} else if (Const.INVALID == tQuestionParams.getEnabled()) {
					// 如果传过来是取消关注，把之前一条记录物理删除
					tQuestionRelRepo.deleteByUIdAndTqId(tQuestionParams.getUserId(), tQuestionParams.getAttentObjId(),
							0);// 0-关注 3-屏蔽
				} else {
					logger.error("参数异常：enabled=" + tQuestionParams.getEnabled());
					return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			// 话题关注
			case Const.OBJ_TYPE_2:
				if (Const.EFFECTIVE == tQuestionParams.getEnabled()) {
					// 如果传过来的参数是关注，保存新的一条关注记录
					TopicRel topicRel = new TopicRel();
					topicRel.setUserId(tQuestionParams.getUserId());
					topicRel.setTopicId(tQuestionParams.getAttentObjId());
					topicRel.setDataType(0);// 关注
					topicRelRepo.save(topicRel);
				} else if (Const.INVALID == tQuestionParams.getEnabled()) {
					// 如果传过来是取消关注，把之前一条记录物理删除
					topicRelRepo.deleteByUIdAndTopId(tQuestionParams.getUserId(), tQuestionParams.getAttentObjId(), 0);// 0-关注3-屏蔽

				} else {
					logger.error("参数异常：enabled=" + tQuestionParams.getEnabled());
					return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			// 用户关注
			case Const.OBJ_TYPE_3:
				if (Const.EFFECTIVE == tQuestionParams.getEnabled()) {
					// 先根据toUserId 去数据库查一次记录，如果有一条点赞记录就新增一条关注记录并关注状态改为：1-互相关注
					UserFollowRel toUserFollowRel = userFollowRelRepo.getByUIdAndToId(tQuestionParams.getAttentObjId(),
							tQuestionParams.getUserId());// 对方的点赞记录
					if (toUserFollowRel != null) {
						// 更新对方关注表，互相关注
						userFollowRelRepo.updFollowStatus(toUserFollowRel.getUserId(), toUserFollowRel.getToUserId(),
								1);// 1-互相关注
						// 如果传过来的参数是关注，保存新的一条关注记录
						UserFollowRel userFollowRel = new UserFollowRel();
						userFollowRel.setUserId(tQuestionParams.getUserId());
						userFollowRel.setToUserId(tQuestionParams.getAttentObjId());
						userFollowRel.setFollowStatus(1);// 互相关注
						userFollowRelRepo.save(userFollowRel);
					} else {
						// 如果传过来的参数是关注，保存新的一条关注记录
						UserFollowRel userFollowRel = new UserFollowRel();
						userFollowRel.setUserId(tQuestionParams.getUserId());
						userFollowRel.setToUserId(tQuestionParams.getAttentObjId());
						userFollowRel.setFollowStatus(0);// 非互相关注
						userFollowRelRepo.save(userFollowRel);
					}
				} else if (Const.INVALID == tQuestionParams.getEnabled()) {
					UserFollowRel toUserFollowRel = userFollowRelRepo.getByUIdAndToId(tQuestionParams.getAttentObjId(),
							tQuestionParams.getUserId());// 对方的点赞记录
					if (null != toUserFollowRel) {
						userFollowRelRepo.updFollowStatus(tQuestionParams.getUserId(), tQuestionParams.getAttentObjId(),
								0);// 0-非互相关注
					}
					// 如果传过来是取消关注，把之前一条记录物理删除
					userFollowRelRepo.deleteByUIdAndToId(tQuestionParams.getUserId(), tQuestionParams.getAttentObjId());
				} else {
					logger.error("参数异常：enabled=" + tQuestionParams.getEnabled());
					return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			default:
				logger.error("参数异常：objType=" + tQuestionParams.getObjType());
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}

		return new AppResp(CodeDef.SUCCESS);
	}

	/**
	 * 问题回答列表[tqId]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "问题回答列表", notes = "POST")
	@RequestMapping(value = "/questionAnswerList", method = RequestMethod.POST)
	public AppResp questionAnswerList(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		TQuestionParams tQuestionParams = Utils.map2Obj(params, TQuestionParams.class);
		// 返回到前台的问题回答列表
		List<Map<String, Object>> questionAnswerMapList = new ArrayList<Map<String, Object>>();

		List<QuestionAnswer> questionAnswerList = questionAnswerRepo.findByTqId(tQuestionParams.getTqId());
		for (QuestionAnswer questionAnswer : questionAnswerList) {
			// TODO 统计数后面从redis中取回答的评论数
			Integer commentNum = qACommentRepo.commentCount(questionAnswer.getId());
			questionAnswer.setCommentNum(commentNum);
			// TODO 统计数后面从redis中取点赞数
			Integer agreeNum = qACAgreeRelRepo.agreeCount(questionAnswer.getId(), 1);// 1-点赞
			questionAnswer.setAgreeNum(agreeNum);

			// 将对象转换成map
			Map<String, Object> questionAnswerMap = Utils.objProps2Map(questionAnswer, true);
			String pattern = "yyyyMMddHHmmssSSS";
			questionAnswerMap.put("createdTime", DateUtils.DateFormatMS(questionAnswer.getCreatedTime(), pattern));
			questionAnswerMap.put("modifiedTime", DateUtils.DateFormatMS(questionAnswer.getModifiedTime(), pattern));
			questionAnswerMapList.add(questionAnswerMap);
		}

		return new AppResp(questionAnswerMapList, CodeDef.SUCCESS);
	}

}
