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
import net.riking.entity.model.AppUser;
import net.riking.service.repo.AppUserRepo;

@RestController
@RequestMapping(value = "/appUser")
public class AppUserController {
	@Autowired
	AppUserRepo appUserRepo;
	
	@ApiOperation(value = "得到<单个>用户信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		AppUser appUser = appUserRepo.findOne(id);
		return new Resp(appUser, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到<批量>用户信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute AppUser appUser){
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		if(StringUtils.isEmpty(appUser.getDeleteState())){
			appUser.setDeleteState("1");
		}
		Example<AppUser> example = Example.of(appUser, ExampleMatcher.matchingAll());
		Page<AppUser> page = appUserRepo.findAll(example,pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "添加或者更新用户信息", notes = "POST")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp addOrUpdate_(@RequestBody AppUser appUser) {
		if(StringUtils.isEmpty(appUser.getId())||StringUtils.isEmpty(appUser.getDeleteState())){
			appUser.setDeleteState("1");
		}
		AppUser save = appUserRepo.save(appUser);
		return new Resp(save, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "启用用户信息", notes = "GET")
	@RequestMapping(value = "/enable", method = RequestMethod.GET)
	public Resp enable_(@RequestParam String id) {
		 int rs = appUserRepo.enable(id);
		return new Resp(rs, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "禁用用户信息", notes = "GET")
	@RequestMapping(value = "/unEnable", method = RequestMethod.GET)
	public Resp unEnable_(@RequestParam String id) {
		 int rs = appUserRepo.unEnable(id);
		return new Resp(rs, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "禁用用户信息", notes = "GET")
	@RequestMapping(value = "/passwordReset", method = RequestMethod.GET)
	public Resp passwordReset_(@RequestParam String id) {
		 int rs = appUserRepo.passwordReset(id);
		return new Resp(rs, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "批量删除用户信息", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<String> ids) {
		int rs = 0;
		if(ids.size()>0){
			rs = appUserRepo.deleteByIds(ids);
		}
		if(rs>0){
			return new Resp().setCode(CodeDef.SUCCESS);
		}else{
			return new Resp().setCode(CodeDef.ERROR);
		}
	}
	
}
