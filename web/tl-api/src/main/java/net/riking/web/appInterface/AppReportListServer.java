package net.riking.web.appInterface;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.QueryReport;
import net.riking.entity.model.ReportList;
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
		//Map<String, List<ReportList>> map = new HashMap<>();
		List<QueryReport>list =reportListRepo.findByDeleteState();
		
		return new AppResp(list, CodeDef.SUCCESS);
	}
}
