package net.riking.web.app;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.config.Const;
import net.riking.core.annos.AuthPass;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserFollowRelRepo;
import net.riking.dao.repo.AppUserGradeRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.SignInRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.AppUserGrade;
import net.riking.entity.model.SignIn;
import net.riking.entity.model.UserFollowRel;
import net.riking.entity.model.UserOperationInfo;
import net.riking.entity.params.UpdUserParams;
import net.riking.entity.params.UserParams;
import net.riking.entity.resp.AppUserResp;
import net.riking.entity.resp.OtherUserResp;
import net.riking.service.AppUserService;
import net.riking.service.SignInService;
import net.riking.service.SysDataService;
import net.riking.util.DateUtils;
import net.riking.util.FileUtils;
import net.riking.util.Utils;

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
	AppUserFollowRelRepo appUserFollowRelRepo;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@Autowired
	AppUserGradeRepo appUserGradeRepo;

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
	Config config;

	@ApiOperation(value = "我的等级详情", notes = "POST")
	@RequestMapping(value = "/myGradeHtml", method = RequestMethod.POST)
	public AppResp myGrade_(@RequestBody UserParams userParams) {
		String userId = userParams.getUserId();
		AppUserDetail userDetail = appUserDetailRepo.findOne(userId);
		return new AppResp(config.getAppHtmlPath() + Const.TL_USER_HTML5_PATH + "?exp=" + userDetail.getExperience()
				+ "&url=" + config.getAppApiPath(), CodeDef.SUCCESS);
	}

	@ApiOperation(value = "等级分级制度", notes = "POST")
	@RequestMapping(value = "/getGradeList", method = RequestMethod.POST)
	public AppResp getGradeList_() {
		List<AppUserGrade> list = appUserGradeRepo.findByIsDeleted(Const.IS_NOT_DELETE);
		return new AppResp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取好友的好友", notes = "POST")
	@RequestMapping(value = "/getFOAF", method = RequestMethod.POST)
	public AppResp getFOAF_(@RequestBody UserParams userParams) {
		HashSet<String> set = new HashSet<>();
		List<UserFollowRel> list = appUserFollowRelRepo.findByUserId(userParams.getUserId());
		UserFollowRel userFollowRel = null;
		for (int i = 0; i < list.size(); i++) {
			userFollowRel = list.get(i);
			if (userFollowRel.getUserId().equals(userParams.getUserId())) {
				set.add(userFollowRel.getToUserId());
			} else {
				set.add(userFollowRel.getUserId());
			}
		}
		if (set.isEmpty()) {
			return new AppResp(null, CodeDef.SUCCESS);
		}
		HashSet<String> set2 = new HashSet<>();
		List<UserFollowRel> list2 = appUserFollowRelRepo.findUserIdByUserIds(set);
		for (int i = 0; i < list2.size(); i++) {
			userFollowRel = list2.get(i);
			if (!set.contains(userFollowRel.getUserId()) && !userFollowRel.getUserId().equals(userParams.getUserId())) {
				set2.add(userFollowRel.getUserId());
			}
			if (!set.contains(userFollowRel.getToUserId())
					&& !userFollowRel.getToUserId().equals(userParams.getUserId())) {
				set2.add(userFollowRel.getToUserId());
			}
		}
		List<AppUserDetail> foafs = null;
		if(!set2.isEmpty()){
			foafs = appUserDetailRepo.findAllByIds(set2);
			foafs.forEach(e -> {
				// e.setPhotoUrl(appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) + e.getPhotoUrl());
				e.setPhotoUrl(FileUtils.getAbsolutePathByProject(Const.TL_PHOTO_PATH) + e.getPhotoUrl());
				e.setGrade();
			});
			foafs.forEach(e -> {
				e.setExperience(null);
			});
		}else{
			foafs = new ArrayList<>();
		}
		return new AppResp(foafs, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到<单个>用户信息", notes = "POST")
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public AppResp get_(@RequestBody UserParams userParams) throws IllegalArgumentException, IllegalAccessException {

		AppUser appUser = appUserRepo.findOne(userParams.getUserId());
		AppUserResp appUserResp = new AppUserResp();
		appUserResp.setUserId(appUser.getId());
		// appUserResp从appUser取值
		appUserResp = (AppUserResp) Utils.fromObjToObjValue(appUser, appUserResp);
		AppUserDetail appUserDetail = appUserDetailRepo.findOne(appUser.getId());
		// appUserResp从appUserDetail取值
		appUserResp = (AppUserResp) Utils.fromObjToObjValue(appUserDetail, appUserResp);
		if (null != appUserResp.getPhotoUrl()) {
			// appUserResp.setPhotoUrl(appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) +
			// appUserResp.getPhotoUrl());
			appUserResp
					.setPhotoUrl(FileUtils.getAbsolutePathByProject(Const.TL_PHOTO_PATH) + appUserResp.getPhotoUrl());
		}
		// 等级
		if (null != appUserResp.getExperience()) {
			appUserResp.setGrade(appUserService.transformExpToGrade(appUserResp.getExperience()));
		}
		return new AppResp(appUserResp, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到他人的信息", notes = "POST")
	@RequestMapping(value = "/getOther", method = RequestMethod.POST)
	public AppResp getOther_(@RequestBody UserParams userParams)
			throws IllegalArgumentException, IllegalAccessException {
		if (userParams.getUserId() == null) {
			userParams.setUserId("");
		}
		if (userParams.getToUserId() == null) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		OtherUserResp otherUserResp = appUserService.getOtherMes(userParams.getToUserId(), userParams.getUserId());
		// TODO 暂时从数据库中获取，后面优化从redis中取
		Integer followNum = userFollowRelRepo.countByUser(userParams.getToUserId());
		Integer answerNum = questionAnswerRepo.answerCountByUserId(userParams.getToUserId());
		Integer fansNum = userFollowRelRepo.countByToUser(userParams.getToUserId());
		otherUserResp.setFollowNum(followNum);
		otherUserResp.setAnswerNum(answerNum);
		otherUserResp.setFansNum(fansNum);
		if (null != otherUserResp.getPhotoUrl()) {
			// otherUserResp.setPhotoUrl(appUserService.getPhotoUrlPath(Const.TL_PHOTO_PATH) +
			// otherUserResp.getPhotoUrl());
			otherUserResp
					.setPhotoUrl(FileUtils.getAbsolutePathByProject(Const.TL_PHOTO_PATH) + otherUserResp.getPhotoUrl());
		}
		// 等级
		if (null != otherUserResp.getExperience()) {
			otherUserResp.setGrade(appUserService.transformExpToGrade(otherUserResp.getExperience()));
		}
		return new AppResp(otherUserResp, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "更新用户信息", notes = "POST")
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public AppResp modify_(@RequestBody UpdUserParams userParams)
			throws IllegalArgumentException, IllegalAccessException {
		if (userParams.getUserId() == null) {
			return new AppResp(CodeDef.EMP.PARAMS_ERROR, CodeDef.EMP.PARAMS_ERROR_DESC);
		}
		AppUser appUser = appUserRepo.findOne(userParams.getUserId());
		if (null == appUser) {
			return new AppResp(CodeDef.EMP.DATA_NOT_FOUND, CodeDef.EMP.DATA_NOT_FOUND_DESC);
		}
		if (StringUtils.isNotBlank(userParams.getEmail()) && StringUtils.isNotBlank(appUser.getEmail())
				&& (!userParams.getEmail().equals(appUser.getEmail()))) {
			userParams.setIsIdentified(0);// 邮箱未认证
		}
		if (null != userParams) {
			appUser = (AppUser) Utils.fromObjToObjValue(userParams, appUser);
		}
		try {
			appUserRepo.save(appUser);
		} catch (Exception e) {
			return new AppResp("", CodeDef.ERROR);
		}

		AppUserDetail appUserDetail = appUserDetailRepo.findOne(appUser.getId());
		if (null == appUserDetail) {
			return new AppResp(CodeDef.EMP.DATA_NOT_FOUND, CodeDef.EMP.DATA_NOT_FOUND_DESC);
		}
		String positionId = appUserDetail.getPositionId();
		if (StringUtils.isNotBlank(appUserDetail.getIndustryId()) && StringUtils.isNotBlank(userParams.getIndustryId())
				&& (!userParams.getIndustryId().equals(appUserDetail.getIndustryId()))) {
			positionId = null;
		}
		if (null != appUserDetail) {
			if (null != userParams) {
				appUserDetail = (AppUserDetail) Utils.fromObjToObjValue(userParams, appUserDetail);
				appUserDetail.setPositionId(positionId);
				appUserDetail.setCheckMyCollectState(userParams.getCheckMyCollectState());
				appUserDetail.setCheckMyDynamicState(userParams.getCheckMyDynamicState());
				appUserDetail.setCheckMyFollowState(userParams.getCheckMyFollowState());
			}
			try {
				appUserDetailRepo.save(appUserDetail);
			} catch (Exception e) {
				return new AppResp("", CodeDef.ERROR);
			}

		}
		return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
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
		if (appUserDetail != null && !seqNum.equals(appUserDetail.getPhoneDeviceId())) {
			appUserDetail.setPhoneDeviceId(seqNum);
			appUserDetail.setPhoneType(userParams.getPhoneType());
			appUserDetailRepo.save(appUserDetail);
			return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
		}
		return new AppResp("", CodeDef.ERROR);
	}

	@AuthPass
	@ApiOperation(value = "上传头像", notes = "POST")
	@RequestMapping(value = "/upLoad", method = RequestMethod.POST)
	public AppResp upLoad(@RequestParam MultipartFile mFile, @RequestParam("userId") String userId) {
		// String url = request.getRequestURL().toString();
		String fileName = null;
		String folderPath = FileUtils.getAbsolutePathByProject(Const.TL_PHOTO_PATH);
		try {
			// fileName = appUserService.savePhotoFile(mFile, Const.TL_PHOTO_PATH);
			fileName = FileUtils.saveMultipartFile(mFile, folderPath);
			fileName = appUserService.updUserPhotoUrl(mFile, userId, fileName);
		} catch (RuntimeException e) {
			// TODO: handle exception
			if (e.getMessage().equals(CodeDef.EMP.GENERAL_ERR + "")) {
				return new AppResp(CodeDef.EMP.GENERAL_ERR, CodeDef.EMP.GENERAL_ERR_DESC);
			}
		}
		String photoUrl = FileUtils.getPhotoUrl(Const.TL_PHOTO_PATH + fileName, this.getClass());
		return new AppResp(photoUrl, CodeDef.SUCCESS);
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
		Map<String, Object> maps = signInService.signIn(signIn, userParams.getUserId(), integral);
		// 如果总积分相同
		if (integral == maps.get("integral")) {
			return new AppResp(CodeDef.EMP.SIGN_ERROR, CodeDef.EMP.SIGN_ERROR_DESC);
		} else {
			return new AppResp(maps, CodeDef.SUCCESS);
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
		// TODO 暂时从数据库中获取，后面优化从redis中取
		Integer followNum = userFollowRelRepo.countByUser(userParams.getUserId());
		Integer answerNum = questionAnswerRepo.answerCountByUserId(userParams.getUserId());
		Integer fansNum = userFollowRelRepo.countByToUser(userParams.getUserId());
		UserOperationInfo userOperationInfo = new UserOperationInfo();
		userOperationInfo.setFollowNum(followNum);
		userOperationInfo.setAnswerNum(answerNum);
		userOperationInfo.setFansNum(fansNum);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, +1);// 加一天
		Date nextDay = DateUtils.StringFormatMS(DateUtils.DateFormatMS(calendar.getTime(), "yyyy-MM-dd"), "yyyy-MM-dd");
		Date today = DateUtils.StringFormatMS(DateUtils.DateFormatMS(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");

		SignIn signIn = signInRepo.getByUIdAndTime(userParams.getUserId(), today, nextDay);
		if (signIn != null) {
			userOperationInfo.setSignStatus(1);// 1-已签到
		} else {
			userOperationInfo.setSignStatus(0);// 0-未签到
		}
		return new AppResp(userOperationInfo, CodeDef.SUCCESS);
	}

	/*
	 * private <T> T merge(T dbObj, T appObj) throws Exception { Field[] fields =
	 * dbObj.getClass().getDeclaredFields(); for (int i = 0; i < fields.length; i++) { Field field =
	 * fields[i]; field.setAccessible(true); Object val = field.get(appObj); if (val != null) {
	 * field.set(dbObj, val); } } return dbObj; }
	 */
}
