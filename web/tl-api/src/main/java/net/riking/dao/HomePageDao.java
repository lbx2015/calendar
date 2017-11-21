package net.riking.dao;

import java.util.List;

import net.riking.entity.model.TopicQuestionResult;

public interface HomePageDao {

	List<TopicQuestionResult> findTopicQuestionByUserId(String userId,String page,String pageCount,String sTime);
	
	List<TopicQuestionResult> findNotTopicQuestion(String userId, String page,String pageCount,String sTime);
}
