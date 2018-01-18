package net.riking.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.config.Const;
import net.riking.dao.repo.ShieldKeyWordRepo;
import net.riking.entity.model.ShieldKeyWord;
import net.riking.service.ShieldKeyWordService;
import net.riking.util.RedisUtil;

@Service("shieldKeywordService")
@Transactional
public class ShieldKeyWordServiceImpl implements ShieldKeyWordService {

	@Autowired
	private ShieldKeyWordRepo shieldKeywordRepo;

	@Override
	public void initKeyWord() {
		Set<String> set = shieldKeywordRepo.findAllKeyWord();
		RedisUtil.getInstall().setSet(Const.SHIELD_KEY_WORD, set);
	}

	@Override
	public void delKeyWord(List<Long> ids) {
		ids.forEach(e -> shieldKeywordRepo.delete(e));
		this.initKeyWord();
	}

	@Override
	public ShieldKeyWordRepo getRepo() {
		return this.shieldKeywordRepo;
	}

	@Override
	public void addKeyWord(ShieldKeyWord keyWord) {
		shieldKeywordRepo.save(keyWord);
		this.initKeyWord();
	}

	@Override
	public String filterKeyWord(String target) {
		Set<String> set = RedisUtil.getInstall().getSet(Const.SHIELD_KEY_WORD);
		set.forEach(e -> target.replace(e, "***"));
		return target;
	}

	@Override
	public boolean checkKeyWord(String target) {
		Set<String> set = RedisUtil.getInstall().getSet(Const.SHIELD_KEY_WORD);
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			if (target.contains(iterator.next())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void addMoreKeyWord(List<ShieldKeyWord> keyWords) {
		// 清空某张表的数据
		shieldKeywordRepo.deleteAll();
		// 导入数据
		shieldKeywordRepo.save(keyWords);
		// 加载到redis中
		this.initKeyWord();
	}

}
