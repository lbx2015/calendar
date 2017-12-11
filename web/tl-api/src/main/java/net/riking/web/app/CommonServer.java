package net.riking.web.app;

import java.util.HashMap;
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
import net.riking.dao.repo.TQuestionRelRepo;
import net.riking.dao.repo.TopicRelRepo;
import net.riking.dao.repo.UserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUserRecommend;
import net.riking.entity.model.AppVersion;
import net.riking.entity.model.Industry;
import net.riking.entity.model.TQuestionRel;
import net.riking.entity.model.TopicRel;
import net.riking.entity.model.UserFollowRel;
import net.riking.entity.params.AppVersionParams;
import net.riking.entity.params.IndustryParams;
import net.riking.entity.params.TQuestionParams;
import net.riking.entity.params.ValidParams;
import net.riking.service.AppUserCommendService;
import net.riking.service.SysDataService;
import net.riking.service.impl.SysDateServiceImpl;
import net.riking.util.RedisUtil;
import net.riking.util.SmsUtil;

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
	UserFollowRelRepo userFollowRelRepo;

	@Autowired
	TQuestionRelRepo tQuestionRelRepo;

	@Autowired
	TopicRelRepo topicRelRepo;

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
		Set<AppUserRecommend> appUserRecommends = appUserCommendServie.findALL();
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
		switch (tQuestionParams.getObjType()) {
			// 问题关注
			case Const.OBJ_TYPE_1:
				if (Const.EFFECTIVE == tQuestionParams.getEnabled()) {
					TQuestionRel rels = tQuestionRelRepo.findByOne(tQuestionParams.getUserId(),
							tQuestionParams.getAttentObjId(), 0);// 0-关注
					if (null == rels) {
						// 如果传过来的参数是关注，保存新的一条关注记录
						TQuestionRel topQuestionRel = new TQuestionRel();
						topQuestionRel.setUserId(tQuestionParams.getUserId());
						topQuestionRel.setTqId(tQuestionParams.getAttentObjId());
						topQuestionRel.setDataType(0);// 关注
						tQuestionRelRepo.save(topQuestionRel);
					}
				} else if (Const.INVALID == tQuestionParams.getEnabled()) {
					// 如果传过来是取消关注，把之前一条记录物理删除
					tQuestionRelRepo.deleteByUIdAndTqId(tQuestionParams.getUserId(), tQuestionParams.getAttentObjId(),
							0);// 0-关注 3-屏蔽
				} else {
					logger.error("参数异常：enabled=" + tQuestionParams.getEnabled());
					return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			// 话题关注
			case Const.OBJ_TYPE_2:
				if (Const.EFFECTIVE == tQuestionParams.getEnabled()) {
					TopicRel rels = topicRelRepo.findByOne(tQuestionParams.getUserId(),
							tQuestionParams.getAttentObjId(), 0);// 0-关注
					if (null == rels) {
						// 如果传过来的参数是关注，保存新的一条关注记录
						TopicRel topicRel = new TopicRel();
						topicRel.setUserId(tQuestionParams.getUserId());
						topicRel.setTopicId(tQuestionParams.getAttentObjId());
						topicRel.setDataType(0);// 关注
						topicRelRepo.save(topicRel);
					}
				} else if (Const.INVALID == tQuestionParams.getEnabled()) {
					// 如果传过来是取消关注，把之前一条记录物理删除
					topicRelRepo.deleteByUIdAndTopId(tQuestionParams.getUserId(), tQuestionParams.getAttentObjId(), 0);// 0-关注3-屏蔽

				} else {
					logger.error("参数异常：enabled=" + tQuestionParams.getEnabled());
					return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			// 用户关注
			case Const.OBJ_TYPE_3:
				if (Const.EFFECTIVE == tQuestionParams.getEnabled()) {
					// 先根据toUserId 去数据库查一次记录，如果有一条点赞记录就新增一条关注记录并关注状态改为：1-互相关注
					UserFollowRel toUserFollowRel = userFollowRelRepo.getByUIdAndToId(tQuestionParams.getAttentObjId(),
							tQuestionParams.getUserId());// 对方的点赞记录
					if (toUserFollowRel != null) {
						UserFollowRel rels = userFollowRelRepo.getByUIdAndToId(tQuestionParams.getUserId(),
								tQuestionParams.getAttentObjId());
						if (null == rels) {
							// 更新对方关注表，互相关注
							userFollowRelRepo.updFollowStatus(toUserFollowRel.getUserId(),
									toUserFollowRel.getToUserId(), 1);// 1-互相关注
							// 如果传过来的参数是关注，保存新的一条关注记录
							UserFollowRel userFollowRel = new UserFollowRel();
							userFollowRel.setUserId(tQuestionParams.getUserId());
							userFollowRel.setToUserId(tQuestionParams.getAttentObjId());
							userFollowRel.setFollowStatus(1);// 互相关注
							userFollowRelRepo.save(userFollowRel);
						}
					} else {
						// 如果传过来的参数是关注，保存新的一条关注记录
						UserFollowRel userFollowRel = new UserFollowRel();
						userFollowRel.setUserId(tQuestionParams.getUserId());
						userFollowRel.setToUserId(tQuestionParams.getAttentObjId());
						userFollowRel.setFollowStatus(0);// 非互相关注
						userFollowRelRepo.save(userFollowRel);
					}
				} else if (Const.INVALID == tQuestionParams.getEnabled()) {
					UserFollowRel toUserFollowRel = userFollowRelRepo.getByUIdAndToId(tQuestionParams.getAttentObjId(),
							tQuestionParams.getUserId());// 对方的点赞记录
					if (null != toUserFollowRel) {
						userFollowRelRepo.updFollowStatus(tQuestionParams.getUserId(), tQuestionParams.getAttentObjId(),
								0);// 0-非互相关注
					}
					// 如果传过来是取消关注，把之前一条记录物理删除
					userFollowRelRepo.deleteByUIdAndToId(tQuestionParams.getUserId(), tQuestionParams.getAttentObjId());
				} else {
					logger.error("参数异常：enabled=" + tQuestionParams.getEnabled());
					return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
				}
				break;
			default:
				logger.error("参数异常：objType=" + tQuestionParams.getObjType());
				return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}

		return new AppResp("", CodeDef.SUCCESS);
	}

}
