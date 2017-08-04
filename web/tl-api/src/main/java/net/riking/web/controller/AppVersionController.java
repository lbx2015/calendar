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
import net.riking.entity.model.AppVersion;
import net.riking.service.repo.AppVersionRepo;
@RestController
@RequestMapping(value = "/appVersion")
public class AppVersionController {
	@Autowired
	AppVersionRepo appVersionRepo;
	
	@ApiOperation(value = "得到单个Web版本信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		AppVersion appVersion = appVersionRepo.findOne(id);
		return new Resp(appVersion, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到所有的Web版本信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute AppVersion appVersion){
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		if(StringUtils.isEmpty(appVersion.getDeleteState())){
			appVersion.setDeleteState("1");
		}
		Example<AppVersion> example = Example.of(appVersion, ExampleMatcher.matchingAll());
		Page<AppVersion> page = appVersionRepo.findAll(example,pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "添加或者更新Web版本信息", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save_(@RequestBody AppVersion appVersion) {
		if(StringUtils.isEmpty(appVersion.getId())||StringUtils.isEmpty(appVersion.getDeleteState())){
			appVersion.setDeleteState("1");
		}
		AppVersion  save = appVersionRepo.save(appVersion);
		return new Resp(save, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "批量删除用户信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore_(@RequestBody Set<String> ids) {
		int rs = 0;
		if(ids.size()>0){
			rs = appVersionRepo.deleteById(ids);
		}
		if(rs>0){
			return new Resp().setCode(CodeDef.SUCCESS);
		}else{
			return new Resp().setCode(CodeDef.ERROR);
		}
	}
}