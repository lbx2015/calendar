package net.riking.web.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.entity.model.ReportList;
import net.riking.service.repo.AppUserReportRepo;
import net.riking.service.repo.ReportListRepo;

@RestController
@RequestMapping(value = "/AppUserReport")
public class AppUserReportRelController {
	@Autowired
	AppUserReportRepo appUserReportRepo;
	@Autowired
	ReportListRepo reportListRepo;
	

	@ApiOperation(value = "获取用户下的报表", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp get(@ModelAttribute String appUserId){
		Set<String>  reportIds = appUserReportRepo.findbyAppUserId(appUserId);
		List<ReportList> reportLists = reportListRepo.findbyReoprtId(reportIds);
		return new Resp(reportLists, CodeDef.SUCCESS);
	}
}
