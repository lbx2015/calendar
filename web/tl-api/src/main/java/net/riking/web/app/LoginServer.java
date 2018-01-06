package net.riking.web.app;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import net.riking.core.entity.model.ModelPropDict;
import net.riking.dao.repo.IndustryRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.Jdpush;
import net.riking.entity.model.MQOptCommon;
import net.riking.entity.params.LoginParams;
import net.riking.entity.resp.AppUserResp;
import net.riking.service.AppUserService;
import net.riking.service.SysDataService;
import net.riking.util.DateUtils;
import net.riking.util.JdpushUtil;
import net.riking.util.MQProduceUtil;
import net.riking.util.SmsUtil;
import net.sf.json.JSONObject;

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
	HttpServletRequest request;

	@Autowired
	SysDataService sysDataService;

	@Autowired
	SmsUtil smsUtil;

	@Autowired
	IndustryRepo industryRepo;

	/*
	 * @Autowired ReportListRepo reportListRepo;
	 * 
	 * @Autowired AppUserReportRelRepo appUserReportRelRepo;
	 */

	@ApiOperation(value = "用户登录及注册", notes = "POST")
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public AppResp login_(@RequestBody LoginParams loginParams) {

		AppUser user = null;
		AppUserDetail detail = null;
		switch (loginParams.getType().intValue()) {
			case Const.LOGIN_REGIST_TYPE_PHONE:
				if (StringUtils.isBlank(loginParams.getPhone())) {// 手机号登录或注册
					return new AppResp(CodeDef.EMP.PHONE_NULL_ERROR, CodeDef.EMP.PHONE_NULL_ERROR_DESC);
				}
				if (StringUtils.isBlank(loginParams.getVerifyCode())) {
					return new AppResp(CodeDef.EMP.VERIFYCODE_NULL_ERROR, CodeDef.EMP.VERIFYCODE_NULL_ERROR_DESC);
				}

				try {
					boolean isRn = smsUtil.checkValidCode(loginParams.getPhone(), loginParams.getVerifyCode());
					if (!isRn) {
						return new AppResp(CodeDef.EMP.CHECK_CODE_ERR, CodeDef.EMP.CHECK_CODE_ERR_DESC);
					}
				} catch (Exception e) {
					// TODO: handle exception
					if (e.getMessage().equals(CodeDef.EMP.CHECK_CODE_TIME_OUT + "")) {
						return new AppResp(CodeDef.EMP.CHECK_CODE_TIME_OUT, CodeDef.EMP.CHECK_CODE_TIME_OUT_DESC);
					} else {
						return new AppResp(CodeDef.EMP.GENERAL_ERR, CodeDef.EMP.GENERAL_ERR_DESC);
					}

				}

				user = appUserService.findByPhone(loginParams.getPhone());
				break;
			case Const.LOGIN_REGIST_TYPE_WECHAT:// 微信登录或注册
				if (StringUtils.isBlank(loginParams.getVerifyCode())) {
					return new AppResp(CodeDef.EMP.VERIFYCODE_NULL_ERROR, CodeDef.EMP.VERIFYCODE_NULL_ERROR_DESC);
				}

				try {
					boolean isRn = smsUtil.checkValidCode(loginParams.getPhone(), loginParams.getVerifyCode());
					if (!isRn) {
						return new AppResp(CodeDef.EMP.CHECK_CODE_ERR, CodeDef.EMP.CHECK_CODE_ERR_DESC);
					}
				} catch (Exception e) {
					// TODO: handle exception
					if (e.getMessage().equals(CodeDef.EMP.CHECK_CODE_TIME_OUT + "")) {
						return new AppResp(CodeDef.EMP.CHECK_CODE_TIME_OUT, CodeDef.EMP.CHECK_CODE_TIME_OUT_DESC);
					} else {
						return new AppResp(CodeDef.EMP.GENERAL_ERR, CodeDef.EMP.GENERAL_ERR_DESC);
					}

				}

				user = appUserService.findByOpenId(loginParams.getOpenId());
				break;
			default:
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}

		if (user != null) {
			/** 有用户数据,登录步骤 */
			// 判断禁用用户给提示
			if (user.getEnabled().intValue() == Const.INVALID) {
				return new AppResp(CodeDef.EMP.USER_INVALID_ERROR, CodeDef.EMP.USER_INVALID_ERROR_DESC);
			}

			detail = appUserService.findDetailByOne(user.getId());
			if (null == detail) {
				detail = new AppUserDetail();
			}
			
			
			if(StringUtils.isNotBlank(loginParams.getPhoneDeviceId())){
				if(StringUtils.isNotBlank(detail.getPhoneDeviceId())  && !detail.getPhoneDeviceId().trim().equals(loginParams.getPhoneDeviceId().trim())){
					//换设备号登录，极光推送
					Jdpush jdpush = new Jdpush();
					jdpush = new Jdpush();
					jdpush.setNotificationTitle(Const.SYS_NAME_FLAG + "账号异地登录");
					jdpush.setMsgTitle("");
					//你的帐号于2018-01-06 14:30在iPhone/Android/其它设备上通过验证码登录。
					String msgContent = "你的帐号于" + DateUtils.getDate("yyyy-MM-dd HH:mm") + "在" + loginParams.getClientTypeName() + "设备上通过验证码登录。";
					jdpush.setMsgContent(msgContent);
					jdpush.setExtrasparam("");
					jdpush.setRegisrationId(detail.getPhoneDeviceId().trim());
					JdpushUtil.sendToRegistrationId(jdpush);
				}
				//更新设备号
				appUserService.updatePhoneDeviceid(user.getId(), loginParams.getPhoneDeviceId().trim());
				detail.setPhoneDeviceId(loginParams.getPhoneDeviceId().trim());
			}
			
			user.setDetail(detail);
			logger.info("用户登录成功：phone={}", user.getPhone());
		} else {
			// 注册步骤
			user = new AppUser();
			user.setPhone(loginParams.getPhone());
			user.setUserName(loginParams.getPhone());
			user.setOpenId(loginParams.getOpenId());

			detail = new AppUserDetail();
			detail.setPhoneDeviceId(loginParams.getPhoneDeviceId());
			detail.setPhoneType(loginParams.getClientType());
			user = appUserService.register(user, detail);
			
			logger.info("用户注册成功：phone={}", user.getPhone());
			
			//加入系统消息通知队列
			ModelPropDict dict = sysDataService.getDict(Const.SYS_NOTICE_T_SYS_NOTICE, Const.SYS_NOTICE_USER_REGISTER, String.valueOf(Const.NOTICE_SYS_INFO));
			if(dict != null){
				MQOptCommon common = new MQOptCommon();
				common.setMqOptType(0);
				common.setAttentObjId(user.getId());
				common.setContent(dict.getValu());
				JSONObject jsonArray = JSONObject.fromObject(common);
				MQProduceUtil.sendTextMessage(Const.SYS_INFO_QUEUE, jsonArray.toString());
			}
		}

		AppUserResp userResp = new AppUserResp();
		userResp.setUserId(user.getId());
		userResp.setUserName(user.getUserName());
		userResp.setOpenId(user.getOpenId());
		userResp.setEmail(user.getEmail());
		userResp.setPhone(user.getPhone());
		userResp.setRealName(user.getDetail().getRealName());
		userResp.setCompanyName(user.getDetail().getCompanyName());
		userResp.setSex(user.getDetail().getSex());
		userResp.setBirthday(user.getDetail().getBirthday());
		userResp.setAddress(user.getDetail().getAddress());
		userResp.setDescript(user.getDetail().getDescript());
		userResp.setPhoneDeviceid(user.getDetail().getPhoneDeviceId());
		userResp.setIntegral(user.getDetail().getIntegral());
		userResp.setExperience(user.getDetail().getExperience());
		if (StringUtils.isNotBlank(user.getDetail().getPhotoUrl())) {
			// 截取资源访问路径
			if (null != user.getDetail().getPhotoUrl()) {
				userResp.setPhotoUrl(
						appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + user.getDetail().getPhotoUrl());
			}
		} else {
			userResp.setPhotoUrl("");
		}
		// 等级
		if (null != userResp.getExperience()) {
			userResp.setGrade(appUserService.transformExpToGrade(userResp.getExperience()));
		}
		userResp.setRemindTime(user.getDetail().getRemindTime());
		userResp.setIsSubscribe(user.getDetail().getIsSubscribe());
		userResp.setPositionId(user.getDetail().getPositionId());
		userResp.setIndustryId(user.getDetail().getIndustryId());
		userResp.setIsGuide(user.getDetail().getIsGuide());
		userResp.setIsIdentify(user.getIsIdentified());
		if (StringUtils.isNotBlank(userResp.getPositionId())) {
			userResp.setPositionName(industryRepo.findOne(userResp.getPositionId()).getName());
		}
		if (StringUtils.isNotBlank(userResp.getIndustryId())) {
			userResp.setIndustryName(industryRepo.findOne(userResp.getIndustryId()).getName());
		}
		return new AppResp(userResp, CodeDef.SUCCESS);

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

}
