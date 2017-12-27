package net.riking.task;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

/**
 * 私信监听
 * @author jc.tan 2017年12月23日
 * @see
 * @since 1.0
 */
@Component("mqSysMesListener")
public class MQSysMesListener implements MessageListener {
	private static final Logger logger = LogManager.getLogger("mqSysMesListener");

	@Override
	public void onMessage(Message message) {
		TextMessage txtMessage = (TextMessage) message;

		try {
			JSONObject jsonobject = JSONObject.fromObject(txtMessage.getText());
			logger.info("get message " + txtMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
