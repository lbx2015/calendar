package net.riking.web.appInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.service.impl.GetDateServiceImpl;
import net.riking.service.repo.AppVersionRepo;
import net.riking.service.repo.ReportListRepo;

/**
 * app的公用接口
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/common")
public class CommonServer {

	@Autowired
	GetDateServiceImpl getDateService;

	@Autowired
	AppVersionRepo appVersionRepo;

	@Autowired
	ReportListRepo reportListRepo;

	@ApiOperation(value = "获取系统时间", notes = "POST")
	@RequestMapping(value = "/getDate", method = RequestMethod.POST)
	public AppResp getDate_() {
		String date = getDateService.getDate();
		return new AppResp(date, CodeDef.SUCCESS);
	}

	// TODO 后面要打开
	// @ApiOperation(value = "获取系统版本", notes = "POST")
	// @RequestMapping(value = "/getappVersion", method = RequestMethod.POST)
	// public AppResp getappVersion(@RequestBody AppVersion app ) {
	// List<AppVersion> appVersion =
	// appVersionRepo.getByVersionNumber(app.getVersionNumber(),app.getType());
	// if (appVersion.size()>0) {
	// for (int i = 0; i < appVersion.size(); i++) {
	// if(null==appVersion.get(i).getApkUrl()){
	// appVersion.get(i).setApkUrl("");
	// }
	// if (null!=appVersion.get(i).getForces()&&"Y".equals(appVersion.get(i).getForces())) {
	// return new AppResp(new
	// AppVersionResult("2",appVersion.get(0).getVersionNote(),appVersion.get(0).getVersionNumber(),appVersion.get(0).getApkUrl()),
	// CodeDef.SUCCESS);
	// }
	// }
	// return new AppResp(new
	// AppVersionResult("1",appVersion.get(0).getVersionNote(),appVersion.get(0).getVersionNumber(),appVersion.get(0).getApkUrl()),
	// CodeDef.SUCCESS);
	// }else {
	// return new AppResp(new AppVersionResult("0","",appVersion.get(0).getVersionNumber(),""),
	// CodeDef.SUCCESS);
	// }
	// }

}
