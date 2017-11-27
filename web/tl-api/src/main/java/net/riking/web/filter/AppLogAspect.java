package net.riking.web.filter;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.riking.entity.AppResp;
import net.riking.entity.model.AppLog;
import net.riking.entity.model.AppUser;
import net.riking.service.repo.AppLogInfoRepo;

@Aspect
@Component
public class AppLogAspect {
	Logger logger = LogManager.getLogger("AppLogAspect");

	ObjectMapper mapper = new ObjectMapper();

	// @Autowired
	// OperLogService operLogService;

	@Autowired
	AppLogInfoRepo appLogInfoRepo;

	@Pointcut("execution(* net.riking..appInterface.*.*(..))")
	public void pointCutLogin() {
	}

	// @Autowired
	// HttpServletRequest request;

	public AppLogAspect() {
	}

	@Around("execution(* net.riking..appInterface.LoginServer.checkValiCode_(..))")
	public AppResp doAroundForCheckValiCode_(ProceedingJoinPoint pjp) throws Throwable {
		AppUser appUser = (AppUser) pjp.getArgs()[0];
		AppResp appResp = (AppResp) pjp.proceed();

		AppUser rs = (AppUser) appResp.get_data();
		AppLog info = new AppLog();
		info.setAppUserId(appUser.getId());
		info.setCreateTime(new Date());
		if (rs != null && StringUtils.isNotBlank(rs.getId())) {
			info.setDoThing("LoginSucc");
			logger.info("{} Login Success", appUser.getPhone());
		} else {
			info.setDoThing("LoginFail");
			logger.info("{} Login Fail", appUser.getPhone());
		}
		appLogInfoRepo.save(info);
		return appResp;
	}

	@Around("execution(* net.riking..appInterface.LoginServer.getValiCode_(..))")
	public AppResp doAroundForGetValiCode_(ProceedingJoinPoint pjp) throws Throwable {
		AppResp appResp = (AppResp) pjp.proceed();
		AppUser rs = (AppUser) appResp.get_data();
		AppLog info = new AppLog();
		info.setAppUserId(rs.getId());
		info.setCreateTime(new Date());
		if (rs != null && StringUtils.isNotBlank(rs.getPhone())) {
			info.setDoThing("getValiCodeSucc");
			logger.info("{} getValiCode Success", rs.getUserName());
		} else {
			info.setDoThing("getValiCodeFail");
			logger.info("{} getValiCode Fail", rs.getUserName());
		}
		appLogInfoRepo.save(info);
		return appResp;
	}
}
