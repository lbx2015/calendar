package net.riking.web.appInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.Industry;
import net.riking.service.AppUserCommendService;
import net.riking.service.SysDataService;
import net.riking.service.repo.AppUserRepo;
import net.riking.service.repo.IndustryRepo;
import net.riking.util.StringUtil;

/**
 * app用户信息操作
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:31:03
 * @used TODO
 */
@RestController
@RequestMapping(value = "/appUserApp")
public class AppUserServer {
	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	IndustryRepo industryRepo;

	@Autowired
	HttpServletRequest request;

	@Autowired
	SysDataService sysDataservice;

	@Autowired
	AppUserCommendService appUserCommendServie;

	@ApiOperation(value = "得到<单个>用户信息", notes = "POST")
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public AppResp get_(@RequestBody AppUser appUser) {
		AppUser dbUser = appUserRepo.findOne(appUser.getId());
		return new AppResp(dbUser, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "添加或者更新用户信息", notes = "POST")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AppResp addOrUpdate_(@RequestBody AppUser appUser) {
		if (StringUtils.isEmpty(appUser.getId()) || StringUtils.isEmpty(appUser.getDeleteState())) {
			appUser.setDeleteState("1");
		}
		AppUser dbUser = appUserRepo.findById(appUser.getId());
		if (null == dbUser) {
			return new AppResp(false, CodeDef.EMP.DATA_NOT_FOUND);
		}
		try {
			merge(dbUser, appUser);
		} catch (Exception e) {
			return new AppResp(false, CodeDef.ERROR);
		}
		AppUser saveUser = appUserRepo.save(dbUser);
		if (null != saveUser && StringUtils.isNotEmpty(saveUser.getId())) {
			return new AppResp(true, CodeDef.SUCCESS);
		}
		return new AppResp(false, CodeDef.ERROR);
	}

	@ApiOperation(value = "更新用户手机设备信息", notes = "POST")
	@RequestMapping(value = "/IsChangeMac", method = RequestMethod.POST)
	public AppResp IsChangeMac_(@RequestBody AppUser appUser) {
		AppUser appUser2 = appUserRepo.findOne(appUser.getId());
		if (null == appUser2) {
			return new AppResp(false, CodeDef.EMP.DATA_NOT_FOUND);
		}
		String seqNum = appUser.getPhoneMacid();
		if (appUser2 != null && !appUser2.getPhoneMacid().equals(seqNum)) {
			appUser2.setPhoneMacid(seqNum);
			appUser2.setPhoneType(appUser.getPhoneType());
			appUserRepo.save(appUser2);
			return new AppResp(true, CodeDef.SUCCESS);
		}
		return new AppResp(false, CodeDef.ERROR);
	}

	@AuthPass
	@ApiOperation(value = "上传头像", notes = "POST")
	@RequestMapping(value = "/upLoad", method = RequestMethod.POST)
	public AppResp upLoad(@RequestParam MultipartFile mFile, @RequestParam("id") String id) {
		String url = request.getRequestURL().toString();
		String suffix = mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("."));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String fileName = sdf.format(new Date()) + suffix;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = mFile.getInputStream();
			String path = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH + Const.TL_PHOTO_PATH;
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String photoUrl = path + fileName;
			fos = new FileOutputStream(photoUrl);
			int len = 0;
			byte[] buf = new byte[1024 * 1024];
			while ((len = is.read(buf)) > -1) {
				fos.write(buf, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new AppResp(false, CodeDef.ERROR);
		} finally {
			try {
				fos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				return new AppResp(false, CodeDef.ERROR);
			}
		}
		// 截取资源访问路径
		String projectPath = StringUtil.getProjectPath(url);
		String photoUrl = projectPath + Const.TL_PHOTO_PATH + fileName;
		// 数据库保存路径
		int rs = appUserRepo.updatePhoto(id, Const.TL_PHOTO_PATH + fileName);
		if (rs > 0) {
			return new AppResp(photoUrl, CodeDef.SUCCESS);
		}
		return new AppResp(CodeDef.ERROR);
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
		// List<Industry> list = industryRepo.findIndustry("0");//查询行业
		return new AppResp(industryRepo.findIndustry(0), CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取行业下面的职位列表", notes = "POST")
	@RequestMapping(value = "/getPositionByIndustry", method = RequestMethod.POST)
	public AppResp getPositionByIndustry(@RequestBody Industry industry) {
		return new AppResp(industryRepo.findPositionByIndustry(industry.getId()), CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取推荐报表", notes = "POST")
	@RequestMapping(value = "/getCommend", method = RequestMethod.POST)
	public AppResp getCommend() {
		return new AppResp(appUserCommendServie.findALL(), CodeDef.SUCCESS);
	}
}
