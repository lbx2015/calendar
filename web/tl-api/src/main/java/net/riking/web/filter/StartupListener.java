package net.riking.web.filter;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import net.riking.config.Config;
import net.riking.config.Const;
import net.riking.config.RedisConfig;
import net.riking.core.service.DataDictService;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.service.MQReceiveService;
import net.riking.service.QuestionKeyWordService;
import net.riking.service.impl.SysDataServiceImpl;
import net.riking.spring.SpringBeanUtil;
import net.riking.task.MQSysInfoListener;
import net.riking.task.MQSysMesListener;
import net.riking.task.MQSysOptListener;
import net.riking.util.RedisUtil;
import net.riking.util.TimerManager;

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
	QuestionKeyWordService questionKeyWordService;

	@Autowired
	SysDataServiceImpl sysDataServiceImpl;

	@Autowired
	RedisConfig redisConfig;

	@Autowired
	MQReceiveService mQReceiveService;
	@Autowired
	TimerManager timerManager;
	
	@Autowired
	MQSysOptListener mqSysOptListener;
	@Autowired
	MQSysInfoListener mqSysInfoListener;
	@Autowired
	MQSysMesListener mqSysMesListener;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {

		logger.info("===== spring bean obejct begin loading ======== ");
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		SpringBeanUtil.getInstance().setWac(wac);
		logger.info("===== spring bean obejct load complete ======== ");

		RedisUtil.redisConfig = redisConfig;
		RedisUtil.getInstall();
		dataDictService.init();
		sysDataServiceImpl.initData();

		questionKeyWordService.initKeyWord();
		
		timerManager.init();
		mQReceiveService.init(Const.SYS_INFO_QUEUE, mqSysInfoListener);// 初始化mq接收信息系统通知队列
		mQReceiveService.init(Const.SYS_MES_QUEUE, mqSysMesListener);// 初始化mq接收信息系统消息队列
		mQReceiveService.init(Const.SYS_OPT_QUEUE, mqSysOptListener);// 初始化mq接收信息系统操作队列
	}
		

	/*private void initWorkflow(ServletContextEvent event) throws InterruptedException {

	}*/

	/**
	 * Shutdown servlet context (currently a no-op method).
	 *
	 * @param servletContextEvent The servlet context event
	 */
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		SpringBeanUtil.getInstance().setWac(null);
		logger.info("destroy startupListener context...");
		
		shutDowncleanUpThreadAndDeregisterJDBCDriver();
        
	}
	
	private void shutDowncleanUpThreadAndDeregisterJDBCDriver() {
	    try {
	        AbandonedConnectionCleanupThread.shutdown();
	        logger.info("Shut-down of AbandonedConnectionCleanupThread successful");
	    } catch (Throwable t) {
	        logger.error("Exception occurred while shut-down of AbandonedConnectionCleanupThread", t);
	    }

	    // This manually deregisters JDBC driver, which prevents Tomcat 7 from
	    // complaining about memory leaks
	    Enumeration<Driver> drivers = DriverManager.getDrivers();
	    while (drivers.hasMoreElements()) {
	        Driver driver = drivers.nextElement();
	        try {
	            java.sql.DriverManager.deregisterDriver(driver);
	            logger.info("JDBC driver de-registered successfully");
	        } catch (Throwable t) {
	            logger.error("Exception occured while deristering jdbc driver", t);
	        }
	    }
	    try {
	        Thread.sleep(2000L);
	    } catch (Exception e) {}
	}

}
