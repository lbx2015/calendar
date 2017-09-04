package net.riking.web.appInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.ReportList;
import net.riking.service.ReportSubmitCaliberService;
import net.riking.service.SysDataService;
import net.riking.service.impl.GetDateServiceImpl;
import net.riking.service.repo.AppUserReportRelRepo;
import net.riking.service.repo.DaysRepo;
import net.riking.service.repo.ReportListRepo;

/**
 * app端报表配置的增删改查
 * 
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/reportListApp")
public class AppReportListServer {
	@Autowired
	ReportListRepo reportListRepo;
	@Autowired
	AppUserReportRelRepo appUserReportRepo;
	@Autowired
	SysDataService sysDataservice;
	@Autowired
	GetDateServiceImpl getDateService;
	@Autowired
	ReportSubmitCaliberService reportSubmitCaliberService;
	@Autowired 
	DaysRepo daysRepo;

	@ApiOperation(value = "app获取所有的报表", notes = "POST")
	@RequestMapping(value = "/getAllReport", method = RequestMethod.POST)
	public AppResp getAllReport(@RequestBody ReportList reportList) {
		reportList.setDeleteState("1");
		PageRequest pageable = new PageRequest(reportList.getPcount(), reportList.getPindex(), null);
		if (StringUtils.isEmpty(reportList.getDeleteState())) {
			reportList.setDeleteState("1");
		}
		Example<ReportList> example = Example.of(reportList, ExampleMatcher.matchingAll());
		Page<ReportList> page = reportListRepo.findAll(example, pageable);
		return new AppResp(page, CodeDef.SUCCESS);
	}
	/*@ApiOperation(value = "app获取所有的报表", notes = "POST")
	@RequestMapping(value = "/getAllReport", method = RequestMethod.POST)
	public AppResp getAllReport(@RequestParam("date") String date,@RequestParam("userId") String userId) {
		Days day = daysRepo.findOne(date);
		Period period = getDateService.getDate(date,"1");
		Period periods = getDateService.getDate(date,"0");
		Set<QueryReport> set;
		Set<QueryReport> freeSet = new HashSet<>();
		if (day.getIsWork()==1) {
			set = reportSubmitCaliberService.findAllByFreeDatefromReportId(userId,period.getWeek(),period.getTen(),period.getMonth(),period.getSeason(),period.getHalfYear(),period.getYear(), 1);
			freeSet = reportSubmitCaliberService.findAllByFreeDatefromReportId(userId,periods.getWeek(),periods.getTen(),periods.getMonth(),periods.getSeason(),periods.getHalfYear(),periods.getYear(), 0);
			for (QueryReport queryReport : freeSet) {
				set.add(queryReport);
			}
		}else {
			set = reportSubmitCaliberService.findAllByFreeDatefromReportId(userId,periods.getWeek(),periods.getTen(),periods.getMonth(),periods.getSeason(),periods.getHalfYear(),periods.getYear(), 0);
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
		
	}*/
	
	/*@ApiOperation(value = "app获取所有的报表", notes = "POST")
	@RequestMapping(value = "/getAllReport", method = RequestMethod.POST)
	public AppResp getAllReport() {
		List<QueryReport>list =reportListRepo.findByDeleteState();
		Map<String, List<QueryReport>> map = new HashMap<>();
		for (QueryReport queryReport : list) {
			String value = sysDataservice.getDict("T_REPORT_LIST", "MODLE_TYPE", queryReport.getModuleType()).getValu();
			if (!map.containsKey(value)) {
				List<QueryReport>lists = new ArrayList<>();
				lists.add(queryReport);
				map.put(value,lists);
			}else {
				List<QueryReport>lists = map.get(value);
				lists.add(queryReport);
				map.put(value, lists);
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
	}*/
}
