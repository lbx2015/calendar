package net.riking.service;

import java.util.List;

import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.TopicResult;

public interface TopicService {

	/**
	 * 可能感兴趣的话题
	 * @param topicId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<TopicResult> findTopicOfInterest(String userId, String topicIds, int begin, int end);

	/**
	 * 关注的话题
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<TopicResult> userFollowTopic(String userId, int begin, int pageCount);

	public void shield(MQOptCommon common) throws IllegalArgumentException, IllegalAccessException;
}
