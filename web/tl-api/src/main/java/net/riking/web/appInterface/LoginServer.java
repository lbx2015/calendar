package net.riking.web.appInterface;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.Jdpush;
import net.riking.service.SysDataService;
import net.riking.service.repo.AppUserRepo;
import net.riking.util.JdpushUtil;
/**
 * 用户登录操作
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:32:04
 * @used TODO
 */
@RestController
public class LoginServer {
	
	private static final Logger logger = LogManager.getLogger("LoginServer");
	@Autowired
	AppUserRepo appUserRepo;
	
	@Autowired
	SysDataService SysDataService;

	@ApiOperation(value = "用户登录", notes = "POST")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public AppResp login_(@RequestBody AppUser appUser, HttpSession session) {
		Example<AppUser> example = Example.of(appUser,ExampleMatcher.matchingAll());
		AppUser appUser2 = appUserRepo.findOne(example);
		if (appUser2 != null) {
			session.setAttribute("currentUser", appUser2);
			return new AppResp(appUser2, CodeDef.SUCCESS);
		} else {
			return new AppResp(CodeDef.ERROR);
		}
	}

	@ApiOperation(value = "发送验证码", notes = "POST")
	@RequestMapping(value = "/getValiCode", method = RequestMethod.POST)
	public AppResp getValiCode_(@RequestBody AppUser appUser) {
		//user = appUser;
		JdpushUtil jdpushUtil = new JdpushUtil();
		String valiCode = "";
		for(int i = 0;i<6;i++){
			valiCode +=(int)(Math.random()*9);
		}
		
		String NotificationTitle ="您的一次性认证码为"+valiCode+"，您在登录金融台历手机客户端，切勿将短信内容告诉他人" ;
		Jdpush jdpush = new Jdpush(NotificationTitle,"","","18171adc0338eaeaf9e","");
		jdpushUtil.sendToRegistrationId(jdpush);
		appUser.setValiCode(valiCode);
		SysDataService.setAppUser(appUser);
		//logger.info("手机{}获取验证码成功",appUser.getPhoneSeqNum());
		return new AppResp(appUser, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "校验验证码", notes = "POST")
	@RequestMapping(value = "/checkValiCode", method = RequestMethod.POST)
	public AppResp checkValiCode_(@RequestBody AppUser appUser, HttpSession session) {
		AppUser user = SysDataService.getAppUser(appUser);
		if(user==null){
			return new AppResp(user, CodeDef.SUCCESS);
		}
		AppUser appUser2 = null;
		if (appUser.getValiCode().equals(user.getValiCode())) {
			Example<AppUser> example = Example.of(appUser,ExampleMatcher.matchingAll());
			appUser2 = appUserRepo.findOne(example);
			if (appUser2 == null) {
				AppUser appUser3 = new AppUser(appUser.getTelephone(), "", 0, "", "", appUser.getTelephone(), "", "123456", "", "", "1", "", "");
				appUser2 = appUserRepo.save(appUser3);
				logger.info("{}注册成功",appUser.getName());
			}
		}
		if(appUser2!=null){
			SysDataService.delAppUser(user);
		}
		session.setAttribute("currentUser", appUser2);
		return new AppResp(appUser2, CodeDef.SUCCESS);
	}

}
