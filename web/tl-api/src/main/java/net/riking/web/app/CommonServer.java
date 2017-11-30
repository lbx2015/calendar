package net.riking.web.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.config.Const;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.dao.repo.AppVersionRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AliSme;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppVersion;
import net.riking.entity.params.AppVersionParams;
import net.riking.service.SysDataService;
import net.riking.service.impl.SysDateServiceImpl;
import net.riking.util.RedisUtil;
import net.riking.util.SmsUtil;
import net.riking.util.Utils;

/**
 * 公共模块
 * 
 * @author james.you
 * @version crateTime：2017年11月28日 上午10:47:36
 * @used TODO
 */
@RestController
@RequestMapping(value = "/common")
public class CommonServer {
	private static final Logger logger = LogManager.getLogger("CommonServer");
	
	@Autowired
	SysDateServiceImpl sysDateService;
	
	@Autowired
	SysDataService sysDataservice;

	@Autowired
	AppVersionRepo appVersionRepo;
	
	@Autowired
	SmsUtil smsUtil;

	/*
	 * @Autowired ReportListRepo reportListRepo;
	 */

	@ApiOperation(value = "获取系统时间", notes = "POST")
	@RequestMapping(value = "/getDate", method = RequestMethod.POST)
	public AppResp getDate_() {
		String date = sysDateService.getDate();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sysDate", date);
		return new AppResp(result, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取系统版本", notes = "POST")
	@RequestMapping(value = "/getAppVersion", method = RequestMethod.POST)
	public AppResp getAppVersion(@RequestBody Map<String, Object> params) {
		AppVersionParams appVersionParams = Utils.map2Obj(params, AppVersionParams.class);
		AppVersion appVersion = appVersionRepo.hasUpdateAppVersion(appVersionParams.getVersionNo(), appVersionParams.getClientType());
		Map<String, Object> result = Utils.objProps2Map(appVersion, true);
		return new AppResp(result, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "发送验证码", notes = "POST")
	@RequestMapping(value = "/getValidCode", method = RequestMethod.POST)
	public AppResp getValidCode_(@RequestBody String phone) throws ClientException {
		// user = appUser;
		String verifyCode = "";
		for (int i = 0; i < 6; i++) {
			verifyCode += (int) (Math.random() * 9);
		}
		SendSmsResponse sendSmsResponse = smsUtil.sendSms(phone, verifyCode);
		if(!sendSmsResponse.getCode().equals("OK")){
			return new AppResp(CodeDef.EMP.SMS_SEND_ERROR, sendSmsResponse.getMessage());
		}
		logger.info("手机{}获取验证码成功", verifyCode);
		RedisUtil.getInstall().setObject(Const.VALID_ + phone.trim(), Const.VALID_CODE_TIME, verifyCode);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("verifyCode", verifyCode);
		return new AppResp(result, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到<所有>邮箱后缀", notes = "POST")
	@RequestMapping(value = "/getAllEmailSuffix", method = RequestMethod.POST)
	public AppResp getAllEmailSuffix_(){
		List<ModelPropDict> list = sysDataservice.getDicts("T_APP_USER", "EMAILSUFFIX");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("emailSuffix", list);
		return new AppResp(result, CodeDef.SUCCESS);
	}
	
	

}
