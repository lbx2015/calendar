package net.riking.dao;

import java.util.List;

import net.riking.entity.model.QAnswerResult;
import net.riking.entity.model.QuestionAnswer;

public interface QAnswerDao {
	/**
	 * 根据问题的id根据评论数，点赞数排序查询第一条回答
	 * @param questionId
	 * @return
	 */
	public QuestionAnswer getAContentByOne(String questionId);

	public List<QAnswerResult> findCollectQAnswer(String userId, int start, int pageCount);

}
