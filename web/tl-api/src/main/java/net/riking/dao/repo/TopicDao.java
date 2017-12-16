package net.riking.dao.repo;

import java.util.List;

import net.riking.entity.model.TopicResult;

public interface TopicDao {

	/**
	 * 可能感兴趣的话题
	 * @param userId
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
	public List<TopicResult> userFollowTopic(String userId, int begin, int end);
}
