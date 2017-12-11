package net.riking.dao;

import java.util.Date;
import java.util.List;

import net.riking.entity.model.QAExcellentResp;
import net.riking.entity.model.TQuestionResult;

public interface TQuestionDao {
	/**
	 * 查询话题首页下一条数据，查询从start到end条数据
	 * @param userId
	 * @param reqTimeStamp
	 * @param count
	 * @return
	 */
	public List<TQuestionResult> findTopicHomeUp(String userId, Date reqTimeStamp, int start, int end);

	/**
	 * 查询话题首页刷新数据，查询从start到end条数据
	 * @param userId
	 * @param reqTimeStamp
	 * @param count
	 * @return
	 */
	public List<TQuestionResult> findTopicHomeDown(String userId, Date reqTimeStamp, int start, int end);

	/**
	 * 根据话题id查询对应问题
	 * @param topic
	 * @param start
	 * @param end
	 * @return
	 */
	public List<TQuestionResult> findEssenceByTid(String topicId, int start, int end);

	/**
	 * 优秀回答者
	 * @param topicId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<QAExcellentResp> findExcellentResp(String topicId, int start, int end);
}
