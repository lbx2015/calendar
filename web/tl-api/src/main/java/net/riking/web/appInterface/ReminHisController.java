package net.riking.web.appInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.entity.model.Remind;
import net.riking.entity.model.RemindHis;
import net.riking.entity.model.ReportList;
import net.riking.service.repo.RemindHisRepo;
import net.riking.service.repo.RemindRepo;


/**
 * 提醒历史的增删改查
 * 
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/reminHisApp")
public class ReminHisController {

	
	@Autowired
	RemindHisRepo remindHisRepo;
	
	@ApiOperation(value = "用户提醒历史新增/修改", notes = "POST")
	@RequestMapping(value = "/save_", method = RequestMethod.POST)
	public Resp save_(@ModelAttribute RemindHis remindHis){
		remindHis = remindHisRepo.save(remindHis);
		return new Resp(remindHis, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "批量删除提醒历史信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore_(@RequestBody String id) {
		remindHisRepo.delete(id);;
			return new Resp().setCode(CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "app获取提醒历史信息", notes = "POST")
	@RequestMapping(value = "/getAllReport_", method = RequestMethod.POST)
	public Resp getAllReport_(@ModelAttribute RemindHis remindHis) {
		PageRequest pageable = new PageRequest(remindHis.getPcount(), remindHis.getPindex(), null);
		Example<RemindHis> example = Example.of(remindHis, ExampleMatcher.matchingAll());
		Page<RemindHis> page = remindHisRepo.findAll(example, pageable);
		List<RemindHis> remindHises = page.getContent();
		Map<String, List<RemindHis>> map = new HashMap<>();
		for (int i = 0; i < remindHises.size(); i++) {
			String date  = remindHises.get(i).getStrDate();
			  if(map.containsKey(date)){
				     map.get(date).add(remindHises.get(i));
				 }else{
					 List<RemindHis> remindHiss = new ArrayList<>();
					 remindHiss.add(remindHises.get(i));
				  map.put(date,remindHiss);
				}
		}
		return new Resp(page, CodeDef.SUCCESS);
	}
}
