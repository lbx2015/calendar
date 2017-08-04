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

	private AppUser user;

	@ApiOperation(value = "用户登录", notes = "POST")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Resp login_(@RequestBody AppUser appUser) {
		Example<AppUser> example = Example.of(appUser,
				ExampleMatcher.matchingAll());
		AppUser appUser2 = appUserRepo.findOne(example);
		if (appUser2 != null) {
			return new Resp(appUser2, CodeDef.SUCCESS);
		} else {
			return new Resp(CodeDef.ERROR);
		}
	}

	@ApiOperation(value = "发送验证码", notes = "POST")
	@RequestMapping(value = "/getValiCode", method = RequestMethod.POST)
	public Resp getValiCode_(@RequestBody AppUser appUser) {
		user = appUser;
		user.setValiCode("1234");
		return new Resp();
	}

	@ApiOperation(value = "校验验证码", notes = "POST")
	@RequestMapping(value = "/checkValiCode", method = RequestMethod.POST)
	public Resp checkValiCode_(@RequestBody AppUser appUser) {
		AppUser appUser2 = null;
		if (user.getTelephone().equals(appUser.getTelephone())
				&& user.getValiCode().equals(appUser.getValiCode())) {
			Example<AppUser> example = Example.of(appUser,ExampleMatcher.matchingAll());
			appUser2 = appUserRepo.findOne(example);
			if (appUser2 == null) {
				appUser2 = appUserRepo.save(appUser);
			}
			
		}
		return new Resp(appUser2, CodeDef.SUCCESS);
	}

}
