package net.riking.service.impl;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.config.ActiveMqConfig;
import net.riking.service.MQReceiveService;
import net.riking.task.MQSysInfoListener;
import net.riking.task.MQSysMesListener;
import net.riking.task.MQSysOptListener;

@Service("mqReceiveService")
@Transactional
public class MQReceiveServiceImpl implements MQReceiveService {

	ConnectionFactory connectionFactory;// 连接工厂

	Connection connection = null;// 连接

	Session session;// 会话 接受或者发送消息的线程

	Destination destination;// 消息的目的地

	MessageConsumer messageConsumer;// 消息的消费者
	
	@Autowired
	ActiveMqConfig mqConfig;

	@Override
	public void init(String queueName, Object mqListener) {
		try {
			// 实例化连接工厂
//			connectionFactory = new ActiveMQConnectionFactory(Const.MQ_USER_NAME, Const.MQ_PASSWORD,
//					Const.MQ_BROKE_URL);
			connectionFactory = new ActiveMQConnectionFactory(mqConfig.getUserName(), mqConfig.getPassWord(),
					mqConfig.getUrl());
			// 通过连接工厂获取连接
			connection = connectionFactory.createConnection();
			// 启动连接
			connection.start();
			// 创建session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 创建一个连接HelloWorld的消息队列
			destination = session.createQueue(queueName);
			// 创建消息消费者
			messageConsumer = session.createConsumer(destination);
			if (mqListener instanceof MQSysInfoListener) {
				// 给消费者设定监听对象
				messageConsumer.setMessageListener((MQSysInfoListener) mqListener);
			} else if (mqListener instanceof MQSysMesListener) {
				// 给消费者设定监听对象
				messageConsumer.setMessageListener((MQSysMesListener) mqListener);
			} else if (mqListener instanceof MQSysOptListener) {
				// 给消费者设定监听对象
				messageConsumer.setMessageListener((MQSysOptListener) mqListener);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
