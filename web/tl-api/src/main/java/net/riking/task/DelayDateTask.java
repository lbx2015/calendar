package net.riking.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.service.ReportSubmitCaliberService;
@Component("delayDateTask")
public class DelayDateTask extends TimerTask  {

	@Autowired
	ReportSubmitCaliberService reportSubmitCaliberService;
	@Override
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
    	String date = sdf.format(Calendar.getInstance().getTime());
    	if ("0501".equals(date)) {
			reportSubmitCaliberService.updateDelayDateAfter("May", "1");
			reportSubmitCaliberService.updateDelayDateAfter("October", "2"); 
		}else if ("1001".equals(date)) {
			reportSubmitCaliberService.updateDelayDateAfter("October", "1");
			reportSubmitCaliberService.updateDelayDateAfter("October", "2");
		}else if ("0601".equals(date)) {
			reportSubmitCaliberService.updateDelayDateBefer("May", "1","4");
			reportSubmitCaliberService.updateDelayDateBefer("October", "2","4");
		}else if ("1101".equals(date)) {
			reportSubmitCaliberService.updateDelayDateBefer("October", "1","4");
			reportSubmitCaliberService.updateDelayDateBefer("October", "2","4");
		}else if ("0701".equals(date)) {
			reportSubmitCaliberService.updateDelayDateBefer("May", "1","5");
			reportSubmitCaliberService.updateDelayDateBefer("October", "2","5");
		}else if ("0101".equals(date)) {
			reportSubmitCaliberService.updateDelayDateBefer("October", "1","5");
			reportSubmitCaliberService.updateDelayDateBefer("October", "2","5");
		}
	}

}
