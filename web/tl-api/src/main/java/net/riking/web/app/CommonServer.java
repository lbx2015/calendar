package net.riking.web.app;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
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
import net.riking.dao.repo.AppUserFollowRelRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.AppVersionRepo;
import net.riking.dao.repo.IndustryRepo;
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppVersion;
import net.riking.entity.model.EmailSuffix;
import net.riking.entity.model.Industry;
import net.riking.entity.model.Recommend;
import net.riking.entity.model.UserFollowRel;
import net.riking.entity.params.AppVersionParams;
import net.riking.entity.params.IndustryParams;
import net.riking.entity.params.TQuestionParams;
import net.riking.entity.params.UserParams;
import net.riking.entity.params.ValidParams;
import net.riking.service.AppUserService;
import net.riking.service.ReCommendService;
import net.riking.service.SysDataService;
import net.riking.service.impl.SysDateServiceImpl;
import net.riking.util.EmailUtil;
import net.riking.util.MQProduceUtil;
import net.riking.util.RedisUtil;
import net.riking.util.SmsUtil;
import net.sf.json.JSONObject;

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
	SysDataService sysDataService;

	@Autowired
	IndustryRepo industryRepo;

	@Autowired
	AppVersionRepo appVersionRepo;

	@Autowired
	SmsUtil smsUtil;

	@Autowired
	AppUserFollowRelRepo userFollowRelRepo;

	@Autowired
	TQuestionRelRepo tQuestionRelRepo;

	@Autowired
	TopicRelRepo topicRelRepo;

	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	AppUserService appUserService;

	@Autowired
	ReCommendService appUserReCommendServie;
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
	public AppResp getAppVersion(@RequestBody AppVersionParams appVersionParams) {
		AppVersion appVersion = appVersionRepo.hasUpdateAppVersion(appVersionParams.getVersionNo(),
				appVersionParams.getClientType());
		return new AppResp(appVersion, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "发送验证码", notes = "POST")
	@RequestMapping(value = "/getValidCode", method = RequestMethod.POST)
	public AppResp getValidCode_(@RequestBody ValidParams validParams) throws ClientException {
		// user = appUser;
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
		List<EmailSuffix> list = sysDataService.getEmailSuffix(EmailSuffix.class.getName().toUpperCase());
		List<String> emailSuffixs = new ArrayList<String>();
		for (EmailSuffix emailSuffix : list) {
			emailSuffixs.add("@" + emailSuffix.getEmailSuffix());
		}
		return new AppResp(emailSuffixs, CodeDef.SUCCESS);
	}

	/**
	 * 
	 * @param userId,email
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "发送邮箱认证", notes = "POST")
	@RequestMapping(value = "/sendEmailVerifyCode", method = RequestMethod.POST)
	public AppResp sendEmailVerifyCode_(@RequestBody UserParams userParams) throws ParseException {
		if (null == userParams) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (null == userParams.getUserId()) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		AppUser appUser = new AppUser();
		appUser = appUserRepo.findOne(userParams.getUserId());
		appUser.setEmail(userParams.getEmail());

		if (StringUtils.isNotBlank(appUser.getEmail())) {
			String verifyCode = "";
			for (int i = 0; i < 6; i++) {
				verifyCode += (int) (Math.random() * 9);
			}
			logger.info("邮箱认证{}获取验证码成功", verifyCode);
			RedisUtil.getInstall().setObject(Const.VALID_ + appUser.getEmail().trim(), Const.VALID_CODE_TIME,
					verifyCode);
			try {
				Email email = sysDataService.getEmail("【悦历】邮箱认证");
				String msg = "您的邮箱认证验证码是" + verifyCode + "，在5分钟内有效。如非本人操作请忽略此验证码。";
				email.setMsg(msg);
				email.addTo(appUser.getEmail());
				// 发送邮箱
				email.send();
				logger.info("收件人：{}，内容：{}", appUser.getEmail(), msg);
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("verifyCode", verifyCode);
			} catch (Exception e) {
				logger.error("邮件发送失败" + e);
				return new AppResp(CodeDef.EMP.EMAIL_ERROR, CodeDef.EMP.EMAIL_ERROR_DESC);
			}
			return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
		} else {
			return new AppResp(CodeDef.EMP.EMAIL_ERROR, CodeDef.EMP.EMAIL_ERROR_DESC);
		}
	}

	/**
	 * 
	 * @param userId verifyCode,email
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "邮箱验证码验证", notes = "POST")
	@RequestMapping(value = "/emailIdentify", method = RequestMethod.POST)
	public AppResp emailIdentify_(@RequestBody UserParams userParams) throws ParseException {
		if (null == userParams) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (null == userParams.getUserId()) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (null == userParams.getEmail()) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		try {
			boolean isRn = smsUtil.checkValidCode(userParams.getEmail(), userParams.getVerifyCode());
			if (!isRn) {
				return new AppResp(CodeDef.EMP.CHECK_CODE_ERR, CodeDef.EMP.CHECK_CODE_ERR_DESC);
			} else {
				appUserRepo.updEmailIndentify(userParams.getUserId(), userParams.getEmail());
				return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
			}
		} catch (Exception e) {
			// TODO: handle exception
			if (e.getMessage().equals(CodeDef.EMP.CHECK_CODE_TIME_OUT + "")) {
				return new AppResp(CodeDef.EMP.CHECK_CODE_TIME_OUT, CodeDef.EMP.CHECK_CODE_TIME_OUT_DESC);
			} else {
				return new AppResp(CodeDef.EMP.GENERAL_ERR, CodeDef.EMP.GENERAL_ERR_DESC);
			}

		}

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
		List<Industry> list = industryRepo.findIndustry(0);// 查询行业
		return new AppResp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取行业下面的职位列表", notes = "POST")
	@RequestMapping(value = "/getPositionByIndustry", method = RequestMethod.POST)
	public AppResp getPositionByIndustry(@RequestBody IndustryParams industryParams) {
		List<Industry> list = industryRepo.findPositionByIndustry(industryParams.getIndustryId());
		if (null == list) {
			return new AppResp(CodeDef.EMP.DATA_NOT_FOUND, CodeDef.EMP.DATA_NOT_FOUND_DESC);
		}
		return new AppResp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取推荐报表", notes = "POST")
	@RequestMapping(value = "/getRecommendReport", method = RequestMethod.POST)
	public AppResp getCommend() {
		Set<Recommend> appUserRecommends = appUserReCommendServie.findALL();
		return new AppResp(appUserRecommends, CodeDef.SUCCESS);
	}

	/**
	 * 问题，话题，用户的关注[userId,objType(1-问题；2-话题；3-用户),attentObjId（关注类型ID）,enabled（1-关注；0-取消）]
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "问题，话题，用户的关注", notes = "POST")
	@RequestMapping(value = "/follow", method = RequestMethod.POST)
	public AppResp follow_(@RequestBody TQuestionParams tQuestionParams) {
		tQuestionParams.setMqOptType(Const.MQ_OPT_FOLLOW);
		JSONObject jsonArray = JSONObject.fromObject(tQuestionParams);
		MQProduceUtil.sendTextMessage(Const.SYS_OPT_QUEUE, jsonArray.toString());
		// 实时返回关注用户状态，具体操作放在mq里面操作
		switch (tQuestionParams.getObjType()) {
			case Const.OBJ_TYPE_1:
				return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
			case Const.OBJ_TYPE_2:
				return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
			// 用户关注
			case Const.OBJ_TYPE_3:
				UserFollowRel userFollowRel = new UserFollowRel();
				userFollowRel.setFollowStatus(0);//未关注
				if (Const.EFFECTIVE == tQuestionParams.getEnabled()) {
					// 先根据toUserId 去数据库查一次记录，如果有一条点赞记录就新增一条关注记录并关注状态改为：2-互相关注
//					UserFollowRel toUserFollowRel = userFollowRelRepo.getByUIdAndToId(tQuestionParams.getAttentObjId(),
//							tQuestionParams.getUserId());// 对方的点赞记录
//					if (toUserFollowRel != null) {
//						UserFollowRel rels = userFollowRelRepo.getByUIdAndToId(tQuestionParams.getUserId(),
//								tQuestionParams.getAttentObjId());
//						if (null == rels) {
//							// 更新对方关注表，互相关注
//							userFollowRel.setFollowStatus(2);// 互相关注
//						}
//					} else {
//						// 如果传过来的参数是关注，保存新的一条关注记录
//						userFollowRel.setFollowStatus(1);// 非互相关注
//					}
					//查到表示有互相关注
					UserFollowRel toUserFollowRel = userFollowRelRepo.getByUIdOrToId(tQuestionParams.getAttentObjId(),
							tQuestionParams.getUserId());
					if(null ==toUserFollowRel){
						userFollowRel.setFollowStatus(1);// 非互相关注
					}else{
						userFollowRel.setFollowStatus(2);// 互相关注
					}
				}
				return new AppResp(userFollowRel.getFollowStatus(), CodeDef.SUCCESS);
			default:
				logger.error("参数异常：objType=" + tQuestionParams.getObjType());
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}

	}

}
