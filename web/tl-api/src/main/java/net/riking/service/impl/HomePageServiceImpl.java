package net.riking.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.dao.HomePageDao;
import net.riking.entity.model.TopicQuestionResult;
import net.riking.service.HomePageService;

@Service("homePageService")
public class HomePageServiceImpl implements HomePageService{
	@Autowired
	HomePageDao homePageDao;

	@Override
	public List<TopicQuestionResult> findTopicQuestionByUserId(String userId, String page,String pageCount,String sTime) {
		// TODO Auto-generated method stub
		return homePageDao.findTopicQuestionByUserId(userId, page,pageCount,sTime);
	}

	@Override
	public List<TopicQuestionResult> findNotTopicQuestion(String page, String pageCount, String sTime,String strList) {
		// TODO Auto-generated method stub
		return homePageDao.findNotTopicQuestion(page, pageCount, sTime,strList);
	}
	
}
