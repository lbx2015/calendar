package net.riking.service;

import java.util.Date;
import java.util.List;

import net.riking.entity.model.TQuestionResult;

public interface TQuestionService {
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
}
