package net.riking.web.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.dao.repo.IndustryRepo;
import net.riking.dao.repo.ReportRepo;
import net.riking.dao.repo.ReportSubcribeRelRepo;
import net.riking.dao.repo.SysDaysRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUserReportResult;
import net.riking.entity.model.Period;
import net.riking.entity.model.QueryReport;
import net.riking.entity.model.ReportCompletedRel;
import net.riking.entity.model.ReportResult;
import net.riking.entity.model.ReportSubcribeRel;
import net.riking.entity.model.SysDays;
import net.riking.service.ReportService;
import net.riking.service.ReportSubmitCaliberService;
import net.riking.service.SysDataService;
import net.riking.service.SysDateService;
import net.riking.util.Utils;

/**
 * 用户获取所属的报表信息
 * @author Lucky.Liu on 2017/8/05.
 */
@RestController
@RequestMapping(value = "/reportSubcribeRel")
public class ReportSubcribeRelServer {

	// @Autowired
	// AppUserReportRelRepo appUserReportRepo;

	@Autowired
	SysDataService sysDataservice;

	@Autowired
	SysDateService sysDateService;

	@Autowired
	ReportSubmitCaliberService reportSubmitCaliberService;

	@Autowired
	SysDaysRepo sysDaysRepo;

	@Autowired
	IndustryRepo industryRepo;

	@Autowired
	ReportSubcribeRelRepo reportSubcribeRelRepo;

	// @Autowired
	// AppUserReportCompletRelRepo appUserReportCompletRelRepo;

	@Autowired
	ReportService reportService;

	@Autowired
	ReportRepo reportRepo;
	// @Autowired
	// Report reportLisService;

	@ApiOperation(value = "app获取用户下的报表", notes = "POST")
	@RequestMapping(value = "/getUserReport", method = RequestMethod.POST)
	public AppResp getUserReport(@RequestBody ReportCompletedRel reportCompletedRel) {
		SysDays day = sysDaysRepo.findOne(reportCompletedRel.getCompletedDate());
		Period period = sysDateService.getDate(reportCompletedRel.getCompletedDate(), "1");
		Period periods = sysDateService.getDate(reportCompletedRel.getCompletedDate(), "0");
		Set<QueryReport> set;
		Set<QueryReport> freeSet = new HashSet<>();
		if (day.getIsWork() == 1) {
			set = reportSubmitCaliberService.findAllByFreeDatefromReportId(reportCompletedRel.getUserId(),
					period.getWeek(), period.getTen(), period.getMonth(), period.getSeason(), period.getHalfYear(),
					period.getYear(), 1);
			freeSet = reportSubmitCaliberService.findAllByFreeDatefromReportId(reportCompletedRel.getUserId(),
					periods.getWeek(), periods.getTen(), periods.getMonth(), periods.getSeason(), periods.getHalfYear(),
					periods.getYear(), 0);
			for (QueryReport queryReport : freeSet) {
				set.add(queryReport);
			}
		} else {
			set = reportSubmitCaliberService.findAllByFreeDatefromReportId(reportCompletedRel.getUserId(),
					periods.getWeek(), periods.getTen(), periods.getMonth(), periods.getSeason(), periods.getHalfYear(),
					periods.getYear(), 0);
		}
		Map<String, Set<QueryReport>> map = new HashMap<>();
		for (QueryReport queryReport : set) {
			String value = sysDataservice.getDict("T_REPORT", "MODLE_TYPE", queryReport.getModuleType()).getValu();
			if (!map.containsKey(value)) {
				Set<QueryReport> set2 = new HashSet<>();
				set2.add(queryReport);
				map.put(value, set2);
			} else {
				Set<QueryReport> set2 = map.get(value);
				set2.add(queryReport);
				map.put(value, set2);
			}
		}
		List<ReportResult> listes = new ArrayList<>();
		for (String title : map.keySet()) {
			ReportResult reportResult = new ReportResult();
			reportResult.setTitle(title);
//			reportResult.setResult(map.get(title));
			listes.add(reportResult);
		}
		return new AppResp(listes, CodeDef.SUCCESS);
	}

	// @ApiOperation(value = "用户添加所属报表", notes = "POST")
	// @RequestMapping(value = "/userAddReport", method = RequestMethod.POST)
	// public AppResp userAddReport_(@ModelAttribute String appUserId){
	// Set<String> reportIds = appUserReportRepo.findbyAppUserId(appUserId);
	// List<ReportList> reportLists = reportListRepo.findByReoprtId(reportIds);
	// return new AppResp(reportLists, CodeDef.SUCCESS);
	// }

	// @ApiOperation(value = "获取行业列表", notes = "POST")
	// @RequestMapping(value = "/findIndustry", method = RequestMethod.POST)
	// public AppResp findIndustry(){
	// //List<Industry> list = industryRepo.findIndustry("0");//查询行业
	// return new AppResp(industryRepo.findIndustry(0),CodeDef.SUCCESS);
	// }
	//
	// @ApiOperation(value = "获取行业下面的职位列表", notes = "POST")
	// @RequestMapping(value = "/getPositionByIndustry", method = RequestMethod.POST)
	// public AppResp getPositionByIndustry(@RequestParam("id") Long id){
	// return new AppResp(industryRepo.findPositionByIndustry(id),CodeDef.SUCCESS);
	// }
	//
	// @ApiOperation(value = "获取职位关联的订阅", notes = "POST")
	// @RequestMapping(value = "/getReportList", method = RequestMethod.POST)
	// public AppResp getReportList(){
	//
	// return new AppResp(CodeDef.SUCCESS);
	// }

