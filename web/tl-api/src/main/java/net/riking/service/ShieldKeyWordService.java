package net.riking.service;

import java.util.List;

import net.riking.dao.repo.ShieldKeyWordRepo;
import net.riking.entity.model.ShieldKeyWord;

public interface ShieldKeyWordService {

	public void initKeyWord();

	public void delKeyWord(List<Long> ids);

	public ShieldKeyWordRepo getRepo();

	public void addKeyWord(ShieldKeyWord keyWord);

	public void addMoreKeyWord(List<ShieldKeyWord> keyWords);

	public String filterKeyWord(String target);

	public boolean checkKeyWord(String target);
}
