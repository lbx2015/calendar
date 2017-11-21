package net.riking.web.appInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.riking.service.repo.RemindHisRepo;


/**
 * 提醒历史的增删改查
 * 
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/reminHisApp")
public class ReminHisServer {

	
	@Autowired
	RemindHisRepo remindHisRepo;
	
	/*@ApiOperation(value = "用户提醒历史新增", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public AppResp save(@RequestBody RemindHis remindHis){
		remindHis = remindHisRepo.save(remindHis);
		return new AppResp(remindHis, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "批量删除提醒历史信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public AppResp delMore(@RequestParam("id") String id) {
		remindHisRepo.delete(id);;
			return new AppResp().setCode(CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "app获取提醒历史信息", notes = "POST")
	@RequestMapping(value = "/getAllReport", method = RequestMethod.POST)
	public AppResp getAllReport(@RequestBody RemindHis remindHis) {
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
		return new AppResp(page, CodeDef.SUCCESS);
	}*/
}
