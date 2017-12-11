package net.riking.service;

import net.riking.entity.model.QuestionAnswer;

public interface QAnswerService {
	/**
	 * 根据问题的id根据评论数，点赞数排序查询第一条回答
	 * @param questionId
	 * @return
	 */
	public QuestionAnswer getAContentByOne(String questionId);

}
