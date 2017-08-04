package net.riking.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.entity.model.AppUser;
import net.riking.service.repo.AppUserRepo;

@RestController
public class LoggedController {
	
	@Autowired
	AppUserRepo appUserRepo;
	
	@ApiOperation(value = "用户登录", notes = "POST")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Resp login_(@RequestBody AppUser appUser) {
		Example<AppUser> example = Example.of(appUser, ExampleMatcher.matchingAll());
		AppUser appUser2 = appUserRepo.findOne(example);
		if(appUser2!=null){
			return new Resp(appUser2, CodeDef.SUCCESS);
		}else{
			return new Resp(CodeDef.ERROR);
		}
	}
}
