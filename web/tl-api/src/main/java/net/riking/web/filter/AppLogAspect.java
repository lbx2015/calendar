package net.riking.web.filter;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class AppLogAspect {
	Logger logger = LogManager.getLogger("AppLogAspect");

	ObjectMapper mapper = new ObjectMapper();

	@Pointcut("execution(* net.riking..appInterface.*.*(..))")
	public void pointCutLogin() {
	}

	@Autowired
	HttpServletRequest request;

	public AppLogAspect() {
	}

	@Around("execution(* net.riking..app.*.*(..))")
	public Object doAroundForCheckValiCode_(ProceedingJoinPoint pjp)
			throws Throwable {

//		String pkParams = request.getHeader("pkParams");
//		String token = request.getHeader("token");
////		if (checkToken(pkParams, token)) {
			return pjp.proceed();
//		}
//		return new AppResp(CodeDef.EMP.REPORT_TOKEN_ERROR,
//				CodeDef.EMP.REPORT_TOKEN_ERROR_DESC);
	}

	public static String Encoder(String str)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] digest = md.digest(str.getBytes("utf-8"));
		
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < digest.length; i++) {
//			sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16)
//					.substring(1));
//		}
		return new String(digest, "utf-8");
	}

	public static boolean checkToken(String str, String token) {
		String md = null;
		try {
			md = Encoder(str);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return token.equals(md);
	}

	// @Around("execution(*
	// net.riking..appInterface.LoginServer.checkValiCode_(..))")
	// public AppResp doAroundForCheckValiCode_(ProceedingJoinPoint pjp) throws
	// Throwable {
	// AppUser appUser = (AppUser) pjp.getArgs()[0];
	// AppResp appResp = (AppResp) pjp.proceed();
	//
	// AppUser rs = (AppUser) appResp.get_data();
	//// AppLog info = new AppLog();
	// info.setAppUserId(appUser.getId());
	// info.setCreateTime(new Date());
	// if (rs != null && StringUtils.isNotBlank(rs.getId())) {
	// info.setDoThing("LoginSucc");
	// logger.info("{} Login Success", appUser.getPhone());
	// } else {
	// info.setDoThing("LoginFail");
	// logger.info("{} Login Fail", appUser.getPhone());
	// }
	// appLogInfoRepo.save(info);
	// return appResp;
	// }
	//
	// @Around("execution(*
	// net.riking..appInterface.LoginServer.getValiCode_(..))")
	// public AppResp doAroundForGetValiCode_(ProceedingJoinPoint pjp) throws
	// Throwable {
	// AppResp appResp = (AppResp) pjp.proceed();
	// AppUser rs = (AppUser) appResp.get_data();
	// AppLog info = new AppLog();
	// info.setAppUserId(rs.getId());
	// info.setCreateTime(new Date());
	// if (rs != null && StringUtils.isNotBlank(rs.getPhone())) {
	// info.setDoThing("getValiCodeSucc");
	// logger.info("{} getValiCode Success", rs.getUserName());
	// } else {
	// info.setDoThing("getValiCodeFail");
	// logger.info("{} getValiCode Fail", rs.getUserName());
	// }
	// appLogInfoRepo.save(info);
	// return appResp;
	// }
}
