package net.riking.web.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.dao.repo.RemindHisRepo;
import net.riking.dao.repo.RemindRepo;
import net.riking.dao.repo.ReportRepo;
import net.riking.dao.repo.SysDaysRepo;
import net.riking.dao.repo.TodoRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.Remind;
import net.riking.entity.model.SynResult;
import net.riking.entity.model.SysDays;
import net.riking.entity.model.Todo;
import net.riking.service.ReportSubmitCaliberService;

/**
 * app和服务器的同步信息
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

	// @Autowired
	// AppUserReportCompletRelRepo appUserReportCompletesRelRepo;
	@Autowired
	SysDaysRepo sysDaysRepo;

	@Autowired
	ReportRepo reportListRepo;

	@Autowired
	ReportSubmitCaliberService reportSubmitCaliberService;

	@ApiOperation(value = "同步所有信息", notes = "POST")
	@RequestMapping(value = "/synchronousAll", method = RequestMethod.POST)
	public AppResp synchronousAll(@RequestBody AppUser appUser) {
		List<Remind> reminds = remindRepo.findByUserId(appUser.getId());
		List<Todo> todos = todoRepo.findByUserId(appUser.getId());
		List<SysDays> days = sysDaysRepo.findAll();
		// List<ReportList> reportList = reportListRepo.findAll();//查询出所有的报表 List<QueryReport>
		// List<ReportList> reportList = reportSubmitCaliberService.findAllReport();
		SynResult result = new SynResult(reminds, todos, days);
		return new AppResp(result, CodeDef.SUCCESS);
	}

	/*
	 * @ApiOperation(value = "同步提醒信息", notes = "POST")
	 * 
	 * @RequestMapping(value = "/synchronousRemindToApp", method = RequestMethod.POST) public
	 * AppResp synchronousRemindToApp(@RequestBody AppUser appUser) { List<Remind> reminds =
	 * remindRepo.findByUserId(appUser.getId()); return new AppResp(reminds, CodeDef.SUCCESS); }
	 */

	// @ApiOperation(value = "同步日历信息", notes = "POST")
	// @RequestMapping(value = "/synchronousDate", method = RequestMethod.POST)
	// public AppResp synchronousDate() {
	// List<SysDays> days = sysDaysRepo.findAll();
	// return new AppResp(days, CodeDef.SUCCESS);
	// }

	@ApiOperation(value = "同步app提醒信息", notes = "POST")
	@RequestMapping(value = "/synchronousReminds", method = RequestMethod.POST)
	public AppResp synchronousReminds(@RequestBody List<Remind> reminds) {
		List<Remind> remindSave = new ArrayList<>();
		List<Remind> remindDele = new ArrayList<>();
		for (int i = 0; i < reminds.size(); i++) {
			if (reminds.get(i).getDeleteState() == 0) {
				remindSave.add(reminds.get(i));
			} else {
				remindDele.add(reminds.get(i));
			}
		}
		reminds = remindRepo.save(remindSave);
		remindRepo.delete(remindDele);
		return new AppResp(0, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "同步待办所有信息", notes = "POST")
	@RequestMapping(value = "/synchronousTodos", method = RequestMethod.POST)
	public AppResp synchronousTodos(@RequestBody List<Todo> todos) {
		List<Todo> todoSave = new ArrayList<>();
		List<Todo> todoDele = new ArrayList<>();
		for (int i = 0; i < todos.size(); i++) {
			if (todos.get(i).getDeleteFlag().intValue() == 0) {// 不删除
				todoSave.add(todos.get(i));
			} else {
				todoDele.add(todos.get(i));
			}
		}
		todos = todoRepo.save(todoSave);
		todoRepo.delete(todoDele);
		return new AppResp(0, CodeDef.SUCCESS);
	}

}
