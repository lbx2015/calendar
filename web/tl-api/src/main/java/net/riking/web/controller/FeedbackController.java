package net.riking.web.controller;

import java.util.Map;

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
import net.riking.core.entity.PageQuery;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.FeedBackRepo;
import net.riking.entity.model.FeedBack;

/**
 * web端app用户操作
 * 
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:34:09
 * @used TODO
 */
@RestController
@RequestMapping(value = "/feedback")
public class FeedbackController {
	@Autowired
	FeedBackRepo feedBackRepo;

	@ApiOperation(value = "得到<单个>反馈信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		FeedBack feedback = feedBackRepo.findOne(id);
		if (null != feedback) {
			return new Resp(feedback, CodeDef.SUCCESS);
		} else {
			return new Resp(null, CodeDef.ERROR);
		}
	}

	@ApiOperation(value = "得到<批量>反馈信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute FeedBack feedback) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<FeedBack> example = Example.of(feedback, ExampleMatcher.matchingAll());
		Page<FeedBack> page = feedBackRepo.findAll(example, pageable);
		return new Resp(page);
	}

	@ApiOperation(value = "回馈处理", notes = "POST")
	@RequestMapping(value = "/handle", method = RequestMethod.POST)
	public Resp handle(@RequestBody Map<String, String> params) {
		FeedBack feedback = feedBackRepo.findOne(params.get("id"));
		feedback.setAccept(Integer.parseInt(params.get("accept")));
		feedBackRepo.save(feedback);
		return new Resp(feedback, CodeDef.SUCCESS);
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
