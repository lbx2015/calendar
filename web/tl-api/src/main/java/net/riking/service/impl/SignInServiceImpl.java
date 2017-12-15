package net.riking.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.SignInRepo;
import net.riking.entity.model.SignIn;
import net.riking.service.SignInService;

@Service("signInService")
@Transactional
public class SignInServiceImpl implements SignInService {
	private static final Logger logger = LogManager.getLogger(SignInService.class);

	@Autowired
	SignInRepo signInRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Override
	public Integer signIn(SignIn signIn, String userId, Integer integral) {
		if (null == signIn) {
			SignIn signInNew = new SignIn();
			signInNew.setUserId(userId);
			signInRepo.save(signInNew);
			if (null != integral) {
				integral = integral + 10;
				appUserDetailRepo.updIntegral(integral, userId);
			}
		}
		return integral;
	}

}
