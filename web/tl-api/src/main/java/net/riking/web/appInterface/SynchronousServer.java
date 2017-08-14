package net.riking.web.appInterface;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUserReportCompleteRel;
import net.riking.entity.model.Remind;
import net.riking.entity.model.RemindHis;
import net.riking.entity.model.SynResult;
import net.riking.entity.model.Todo;
import net.riking.service.repo.AppUserReportCompleteRelRepo;
import net.riking.service.repo.RemindHisRepo;
import net.riking.service.repo.RemindRepo;
import net.riking.service.repo.TodoRepo;

/**app和服务器的同步信息
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/synchronous")
public class SynchronousServer {

	@Autowired
	RemindRepo remindRepo;
	@Autowired
	RemindHisRepo remindHisRepo;
	@Autowired
	TodoRepo todoRepo;
	@Autowired
	AppUserReportCompleteRelRepo appUserReportCompletesRelRepo;

	@ApiOperation(value = "同步所有信息", notes = "POST")
	@RequestMapping(value = "/synchronousAll", method = RequestMethod.POST)
	public AppResp synchronousAll(@RequestParam("userId") String userId) {
		List<Remind> reminds = remindRepo.findByUserId(userId);
		List<RemindHis> remindHis = remindHisRepo.findByUserId(userId);
		List<Todo> todos = todoRepo.findByUserId(userId);
		List<AppUserReportCompleteRel> appUserReportCompleteRel = appUserReportCompletesRelRepo.findAll();
		SynResult result = new SynResult(reminds, remindHis, todos,appUserReportCompleteRel);
		return new AppResp(result, CodeDef.SUCCESS);
	}
	
	
	@ApiOperation(value = "同步app提醒信息", notes = "POST")
	@RequestMapping(value = "/synchronousReminds", method = RequestMethod.POST)
	public AppResp synchronousReminds(@RequestBody List<Remind> reminds) {
		List<Remind> remindSave = new ArrayList<>(); 
		List<Remind> remindDele = new  ArrayList<>();
		for (int i = 0; i < reminds.size(); i++) {
			if (reminds.get(i).getDeleteState()==0) {
				remindSave.add(reminds.get(i));
			}else {
				remindDele.add(reminds.get(i));
			}
		}
		reminds = remindRepo.save(remindSave);
		remindRepo.delete(remindDele);
		return new AppResp(0, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "同步app提醒历史信息", notes = "POST")
	@RequestMapping(value = "/synchronousRemindHis", method = RequestMethod.POST)
	public AppResp synchronousRemindHis(@RequestBody List<RemindHis> remindHis) {
		List<RemindHis> remindHisSave = new ArrayList<>(); 
		List<RemindHis> remindHisDele = new  ArrayList<>();
		for (int i = 0; i < remindHis.size(); i++) {
			if (remindHis.get(i).getDeleteState()==0) {
				remindHisSave.add(remindHis.get(i));
			}else {
				remindHisSave.add(remindHis.get(i));
			}
		}
		remindHis = remindHisRepo.save(remindHisSave);
		remindHisRepo.delete(remindHisDele);
		return new AppResp(0, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "同步app代办信息", notes = "POST")
	@RequestMapping(value = "/synchronousComplete", method = RequestMethod.POST)
	public AppResp synchronousComplete(@RequestBody List<AppUserReportCompleteRel> appUserReportCompleteRel) {
		List<AppUserReportCompleteRel> appUserReportCompleteRelSave = new ArrayList<>(); 
		List<AppUserReportCompleteRel> AppUserReportCompleteRelDele = new  ArrayList<>();
		for (int i = 0; i < appUserReportCompleteRel.size(); i++) {
			if (appUserReportCompleteRel.get(i).getIsComplete()==1) {
				appUserReportCompleteRelSave.add(appUserReportCompleteRel.get(i));
			}else {
				AppUserReportCompleteRelDele.add(appUserReportCompleteRel.get(i));
			}
		}
		appUserReportCompletesRelRepo.save(appUserReportCompleteRelSave);
		appUserReportCompletesRelRepo.delete(AppUserReportCompleteRelDele);
		return new AppResp( CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "同步用户报表完成信息", notes = "POST")
	@RequestMapping(value = "/synchronousTodos", method = RequestMethod.POST)
	public AppResp synchronousTodos(@RequestBody List<Todo> todos) {
		List<Todo> todoSave = new ArrayList<>(); 
		List<Todo> todoDele = new  ArrayList<>();
		for (int i = 0; i < todos.size(); i++) {
			if (todos.get(i).getDeleteState()==0) {
				todoSave.add(todos.get(i));
			}else {
				todoDele.add(todos.get(i));
			}
		}
		todos = todoRepo.save(todoSave);
		todoRepo.delete(todoDele);
		return new AppResp(0, CodeDef.SUCCESS);
	}
	
	
}
