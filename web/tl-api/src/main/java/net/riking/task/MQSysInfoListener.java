package net.riking.task;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.config.Const;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.SysNoticeRepo;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.Jdpush;
import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.SysNotice;
import net.riking.util.JdpushUtil;
import net.sf.json.JSONObject;

/**
 * 系统通知监听
 * @author jc.tan 2017年12月23日
 * @see
 * @since 1.0
 */
@Component("mqSysInfoListener")
public class MQSysInfoListener implements MessageListener {
	private static final Logger logger = LogManager.getLogger("mqSysInfoListener");
	@Autowired
	private SysNoticeRepo sysNoticeRepo;
	@Autowired
	private AppUserDetailRepo appUserDetailRepo;
	
	@Override
	public void onMessage(Message message) {
		TextMessage txtMessage = (TextMessage) message;
		try {
			JSONObject jsonobject = JSONObject.fromObject(txtMessage.getText());
			MQOptCommon optCommon = (MQOptCommon) JSONObject.toBean(jsonobject, MQOptCommon.class);
			//是否发送极光
			boolean isSendJdPush = false;
			SysNotice sysNotice = null;
			Jdpush jdpush = null;
			String title = "";
			String content = "";
			
			
			switch (optCommon.getMqOptType()) {
				case Const.MQ_SYS_INFO:
				sysNotice = new SysNotice();
				sysNotice.setTitle("新用户注册");
				sysNotice.setContent(optCommon.getContent());
				sysNotice.setFromUserId(Const.SYS_NOTICE_FROME_SYS);
				sysNotice.setNoticeUserId(optCommon.getAttentObjId());
				sysNotice.setDataType(Const.NOTICE_SYS_INFO);
				sysNoticeRepo.save(sysNotice);
				break;
			}
			if(isSendJdPush){
				
				jdpush = new Jdpush();
				jdpush = new Jdpush();
				jdpush.setNotificationTitle(title);
				jdpush.setMsgTitle(title);
				jdpush.setMsgContent(content);
				jdpush.setExtrasparam("");
				if(sysNotice != null){
					AppUserDetail noticeUserDetail = appUserDetailRepo.findOne(sysNotice.getNoticeUserId());
					if(StringUtils.isNotBlank(noticeUserDetail.getPhoneDeviceId())){
						jdpush.setRegisrationId(noticeUserDetail.getPhoneDeviceId());
						JdpushUtil.sendToRegistrationId(jdpush);
					}
				}
			}
			logger.info("get message " + txtMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
