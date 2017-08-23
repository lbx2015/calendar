package net.riking.web.controller;

import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.entity.PageQuery;
import net.riking.entity.model.ReportSubmitCaliber;
import net.riking.service.repo.ReportSubmitCaliberRepo;

@RestController
@RequestMapping(value = "/reportSubmitCaliber")
public class ReportSubmitCaliberController {
	
	@Autowired
	ReportSubmitCaliberRepo reportSubmitCaliberRepo;
	
	@ApiOperation(value = "得到单个口径配置信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		ReportSubmitCaliber reportSubmitCaliber = reportSubmitCaliberRepo.findOne(id);
		return new Resp(reportSubmitCaliber, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到当前报表所有的口径配置信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute ReportSubmitCaliber reportSubmitCaliber){
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		reportSubmitCaliber.setDeleteState("1");
		Example<ReportSubmitCaliber> example = Example.of(reportSubmitCaliber, ExampleMatcher.matchingAll());
		Page<ReportSubmitCaliber> page = reportSubmitCaliberRepo.findAll(example,pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "添加或者更新口径配置信息", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save_(@RequestBody ReportSubmitCaliber reportSubmitCaliber) {
		if(StringUtils.isEmpty(reportSubmitCaliber.getId())||reportSubmitCaliber.getDeleteState()==null){
			reportSubmitCaliber.setDeleteState("1");
		}
		ReportSubmitCaliber  save = reportSubmitCaliberRepo.save(reportSubmitCaliber);
		return new Resp(save, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "批量删除口径配置信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore_(@RequestBody Set<String> ids) {
		int rs = 0;
		if(ids.size()>0){
			rs = reportSubmitCaliberRepo.deleteByIds(ids);
		}
		if(rs>0){
			return new Resp().setCode(CodeDef.SUCCESS);
		}else{
			return new Resp().setCode(CodeDef.ERROR);
		}
	}
	
	@ApiOperation(value = "启用用户信息", notes = "GET")
	@RequestMapping(value = "/enable", method = RequestMethod.GET)
	public Resp enable_(@RequestParam String id) {
		int rs = reportSubmitCaliberRepo.enable(id);
		return new Resp(rs, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "禁用用户信息", notes = "GET")
	@RequestMapping(value = "/unEnable", method = RequestMethod.GET)
	public Resp unEnable_(@RequestParam String id) {
		int rs = reportSubmitCaliberRepo.unEnable(id);
		return new Resp(rs, CodeDef.SUCCESS);
	}
}