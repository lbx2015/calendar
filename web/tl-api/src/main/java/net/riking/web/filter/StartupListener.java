package net.riking.web.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.riking.entity.model.*;
import net.riking.task.InfoJobfillTask;
import net.riking.workflow.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.riking.config.Config;
import net.riking.core.entity.Target;
import net.riking.core.service.DataDictService;
import net.riking.core.utils.IOUtils;
import net.riking.core.workflow.Workflow;
import net.riking.core.workflow.WorkflowMgr;

@Component
public class StartupListener implements ServletContextListener {
	private static final Logger logger = LogManager.getLogger(StartupListener.class);

	@Autowired
	WorkflowMgr workflowMgr;
	@Autowired
	Config config;

	@Autowired
	DataDictService dataDictService;
	
	@Autowired
	InfoJobfillTask infoJobfillTask;

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

		dataDictService.init();
	}

	@Autowired
	EmployeeStateListener employeeStateListener;
	
	@Autowired
    AmlBigAmountStateListener amlBigAmountStateListener;
	
	@Autowired
	AmlSuspStateListener amlSuspStateListener;
	
	@Autowired
	BaseCorpCustStateListener baseCorpCustStateListener;

	@Autowired
	BaseAifStateListener baseAifStateListener;

	@Autowired
	BaseIndvCustStateListener baseIndvCustStateListener;

	@Autowired
	BaseTrnStateListener baseTrnStateListener;
	
	private void initWorkflow(ServletContextEvent event) throws InterruptedException {
		InputStream tempIs = StartupListener.class.getResourceAsStream("/workflow.json");
		if (tempIs != null) {
			try {
				String str = IOUtils.readStreamAsString(tempIs, "utf-8");
				workflowMgr.addWorkflows(str);
				Workflow workflow = workflowMgr.getWorkflow(config.getAmlWorkId());
				workflow.addStateListener(Target.of(Employee.class), employeeStateListener);
				workflow.addStateListener(Target.of(BigAmount.class), amlBigAmountStateListener);
				workflow.addStateListener(Target.of(AmlSuspicious.class), amlSuspStateListener);
				Workflow baseInfoWorkflow = workflowMgr.getWorkflow(config.getBaseInfoWorkId());
				baseInfoWorkflow.addStateListener(Target.of(BaseCorpCust.class), baseCorpCustStateListener);
				baseInfoWorkflow.addStateListener(Target.of(BaseAif.class), baseAifStateListener);
				baseInfoWorkflow.addStateListener(Target.of(BaseIndvCust.class), baseIndvCustStateListener);
				baseInfoWorkflow.addStateListener(Target.of(BaseTrn.class), baseTrnStateListener);
				//给基础信息导入的数据添加job_id 五分钟执行一次
				Thread thread = new Thread(infoJobfillTask);
				Thread.sleep(1000);
				thread.start();
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
