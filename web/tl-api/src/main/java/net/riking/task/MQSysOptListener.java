package net.riking.task;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 系统操作监听
 * @author jc.tan 2017年12月23日
 * @see
 * @since 1.0
 */
@Component("mqSysOptListener")
public class MQSysOptListener implements MessageListener {
	private static final Logger logger = LogManager.getLogger("mqSysOptListener");

	@Override
	public void onMessage(Message message) {
		TextMessage txtMessage = (TextMessage) message;
		try {
			logger.info("get message " + txtMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
