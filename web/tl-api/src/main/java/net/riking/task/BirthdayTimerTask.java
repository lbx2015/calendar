package net.riking.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import net.riking.entity.model.Jdpush;
import net.riking.service.repo.AppUserRepo;
import net.riking.util.JdpushUtil;

public class BirthdayTimerTask extends TimerTask {

	@Autowired
	AppUserRepo appUserRepo;
	 @Override
	    public void run() {
	        try {
	        	SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
	        	String date = sdf.format(Calendar.getInstance().getTime());
	            Set<String>  set = appUserRepo.findByDate(date);
	            JdpushUtil jdpushUtil = new JdpushUtil();
	    		Jdpush jdpush = new Jdpush();
	    		jdpush.setNotificationTitle("金融台历祝您生日快乐");
	    		jdpush.setMsgTitle("今天生日哟，不要忘了。");
	    		jdpush.setMsgContent("生日对自己好一点，来场说走就走的旅行吧");
	    		jdpush.setExtrasparam("这是空");
	            for (String string : set) {
		    		jdpush.setRegisrationId(string);
			        jdpushUtil.sendToRegistrationId(jdpush);
				}
	             //在这里写你要执行的内容
	           // System.out.println("执行当前时间"+formatter.format(Calendar.getInstance().getTime()));
	        } catch (Exception e) {
	            System.out.println("-------------解析信息发生异常--------------");
	        }
	    }

}
