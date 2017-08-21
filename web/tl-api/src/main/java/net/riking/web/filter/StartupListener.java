package net.riking.web.filter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.config.Config;
import net.riking.config.RedisConfig;
import net.riking.core.service.DataDictService;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.service.impl.SysDataServiceImpl;
import net.riking.util.RedisUtil;
import net.riking.util.TimerManager;

@Component
public class StartupListener implements ServletContextListener {
	private static final Logger logger = LogManager
			.getLogger(StartupListener.class);

	@Autowired
	WorkflowMgr workflowMgr;
	@Autowired
	Config config;

	@Autowired
	DataDictService dataDictService;

	@Autowired
	SysDataServiceImpl sysDataServiceImpl;
	
	@Autowired
	RedisConfig redisConfig;
	
	@Autowired
	TimerManager timerManager;
	
//	@Autowired
//	JedisUtil jedisUtil;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			initWorkflow(event);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		RedisUtil.redisConfig = redisConfig;
		RedisUtil.getInstall();
		dataDictService.init();
//		jedisUtil.init();
		sysDataServiceImpl.initData();
		timerManager.init();
	}

	private void initWorkflow(ServletContextEvent event)
			throws InterruptedException {
		
	}

	/**
	 * Shutdown servlet context (currently a no-op method).
	 *
	 * @param servletContextEvent
	 *            The servlet context event
	 */
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		logger.info("destroy startupListener context...");
	}

}
