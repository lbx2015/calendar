package net.riking.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.core.entity.model.ModelPropDict;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.SignInRepo;
import net.riking.entity.model.SignIn;
import net.riking.service.SignInService;
import net.riking.service.SysDataService;

@Service("signInService")
@Transactional
public class SignInServiceImpl implements SignInService {
	private static final Logger logger = LogManager.getLogger(SignInService.class);

	@Autowired
	SignInRepo signInRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Autowired
	SysDataService sysDataService;

	@Override
	public Map<String, Object> signIn(SignIn signIn, String userId, Integer integral) {
		ModelPropDict propDict = sysDataService.getDict("T_APP_USER", "INTEGRAL", "SIGN_INTEGRAL");
		Integer signIntegral = Integer.parseInt(propDict.getValu());
		if (null == signIn) {
			SignIn signInNew = new SignIn();
			signInNew.setUserId(userId);
			signInRepo.save(signInNew);
			if (null != integral) {
				integral = integral + signIntegral;
				appUserDetailRepo.updIntegral(integral, userId);
			}
		}
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("signIntegral", signIntegral);
		maps.put("integral", integral);
		return maps;
	}

}
