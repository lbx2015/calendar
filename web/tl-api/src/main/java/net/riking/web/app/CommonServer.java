package net.riking.web.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.util.TextUtils;
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
import net.riking.config.Const;
import net.riking.core.annos.AuthPass;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.dao.repo.AppVersionRepo;
import net.riking.dao.repo.IndustryRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUserRecommend;
import net.riking.entity.model.AppVersion;
import net.riking.entity.model.Industry;
import net.riking.entity.params.AppVersionParams;
import net.riking.entity.params.IndustryParams;
import net.riking.entity.params.ValidParams;
import net.riking.service.AppUserCommendService;
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
	IndustryRepo industryRepo;

	@Autowired
	AppVersionRepo appVersionRepo;

	@Autowired
	SmsUtil smsUtil;

	@Autowired
	AppUserCommendService appUserCommendServie;
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
		AppVersion appVersion = appVersionRepo.hasUpdateAppVersion(appVersionParams.getVersionNo(),
				appVersionParams.getClientType());
		Map<String, Object> result = Utils.objProps2Map(appVersion, true);
		return new AppResp(result, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "发送验证码", notes = "POST")
	@RequestMapping(value = "/getValidCode", method = RequestMethod.POST)
	public AppResp getValidCode_(@RequestBody Map<String, Object> params) throws ClientException {
		// user = appUser;
		ValidParams validParams = Utils.map2Obj(params, ValidParams.class);
		String phone = validParams.getPhone();
		if (null == phone) {
			return new AppResp(CodeDef.EMP.PHONE_NULL_ERROR, CodeDef.EMP.PHONE_NULL_ERROR_DESC);
		} else if (!isMobileNO(phone)) {
			return new AppResp(CodeDef.EMP.PHONE_FORM_ERROR, CodeDef.EMP.PHONE_FORM_ERROR_DESC);
		} else {
			String verifyCode = "";
			for (int i = 0; i < 6; i++) {
				verifyCode += (int) (Math.random() * 9);
			}
			SendSmsResponse sendSmsResponse = smsUtil.sendSms(phone, verifyCode);
			if (!sendSmsResponse.getCode().equals("OK")) {
				return new AppResp(CodeDef.EMP.SMS_SEND_ERROR, sendSmsResponse.getMessage());
			}
			logger.info("手机{}获取验证码成功", verifyCode);
			RedisUtil.getInstall().setObject(Const.VALID_ + phone.trim(), Const.VALID_CODE_TIME, verifyCode);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("verifyCode", verifyCode);
			return new AppResp(result, CodeDef.SUCCESS);
		}
	}

	@ApiOperation(value = "得到<所有>邮箱后缀", notes = "POST")
	@RequestMapping(value = "/getAllEmailSuffix", method = RequestMethod.POST)
	public AppResp getAllEmailSuffix_() {
		List<ModelPropDict> list = sysDataservice.getDicts("T_APP_USER", "EMAILSUFFIX");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("emailSuffix", list);
		return new AppResp(result, CodeDef.SUCCESS);
	}

	/**
	 * 判断手机格式是否正确
	 *
	 * @param mobiles
	 * @return 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	 *         联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
	 *         总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
	 */
	public static boolean isMobileNO(String mobiles) {
		String phone = mobiles.replaceAll("[^\\d]", "");
		// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		String telRegex = "[1][34578]\\d{9}";
		if (TextUtils.isEmpty(phone))
			return false;
		else
			return phone.trim().matches(telRegex);
	}

	@AuthPass
	@ApiOperation(value = "获取行业列表", notes = "POST")
	@RequestMapping(value = "/findIndustry", method = RequestMethod.POST)
	public AppResp findIndustry() {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		List<Industry> list = industryRepo.findIndustry(0);// 查询行业
		for (Industry industry : list) {
			Map<String, Object> map = Utils.objProps2Map(industry, true);
			maps.add(map);
		}
		return new AppResp(maps, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取行业下面的职位列表", notes = "POST")
	@RequestMapping(value = "/getPositionByIndustry", method = RequestMethod.POST)
	public AppResp getPositionByIndustry(@RequestBody Map<String, Object> params) {
		IndustryParams industryParams = Utils.map2Obj(params, IndustryParams.class);
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		List<Industry> list = industryRepo.findPositionByIndustry(industryParams.getIndustryId());
		if (null == list) {
			return new AppResp(CodeDef.EMP.DATA_NOT_FOUND, CodeDef.EMP.DATA_NOT_FOUND_DESC);
		}
		for (Industry industry : list) {
			Map<String, Object> map = Utils.objProps2Map(industry, true);
			maps.add(map);
		}
		return new AppResp(maps, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取推荐报表", notes = "POST")
	@RequestMapping(value = "/getCommend", method = RequestMethod.POST)
	public AppResp getCommend() {
		Set<AppUserRecommend> appUserRecommends = appUserCommendServie.findALL();
		Set<Map<String, Object>> sets = new HashSet<Map<String, Object>>();
		for (AppUserRecommend appUserRecommend : appUserRecommends) {
			Map<String, Object> map = Utils.objProps2Map(appUserRecommend, true);
			sets.add(map);
		}
		return new AppResp(sets, CodeDef.SUCCESS);
	}
}
