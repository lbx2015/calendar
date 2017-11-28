package net.riking.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.TopicQuestion;

/**
 * 
 * 〈行业资讯评论点赞信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface TopicQuestionRepo
		extends JpaRepository<TopicQuestion, String>, JpaSpecificationExecutor<TopicQuestion> {
	// /**
	// *
	// * @param qacId
	// * @return
	// */
	// @Query("select t.id,t.,t.title,t.content,t.topicId,t.userId from TopicQuestion t where qacId
	// = ?1 ")
	// Integer commentCount(String qacId);
}
