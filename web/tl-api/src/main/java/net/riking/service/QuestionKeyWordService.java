package net.riking.service;

import java.util.List;
import java.util.Set;

import net.riking.dao.repo.QuestionKeyWordRepo;
import net.riking.entity.model.QuestionKeyWord;
import net.riking.entity.model.Topic;

public interface QuestionKeyWordService {
	
	public void initKeyWord();
	
	public void delKeyWord(List<Long> ids);
	
	public QuestionKeyWordRepo getRepo();
	
	public void addKeyWord(QuestionKeyWord keyWord);
	
	public Set<String> getTopicIdByQuestion(String questionTitle);
	
	public List<Topic> getTopicByTopicIds(Set<String> topicIds);
	
	public List<Topic> getTopicByQuestion(String questionTitle);
}
