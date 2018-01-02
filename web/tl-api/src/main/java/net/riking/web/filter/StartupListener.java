package net.riking.web.filter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import net.riking.config.Config;
import net.riking.config.RedisConfig;
import net.riking.core.service.DataDictService;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.service.MQReceiveService;
import net.riking.service.QuestionKeyWordService;
import net.riking.service.impl.SysDataServiceImpl;
import net.riking.spring.SpringBeanUtil;
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

	// @Autowired
	// JedisUtil jedisUtil;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {

		logger.info("===== spring bean obejct begin loading ======== ");
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		SpringBeanUtil.getInstance().setWac(wac);
		logger.info("===== spring bean obejct load complete ======== ");

		try {
			initWorkflow(event);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		RedisUtil.redisConfig = redisConfig;
		RedisUtil.getInstall();
		dataDictService.init();
		// jedisUtil.init();
		sysDataServiceImpl.initData();
		questionKeyWordService.initKeyWord();
		// timerManager.init();
//		mQReceiveService.init(Const.SYS_INFO_QUEUE, new MQSysInfoListener());// 初始化mq接收信息系统通知队列
//		mQReceiveService.init(Const.SYS_MES_QUEUE, new MQSysMesListener());// 初始化mq接收信息系统消息队列
//		mQReceiveService.init(Const.SYS_OPT_QUEUE, new MQSysOptListener());// 初始化mq接收信息系统操作队列
		// jedisUtil.init();
		sysDataServiceImpl.initData();
//		timerManager.init();	
	}

	private void initWorkflow(ServletContextEvent event) throws InterruptedException {

	}

	/**
	 * Shutdown servlet context (currently a no-op method).
	 *
	 * @param servletContextEvent The servlet context event
	 */
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		SpringBeanUtil.getInstance().setWac(null);
		logger.info("destroy startupListener context...");
		
		/*final Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                final Driver driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);
            } catch (final SQLException e) {
                logger.warn("Unable to de-register driver", e);
            }
        }

        AbandonedConnectionCleanupThread.shutdown();*/
        
	}

}
