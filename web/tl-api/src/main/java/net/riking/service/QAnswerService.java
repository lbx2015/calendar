package net.riking.service;

import java.util.List;

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
}
