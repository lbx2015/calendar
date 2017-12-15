package net.riking.web.app;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.annos.AuthPass;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.SignInRepo;
import net.riking.dao.repo.UserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.SignIn;
import net.riking.entity.params.UpdUserParams;
import net.riking.entity.params.UserParams;
import net.riking.entity.resp.AppUserResp;
import net.riking.service.AppUserService;
import net.riking.service.SignInService;
import net.riking.service.SysDataService;
import net.riking.util.DateUtils;
import net.riking.util.StringUtil;

/**
 * app用户信息操作
 * @author jc.tan 2017年11月30日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/user")
public class AppUserServer {
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
	UserFollowRelRepo userFollowRelRepo;

	@Autowired
	SignInService signInService;

	@ApiOperation(value = "得到<单个>用户信息", notes = "POST")
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public AppResp get_(@RequestBody UserParams userParams) throws IllegalArgumentException, IllegalAccessException {

		AppUser appUser = appUserRepo.findOne(userParams.getUserId());
		AppUserResp appUserResp = new AppUserResp();
		appUserResp.setUserId(appUser.getId());
		// appUserResp从appUser取值
		appUserResp = (AppUserResp) fromObjToObjValue(appUser, appUserResp);
		AppUserDetail appUserDetail = appUserDetailRepo.findOne(appUser.getId());
		// appUserResp从appUserDetail取值
		appUserResp = (AppUserResp) fromObjToObjValue(appUserDetail, appUserResp);
		if (null != appUserResp.getPhotoUrl()) {
			appUserResp.setPhotoUrl(appUserService.getPhotoUrlPath() + appUserResp.getPhotoUrl());
		}
		// 等级
		if (null != appUserResp.getExperience()) {
			appUserResp.setGrade(appUserService.transformExpToGrade(appUserResp.getExperience()));
		}
		return new AppResp(appUserResp, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "更新用户信息", notes = "POST")
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public AppResp modify_(@RequestBody UpdUserParams userParams)
			throws IllegalArgumentException, IllegalAccessException {
		if (userParams.getUserId() == null) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		AppUser dbUser = appUserRepo.findOne(userParams.getUserId());
		if (null == dbUser) {
			return new AppResp(CodeDef.EMP.DATA_NOT_FOUND, CodeDef.EMP.DATA_NOT_FOUND_DESC);
		}

		AppUser appUser = appUserRepo.findOne(dbUser.getId());
		if (null != userParams) {
			appUser = (AppUser) fromObjToObjValue(userParams, appUser);
		}
		try {
			appUserRepo.save(appUser);
		} catch (Exception e) {
			return new AppResp("", CodeDef.ERROR);
		}

		AppUserDetail appUserDetail = appUserDetailRepo.findOne(dbUser.getId());
		if (null == appUserDetail) {
			return new AppResp(CodeDef.EMP.DATA_NOT_FOUND, CodeDef.EMP.DATA_NOT_FOUND_DESC);
		}
		if (null != appUserDetail) {
			if (null != userParams) {
				appUserDetail = (AppUserDetail) fromObjToObjValue(userParams, appUserDetail);
			}
			try {
				appUserDetailRepo.save(appUserDetail);
			} catch (Exception e) {
				return new AppResp("", CodeDef.ERROR);
			}

		}
		return new AppResp("", CodeDef.SUCCESS);
	}

	@ApiOperation(value = "更新用户手机设备信息", notes = "POST")
	@RequestMapping(value = "/IsChangeDevice", method = RequestMethod.POST)
	public AppResp IsChangeDevice_(@RequestBody UserParams userParams) {
		// 将map转换成参数对象
		AppUserDetail appUserDetail = appUserDetailRepo.findOne(userParams.getUserId());
		if (null == appUserDetail) {
			return new AppResp(CodeDef.EMP.DATA_NOT_FOUND, CodeDef.EMP.DATA_NOT_FOUND_DESC);
		}
		String seqNum = userParams.getPhoneDeviceid();
		if (appUserDetail != null && !seqNum.equals(appUserDetail.getPhoneDeviceid())) {
			appUserDetail.setPhoneDeviceid(seqNum);
			appUserDetail.setPhoneType(userParams.getPhoneType());
			appUserDetailRepo.save(appUserDetail);
			return new AppResp("", CodeDef.SUCCESS);
		}
		return new AppResp("", CodeDef.ERROR);
	}

	@AuthPass
	@ApiOperation(value = "上传头像", notes = "POST")
	@RequestMapping(value = "/upLoad", method = RequestMethod.POST)
	public AppResp upLoad(@RequestParam MultipartFile mFile, @RequestParam("userId") String userId) {
		String url = request.getRequestURL().toString();
		String fileName = null;
		try {
			fileName = appUserService.uploadPhoto(mFile, userId);
		} catch (RuntimeException e) {
			// TODO: handle exception
			if (e.getMessage().equals(CodeDef.EMP.GENERAL_ERR + "")) {
				return new AppResp(CodeDef.EMP.GENERAL_ERR, CodeDef.EMP.GENERAL_ERR_DESC);
			}
		}
		// 截取资源访问路径
		String projectPath = StringUtil.getProjectPath(url);
		String photoUrl = projectPath + Const.TL_PHOTO_PATH + fileName;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("photoUrl", photoUrl);
		return new AppResp(result, CodeDef.SUCCESS);
	}

	/**
	 * userId
	 * @param userParams
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "签到", notes = "POST")
	@RequestMapping(value = "/signIn", method = RequestMethod.POST)
	public AppResp signIn_(@RequestBody UserParams userParams) throws ParseException {
		if (null == userParams) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (null == userParams.getUserId()) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, +1);// 加一天
		Date nextDay = DateUtils.StringFormatMS(DateUtils.DateFormatMS(calendar.getTime(), "yyyy-MM-dd"), "yyyy-MM-dd");
		Date today = DateUtils.StringFormatMS(DateUtils.DateFormatMS(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");

		SignIn signIn = signInRepo.getByUIdAndTime(userParams.getUserId(), today, nextDay);
		Integer integral = appUserDetailRepo.getIntegral(userParams.getUserId());
		Integer integralNew = signInService.signIn(signIn, userParams.getUserId(), integral);
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("integral", integralNew);
		if (integral == integralNew) {
			return new AppResp("", CodeDef.SUCCESS);
		} else {
			return new AppResp(map, CodeDef.SUCCESS);
		}
	}

	/**
	 * 
	 * @param userId
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "关注数、回答数、粉丝数获取", notes = "POST")
	@RequestMapping(value = "/getOperateNumber", method = RequestMethod.POST)
	public AppResp getOperateNumber_(@RequestBody UserParams userParams) throws ParseException {
		if (null == userParams) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		if (null == userParams.getUserId()) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		// TODO 暂时从数据库中获取，后面优化从redis中取
		Integer followNum = userFollowRelRepo.countByUser(userParams.getUserId());
		Integer answerNum = questionAnswerRepo.answerCountByUserId(userParams.getUserId());
		Integer fansNum = userFollowRelRepo.countByToUser(userParams.getUserId());
		map.put("followNum", followNum);
		map.put("answerNum", answerNum);
		map.put("fansNum", fansNum);
		return new AppResp(map, CodeDef.SUCCESS);
	}

	/**
	 * fromObj的值赋在toObj上
	 * @param fromObj
	 * @param toObj
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private Object fromObjToObjValue(Object fromObj, Object toObj)
			throws IllegalArgumentException, IllegalAccessException {
		Field[] fromObjfields = fromObj.getClass().getDeclaredFields();
		for (Field fromObjfield : fromObjfields) {
			fromObjfield.setAccessible(true);
			Field[] toObjfields = toObj.getClass().getDeclaredFields();
			for (Field toObjfield : toObjfields) {
				toObjfield.setAccessible(true);
				if (fromObjfield.getName().equals(toObjfield.getName())) {
					if (null != fromObjfield.get(fromObj)) {
						toObjfield.set(toObj, fromObjfield.get(fromObj));
					}
				}
			}
		}
		return toObj;
	}

	private <T> T merge(T dbObj, T appObj) throws Exception {
		Field[] fields = dbObj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			Object val = field.get(appObj);
			if (val != null) {
				field.set(dbObj, val);
			}
		}
		return dbObj;
	}

}
