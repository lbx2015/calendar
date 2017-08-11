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
import net.riking.entity.model.AppLogInfo;
import net.riking.entity.model.AppUser;
import net.riking.service.repo.AppLogInfoRepo;

@Aspect
@Component
public class AppLogAspect {
	private static final String AppUser = null;
	Logger logger = LogManager.getLogger("AppLogAspect");
	ObjectMapper mapper = new ObjectMapper();

//	@Autowired
//	OperLogService operLogService;
	
	@Autowired
	AppLogInfoRepo appLogInfoRepo;
	

	@Pointcut("execution(* net.riking..appInterface.*.*(..))")
	public void pointCutLogin() {
	}

//	@Autowired
//	HttpServletRequest request;

	public AppLogAspect() {
	}

  
  
	@Around("execution(* net.riking..appInterface.LoginServer.checkValiCode_(..))")
    public AppResp doAroundForCheckValiCode_(ProceedingJoinPoint pjp) throws Throwable { 
		AppUser appUser = (AppUser)pjp.getArgs()[0];
		AppResp appResp = (AppResp)pjp.proceed();
		AppUser rs =  (AppUser)appResp.get_data();
		AppLogInfo info = new AppLogInfo();
    	info.setAppUserId(appUser.getId());
    	info.setCreateTime(new Date());
		if(rs!=null&&StringUtils.isNotBlank(rs.getId())){
	    	info.setDoThing("LoginSucc");
	    	logger.info("{} Login Success",appUser.getName());
		}else{
			info.setDoThing("LoginFail");
			logger.info("{} Login Fail",appUser.getName());
		}
		appLogInfoRepo.save(info);
		return appResp;
    }  
	
	@Around("execution(* net.riking..appInterface.LoginServer.getValiCode_(..))")
    public AppResp doAroundForGetValiCode_(ProceedingJoinPoint pjp) throws Throwable { 
		AppResp appResp = (AppResp)pjp.proceed();
		AppUser rs =  (AppUser)appResp.get_data();
		AppLogInfo info = new AppLogInfo();
    	info.setAppUserId(rs.getId());
    	info.setCreateTime(new Date());
		if(rs!=null&&StringUtils.isNotBlank(rs.getTelephone())){
	    	info.setDoThing("getValiCodeSucc");
	    	logger.info("{} getValiCode Success",rs.getName());
		}else{
			info.setDoThing("getValiCodeFail");
			logger.info("{} getValiCode Fail",rs.getName());
		}
		appLogInfoRepo.save(info);
		return appResp;
    }  
}