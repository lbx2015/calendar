package net.riking.web.app;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.dao.repo.AppUserRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.params.LoginParams;
import net.riking.service.AppUserService;
import net.riking.service.SysDataService;
import net.riking.util.SmsUtil;
import net.riking.util.Utils;

/**
 * 用户登录注册接口
 * 
 * @author james.you
 * @version crateTime：2017年11月29日 下午1:55:35
 * @used TODO
 */
@RestController
public class LoginServer {

	private static final Logger logger = LogManager.getLogger("LoginServer");

	@Autowired
	AppUserService appUserService;

	@Autowired
	SysDataService sysDataService;
	
	@Autowired
	SmsUtil smsUtil;

	/*
	 * @Autowired ReportListRepo reportListRepo;
	 * 
	 * @Autowired AppUserReportRelRepo appUserReportRelRepo;
	 */

	

	@ApiOperation(value = "用户登录及注册", notes = "POST")
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public AppResp login_(@RequestBody Map<String, Object> params) {
		
		LoginParams loginParams = Utils.map2Obj(params, LoginParams.class);
		
		AppUser user = null;
		switch (loginParams.getType().intValue()) {
			case Const.LOGIN_REGIST_TYPE_PHONE:
				if(StringUtils.isBlank(loginParams.getPhone())){
					return new AppResp(CodeDef.EMP.PHONE_NULL_ERROR, CodeDef.EMP.PHONE_NULL_ERROR_DESC);
				}
				user = appUserService.findByPhone(loginParams.getPhone());
				break;
			case Const.LOGIN_REGIST_TYPE_WECHAT:
				if(StringUtils.isBlank(loginParams.getVerifyCode())){
					return new AppResp(CodeDef.EMP.VERIFYCODE_NULL_ERROR, CodeDef.EMP.VERIFYCODE_NULL_ERROR_DESC);
				}
				user = appUserService.findByOpenId(loginParams.getOpenId());
				break;
			default:
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		
		if(user != null){
			//判断禁用用户给提示
			if(user.getEnabled().intValue() == Const.INVALID){
				return new AppResp(CodeDef.EMP.USER_INVALID_ERROR, CodeDef.EMP.USER_INVALID_ERROR_DESC);
			}
			
			return new AppResp();
		}
		
		boolean isRn = smsUtil.checkValidCode(loginParams.getPhone(), loginParams.getVerifyCode());
		if(!isRn){
			return new AppResp(CodeDef.EMP.CHECK_CODE_ERR, CodeDef.EMP.CHECK_CODE_ERR_DESC);
		}
		
		return null;
		
//		if (StringUtils.isBlank(appUser.getDeleteState())) {
//			appUser.setDeleteState("1");
//		}
//		if (StringUtils.isBlank(appUser.getEnabled())) {
//			appUser.setEnabled("1");
//		}
//		Example<AppUser> example = Example.of(appUser, ExampleMatcher.matchingAll());
//		AppUser appUser2 = appUserRepo.findOne(example);
//		if (appUser2 != null) {
//			session.setAttribute("currentUser", appUser2);
//			return new AppResp(appUser2, CodeDef.SUCCESS);
//		}
//		List<AppUser> list = appUserRepo.findByDeleteStateAndTelephone("1", appUser.getPhone());
//		if (null != list && list.size() > 0) {
//			return new AppResp(CodeDef.EMP.USER_PASS_ERR);
//		}
//		return new AppResp(CodeDef.EMP.DATA_NOT_FOUND);

	}

	// TODO 暂时注释
	// @ApiOperation(value = "根据openId查询用户信息", notes = "POST")
	// @RequestMapping(value = "/getUserByOpenId", method = RequestMethod.POST)
	// public AppResp getUserByOpenId(@RequestBody String openId, HttpSession
	// session) {
	// AppUser user = appUserRepo.findByOpenId(openId);
	// if ("0".equals(user.getDeleteState()) || "0".equals(user.getEnabled())) {
	// return new AppResp(user, CodeDef.ERROR);// 被删除或者禁用
	// } else {
	// return new AppResp(user, CodeDef.SUCCESS);// 不管用户是否存在 都直接返回
	// }
	// }

	// TODO 暂时注释
	// @ApiOperation(value = "校验验证码", notes = "POST")
	// @RequestMapping(value = "/checkValiCode", method = RequestMethod.POST)
	// public AppResp checkValiCode_(@RequestBody AppUser appUser, HttpSession
	// session) {
	// AppUser user = sysDataService.getAppUser(appUser);
	// if (user == null) {
	// return new AppResp(user, CodeDef.EMP.CHECK_CODE_TIME_OUT);
	// }
	// List<AppUser> list;
	// AppUser appUser2 = null;
	// if (appUser.getValiCode().equals(user.getValiCode())) {
	//
	// list = appUserRepo.findByDeleteStateAndTelephone("1",
	// appUser.getPhone());
	// if (list.size() > 0) {
	// appUser2 = list.get(0);
	// }
	// if (appUser2 == null) {
	// AppUser appUser3 = new AppUser(appUser.getPhone(), appUser.getPhone(),
	// appUser.getPhone().substring(5),
	// user.getPhoneMacid(), "1", "1", "0800", user.getOpenId(),
	// user.getIsSubscribe(),
	// user.getIsGuide());
	// appUser2 = appUserRepo.save(appUser3);
	// // List<AppUserReportRel> appUserReportRels = reportListRepo.findAllId();
	// // for (AppUserReportRel appUserReportRel : appUserReportRels) {
	// // appUserReportRel.setAppUserId(appUser2.getId());
	// // }
	// // appUserReportRelRepo.save(appUserReportRels);
	// logger.info("{}注册成功", appUser.getUserName());
	// }
	// sysDataService.delAppUser(user);
	// } else {
	// return new AppResp(appUser2, CodeDef.EMP.CHECK_CODE_ERR);
	// }
	// session.setAttribute("currentUser", appUser2);
	// return new AppResp(appUser2, CodeDef.SUCCESS);
	// }

}
