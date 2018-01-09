package net.riking.web.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.ContactsInviteRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.SignInRepo;
import net.riking.dao.repo.AppUserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserResult;
import net.riking.entity.params.UserParams;
import net.riking.service.AppUserService;
import net.riking.service.SignInService;
import net.riking.service.SysDataService;
import net.riking.util.FileUtils;
import net.riking.util.MQProduceUtil;
import net.sf.json.JSONObject;

/**
 * app用户人脉
 * @author jc.tan 2017年11月30日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/userContacts")
public class AppUserContactsServer {
	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Autowired
	HttpServletRequest request;

	@Autowired
	SysDataService sysDataservice;

	@Autowired
	AppUserService appUserService;

	@Autowired
	SignInRepo signInRepo;

	@Autowired
	QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	AppUserFollowRelRepo userFollowRelRepo;

	@Autowired
	SignInService signInService;

	@Autowired
	ContactsInviteRepo contactsInviteRepo;

	/**
	 * userId
	 * @param userParams
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@ApiOperation(value = "我的同事", notes = "POST")
	@RequestMapping(value = "/colleague", method = RequestMethod.POST)
	public AppResp colleague_(@RequestBody UserParams userParams)
			throws IllegalArgumentException, IllegalAccessException {
		if (StringUtils.isBlank(userParams.getUserId())) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		userParams.setPcount(Const.APP_PAGENO_30);
		if (userParams.getPindex() == null) {
			userParams.setPindex(0);
		}
		if (userParams.getPindex() != 0 && userParams.getPindex() != null) {
			userParams.setPindex(userParams.getPindex() - 1);
		}
		AppUser appUser = appUserRepo.findOne(userParams.getUserId());
		if (appUser.getEmail() == null) {
			return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
		} else {
			List<AppUserResult> appUserResults = appUserRepo.findAllByEmail(
					("@" + appUser.getEmail().split("@")[1]).trim(), userParams.getUserId(),
					new PageRequest(userParams.getPindex(), userParams.getPcount()));
			for (AppUserResult appUserResult : appUserResults) {
				if (null != appUserResult.getPhotoUrl()) {
//					appUserResult.setPhotoUrl(
//							appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + appUserResult.getPhotoUrl());
					appUserResult.setPhotoUrl(
							FileUtils.getPhotoUrl(Const.TL_PHOTO_PATH, this.getClass()) + appUserResult.getPhotoUrl());
					
				}
				// 等级
				if (null != appUserResult.getExperience()) {
					appUserResult.setGrade(appUserService.transformExpToGrade(appUserResult.getExperience()));
				}
			}
			return new AppResp(appUserResults, CodeDef.SUCCESS);
		}
	}

	/**
	 * userId list<String> phones
	 * @param userParams
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@ApiOperation(value = "通讯录", notes = "POST")
	@RequestMapping(value = "/contacts", method = RequestMethod.POST)
	public AppResp contacts_(@RequestBody UserParams userParams)
			throws IllegalArgumentException, IllegalAccessException {
		if (StringUtils.isBlank(userParams.getUserId())) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		// List<String> phones = userParams.getPhones();
		//
		// List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		//
		// List<String> contactsPhones = contactsInviteRepo.findByUserId(userParams.getUserId());
		// for (String phone : phones) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("phone", phone);
		// map.put("invite", 0);// 0-未邀请
		// for (String contactsPhone : contactsPhones) {
		// if (phone.equals(contactsPhone)) {
		// map.put("invite", 1);// 1-已邀请
		// }
		// }
		// list.add(map);
		// }
		List<String> contactsPhones = contactsInviteRepo.findByUserId(userParams.getUserId());
		return new AppResp(contactsPhones, CodeDef.SUCCESS);

	}

	/**
	 * userId phone
	 * @param userParams
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@ApiOperation(value = "通讯录的邀请", notes = "POST")
	@RequestMapping(value = "/contactsInvite", method = RequestMethod.POST)
	public AppResp contactsInvite_(@RequestBody UserParams userParams)
			throws IllegalArgumentException, IllegalAccessException {
		if (StringUtils.isBlank(userParams.getUserId()) || StringUtils.isBlank(userParams.getPhone())) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		userParams.setMqOptType(Const.MQ_OPT_CONTACTS_INVITE);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mqOptType", Const.MQ_OPT_CONTACTS_INVITE);
		map.put("userId", userParams.getUserId());
		map.put("phone", userParams.getPhone());
		JSONObject jsonArray = JSONObject.fromObject(map);
		MQProduceUtil.sendTextMessage(Const.SYS_OPT_QUEUE, jsonArray.toString());

		return new AppResp(Const.EMPTY, CodeDef.SUCCESS);

	}
}
