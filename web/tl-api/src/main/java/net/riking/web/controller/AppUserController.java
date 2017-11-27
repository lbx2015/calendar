package net.riking.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.entity.model.AppUser;
import net.riking.service.repo.AppUserRepo;

/**
 * web端app用户操作
 * 
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:34:09
 * @used TODO
 */
@RestController
@RequestMapping(value = "/appUser")
public class AppUserController {
	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	HttpServletRequest request;

	@ApiOperation(value = "得到<单个>用户信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		AppUser appUser = appUserRepo.findOne(id);
		return new Resp(appUser, CodeDef.SUCCESS);
	}

	// TODO 暂时注释
	// @ApiOperation(value = "得到<批量>用户信息", notes = "GET")
	// @RequestMapping(value = "/getMore", method = RequestMethod.GET)
	// public Resp getMore_(@ModelAttribute PageQuery query,
	// @ModelAttribute AppUser appUser) {
	// PageRequest pageable = new PageRequest(query.getPindex(),
	// query.getPcount(), query.getSortObj());
	// if (StringUtils.isEmpty(appUser.getDeleteState())) {
	// appUser.setDeleteState("1");
	// }
	// Example<AppUser> example = Example.of(appUser,
	// ExampleMatcher.matchingAll());
	// Page<AppUser> page = appUserRepo.findAll(example, pageable);
	// String url = request.getRequestURL().toString();
	// setPhotoUrl(url, page.getContent());
	// return new Resp(page, CodeDef.SUCCESS);
	// }

	// TODO 暂时注释
	// @ApiOperation(value = "添加或者更新用户信息", notes = "POST")
	// @RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	// public Resp addOrUpdate_(@RequestBody AppUser appUser) {
	// if (StringUtils.isEmpty(appUser.getId()) || StringUtils.isEmpty(appUser.getDeleteState())) {
	// appUser.setDeleteState("1");
	// }
	// AppUser save = appUserRepo.save(appUser);
	// return new Resp(save, CodeDef.SUCCESS);
	// }

	// TODO 暫時注釋
	// @ApiOperation(value = "启用用户信息", notes = "GET")
	// @RequestMapping(value = "/enable", method = RequestMethod.GET)
	// public Resp enable_(@RequestParam String id) {
	// int rs = appUserRepo.enable(id);
	// return new Resp(rs, CodeDef.SUCCESS);
	// }
	//
	// @ApiOperation(value = "禁用用户信息", notes = "GET")
	// @RequestMapping(value = "/unEnable", method = RequestMethod.GET)
	// public Resp unEnable_(@RequestParam String id) {
	// int rs = appUserRepo.unEnable(id);
	// return new Resp(rs, CodeDef.SUCCESS);
	// }
	//
	// @ApiOperation(value = "禁用用户信息", notes = "GET")
	// @RequestMapping(value = "/passwordReset", method = RequestMethod.GET)
	// public Resp passwordReset_(@RequestParam String id) {
	// int rs = appUserRepo.passwordReset(id);
	// return new Resp(rs, CodeDef.SUCCESS);
	// }

	// TODO 暂时注释
	// @ApiOperation(value = "批量删除用户信息", notes = "POST")
	// @RequestMapping(value = "/delMore", method = RequestMethod.POST)
	// public Resp delMore_(@RequestBody Set<String> ids) {
	// int rs = 0;
	// if (ids.size() > 0) {
	// rs = appUserRepo.deleteByIds(ids);
	// }
	// if (rs > 0) {
	// return new Resp().setCode(CodeDef.SUCCESS);
	// } else {
	// return new Resp().setCode(CodeDef.ERROR);
	// }
	// }

	// TODO 暫時注釋
	// @AuthPass
	// @ApiOperation(value = "上传头像", notes = "POST")
	// @RequestMapping(value = "/upLoad", method = RequestMethod.POST)
	// public Resp upLoad(HttpServletRequest request, @RequestParam("id") String id) {
	// MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
	// MultipartFile mFile = mRequest.getFile("fileName");
	// String suffix =
	// mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("."));
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	// String fileName = sdf.format(new Date()) + suffix;
	// InputStream is = null;
	// FileOutputStream fos = null;
	// try {
	// is = mFile.getInputStream();
	// String path = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH +
	// Const.TL_PHOTO_PATH;
	// File dir = new File(path);
	// if (!dir.exists()) {
	// dir.mkdirs();
	// }
	// String photoUrl = path + fileName;
	// fos = new FileOutputStream(photoUrl);
	// int len = 0;
	// byte[] buf = new byte[1024 * 1024];
	// while ((len = is.read(buf)) > -1) {
	// fos.write(buf, 0, len);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// return new Resp(false, CodeDef.ERROR);
	// } finally {
	// try {
	// fos.close();
	// is.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// return new Resp(false, CodeDef.ERROR);
	// }
	// }
	// // 截取资源访问路径
	// String url = request.getRequestURL().toString();
	// String projectPath = StringUtil.getProjectPath(url);
	// int rs = appUserRepo.updatePhoto(id, projectPath + Const.TL_PHOTO_PATH + fileName);
	// if (rs > 0) {
	// return new Resp(true, CodeDef.SUCCESS);
	// }
	// return new Resp(CodeDef.ERROR);
	// }

	// TODO 暂时注释
	// private void setPhotoUrl(String url, List<AppUser> list) {
	// String projectPath = StringUtil.getProjectPath(url);
	// for (AppUser appUser : list) {
	// if (appUser.getPhotoUrl() != null && !appUser.getPhotoUrl().contains("http"))
	// appUser.setPhotoUrl(projectPath + appUser.getPhotoUrl());
	// }
	// }
}
