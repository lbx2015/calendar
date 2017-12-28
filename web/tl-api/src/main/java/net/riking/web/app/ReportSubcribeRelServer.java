package net.riking.web.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.dao.repo.IndustryRepo;
import net.riking.dao.repo.ReportRepo;
import net.riking.dao.repo.ReportSubscribeRelRepo;
import net.riking.dao.repo.SysDaysRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.Period;
import net.riking.entity.model.QueryReport;
import net.riking.entity.model.ReportCompletedRel;
import net.riking.entity.model.ReportResult;
import net.riking.entity.model.SysDays;
import net.riking.service.ReportService;
import net.riking.service.ReportSubmitCaliberService;
import net.riking.service.SysDataService;
import net.riking.service.SysDateService;

/**
 * 用户获取所属的报表信息
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/reportSubcribeRel")
public class ReportSubcribeRelServer {

	// @Autowired
	// AppUserReportRelRepo appUserReportRepo;

	@Autowired
	SysDataService sysDataservice;

	@Autowired
	SysDateService sysDateService;

	@Autowired
	ReportSubmitCaliberService reportSubmitCaliberService;

	@Autowired
	SysDaysRepo sysDaysRepo;

	@Autowired
	IndustryRepo industryRepo;

	@Autowired
	ReportSubscribeRelRepo reportSubscribeRelRepo;

	// @Autowired
	// AppUserReportCompletRelRepo appUserReportCompletRelRepo;

	@Autowired
	ReportService reportService;

	@Autowired
	ReportRepo reportRepo;
	// @Autowired
	// Report reportLisService;

	@ApiOperation(value = "app获取用户下的报表", notes = "POST")
	@RequestMapping(value = "/getUserReport", method = RequestMethod.POST)
	public AppResp getUserReport(@RequestBody ReportCompletedRel reportCompletedRel) {
		SysDays day = sysDaysRepo.findOne(reportCompletedRel.getCompletedDate());
		Period period = sysDateService.getDate(reportCompletedRel.getCompletedDate(), "1");
		Period periods = sysDateService.getDate(reportCompletedRel.getCompletedDate(), "0");
		Set<QueryReport> set;
		Set<QueryReport> freeSet = new HashSet<>();
		if (day.getIsWork() == 1) {
			set = reportSubmitCaliberService.findAllByFreeDatefromReportId(reportCompletedRel.getUserId(),
					period.getWeek(), period.getTen(), period.getMonth(), period.getSeason(), period.getHalfYear(),
					period.getYear(), 1);
			freeSet = reportSubmitCaliberService.findAllByFreeDatefromReportId(reportCompletedRel.getUserId(),
					periods.getWeek(), periods.getTen(), periods.getMonth(), periods.getSeason(), periods.getHalfYear(),
					periods.getYear(), 0);
			for (QueryReport queryReport : freeSet) {
				set.add(queryReport);
			}
		} else {
			set = reportSubmitCaliberService.findAllByFreeDatefromReportId(reportCompletedRel.getUserId(),
					periods.getWeek(), periods.getTen(), periods.getMonth(), periods.getSeason(), periods.getHalfYear(),
					periods.getYear(), 0);
		}
		Map<String, Set<QueryReport>> map = new HashMap<>();
		for (QueryReport queryReport : set) {
			String value = sysDataservice.getDict("T_REPORT", "MODLE_TYPE", queryReport.getModuleType()).getValu();
			if (!map.containsKey(value)) {
				Set<QueryReport> set2 = new HashSet<>();
				set2.add(queryReport);
				map.put(value, set2);
			} else {
				Set<QueryReport> set2 = map.get(value);
				set2.add(queryReport);
				map.put(value, set2);
			}
		}
		List<ReportResult> listes = new ArrayList<>();
		for (String title : map.keySet()) {
			ReportResult reportResult = new ReportResult();
			reportResult.setTitle(title);
			// reportResult.setResult(map.get(title));
			listes.add(reportResult);
		}
		return new AppResp(listes, CodeDef.SUCCESS);
	}

	//
	// @ApiOperation(value = "历史核销和逾期报表", notes = "POST")
	// @RequestMapping(value = "/findAllUserReport", method = RequestMethod.POST)
	// public AppResp findAllUserReport(@RequestBody AppUserReportCompleteRel
	// appUserReportCompleteRel) {
	// List<AppUserReportCompleteRel> list =
	// reportSubmitCaliberService.findAllUserReport(appUserReportCompleteRel);
	// return new AppResp(list, CodeDef.SUCCESS);
	// }

}
