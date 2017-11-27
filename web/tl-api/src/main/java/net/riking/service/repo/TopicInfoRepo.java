package net.riking.service.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Topic;

/**
 * 
 * 〈话题信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface TopicInfoRepo extends JpaRepository<Topic, String>, JpaSpecificationExecutor<Topic> {

	/**
	 * TODO 暂时没用到 根据时间戳返回资讯列表（下一页数据）
	 * @param reqTimeStamp
	 * @return
	 */
	@Query("select t.id,t.createdTime,t.title,t.content,t.author from TopicInfo t join TopicFollowInfo f where t.id = f.topicId and f.userId =?1 and t.createdTime < ?2 and t.enabled = 1 and f.enabled =1 order by t.createdTime desc")
	List<Topic> findNewsListPageNext(String userId, String reqTimeStamp, Pageable pageable);

	/**
	 * TODO 暂时没用到 根据时间戳返回资讯列表（刷新第一页数据顺序排列）
	 * @param reqTimeStamp
	 * @return
	 */
	@Query("select t.id,t.createdTime,t.title,t.content,t.author from TopicInfo t join TopicFollowInfo f where t.id = f.topicId and f.userId =?1 and t.createdTime > ?2 and t.enabled = 1 and f.enabled =1 order by t.createdTime asc")
	List<Topic> findNewsListRefresh(String userId, String reqTimeStamp, Pageable pageable);

	/**
	 * 根据userId找出关注的话题
	 * @param userId
	 * @return
	 */
	@Query("select t.id,t.createdTime,t.title,t.content,t.author from TopicInfo t join TopicFollowInfo f where t.id = f.topicId and f.userId =?1 and t.enabled = 1 and f.enabled = 1")
	List<Topic> findByUserId(String userId);
}
