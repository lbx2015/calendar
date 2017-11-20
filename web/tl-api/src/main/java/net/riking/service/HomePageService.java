package net.riking.service;

import java.util.List;

import net.riking.entity.model.TopicQuestionResult;

public interface HomePageService {
	
	List<TopicQuestionResult> findTopicQuestionByUserId(String userId,String page,String pageCount,String sTime);
	
	List<TopicQuestionResult> findNotTopicQuestion(String page,String pageCount, String sTime,String strList);

}
