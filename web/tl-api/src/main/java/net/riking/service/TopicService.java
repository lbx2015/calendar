package net.riking.service;

import java.util.List;

import net.riking.entity.model.TopicResult;

public interface TopicService {

	/**
	 * 可能感兴趣的话题
	 * @param topicId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<TopicResult> findTopicOfInterest(String userId, int begin, int end);
}
