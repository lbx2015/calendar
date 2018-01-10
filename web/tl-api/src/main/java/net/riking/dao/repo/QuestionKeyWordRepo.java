package net.riking.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.riking.entity.model.QuestionKeyWord;

public interface QuestionKeyWordRepo extends JpaRepository<QuestionKeyWord, Long>, JpaSpecificationExecutor<QuestionKeyWord> {
	
//	@Query("select topicId from QuestionKeyWord  where keyWord = ?1")
//	public Set<String> getTopicIdByKeyWord(String keyWord);

}
