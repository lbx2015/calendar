package net.riking.util;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.riking.config.Const;
import net.riking.entity.model.News;
import net.sf.json.JSONArray;

public class MQProduceUtil {
	private static final Logger logger = LogManager.getLogger("MQProduceUtil");

	// 默认连接用户名
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;

	// 默认连接密码
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;

	// 默认连接地址
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;

	// 连接工厂
	public static ConnectionFactory connectionFactory;

	// 连接
	public static Connection connection = null;

	// 会话 接受或者发送消息的线程
	public static Session session;

	// 消息的目的地
	public static Destination destination;

	// 消息生产者
	public static MessageProducer messageProducer;

	public static void init(String queueName) {
		// 创建一个链接工厂
		connectionFactory = new ActiveMQConnectionFactory(Const.MQ_USER_NAME, Const.MQ_PASSWORD, Const.MQ_BROKE_URL);
		try {
			// 从工厂中创建一个链接
			connection = connectionFactory.createConnection();
			// 开启链接
			connection.start();
			// 创建一个事务（这里通过参数可以设置事务的级别）
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			// 创建一个消息队列
			destination = session.createQueue(queueName);
			// 创建消息生产者
			messageProducer = session.createProducer(destination);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 发送文本消息
	 * @param session
	 * @param messageProducer 消息生产者
	 * @throws Exception
	 */
	public static void sendTextMessage(String queueName, String msg) {

		MQProduceUtil.init(queueName);
		try {
			// 发送消息
			TextMessage textMessage = session.createTextMessage(msg);
			logger.info(msg);
			messageProducer.send(textMessage);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resourceClose();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	private static void resourceClose() throws JMSException {
		if (messageProducer != null) {
			messageProducer.close();
		}
		if (session != null) {
			session.close();
		}
		if (connection != null) {
			connection.close();
		}
	}

	public static void main(String[] args) {
		List<News> list = new ArrayList<News>();
		News news = new News();
		news.setContent("aaaaaa");
		news.setId("weruwoieuhhh");
		list.add(news);
		JSONArray jsonArray = JSONArray.fromObject(list);
		MQProduceUtil.sendTextMessage(Const.SYS_INFO_QUEUE, jsonArray.toString());
	}
}