	@ApiOperation(value = "查询用户订阅的报表", notes = "POST")
	@RequestMapping(value = "/findUserReportList", method = RequestMethod.POST)
	public AppResp findUserReportList(@RequestParam("userId") String userId) {
		// List<Report> list = appUserReportRepo.findUserReportList(appUser.getId());
		// List<ReportFrequency> list = reportLisService.findAppUserReportById(userId);
		List<Map<String, Object>> newsInfoMapList = new ArrayList<Map<String, Object>>();
		List<ReportSubcribeRel> ReportSubcribeRelList = reportRepo.findByUserId(userId);
		for (ReportSubcribeRel reportSubcribeRel : ReportSubcribeRelList) {
			Map<String, Object> newsInfoMap = Utils.objProps2Map(reportSubcribeRel, true);
			newsInfoMapList.add(newsInfoMap);
		}
		return new AppResp(newsInfoMapList, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "更新用户报表订阅", notes = "POST")
	@RequestMapping(value = "/userAddReportEdit", method = RequestMethod.POST)
	public AppResp userAddReportEdit(@RequestBody AppUserReportResult appUserReportResult) {
		// 根据userId查询出数据库里订阅的List
		// List<AppUserReportRel> list =
		// appUserReportRepo.findUserReportList(appUserReportResult.getUserId());
		List<String> idList = appUserReportResult.getList();// 传过来的id集合
		// 全部删除
		reportSubcribeRelRepo.deleteReportRelByUserId(appUserReportResult.getUserId());
		// 批量插入
		ReportSubcribeRel reportSubcribeRel = null;
		for (String string : idList) {
			reportSubcribeRel = new ReportSubcribeRel();
			reportSubcribeRel.setUserId(appUserReportResult.getUserId());
			reportSubcribeRel.setReportId(string);
			reportSubcribeRel.setIsComplete(0);// 未完成
			reportSubcribeRelRepo.save(reportSubcribeRel);
		}
		// if(idList.size() == 0){
		// //全部删除
		// appUserReportRepo.deleteReportRelByUserId(appUserReportResult.getUserId());
		// }else{
		// //根据id查询用户订阅的报表
		// List<String> list =
		// appUserReportRepo.findReportByUserId(appUserReportResult.getUserId());
		//
		// AppUserReportRel appUserReportRel = null;
		// if(list != null && list.size() > 0){//之前有订阅
		// List<String> idList2 = appUserReportResult.getList();//传过来的id集合
		// //传过来的集合 和 数据库集合 差集
		// idList.removeAll(list);
		// for (String string : idList) {
		// appUserReportRel = new AppUserReportRel();
		// appUserReportRel.setAppUserId(appUserReportResult.getUserId());
		// appUserReportRel.setReportId(string);
		// appUserReportRel.setIsComplete("0");//未完成
		// appUserReportRepo.save(appUserReportRel);
		// }
		//
		// list.removeAll(idList2);
		// if(list !=null && list.size() > 0){
		// String strList = String.join(",", list);
		// appUserReportRepo.deleteReportRel(appUserReportResult.getUserId(), strList);
		// }
		// }else{//没有订阅过
		// for (String string : idList) {
		// appUserReportRel = new AppUserReportRel();
		// appUserReportRel.setAppUserId(appUserReportResult.getUserId());
		// appUserReportRel.setReportId(string);
		// appUserReportRel.setIsComplete("0");//未完成
		// appUserReportRepo.save(appUserReportRel);
		// }
		// }
		// }

		return new AppResp(CodeDef.SUCCESS);
	}
	// TODO
	// @ApiOperation(value = "新增报表订阅", notes = "POST")
	// @RequestMapping(value = "/updateUserReportRelById", method = RequestMethod.POST)
	// public AppResp updateUserReportRelById(@RequestBody ReportSubcribeRel reportSubcribeRel) {
	// if (reportSubcribeRel.getType().equals("0")) {// 取消订阅
	// appUserReportRepo.deleteReportRelByUserIdAndReportId(appUserReportRel.getAppUserId(),
	// appUserReportRel.getReportId());
	// } else {
	// appUserReportRel.setIsComplete("1");
	// appUserReportRepo.save(appUserReportRel);
	// }
	//
	// return new AppResp(CodeDef.SUCCESS);
	// }
	//
	// @ApiOperation(value = "历史核销和逾期报表", notes = "POST")
	// @RequestMapping(value = "/findAllUserReport", method = RequestMethod.POST)
	// public AppResp findAllUserReport(@RequestBody AppUserReportCompleteRel
	// appUserReportCompleteRel) {
	// List<AppUserReportCompleteRel> list =
	// reportSubmitCaliberService.findAllUserReport(appUserReportCompleteRel);
	// return new AppResp(list, CodeDef.SUCCESS);
	// }

}
