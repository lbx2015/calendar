package net.riking.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import net.riking.entity.ApiResp;
import net.riking.entity.Data;
import net.riking.service.AppUserService;
import net.riking.util.StringUtil;

/**
 * 公共控制层
 * @author jc.tan 2017年12月27日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/commonController")
public class CommonController {
	protected final transient Logger logger = LogManager.getLogger(getClass());

	@Autowired
	AppUserService appUserService;

	@Autowired
	HttpServletRequest request;

	@AuthPass
	@ApiOperation(value = "提问/回答上传图片到临时路径", notes = "POST")
	@RequestMapping(value = "/upLoad", method = RequestMethod.POST)
	public ApiResp upLoad(@RequestParam("file") MultipartFile mFile) {
		String fileName = null;
		try {
			fileName = appUserService.savePhotoFile(mFile, Const.TL_TEMP_PHOTO_PATH);
		} catch (RuntimeException e) {
			// TODO: handle exception
			if (e.getMessage().equals(CodeDef.EMP.GENERAL_ERR + "")) {
			}
		}
		// 截取资源访问路径
		String projectPath = StringUtil.getProjectPath(request.getRequestURL().toString());
		projectPath = projectPath + "/" + Const.TL_TEMP_PHOTO_PATH;
		Data data = new Data(projectPath + fileName, fileName);
		return new ApiResp(data, (short) 0);

	}

}
