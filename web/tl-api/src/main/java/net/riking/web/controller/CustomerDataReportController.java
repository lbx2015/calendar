package net.riking.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.DataDictService;
import net.riking.entity.model.AmlRuleEngine;
import net.riking.entity.model.AmlSuspiciousQuery;
import net.riking.service.repo.AmlRuleEngineRepo;
import net.riking.service.repo.CustomerDataReportRepo;

@RestController
@RequestMapping("/customerDataReport")
public class CustomerDataReportController {
	@Autowired
	CustomerDataReportRepo customerDataReportRepo;
	@Autowired
	DataDictService dataDictService;
	@Autowired
	AmlRuleEngineRepo amlRuleEngineRepo;
	@RequestMapping(value = "/counterpartyArea", method = RequestMethod.GET)
	public Resp getCounterpartyArea(@RequestParam String tcid, @RequestParam String start,@RequestParam String end) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AmlSuspiciousQuery> list = customerDataReportRepo.getArea(sdf.parse(start),sdf.parse(end),tcid);
		if (list.size() > 0) {
			List<ModelPropDict> list2 = dataDictService.getDicts("T_AML_SUSPICIOUS", "COUNTRY");
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

	@RequestMapping(value = "/customerTradeWay", method = RequestMethod.GET)
	public Resp getCustomerTradeWay(@RequestParam String tcid, @RequestParam String start,@RequestParam String end) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AmlSuspiciousQuery> list = customerDataReportRepo.getWay(sdf.parse(start),sdf.parse(end),tcid);
		if (list.size() > 0) {
			List<ModelPropDict> list2 = dataDictService.getDicts("T_AML_BIGAMOUNT","TSTP");
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

	@RequestMapping(value = "/customerFundFlows", method = RequestMethod.GET)
	public Resp getCustomerFundFlows(@RequestParam String tcid,  @RequestParam String start,@RequestParam String end) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AmlSuspiciousQuery> list = customerDataReportRepo.getFlow(sdf.parse(start),sdf.parse(end),tcid);
		if (list.size() > 0) {
			List<ModelPropDict> list2 = dataDictService.getDicts("T_AML_SUSPICIOUS","COUNTRY");
			for (AmlSuspiciousQuery amlSuspiciousQuery : list) {
				for (ModelPropDict modelPropDict : list2) {
					if (amlSuspiciousQuery.getStr() != null) {
						if (amlSuspiciousQuery.getStr().contains(modelPropDict.getKe())) {
							amlSuspiciousQuery.setStr(modelPropDict.getValu());
						}else if(amlSuspiciousQuery.getStr().contains("CHN")){
							amlSuspiciousQuery.setStr("中国大陆地区一般贸易区");
						}else if(amlSuspiciousQuery.getStr().contains("Z01")){
							amlSuspiciousQuery.setStr("中国大陆地区保税区");
						}else if(amlSuspiciousQuery.getStr().contains("Z02")){
							amlSuspiciousQuery.setStr("中国大陆地区加工区");
						}else if(amlSuspiciousQuery.getStr().contains("Z03")){
							amlSuspiciousQuery.setStr("中国大陆地区钻石交易所");
						}
					}
				}
			}
		}
		return new Resp(list, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/customerRule", method = RequestMethod.GET)
	public Resp getCustomerRule(@RequestParam String tcid,  @RequestParam String start,@RequestParam String end) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AmlSuspiciousQuery> list = new ArrayList<AmlSuspiciousQuery>();
		List<String> list2 = customerDataReportRepo.getRule(sdf.parse(start),sdf.parse(end),tcid);
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
