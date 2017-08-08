package net.riking.web.appInterface;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
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

	@ApiOperation(value = "app获取所有的报表", notes = "POST")
	@RequestMapping(value = "/getAllReport", method = RequestMethod.POST)
	public Resp getAllReport(@RequestBody ReportList reportList) {
		reportList.setDeleteState("1");
		PageRequest pageable = new PageRequest(reportList.getPcount(), reportList.getPindex(), null);
		if (StringUtils.isEmpty(reportList.getDeleteState())) {
			reportList.setDeleteState("1");
		}
		Example<ReportList> example = Example.of(reportList, ExampleMatcher.matchingAll());
		Page<ReportList> page = reportListRepo.findAll(example, pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}
}
