package net.riking.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.dao.repo.ReportRepo;
import net.riking.service.SysDataService;

/**
 * 数据统计
 * @author jc.tan 2018年1月5日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/dataCount")
public class dataCountController {
	@Autowired
	SysDataService sysDataService;

	@Autowired
	ReportRepo reportRepo;

	@ApiOperation(value = "报表关注数统计", notes = "GET")
	@RequestMapping(value = "/reportFollowCount", method = RequestMethod.GET)
	public Resp reportFollowCount_() {
		List<ModelPropDict> modelPropDicts = sysDataService.getDicts("T_REPORT", "REPORT_TYPE");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (ModelPropDict modelPropDict : modelPropDicts) {
			Map<String, Object> map = new HashMap<String, Object>();
			Integer count = reportRepo.countSubscribeNumByReportType(modelPropDict.getKe());
			map.put("key", modelPropDict.getKe());
			map.put("value", count);
			list.add(map);
		}

		return new Resp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "报表类型关注数统计", notes = "GET")
	@RequestMapping(value = "/reportTypeFollowCount", method = RequestMethod.GET)
	public Resp reportTypeFollowCount_(@RequestParam("reportType") String reportType) {
		reportType = reportType.trim().toUpperCase();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		return new Resp(list, CodeDef.SUCCESS);
	}

}
