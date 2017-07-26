package net.riking.web.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.riking.config.Config;
import net.riking.core.entity.Resp;
import net.riking.core.entity.TokenHolder;
import net.riking.service.UserBranchServiceImpl;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.BigAmountRepo;

@RestController
@RequestMapping(value = "/exhibition")
public class ExhibitionController {

	// private static final boolean BigAmount = false;

	@Autowired
	Config config;

	@Autowired
	BigAmountRepo bigAmountRepo;

	@Autowired
	AmlSuspiciousRepo amlSuspiciousRepo;
	
	@Autowired
	UserBranchServiceImpl userBranchServiceImpl;

	@RequestMapping(value = "/getCount", method = RequestMethod.GET)
	public Resp getCount(@RequestParam String start, @RequestParam String end) throws Exception {
		Map<String, Object> countMap = new HashMap<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		HashSet<String> set = new HashSet<String>();
		HashSet<String> set2 = new HashSet<String>();
		List<String> set3= userBranchServiceImpl.get(TokenHolder.get().getUserId());
		if (TokenHolder.get().getBranchId()==null) {
			set3.add("");
		}
		set.add("PRE_EXPORTOVER");
		set.add("PRE_STORAGE");
		set2.add("SISP_PRE_END");
		Integer b_list = null;
		Integer b_list2 = null;
		Integer list = null;
		Integer list2 = null;
		Integer list3 = null;
		if (start.equals("") && end.equals("")) {
			// 大额总交易
			b_list = bigAmountRepo.findByRpdtStart(set3);
			// 大额已上报
			b_list2 = bigAmountRepo.findByRpdtDate(set,set3);
			// 可疑总量
			list = amlSuspiciousRepo.findByRpdts(set3);
			// 可疑已上报
			list2 = amlSuspiciousRepo.findByRpdtbetween(set,set3);
			// 可以不上报
			list3 = amlSuspiciousRepo.findByRpdtbetween(set2,set3);
		}else{
			if (start.equals("")) {
				b_list = bigAmountRepo.findByRpdtEnd(sdf.parse(end),set3);
				b_list2 =bigAmountRepo.findByRpdtEndAndStatus(sdf.parse(end),set,set3);
				list =amlSuspiciousRepo.findByRpdtEnds(sdf.parse(end),set3);
				list2 =amlSuspiciousRepo.findByRpdtEnd(sdf.parse(end), set,set3);
				list3 =amlSuspiciousRepo.findByRpdtEnd(sdf.parse(end), set2,set3);
			} else if (end.equals("")) {
				b_list = bigAmountRepo.findByRpdtStart(sdf.parse(start),set3);
				b_list2 =bigAmountRepo.findByRpdtStartAndStatus(sdf.parse(start), set,set3);
				list =amlSuspiciousRepo.findByRpdtStarts(sdf.parse(start),set3);
				list2 =amlSuspiciousRepo.findByRpdtStart(sdf.parse(start), set,set3);
				list3 =amlSuspiciousRepo.findByRpdtStart(sdf.parse(start), set2,set3);
			} else {
				// 大额总交易
				b_list = bigAmountRepo.findByRpdtbetween(sdf.parse(start), sdf.parse(end),set3);
				// 大额已上报
				b_list2 = bigAmountRepo.findByRpdtbetweenDateAndStatus(sdf.parse(start), sdf.parse(end), set,set3);
				// 可疑总量
				list = amlSuspiciousRepo.findByRpdtbetweens(sdf.parse(start), sdf.parse(end),set3);
				// 可疑已上报
				list2 = amlSuspiciousRepo.findByRpdtbetween(sdf.parse(start), sdf.parse(end), set,set3);
				// 可以不上报
				list3 = amlSuspiciousRepo.findByRpdtbetween(sdf.parse(start), sdf.parse(end), set2,set3);
			}
		} 

		countMap.put("bigYiExport", b_list2);
		// 大额未上报
		countMap.put("bigWeiExport", b_list - b_list2);
		// 大额交易量
		countMap.put("bigAmount", b_list);

		countMap.put("amlSuspicious", list);

		countMap.put("susDot", list3);
		// 可疑未上报
		countMap.put("susWeiExport", list - list2 - list3);
		countMap.put("total", list + b_list);
		countMap.put("susYiExport", list2);

		return new Resp(countMap);
	}
}
