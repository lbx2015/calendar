package net.riking.web.app;

import java.lang.reflect.Field;
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
import net.riking.dao.repo.IndustryRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.params.UpdUserParams;
import net.riking.entity.params.UserParams;
import net.riking.entity.resp.AppUserResp;
import net.riking.service.AppUserCommendService;
import net.riking.service.AppUserService;
import net.riking.service.SysDataService;
import net.riking.util.StringUtil;
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
	AppUserDetailRepo appUserDetailRepo;

	@Autowired
	IndustryRepo industryRepo;

	@Autowired
	HttpServletRequest request;

	@Autowired
	SysDataService sysDataservice;

	@Autowired
	AppUserService appUserService;

	@Autowired
	AppUserCommendService appUserCommendServie;

	@ApiOperation(value = "得到<单个>用户信息", notes = "POST")
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public AppResp get_(@RequestBody Map<String, Object> params)
			throws IllegalArgumentException, IllegalAccessException {
		// 将map转换成参数对象
		UserParams userParams = Utils.map2Obj(params, UserParams.class);
		AppUser appUser = appUserRepo.findOne(userParams.getUserId());
		AppUserResp appUserResp = new AppUserResp();
		appUserResp.setUserId(appUser.getId());
		// appUserResp从appUser取值
		appUserResp = (AppUserResp) fromObjToObjValue(appUserResp, appUser);
		AppUserDetail appUserDetail = appUserDetailRepo.findOne(appUser.getId());
		// appUserResp从appUserDetail取值
		appUserResp = (AppUserResp) fromObjToObjValue(appUserResp, appUserDetail);
		// 将对象转换成map
		Map<String, Object> appUserRespMapNew = Utils.objProps2Map(appUserResp, true);
		return new AppResp(appUserRespMapNew, CodeDef.SUCCESS);
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
					toObjfield.set(toObj, fromObjfield.get(fromObj));
				}
			}
		}
		return toObj;
	}

	@ApiOperation(value = "更新用户信息", notes = "POST")
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public AppResp modify_(@RequestBody Map<String, Object> params)
			throws IllegalArgumentException, IllegalAccessException {
		UpdUserParams userParams = Utils.map2Obj(params, UpdUserParams.class);

		AppUser dbUser = appUserRepo.findOne(userParams.getUserId());
		if (null == dbUser) {
			return new AppResp(false, CodeDef.EMP.DATA_NOT_FOUND);
		}

		AppUser appUser = appUserRepo.findOne(dbUser.getId());
		if (null != userParams) {
			Field[] userParamsfields = userParams.getClass().getDeclaredFields();
			for (Field userParamsfield : userParamsfields) {
				userParamsfield.setAccessible(true);
				Field[] appUserfields = appUser.getClass().getDeclaredFields();
				for (Field appUserfield : appUserfields) {
					appUserfield.setAccessible(true);
					if (userParamsfield.getName().equals(appUserfield.getName())) {
						// 如果接收对象里面的属性不为空，设置进appUser类中
						if (null != userParamsfield.get(userParams)) {
							appUserfield.set(appUser, userParamsfield.get(userParams));
						}
					}
				}
			}
		}
		try {
			appUserRepo.save(appUser);
		} catch (Exception e) {
			return new AppResp(false, CodeDef.ERROR);
		}

		AppUserDetail appUserDetail = appUserDetailRepo.findOne(dbUser.getId());
		if (null != appUserDetail) {
			if (null != userParams) {
				Field[] userParamsfields = userParams.getClass().getDeclaredFields();
				for (Field userParamsfield : userParamsfields) {
					userParamsfield.setAccessible(true);
					Field[] appUserDetailfields = appUserDetail.getClass().getDeclaredFields();
					for (Field appUserDetailfield : appUserDetailfields) {
						appUserDetailfield.setAccessible(true);
						if (userParamsfield.getName().equals(appUserDetailfield.getName())) {
							// 如果接收对象里面的属性不为空，设置进appUserDetail类中
							if (null != userParamsfield.get(userParams)) {
								appUserDetailfield.set(appUserDetail, userParamsfield.get(userParams));
							}
						}
					}
				}
			}
			try {
				appUserDetailRepo.save(appUserDetail);
			} catch (Exception e) {
				return new AppResp(false, CodeDef.ERROR);
			}

		}
		return new AppResp(true, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "更新用户手机设备信息", notes = "POST")
	@RequestMapping(value = "/IsChangeDevice", method = RequestMethod.POST)
	public AppResp IsChangeDevice_(@RequestBody Map<String, Object> params) {
		// 将map转换成参数对象
		UserParams userParams = Utils.map2Obj(params, UserParams.class);
		AppUserDetail appUserDetail = appUserDetailRepo.findOne(userParams.getUserId());
		if (null == appUserDetail) {
			return new AppResp(false, CodeDef.EMP.DATA_NOT_FOUND);
		}
		String seqNum = userParams.getPhoneDeviceid();
		if (appUserDetail != null && !seqNum.equals(appUserDetail.getPhoneDeviceid())) {
			appUserDetail.setPhoneDeviceid(seqNum);
			appUserDetail.setPhoneType(userParams.getPhoneType());
			appUserDetailRepo.save(appUserDetail);
			return new AppResp(true, CodeDef.SUCCESS);
		}
		return new AppResp(false, CodeDef.ERROR);
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

	@ApiOperation(value = "更新用户信息", notes = "POST")
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public AppResp updateUser(@RequestBody AppUser appUser) {
		appUserRepo.save(appUser);
		return new AppResp(CodeDef.ERROR);
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

	@AuthPass
	@ApiOperation(value = "获取行业列表", notes = "POST")
	@RequestMapping(value = "/findIndustry", method = RequestMethod.POST)
	public AppResp findIndustry() {
		// List<Industry> list = industryRepo.findIndustry(0);// 查询行业
		return new AppResp(industryRepo.findIndustry(0), CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取行业下面的职位列表", notes = "POST")
	@RequestMapping(value = "/getPositionByIndustry", method = RequestMethod.POST)
	public AppResp getPositionByIndustry(@RequestParam("industryId") String industryId) {
		return new AppResp(industryRepo.findPositionByIndustry(industryId), CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取推荐报表", notes = "POST")
	@RequestMapping(value = "/getCommend", method = RequestMethod.POST)
	public AppResp getCommend() {
		return new AppResp(appUserCommendServie.findALL(), CodeDef.SUCCESS);
	}

}
