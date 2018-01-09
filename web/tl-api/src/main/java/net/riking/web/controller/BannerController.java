package net.riking.web.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.entity.PageQuery;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.BannerRepo;
import net.riking.entity.model.Banner;
import net.riking.service.BannerService;

/**
 * web端横幅操作
 * 
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:34:09
 * @used TODO
 */
@RestController
@RequestMapping(value = "/banner")
public class BannerController {
	@Autowired
	BannerRepo bannerRepo;

	@Autowired
	BannerService bannerService;

	@ApiOperation(value = "得到<单个>信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		Banner banner = bannerRepo.findOne(id);
		if (null != banner) {
			return new Resp(banner, CodeDef.SUCCESS);
		} else {
			return new Resp(null, CodeDef.ERROR);
		}
	}

	@ApiOperation(value = "得到<批量>信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute Banner banner) {
		banner.setIsDeleted(Const.EFFECTIVE);
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<Banner> example = Example.of(banner, ExampleMatcher.matchingAll());
		Page<Banner> page = bannerRepo.findAll(example, pageable);
		return new Resp(page);
	}

	@ApiOperation(value = "添加或者更新信息", notes = "POST")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp addOrUpdate_(@RequestBody Banner banner) {
		// 修改
		banner.setIsAduit("0");
		Banner banner2 = bannerRepo.findOne(banner.getId());
		if (banner2 != null) {
			if (!StringUtils.isBlank(banner.getBannerURL())) {
				// 判断是否有更换图片
				bannerService.moveFile(banner);
			} else {
				banner.setBannerURL(banner2.getBannerURL());
			}
		} else {
			bannerService.moveFile(banner);
		}
		bannerRepo.save(banner);
		return new Resp(banner, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "启用信息", notes = "GET")
	@RequestMapping(value = "/enable", method = RequestMethod.GET)
	public Resp enable_(@RequestParam String id) {
		Banner banner = bannerRepo.findOne(id);
		banner.setEnabled("1");
		banner = bannerRepo.save(banner);
		return new Resp(banner, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "禁用信息", notes = "GET")
	@RequestMapping(value = "/unEnable", method = RequestMethod.GET)
	public Resp unEnable_(@RequestParam String id) {
		Banner banner = bannerRepo.findOne(id);
		banner.setEnabled("0");
		banner = bannerRepo.save(banner);
		return new Resp(banner, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "审核", notes = "POST")
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public Resp verifyMore(@RequestBody Map<String, String> params) {
		Banner banner = bannerRepo.findOne(params.get("id"));
		if (banner != null) {
			banner.setIsAduit(params.get("isAduit"));
			banner = bannerRepo.save(banner);
			return new Resp(banner, CodeDef.SUCCESS);
		} else {
			return new Resp(null, CodeDef.ERROR);
		}

	}

	@ApiOperation(value = "删除banner信息", notes = "POST")
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public Resp delMore(@RequestBody String id) {
		id = id.replaceAll("\"", "");
		Banner banner = bannerRepo.findOne(id);
		if (banner != null) {
			banner.setIsDeleted(Const.INVALID);
			bannerRepo.save(banner);
			return new Resp(banner, CodeDef.SUCCESS);
		}
		return new Resp(null, CodeDef.ERROR);
	}

	// TODO 暫時注釋
	// @AuthPass
	// @ApiOperation(value = "上传头像", notes = "POST")
	// @RequestMapping(value = "/upLoad", method = RequestMethod.POST)
	// public Resp upLoad(HttpServletRequest request, @RequestParam("id") String
	// id) {
	// MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest)
	// request;
	// MultipartFile mFile = mRequest.getFile("fileName");
	// String suffix =
	// mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("."));
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	// String fileName = sdf.format(new Date()) + suffix;
	// InputStream is = null;
	// FileOutputStream fos = null;
	// try {
	// is = mFile.getInputStream();
	// String path = this.getClass().getResource("/").getPath() +
	// Const.TL_STATIC_PATH +
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
	// int rs = appUserRepo.updatePhoto(id, projectPath + Const.TL_PHOTO_PATH +
	// fileName);
	// if (rs > 0) {
	// return new Resp(true, CodeDef.SUCCESS);
	// }
	// return new Resp(CodeDef.ERROR);
	// }

	// TODO 暂时注释
	// private void setPhotoUrl(String url, List<AppUser> list) {
	// String projectPath = StringUtil.getProjectPath(url);
	// for (AppUser appUser : list) {
	// if (appUser.getPhotoUrl() != null &&
	// !appUser.getPhotoUrl().contains("http"))
	// appUser.setPhotoUrl(projectPath + appUser.getPhotoUrl());
	// }
	// }
}
