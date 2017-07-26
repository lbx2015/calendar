package net.riking.web.filter;

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

import net.riking.core.service.OperLogService;

@Aspect
@Component
public class WebLogAspect {
	Logger logger = LogManager.getLogger("WebLogAspect");
	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	OperLogService operLogService;

	@Pointcut("execution(* net.riking..controller.*.*(..))")
	public void mapperCut() {
	}

	@Autowired
	HttpServletRequest request;

	public WebLogAspect() {
	}

	@Around(value = "mapperCut()")
	public Object cacheProxy(ProceedingJoinPoint point) throws Throwable {
		Object result = operLogService.proceed(request, point);
		return result;
	}
}
