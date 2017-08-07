package net.riking.web.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.riking.config.Config;
import net.riking.config.RedisConfig;
import net.riking.core.service.DataDictService;
import net.riking.core.utils.IOUtils;
import net.riking.core.workflow.Workflow;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.service.repo.impl.SysDataServiceImpl;
import net.riking.util.RedisUtil;

@Component
public class StartupListener implements ServletContextListener {
	private static final Logger logger = LogManager.getLogger(StartupListener.class);

	@Autowired
	WorkflowMgr workflowMgr;
	@Autowired
	Config config;
	@Autowired
	RedisConfig redisConfig;

	@Autowired
	DataDictService dataDictService;
	
	@Autowired
	SysDataServiceImpl sysDataServiceImpl;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {

		try {
			initWorkflow(event);
			RedisUtil.redisConfig = redisConfig;
			RedisUtil.getInstall();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		dataDictService.init();
		sysDataServiceImpl.initData();
	}

	
	private void initWorkflow(ServletContextEvent event) throws InterruptedException {
		InputStream tempIs = StartupListener.class.getResourceAsStream("/workflow.json");
		if (tempIs != null) {
			try {
				String str = IOUtils.readStreamAsString(tempIs, "utf-8");
				workflowMgr.addWorkflows(str);
				Workflow workflow = workflowMgr.getWorkflow(config.getAmlWorkId());
				Workflow baseInfoWorkflow = workflowMgr.getWorkflow(config.getBaseInfoWorkId());
				logger.info("--------------workflow init");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
