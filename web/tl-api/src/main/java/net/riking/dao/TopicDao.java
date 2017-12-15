package net.riking.dao;

import java.util.List;

import net.riking.entity.model.TopicResult;

public interface TopicDao {

	/**
	 * 可能感兴趣的话题
	 * @param topicId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<TopicResult> findTopicOfInterest(String userId, int begin, int end);
}
