package net.riking.web.appInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.service.repo.AppVersionRepo;
import net.riking.service.repo.ReportListRepo;
import net.riking.service.repo.impl.GetDateServiceImpl;

/**
 * app的公用接口
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/common")
public class CommonController {

	@Autowired
	GetDateServiceImpl getDateService;
	
	@Autowired
	AppVersionRepo appVersionRepo;
	
	@Autowired
	ReportListRepo reportListRepo;
	
	@ApiOperation(value = "获取系统时间", notes = "POST")
	@RequestMapping(value = "/getDate", method = RequestMethod.GET)
	public Resp getDate_() {
		String date =  getDateService.getDate();
		return new Resp(date, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "获取系统版本", notes = "POST")
	@RequestMapping(value = "/getappVersion_", method = RequestMethod.POST)
	public Resp getappVersion_() {
		//AppVersion appVersion = appVersionRepo.findFirstByDeleteStateOrderByRenewalTime();
 		return new Resp(null, CodeDef.SUCCESS);
	}
	

}
