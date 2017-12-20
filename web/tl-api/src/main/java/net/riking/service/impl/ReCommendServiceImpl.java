package net.riking.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.dao.ReCommendDao;
import net.riking.entity.model.Recommend;
import net.riking.service.ReCommendService;

@Service("reCommendSerice")
@Transactional
public class ReCommendServiceImpl implements ReCommendService {

	@Autowired
	private ReCommendDao reCommendDao;

	@Override
	public Set<Recommend> findALL() {
		// TODO Auto-generated method stub
		return reCommendDao.findALL();
	}

}
