package net.riking.task;

import java.util.List;
import java.util.TimerTask;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.dao.AppUserDao;
import net.riking.dao.ReportCompletedRelDao;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.Jdpush;
import net.riking.spring.SpringBeanUtil;
import net.riking.util.DateUtils;
import net.riking.util.JdpushUtil;

@Component("birthdayTimerTask")
public class BirthdayTimerTask extends TimerTask {
	protected final transient Logger logger = LogManager.getLogger(BirthdayTimerTask.class);

	@Autowired
	AppUserDao appUserDao;

	@Override
	public void run() {
		try {
			appUserDao = (AppUserDao) SpringBeanUtil.getInstance().getBean("appUserDao");
			String currentDate = DateUtils.getDate("MMdd");
			List<AppUserDetail> list = appUserDao.findPhoneDeviceByBirthDay(currentDate);
			if (list != null && list.size() > 0) {
				Jdpush jdpush = null;
				
				for (AppUserDetail data : list) {
					jdpush = new Jdpush();
					jdpush.setNotificationTitle(data.getUserName() + "先生/女士：祝您生日快乐");
					jdpush.setMsgTitle(data.getUserName() + "先生/女士：祝您生日快乐");
					jdpush.setMsgContent("亲爱的朋友，今天是你的生日，我为你放飞了一群祝福，为你洒下了一地幸福，祝你生日快乐，生活幸福，笑容天天，美梦实现。");
					jdpush.setExtrasparam("");
					if(StringUtils.isNotBlank(data.getPhoneDeviceId())){
						jdpush.setRegisrationId(data.getPhoneDeviceId());
						JdpushUtil.sendToRegistrationId(jdpush);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

}
