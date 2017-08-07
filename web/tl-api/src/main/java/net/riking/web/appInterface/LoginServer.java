package net.riking.web.appInterface;

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
/**
 * 
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:32:04
 * @used TODO
 * 用户登录操作
 */
@RestController
public class LoginServer {

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
		if(user==null){
			return new Resp(user, CodeDef.SUCCESS);
		}
		AppUser appUser2 = null;
		if (appUser.getTelephone().equals(user.getTelephone())
				&& appUser.getValiCode().equals(user.getValiCode())) {
			Example<AppUser> example = Example.of(appUser,ExampleMatcher.matchingAll());
			appUser2 = appUserRepo.findOne(example);
			if (appUser2 == null) {
				appUser2 = appUserRepo.save(appUser);
			}
			
		}
		return new Resp(appUser2, CodeDef.SUCCESS);
	}

}
