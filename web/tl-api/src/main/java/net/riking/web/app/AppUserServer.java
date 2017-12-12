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
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.params.UpdUserParams;
import net.riking.entity.params.UserParams;
import net.riking.entity.resp.AppUserResp;
import net.riking.service.AppUserService;
import net.riking.service.SysDataService;
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

	@ApiOperation(value = "得到<单个>用户信息", notes = "POST")
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public AppResp get_(@RequestBody UserParams userParams) throws IllegalArgumentException, IllegalAccessException {

		AppUser appUser = appUserRepo.findOne(userParams.getUserId());
		AppUserResp appUserResp = new AppUserResp();
		appUserResp.setUserId(appUser.getId());
		// appUserResp从appUser取值
		appUserResp = (AppUserResp) fromObjToObjValue(appUserResp, appUser);
		AppUserDetail appUserDetail = appUserDetailRepo.findOne(appUser.getId());
		// appUserResp从appUserDetail取值
		appUserResp = (AppUserResp) fromObjToObjValue(appUserResp, appUserDetail);

		return new AppResp(appUserResp, CodeDef.SUCCESS);
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
	public AppResp modify_(@RequestBody UpdUserParams userParams)
			throws IllegalArgumentException, IllegalAccessException {

		AppUser dbUser = appUserRepo.findOne(userParams.getUserId());
		if (null == dbUser) {
			return new AppResp(CodeDef.EMP.DATA_NOT_FOUND, CodeDef.EMP.DATA_NOT_FOUND_DESC);
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
			return new AppResp("", CodeDef.ERROR);
		}

		AppUserDetail appUserDetail = appUserDetailRepo.findOne(dbUser.getId());
		if (null == appUserDetail) {
			return new AppResp(CodeDef.EMP.DATA_NOT_FOUND, CodeDef.EMP.DATA_NOT_FOUND_DESC);
		}
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
	public AppResp upLoad(@RequestParam MultipartFile mFile, @RequestBody UserParams userParams) {
		String url = request.getRequestURL().toString();
		String fileName = null;
		try {
			fileName = appUserService.uploadPhoto(mFile, userParams.getUserId());
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
