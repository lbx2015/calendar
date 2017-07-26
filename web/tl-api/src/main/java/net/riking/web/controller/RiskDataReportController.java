package net.riking.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.DataDictService;
import net.riking.entity.model.AmlRuleEngine;
import net.riking.entity.model.AmlSuspiciousQuery;
import net.riking.service.repo.AmlRuleEngineRepo;
import net.riking.service.repo.RiskDataReport;

@RestController
@RequestMapping(value = "/riskDataReports")
public class RiskDataReportController {

	@Autowired
	Config config;

	@Autowired
	DataDictService dataDictService;

	@Autowired
	RiskDataReport riskDataReport;

	@Autowired
	AmlRuleEngineRepo amlRuleEngineRepo;


	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。

	@RequestMapping(value = "/getDistCustomer", method = RequestMethod.GET)
	public Resp getDistCustomer(@RequestParam("start") String start,@RequestParam("end") String end) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AmlSuspiciousQuery> list1 = riskDataReport.getCust(sdf.parse(start),sdf.parse(end));
		return new Resp(list1, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getWork", method = RequestMethod.GET)
	public Resp getWork(@RequestParam("start") String start,@RequestParam("end") String end) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AmlSuspiciousQuery> list = riskDataReport.getWork(sdf.parse(start),sdf.parse(end));
		if (list.size() > 0) {
			HashSet<String> set = new HashSet<String>();
			set.add("DGDS");
			List<ModelPropDict> list2 = dataDictService.getDictsByFields("T_AML_BIGAMOUNT", set);
			for (ModelPropDict modelPropDict : list2) {
				for (AmlSuspiciousQuery amlSuspiciousQuery : list) {
					if (amlSuspiciousQuery.getStr() != null
							&& amlSuspiciousQuery.getStr().equals(modelPropDict.getKe())) {
						amlSuspiciousQuery.setStr(modelPropDict.getValu());
					}
				}
			}
		}
		return new Resp(list, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getArea", method = RequestMethod.GET)
	public Resp getArea(@RequestParam("start") String start,@RequestParam("end") String end) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AmlSuspiciousQuery> list = riskDataReport.getArea(sdf.parse(start),sdf.parse(end));
		if (list.size() > 0) {
			HashSet<String> set = new HashSet<String>();
			set.add("COUNTRY");
			List<ModelPropDict> list2 = dataDictService.getDictsByFields("T_AML_SUSPICIOUS", set);
			for (ModelPropDict modelPropDict : list2) {
				for (AmlSuspiciousQuery amlSuspiciousQuery : list) {
					if (amlSuspiciousQuery.getStr() != null
							&& amlSuspiciousQuery.getStr().equals(modelPropDict.getKe())) {
						amlSuspiciousQuery.setStr(modelPropDict.getValu());
					}
				}
			}
		}
		return new Resp(list, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getWay", method = RequestMethod.GET)
	public Resp getWay(@RequestParam("start") String start,@RequestParam("end") String end) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AmlSuspiciousQuery> list = riskDataReport.getWay(sdf.parse(start),sdf.parse(end));
		if (list.size() > 0) {
			HashSet<String> set = new HashSet<String>();
			set.add("TSTP");
			List<ModelPropDict> list2 = dataDictService.getDictsByFields("T_AML_BIGAMOUNT", set);
			for (ModelPropDict modelPropDict : list2) {
				for (AmlSuspiciousQuery amlSuspiciousQuery : list) {
					if (amlSuspiciousQuery.getStr() != null
							&& amlSuspiciousQuery.getStr().equals(modelPropDict.getKe())) {
						amlSuspiciousQuery.setStr(modelPropDict.getValu());
					}
				}
			}
		}
		return new Resp(list, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getFlow", method = RequestMethod.GET)
	public Resp getFlow(@RequestParam("start") String start,@RequestParam("end") String end) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AmlSuspiciousQuery> list = riskDataReport.getFlow(sdf.parse(start),sdf.parse(end));
		if (list.size() > 0) {
			HashSet<String> set = new HashSet<String>();
			set.add("COUNTRY");
			List<ModelPropDict> list2 = dataDictService.getDictsByFields("T_AML_SUSPICIOUS", set);
			for (AmlSuspiciousQuery amlSuspiciousQuery : list) {
				for (ModelPropDict modelPropDict : list2) {
					if (amlSuspiciousQuery.getStr() != null) {
						if (amlSuspiciousQuery.getStr().contains(modelPropDict.getKe())) {
							amlSuspiciousQuery.setStr(modelPropDict.getValu());
						} else if (amlSuspiciousQuery.getStr().contains("CHN")) {
							amlSuspiciousQuery.setStr("中国大陆地区一般贸易区");
						} else if (amlSuspiciousQuery.getStr().contains("Z01")) {
							amlSuspiciousQuery.setStr("中国大陆地区保税区");
						} else if (amlSuspiciousQuery.getStr().contains("Z02")) {
							amlSuspiciousQuery.setStr("中国大陆地区加工区");
						} else if (amlSuspiciousQuery.getStr().contains("Z03")) {
							amlSuspiciousQuery.setStr("中国大陆地区钻石交易所");
						}
					}
				}
			}
		}
		return new Resp(list, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getRule", method = RequestMethod.GET)
	public Resp getRule(@RequestParam("start") String start,@RequestParam("end") String end) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AmlSuspiciousQuery> list = new ArrayList<AmlSuspiciousQuery>();
		List<String> list2 = riskDataReport.getRule(sdf.parse(start),sdf.parse(end));
		List<AmlRuleEngine> ruleList = amlRuleEngineRepo.findAll();
		AmlSuspiciousQuery query;
		long sum;
		for (int i = 0; i < ruleList.size(); i++) {
			sum = 0;
			for (int j = 0; j < list2.size(); j++) {
				if (list2.get(j) != null) {
					if (list2.get(j).contains(ruleList.get(i).getRuleNo())) {
						sum++;
					}
				}
			}
			if (sum > 0) {
				query = new AmlSuspiciousQuery(ruleList.get(i).getRuleNo(), sum, ruleList.get(i).getRuleName());
				list.add(query);
			}
		}
		return new Resp(list, CodeDef.SUCCESS);
	}
}
