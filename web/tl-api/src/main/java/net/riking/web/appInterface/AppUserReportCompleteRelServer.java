package net.riking.web.appInterface;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUserReportCompleteRel;
import net.riking.entity.model.QureyResulte;
import net.riking.service.repo.AppUserReportCompletRelRepo;

/***
 * 获取完成报表
 * @author Lucky.Liu
 * @version crateTime：2017年8月23日 下午5:22:26
 * @used TODO
 */
@RestController
@RequestMapping(value = "/appUserReportCompleteRel")
public class AppUserReportCompleteRelServer {
	@Autowired
	AppUserReportCompletRelRepo appUserReportCompleteRelRepo;

	@ApiOperation(value = "用户报表完成情况新增", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public AppResp save(@RequestBody AppUserReportCompleteRel appUserReportCompleteRel) {
		appUserReportCompleteRelRepo.save(appUserReportCompleteRel);
		return new AppResp(CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "用户获取当天报表完成情况", notes = "POST")
	@RequestMapping(value = "/getAllReport", method = RequestMethod.POST)
	public AppResp getAllReport(@RequestBody AppUserReportCompleteRel completeRel) {
		List<QureyResulte> completeRels = appUserReportCompleteRelRepo.getReportId(completeRel.getAppUserId(),
				completeRel.getCompleteDate());
		return new AppResp(completeRels, CodeDef.SUCCESS);

	}
	
	@ApiOperation(value = "获取当天未完成和已完成的报表列表", notes = "POST")
	@RequestMapping(value = "/getReportByIdAndTime", method = RequestMethod.POST)
	public AppResp getReportByIdAndTime(@RequestBody AppUserReportCompleteRel completeRel){
		//List<>
		
		return new AppResp(CodeDef.SUCCESS);
	}
}
