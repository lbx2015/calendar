package net.riking.web.app;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import net.riking.dao.repo.FeedBackRepo;
import net.riking.dao.repo.QuestionAnswerRepo;
import net.riking.dao.repo.SignInRepo;
import net.riking.dao.repo.UserFollowRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.FeedBack;
import net.riking.service.AppUserService;
import net.riking.service.SignInService;
import net.riking.service.SysDataService;

/**
 * app用户信息操作
 * @author jc.tan 2017年11月30日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/feedBack")
public class AppUserFeedBackServer {
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

	@Autowired
	FeedBackRepo feedBackRepo;

	/**
	 * 
	 * @param mFile
	 * @param userId
	 * @return
	 */
	@AuthPass
	@ApiOperation(value = "意见反馈", notes = "POST")
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	public AppResp publish_(@RequestParam("mFile") MultipartFile[] mFiles, @RequestParam("userId") String userId,
			@RequestParam("content") String content) {
		String fileName = "";
		List<String> fileNames = new ArrayList<String>();
		try {
			for (MultipartFile mFile : mFiles) {
				String photoName = appUserService.savePhotoFile(mFile, Const.TL_FEED_BACK_PHOTO_PATH);
				fileNames.add(photoName);
			}
		} catch (RuntimeException e) {
			// TODO: handle exception
			if (e.getMessage().equals(CodeDef.EMP.GENERAL_ERR + "")) {
				return new AppResp(CodeDef.EMP.GENERAL_ERR, CodeDef.EMP.GENERAL_ERR_DESC);
			}
		}
		for (int i = 0; i < fileNames.size(); i++) {
			if (i < fileNames.size() - 1) {
				fileName += fileNames.get(i) + ",";
			} else {
				fileName += fileNames.get(i);
			}
		}
		FeedBack back = new FeedBack(content, fileName, userId);
		feedBackRepo.save(back);
		if (mFiles.length == 0) {
			return new AppResp("上传图片为空", CodeDef.SUCCESS);
		}
		return new AppResp(CodeDef.SUCCESS);
	}
}
