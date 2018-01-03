package net.riking.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
public class QuestionKeyWordServiceImpl implements QuestionKeyWordService {
	
	@Autowired
	QuestionKeyWordRepo questionKeyWordRepo;
	
	@Autowired
	TopicRepo topicRepo;

	@SuppressWarnings("static-access")
	@Override
	public void initKeyWord() {
		List<QuestionKeyWord> list = questionKeyWordRepo.findAll();
		if(!list.isEmpty()){
			HashMap<String,Set<String>> map = new HashMap<>();
			for (QuestionKeyWord questionKeyWord : list) {
				if(map.containsKey(questionKeyWord.getKeyWord())){
					map.get(questionKeyWord.getKeyWord()).add(questionKeyWord.getTopicId());
				}else{
					Set<String> set = new HashSet<>();
					set.add(questionKeyWord.getTopicId());
					map.put(questionKeyWord.getKeyWord(), set);
				}
			}
			
			RedisUtil.getInstall().setSet(Const.KEY_WORD, new HashSet<String>(map.keySet()));
			for (String keyWord : map.keySet()) {
				RedisUtil.getInstall().setSet(keyWord, map.get(keyWord));
			}
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public Set<String> getTopicIdByQuestion(String questionTitle) {
		Set<String> set = RedisUtil.getInstall().getSet(Const.KEY_WORD);
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
		return this.getTopicByTopicIds(this.getTopicIdByQuestion(questionTitle));
	}

	@SuppressWarnings("static-access")
	@Override
	public void delKeyWord(List<Long> ids) {
		List<QuestionKeyWord> list = questionKeyWordRepo.findAll(ids);
		for (QuestionKeyWord keyWord : list) {
			questionKeyWordRepo.delete(keyWord.getId());
			Set<String> set = RedisUtil.getInstall().getSet(keyWord.getKeyWord());
			set.remove(keyWord.getTopicId());
			RedisUtil.getInstall().setSet(keyWord.getKeyWord(), set);
		}
	}

	
	@SuppressWarnings("static-access")
	@Override
	public void addKeyWord(QuestionKeyWord keyWord) {
		if(null==keyWord.getId()){//新增
			questionKeyWordRepo.save(keyWord);
			Set<String> set = RedisUtil.getInstall().getSet(keyWord.getKeyWord());
			if(null == set){
				set = new HashSet<>();
				set.add(keyWord.getTopicId());
				RedisUtil.getInstall().setSet(keyWord.getKeyWord(),set);
			}else{
				set.add(keyWord.getTopicId());
				RedisUtil.getInstall().setSet(keyWord.getKeyWord(), set);
			}
		}else{//修改
			questionKeyWordRepo.save(keyWord);
			RedisUtil.getInstall().setSet(keyWord.getKeyWord(), questionKeyWordRepo.getTopicIdByKeyWord(keyWord.getKeyWord()));
		}
		
	}

	@Override
	public QuestionKeyWordRepo getRepo() {
		return questionKeyWordRepo;
	}
	
}
