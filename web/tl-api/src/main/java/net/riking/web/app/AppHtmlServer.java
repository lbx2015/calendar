package net.riking.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.config.Const;
import net.riking.entity.AppResp;

@RestController
@RequestMapping(value = "/appHtml")
public class AppHtmlServer {
	
	@Autowired
	Config config;
	
	@ApiOperation(value = "跳转<关于>html5页面", notes = "POST")
	@RequestMapping(value = "/aboutHtml", method = RequestMethod.POST)	
	public AppResp aboutApp(@RequestParam("versionNumber")String versionNumber) {
		return new AppResp(config.getAppHtmlPath()+Const.TL_ABOUT_HTML5_PATH+"?versionNumber="+versionNumber,CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "跳转<app使用协议>html5页面", notes = "POST")
	@RequestMapping(value = "/policyHtml", method = RequestMethod.POST)	
	public AppResp policyApp() {
		return new AppResp(config.getAppHtmlPath() + Const.TL_POLICY_HTML5_PATH,CodeDef.SUCCESS);
	}

}
