package net.riking.web.appInterface;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.entity.model.Remind;
import net.riking.entity.model.Todo;
import net.riking.service.getDateService;
import net.riking.service.impl.GetDateServiceImpl;
import net.riking.service.repo.RemindRepo;

/**
 * 提醒的增删改查
 * 
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/remindApp")
public class RemindServer {

	@Autowired
	RemindRepo remindRepo;
	
	@Autowired
	GetDateServiceImpl getDateServiceImpl;
	
	@ApiOperation(value = "用户提醒新增/修改", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save(@RequestBody Remind remind){
		remind = remindRepo.save(remind);
		return new Resp(remind, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "批量删除提醒信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestParam("id") String id) {
		remindRepo.delete(id);;
			return new Resp().setCode(CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "获取一天提醒信息", notes = "POST")
	@RequestMapping(value = "/getDay", method = RequestMethod.POST)
	public Resp getDay(@RequestParam("userId") String userId,@RequestParam("date") String date,@RequestParam("currWeek") String currWeek,@RequestParam("repeatFlag") Integer repeatFlag) {
		List<Remind> reminds = remindRepo.findOneDay(userId, date, currWeek, repeatFlag);
			return new Resp(reminds,CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "获取一个月提醒信息", notes = "POST")
	@RequestMapping(value = "/getMonth", method = RequestMethod.POST)
	public Resp getMonth(@RequestParam("userId") String userId,@RequestParam("date") String date) {
		Set<String> data = remindRepo.findMouthByStrDate(userId, date);
		Map<String, Set<String>> map = getDateServiceImpl.getMounthWeek(date);
			return new Resp(data,CodeDef.SUCCESS);
	}

}
