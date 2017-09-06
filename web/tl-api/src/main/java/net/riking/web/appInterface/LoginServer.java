package net.riking.web.appInterface;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.exceptions.ClientException;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.AliSme;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserReportRel;
import net.riking.service.SysDataService;
import net.riking.service.repo.AppUserRepo;
import net.riking.service.repo.AppUserReportRelRepo;
import net.riking.service.repo.ReportListRepo;
import net.riking.util.SmsUtil;

/**
 * 用户登录操作
 * 
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
	SysDataService sysDataService;
	
	@Autowired
	ReportListRepo reportListRepo;
	
	@Autowired
	AppUserReportRelRepo appUserReportRelRepo;

	@Autowired
	SmsUtil smsUtil;

	@ApiOperation(value = "用户登录", notes = "POST")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public AppResp login_(@RequestBody AppUser appUser, HttpSession session) {
		if (StringUtils.isBlank(appUser.getDeleteState())) {
			appUser.setDeleteState("1");
		}
		if (StringUtils.isBlank(appUser.getEnabled())) {
			appUser.setEnabled("1");
		}
		Example<AppUser> example = Example.of(appUser,
				ExampleMatcher.matchingAll());
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
	public AppResp getValiCode_(@RequestBody AppUser appUser)
			throws ClientException {
		// user = appUser;
		String valiCode = "";
		for (int i = 0; i < 6; i++) {
			valiCode += (int) (Math.random() * 9);
		}
		AliSme aliSms = new AliSme(appUser.getTelephone(), "悦历", "SMS_85110022",
				valiCode);
		smsUtil.sendSms(aliSms);
		appUser.setValiCode(valiCode);
		sysDataService.setAppUser(appUser);
		// logger.info("手机{}获取验证码成功",appUser.getPhoneSeqNum());
		return new AppResp(appUser, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "校验验证码", notes = "POST")
	@RequestMapping(value = "/checkValiCode", method = RequestMethod.POST)
	public AppResp checkValiCode_(@RequestBody AppUser appUser,
			HttpSession session) {
		AppUser user = sysDataService.getAppUser(appUser);
		if (user == null) {
			return new AppResp(user, CodeDef.EMP.CHECK_CODE_TIME_OUT);
		}
		List<AppUser> list;
		AppUser appUser2 = null ;
		if (appUser.getValiCode().equals(user.getValiCode())) {
			
			list = appUserRepo.findByDeleteStateAndTelephone("1",appUser.getTelephone());
			if (list.size()>0) {
				appUser2=list.get(0);
			}
			if (appUser2==null) {
				AppUser appUser3 = new AppUser(appUser.getTelephone(),
						appUser.getTelephone(), appUser.getTelephone().substring(5), user.getPhoneSeqNum(),
						"1", "1","0800");
				appUser2 = appUserRepo.save(appUser3);
				List<AppUserReportRel> appUserReportRels = reportListRepo.findAllId();
				for (AppUserReportRel appUserReportRel : appUserReportRels) {
					appUserReportRel.setAppUserId(appUser2.getId());
				}
				appUserReportRelRepo.save(appUserReportRels);
				logger.info("{}注册成功", appUser.getName());
			}
			sysDataService.delAppUser(user);
		}else{
			return new AppResp(appUser2, CodeDef.EMP.CHECK_CODE_ERR);
		}
		session.setAttribute("currentUser", appUser2);
		return new AppResp(appUser2, CodeDef.SUCCESS);
	}

}
