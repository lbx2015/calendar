package net.riking.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.entity.model.Jdpush;
import net.riking.util.JdpushUtil;

@Component("birthdayTimerTask")
public class BirthdayTimerTask extends TimerTask {
	protected final transient Logger logger = LogManager.getLogger(BirthdayTimerTask.class);

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Override
	public void run() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
			String date = sdf.format(Calendar.getInstance().getTime());
			Set<String> set = new HashSet<String>();
			// TODO 暫時注釋
			appUserDetailRepo.findByDate(date);
			if (set.size() < 1) {
				return;
			}
			Jdpush jdpush = new Jdpush();
			jdpush.setNotificationTitle("祝您生日快乐");
			jdpush.setMsgTitle("生日快乐");
			jdpush.setMsgContent("亲爱的朋友，今天是你的生日，我为你放飞了一群祝福，为你洒下了一地幸福，祝你生日快乐，生活幸福，笑容天天，美梦实现。");
			jdpush.setExtrasparam("");
			for (String string : set) {
				jdpush.setRegisrationId(string);
				JdpushUtil.sendToRegistrationId(jdpush);
			}
			// 在这里写你要执行的内容
			// System.out.println("执行当前时间"+formatter.format(Calendar.getInstance().getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
