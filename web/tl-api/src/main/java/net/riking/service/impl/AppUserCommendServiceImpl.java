package net.riking.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.dao.AppUserCommendDao;
import net.riking.entity.model.AppUserRecommend;
import net.riking.service.AppUserCommendService;

@Service("appUserCommendSerice")
@Transactional
public class AppUserCommendServiceImpl implements AppUserCommendService {

	@Autowired
	private AppUserCommendDao appUserCommendDao;
	@Override
	public Set<AppUserRecommend> findALL() {
		// TODO Auto-generated method stub
		return appUserCommendDao.findALL();
	}

}
