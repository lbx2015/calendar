package net.riking.web.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.dao.repo.RemindHisRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.RemindHis;

/**
 * 提醒历史的增删改查
 * 
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/remindHis")
public class RemindHisServer {

	@Autowired
	RemindHisRepo remindHisRepo;

	@ApiOperation(value = "用户提醒历史新增", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public AppResp save(@RequestBody RemindHis remindHis) {
		remindHis = remindHisRepo.save(remindHis);
		return new AppResp(remindHis, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "批量删除提醒历史信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public AppResp delMore(@RequestParam("remindHisId") String remindHisId) {
		remindHisRepo.delete(remindHisId);

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
			String date = remindHises.get(i).getStrDate();
			if (map.containsKey(date)) {
				map.get(date).add(remindHises.get(i));
			} else {
				List<RemindHis> remindHiss = new ArrayList<>();
				remindHiss.add(remindHises.get(i));
				map.put(date, remindHiss);
			}
		}
		return new AppResp(page, CodeDef.SUCCESS);
	}
}
