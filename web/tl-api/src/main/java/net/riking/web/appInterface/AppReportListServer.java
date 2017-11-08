package net.riking.web.appInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.QueryReport;
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

	/*@ApiOperation(value = "app获取所有的报表", notes = "POST")
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
	}*/
	//第一版本的写法
//	@ApiOperation(value = "app获取所有的报表", notes = "POST")
//	@RequestMapping(value = "/getAllReport", method = RequestMethod.POST)
//	public AppResp getAllReport() {
//		Set<QueryReport> set;
//			set = reportSubmitCaliberService.findAllfromReportId();
//		Map<String, Set<QueryReport>> map = new HashMap<>();
//		for (QueryReport queryReport : set) {
//													//String table, String field, String key
//			String value = sysDataservice.getDict("T_REPORT_LIST", "MODLE_TYPE", queryReport.getModuleType()).getValu();
//			if (!map.containsKey(value)) {
//				Set<QueryReport> set2 = new HashSet<>();
//				set2.add(queryReport);
//				map.put(value,set2);
//			}else {
//				Set<QueryReport> set2 =map.get(value);
//				set2.add(queryReport);
//				map.put(value, set2);
//			}
//		}
//		List<ReportResult> listes =  new ArrayList<>();
//		for (String title : map.keySet()) {
//			ReportResult reportResult = new ReportResult();
//			reportResult.setTitle(title);
//			reportResult.setResult(map.get(title));
//			listes.add(reportResult);
//		}
//		return new AppResp(listes, CodeDef.SUCCESS);
//		
//	}
	
	/**
	 * 
	 * @author tao.yuan
	 * @version crateTime：2017年11月6日 下午3:41:08
	 * @used TODO
	 * @return
	 */
	@ApiOperation(value = "app获取所有的报表", notes = "POST")
	@RequestMapping(value = "/getAllReport", method = RequestMethod.POST)
	public AppResp getAllReport() {
		List<QueryReport> list = reportSubmitCaliberService.findAllReport();
		return new AppResp(list, CodeDef.SUCCESS);
	}
}
