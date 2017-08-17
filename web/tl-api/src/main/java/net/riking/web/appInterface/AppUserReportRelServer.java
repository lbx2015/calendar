package net.riking.web.appInterface;

import java.util.ArrayList;
import java.util.HashMap;
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
import net.riking.entity.model.AppUserReportRel;
import net.riking.entity.model.QueryReport;
import net.riking.entity.model.ReportList;
import net.riking.entity.model.ReportResult;
import net.riking.service.SysDataService;
import net.riking.service.repo.AppUserReportRelRepo;
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
	

	@ApiOperation(value = "app获取用户下的报表", notes = "POST")
	@RequestMapping(value = "/getUserRepor", method = RequestMethod.POST)
	public AppResp getUserReport(@RequestBody AppUserReportRel appUserReportRel){
		Set<String>  reportIds = appUserReportRepo.findbyAppUserId(appUserReportRel.getAppUserId());
		List<QueryReport> list = reportListRepo.findByIds(reportIds);
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
	}
	
	@ApiOperation(value = "用户添加所属报表", notes = "POST")
	@RequestMapping(value = "/userAddReport", method = RequestMethod.POST)
	public AppResp userAddReport_(@ModelAttribute String appUserId){
		Set<String>  reportIds = appUserReportRepo.findbyAppUserId(appUserId);
		List<ReportList> reportLists = reportListRepo.findByReoprtId(reportIds);
		return new AppResp(reportLists, CodeDef.SUCCESS);
	}
	
	
}
