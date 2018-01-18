package net.riking.service;

import java.util.List;

import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.QAnswerResult;
import net.riking.entity.model.QuestionAnswer;

public interface QAnswerService {
	/**
	 * 根据问题的id根据评论数，点赞数排序查询第一条回答
	 * @param questionId
	 * @return
	 */
	public QuestionAnswer getAContentByOne(String questionId);

	/**
	 * 收藏的回答
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<QAnswerResult> findCollectQAnswer(String userId, int start, int pageCount);
	
	/**
	 * 话题下  优秀回答者回答列表
	 * @used TODO
	 * @param topicId
	 * @param userId
	 * @param start
	 * @param pageCount
	 * @return
	 */
	public List<QAnswerResult> findQAByTopicIdAndUserId(String topicId, String userId, int start, int pageCount);
	
	/**
	 * 话题下  优秀回答者回答数
	 * @used TODO
	 * @param topicId
	 * @param userId
	 * @return
	 */
	public Long findQACountByTopicIdAndUserId(String topicId, String userId);
	
	/**
	 * 问题title关键字找到该问题下最新回答 
	 * @used TODO
	 * @param keyWord
	 * @return
	 */
	public List<QAnswerResult> findQAResultByKeyWord(String keyWord);

	/***
	 * 问题回答点赞或收藏
	 * 返回：true-点赞或收藏成功；false-取消点赞或收藏
	 * @author james.you
	 * @version crateTime：2017年12月27日 下午4:39:39
	 * @used TODO
	 * @param common
	 * @return true-点赞或收藏成功；false-取消点赞或收藏
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean qaAgreeCollect(MQOptCommon common) throws IllegalArgumentException, IllegalAccessException;

	public void qACommentPub(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException;

	public void commentReply(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException;
}
