package net.riking.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.config.Const;
import net.riking.dao.repo.QuestionKeyWordRepo;
import net.riking.dao.repo.TopicRepo;
import net.riking.entity.model.QuestionKeyWord;
import net.riking.entity.model.Topic;
import net.riking.service.QuestionKeyWordService;
import net.riking.util.RedisUtil;
/**
 * 问题title 关键字 获取 到  topic
 * @author you.fei
 * @version crateTime：2018年1月2日 上午11:00:37
 * @used TODO
 */
@Service("questionKeyWordService")
@Transactional
public class QuestionKeyWordServiceImpl implements QuestionKeyWordService {
	
	@Autowired
	QuestionKeyWordRepo questionKeyWordRepo;
	
	@Autowired
	TopicRepo topicRepo;

	@Override
	public void initKeyWord() {
		List<QuestionKeyWord> list = questionKeyWordRepo.findAll();
		if(!list.isEmpty()){
			HashMap<String,Set<String>> map = new HashMap<>();
			for (QuestionKeyWord questionKeyWord : list) {
				if(map.containsKey(questionKeyWord.getKeyWord()) && StringUtils.isNotBlank(questionKeyWord.getTopicIds())){
					map.get(questionKeyWord.getKeyWord()).addAll(Arrays.asList(questionKeyWord.getTopicIds().split(",")));
				}else{
					Set<String> set = new HashSet<>();
					if(StringUtils.isNotBlank(questionKeyWord.getTopicIds())){
						set.addAll(Arrays.asList(questionKeyWord.getTopicIds().split(",")));
					}
					map.put(questionKeyWord.getKeyWord(), set);
				}
			}
			RedisUtil.getInstall().setSet(Const.QUESTION_KEY_WORD, new HashSet<String>(map.keySet()));
			for (String keyWord : map.keySet()) {
				RedisUtil.getInstall().setSet(keyWord, map.get(keyWord));
			}
		}
	}

	@Override
	public Set<String> getTopicIdByQuestion(String questionTitle) {
		Set<String> set = RedisUtil.getInstall().getSet(Const.QUESTION_KEY_WORD);
		Set<String> set2 = new HashSet<>();
		if(null!=set && set.size()>0){
			for (String keyWord : set) {
				if(questionTitle.contains(keyWord)){
					set2.add(keyWord);
				}
			}
		}
		Set<String> set3= new HashSet<>();
		Iterator<String> iterator = set2.iterator();
		while (iterator.hasNext()) {
			set3.addAll(RedisUtil.getInstall().getSet(iterator.next()));
		}
		return set3;
	}

	@Override
	public List<Topic> getTopicByTopicIds(Set<String> topicIds) {
		List<Topic> list = topicRepo.findAllByIdsAndIsDelete(topicIds);
		return list;
	}

	@Override
	public List<Topic> getTopicByQuestion(String questionTitle) {
		Set<String> topicIds = this.getTopicIdByQuestion(questionTitle);
		if(topicIds.isEmpty()){
			return null;
		}
		return this.getTopicByTopicIds(topicIds);
	}

	@Override
	public void delKeyWord(List<Long> ids) {
//		List<QuestionKeyWord> list = questionKeyWordRepo.findAll(ids);
//		for (QuestionKeyWord keyWord : list) {
//			questionKeyWordRepo.delete(keyWord.getId());
//		}
		if(!ids.isEmpty()){
			ids.forEach(e->{questionKeyWordRepo.delete(e);});
			this.initKeyWord();
		}
	}

	
	@Override
	public void addKeyWord(QuestionKeyWord keyWord) {
		questionKeyWordRepo.save(keyWord);
		this.initKeyWord();
	}

	@Override
	public QuestionKeyWordRepo getRepo() {
		return questionKeyWordRepo;
	}
	
}
