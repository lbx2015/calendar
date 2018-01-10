package net.riking.service;

import java.util.Date;
import java.util.List;

import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.QAExcellentResp;
import net.riking.entity.model.QAnswerResult;
import net.riking.entity.model.QuestResult;
import net.riking.entity.model.TQuestionResult;

public interface TQuestionService {
	/**
	 * 查询话题首页下一条数据，查询从start到end条数据
	 * @param userId
	 * @param reqTimeStamp
	 * @param count
	 * @return
	 */
	public List<TQuestionResult> findTopicHomeUp(String userId, Date reqTimeStamp, String tqIds, int start, int end);

	/**
	 * 查询话题首页刷新数据，查询从start到end条数据
	 * @param userId
	 * @param reqTimeStamp
	 * @param count
	 * @return
	 */
	public List<TQuestionResult> findTopicHomeDown(String userId, Date reqTimeStamp, String tqIds, int start, int end);

	/**
	 * 根据话题id查询对应问题
	 * @param topic
	 * @param start
	 * @param end
	 * @return
	 */
	public List<QAnswerResult> findEssenceByTid(String topicId, String userId, int start, int pageCount);

	/**
	 * 优秀回答者
	 * @param topicId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<QAExcellentResp> findExcellentResp(String topicId, int start, int pageCount);

	/**
	 * 关注的问题
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<QuestResult> userFollowQuest(String userId, int start, int pageCount);

	/***
	 * 问题，话题，用户的关注操作
	 * 返回：true-关注；false-取消关注
	 * @author james.you
	 * @version crateTime：2017年12月27日 下午5:01:33
	 * @used TODO
	 * @param optCommon
	 * @return true-关注；false-取消关注
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean follow(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException;

}
