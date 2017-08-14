package net.riking.web.appInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.record.common.FeatFormulaErr2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.QueryReport;
import net.riking.entity.model.ReportResult;
import net.riking.entity.model.ReportResultList;
import net.riking.service.SysDataService;
import net.riking.service.repo.AppUserReportRepo;
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
	AppUserReportRepo appUserReportRepo;
	@Autowired
	SysDataService sysDataservice;

/*	@ApiOperation(value = "app获取所有的报表", notes = "POST")
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
	@ApiOperation(value = "app获取所有的报表", notes = "POST")
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
		ReportResultList resultList = new ReportResultList();
		for (String title : map.keySet()) {
			ReportResult reportResult = new ReportResult();
			reportResult.setTitle(title);
			reportResult.setResult(map.get(title));
			resultList.getList().add(reportResult);
		}
		
		return new AppResp(resultList, CodeDef.SUCCESS);
	}
}
