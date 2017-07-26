package net.riking.web.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.riking.core.service.RemoteOAuthService;

@Aspect
@Component
@Order(0)
public class OAuthAspect {
	Logger logger = LogManager.getLogger("OAuthAspect");
	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	RemoteOAuthService remoteOAuthService;

	@Pointcut("execution(* net.riking..controller.*.*(..))")
	public void mapperCut() {
	}

	@Autowired
	HttpServletRequest request;

	public OAuthAspect() {
	}

	@Around(value = "mapperCut()")
	public Object cacheProxy(ProceedingJoinPoint point) throws Throwable {
		Object result = remoteOAuthService.proceed(point, request);
		return result;
	}

}
