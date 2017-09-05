package net.riking.web.appInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUserReportCompleteRel;
import net.riking.entity.model.Days;
import net.riking.entity.model.Period;
import net.riking.entity.model.QueryReport;
import net.riking.entity.model.ReportList;
import net.riking.entity.model.ReportResult;
import net.riking.service.ReportSubmitCaliberService;
import net.riking.service.SysDataService;
import net.riking.service.impl.GetDateServiceImpl;
import net.riking.service.repo.AppUserReportRelRepo;
import net.riking.service.repo.DaysRepo;
import net.riking.service.repo.ReportListRepo;


/**用户获取所属的报表信息
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/appUserReport")
public class AppUserReportRelServer {

	@Autowired
	AppUserReportRelRepo appUserReportRepo;
	@Autowired
	ReportListRepo reportListRepo;
	@Autowired
	SysDataService sysDataservice;
	@Autowired
	GetDateServiceImpl getDateService;
	@Autowired
	ReportSubmitCaliberService reportSubmitCaliberService;
	@Autowired 
	DaysRepo daysRepo;
	

	@ApiOperation(value = "app获取用户下的报表", notes = "POST")
	@RequestMapping(value = "/getUserRepor", method = RequestMethod.POST)
	public AppResp getUserReport(@RequestBody AppUserReportCompleteRel appUserComplete){
		Days day = daysRepo.findOne(appUserComplete.getCompleteDate());
		Period period = getDateService.getDate(appUserComplete.getCompleteDate(),"1");
		Period periods = getDateService.getDate(appUserComplete.getCompleteDate(),"0");
		Set<QueryReport> set;
		Set<QueryReport> freeSet = new HashSet<>();
		if (day.getIsWork()==1) {
			set = reportSubmitCaliberService.findAllByFreeDatefromReportId(appUserComplete.getAppUserId(),period.getWeek(),period.getTen(),period.getMonth(),period.getSeason(),period.getHalfYear(),period.getYear(), 1);
			freeSet = reportSubmitCaliberService.findAllByFreeDatefromReportId(appUserComplete.getAppUserId(),periods.getWeek(),periods.getTen(),periods.getMonth(),periods.getSeason(),periods.getHalfYear(),periods.getYear(), 0);
			for (QueryReport queryReport : freeSet) {
				set.add(queryReport);
			}
		}else {
			set = reportSubmitCaliberService.findAllByFreeDatefromReportId(appUserComplete.getAppUserId(),periods.getWeek(),periods.getTen(),periods.getMonth(),periods.getSeason(),periods.getHalfYear(),periods.getYear(), 0);
		}
		Map<String, Set<QueryReport>> map = new HashMap<>();
		for (QueryReport queryReport : set) {
			String value = sysDataservice.getDict("T_REPORT_LIST", "MODLE_TYPE", queryReport.getModuleType()).getValu();
			if (!map.containsKey(value)) {
				Set<QueryReport> set2 = new HashSet<>();
				set2.add(queryReport);
				map.put(value,set2);
			}else {
				Set<QueryReport> set2 =map.get(value);
				set2.add(queryReport);
				map.put(value, set2);
			}
		}
		List<ReportResult> listes =  new ArrayList<>();
		for (String title : map.keySet()) {
			ReportResult reportResult = new ReportResult();
			reportResult.setTitle(title);
			reportResult.setResult(map.get(title));
			listes.add(reportResult);
		}
		return new AppResp(listes, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "用户添加所属报表", notes = "POST")
	@RequestMapping(value = "/userAddReport", method = RequestMethod.POST)
	public AppResp userAddReport_(@ModelAttribute String appUserId){
		Set<String>  reportIds = appUserReportRepo.findbyAppUserId(appUserId);
		List<ReportList> reportLists = reportListRepo.findByReoprtId(reportIds);
		return new AppResp(reportLists, CodeDef.SUCCESS);
	}
	
	
}
