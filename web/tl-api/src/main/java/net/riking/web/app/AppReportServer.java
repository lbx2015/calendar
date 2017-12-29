package net.riking.web.app;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.config.Const;
import net.riking.core.entity.PageQuery;
import net.riking.dao.repo.RemindRepo;
import net.riking.dao.repo.ReportCompletedRelRepo;
import net.riking.dao.repo.ReportRepo;
import net.riking.dao.repo.ReportSubscribeRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.Remind;
import net.riking.entity.model.Report;
import net.riking.entity.model.ReportCompletedRel;
import net.riking.entity.model.ReportListResult;
import net.riking.entity.model.ReportSubscribeRel;
import net.riking.entity.params.RCompletedRelParams;
import net.riking.entity.params.ReportCompletedRelParam;
import net.riking.entity.params.ReportParams;
import net.riking.entity.params.SubscribeReportParam;
import net.riking.entity.resp.CurrentReportTaskResp;
import net.riking.entity.resp.ReportCompletedRelResult;
import net.riking.service.ReportAgenceFrencyService;
import net.riking.service.ReportService;
import net.riking.service.ReportSubmitCaliberService;
import net.riking.service.SysDataService;
import net.riking.util.DateUtils;
import net.riking.util.MergeUtil;
import net.riking.util.Utils;

/**
 * 报表信息接口
 * @author jc.tan 2017年11月29日
 * @see
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/report")
public class AppReportServer {
	@Autowired
	ReportService reportService;
	
	@Autowired
	ReportRepo reportRepo;

	@Autowired
	ReportSubscribeRelRepo reportSubscribeRelRepo;

	@Autowired
	ReportCompletedRelRepo reportCompletedRelRepo;

	@Autowired
	SysDataService sysDataservice;

	@Autowired
	ReportSubmitCaliberService reportSubmitCaliberService;

	@Autowired
	ReportAgenceFrencyService reportAgenceFrencyService;

	@Autowired
	RemindRepo remindRepo;
	
	@Autowired
	Config config;
	
	
	@ApiOperation(value = "跳转<报文详情>html5页面", notes = "POST")
	@RequestMapping(value = "/reportHtml", method = RequestMethod.POST)	
	public AppResp reportApp(@RequestBody Report report) {
		return new AppResp(config.getAppHtmlPath() + Const.TL_REPORT_HTML5_PATH +"?id="+ report.getId() 
																		+"&url="+config.getAppApiPath(),CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "得到<单个>报表信息", notes = "GET")
	@RequestMapping(value = "/getOne", method = RequestMethod.GET)
	public AppResp getOne(@RequestParam("id") String id) {
		Report reportList = reportRepo.findOne(id);
		return new AppResp(reportList, CodeDef.SUCCESS);
	}
	

	/**
	 * 可根据报表名称查询相关报表
	 * @author james.you[userId]
	 * @param [userId] or [reportName]
	 * @version crateTime：2017年11月6日 下午3:41:08
	 * @used TODO
	 * @return
	 */
	@ApiOperation(value = "可根据报表名称查询相关报表", notes = "POST")
	@RequestMapping(value = "/getReports", method = RequestMethod.POST)
	public AppResp getReports_(@RequestBody Map<String, Object> params) {
		ReportParams reportParams = Utils.map2Obj(params, ReportParams.class);
		if (StringUtils.isBlank(reportParams.getUserId())) {
			reportParams.setUserId("");
		}
		if (StringUtils.isBlank(reportParams.getReportName())) {
			reportParams.setReportName("");
		}
		List<ReportListResult> reportListResult = reportService.getReportByParam(reportParams.getReportName(),
				reportParams.getUserId());

		return new AppResp(reportListResult, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "查询用户订阅的报表", notes = "POST")
	@RequestMapping(value = "/findSubscribeReportList", method = RequestMethod.POST)
	public AppResp findSubscribeReportList_(@RequestBody HashMap<String, String> params) {
		List<ReportSubscribeRel> list = reportSubscribeRelRepo.findSubscribeReportList(params.get("userId"));
		return new AppResp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "更新用户报表订阅", notes = "POST")
	@RequestMapping(value = "/modifySubscribeReport", method = RequestMethod.POST)
	public AppResp modifySubscribeReport_(@RequestBody Map<String, Object> params) {
		SubscribeReportParam relParam = Utils.map2Obj(params, SubscribeReportParam.class);
		String[] arr = {};
		if (StringUtils.isNotBlank(relParam.getReportIds())) {
			arr = relParam.getReportIds().split(",");
		}

		String currentDate = DateUtils.getDate("yyyyMMdd");
		reportService.addReportTaskByUserSubscribe(relParam.getUserId(), arr, currentDate);
		return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "查询逾期报表", notes = "POST")
	@RequestMapping(value = "/findExpireTasks", method = RequestMethod.POST)
	public AppResp findExpireTasks_(@RequestBody Map<String, Object> params) {
		ReportCompletedRelParam relParams = Utils.map2Obj(params, ReportCompletedRelParam.class);
		PageQuery page = new PageQuery();
		page.setPcount(Const.APP_PAGENO_30);
		page.setPindex(relParams.getPindex());
		List<ReportCompletedRelResult> list = reportService.findExpireReportByPage(relParams.getUserId(), page);

		return new AppResp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "查询历史核销报表", notes = "POST")
	@RequestMapping(value = "/findHisCompletedTasks", method = RequestMethod.POST)
	public AppResp findHisCompletedTasks_(@RequestBody Map<String, Object> params) {
		ReportCompletedRelParam relParams = Utils.map2Obj(params, ReportCompletedRelParam.class);
		PageQuery page = new PageQuery();
		page.setPcount(Const.APP_PAGENO_30);
		page.setPindex(relParams.getPindex());
		List<ReportCompletedRelResult> list = reportService.findHisCompletedReportByPage(relParams.getUserId(), page);
		return new AppResp(list, CodeDef.SUCCESS);
	}

	/**
	 * 获取当天未完成和已完成的报表任务
	 * @param params [userId] [currentDate]
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "获取当天未完成和已完成的报表任务", notes = "POST")
	@RequestMapping(value = "/findCurrentTasks", method = RequestMethod.POST)
	public AppResp findCurrentTasks_(@RequestBody Map<String, Object> params) throws ParseException {
		RCompletedRelParams relParams = Utils.map2Obj(params, RCompletedRelParams.class);
		List<CurrentReportTaskResp> list = reportService.findCurrentTasks(relParams.getUserId(),
				relParams.getCurrentDate());
		return new AppResp(list, CodeDef.SUCCESS);
	}

	/**
	 * 报表完成核销
	 * @author james.you
	 * @version crateTime：2017年12月16日 下午6:36:44
	 * @used TODO
	 * @param params
	 * @return
	 */
	@ApiOperation(value = "报表完成核销", notes = "POST")
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public AppResp complete_(@RequestBody Map<String, Object> params) {
		RCompletedRelParams relParams = Utils.map2Obj(params, RCompletedRelParams.class);

		if (relParams.getIsCompleted() == 0) {// 未完成
			reportCompletedRelRepo.updateNotComplated(relParams.getUserId(), relParams.getReportId(),
					relParams.getSubmitStartTime(), relParams.getSubmitEndTime());

		} else {// 完成
			String currentDate = DateUtils.getDate("yyyyMMddHHmm");
			
			// 逾期任务核销，给予提示
			if (Long.parseLong(currentDate) > Long.parseLong(relParams.getSubmitEndTime())) {
				return new AppResp(CodeDef.EMP.REPORT_EXPIRETASK_ERROR, CodeDef.EMP.REPORT_EXPIRETASK_ERROR_DESC);
			}
			
			// 未到核销时间，给予提示
			if (Long.parseLong(currentDate) < Long.parseLong(relParams.getSubmitStartTime())) {
				return new AppResp(CodeDef.EMP.REPORT_NOTTO_COMPLETEDATE_ERROR, CodeDef.EMP.REPORT_NOTTO_COMPLETEDATE_ERROR_DESC);
			}
			
			reportCompletedRelRepo.updateComplated(relParams.getUserId(), relParams.getReportId(),
					relParams.getSubmitStartTime(), relParams.getSubmitEndTime(), currentDate);
			// 移除闹钟设置记录
			if (null != relParams.getRemindId()) {
				Remind entity = new Remind();
				entity.setRemindId(relParams.getRemindId());
				remindRepo.delete(entity);
			}

		}

		return new AppResp(Const.EMPTY, CodeDef.SUCCESS);
	}

	/**
	 * 获取当天之后的打点日期[userId, currentMonth]
	 * @param params
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "获取当月任务日期", notes = "POST")
	@RequestMapping(value = "/getTaskDate", method = RequestMethod.POST)
	public AppResp getTaskDate_(@RequestBody Map<String, Object> params) throws ParseException {
		ReportCompletedRelParam relParams = Utils.map2Obj(params, ReportCompletedRelParam.class);
		String currentMonth = relParams.getCurrentMonth();
		List<ReportCompletedRel> taskList = reportCompletedRelRepo.findTasksByUserId(relParams.getUserId(),
				relParams.getCurrentMonth());

		// 有任务的日期
		List<String> taskDateList = new ArrayList<String>();
		for (ReportCompletedRel data : taskList) {
			String submitEndDate = data.getSubmitEndTime().substring(0, 6);
			if (Integer.parseInt(submitEndDate) == Integer.parseInt(currentMonth)) {
				// 如果是当月情况，添加时间
				String submitEndDateDay = data.getSubmitEndTime().substring(6, 8);
				for (int i = 1; i <= Integer.parseInt(submitEndDateDay); i++) {
					String _date = submitEndDate + (i < 10 ? "0" + i : i);
					if (!taskDateList.contains(_date)){
						// 判断是否与国家节假日，延迟上报截止时间
						_date = Utils.getWorkday(_date);
						taskDateList.add(_date);
					}
						
				}
			} else {
				// 非当月，则添加当月所有日期
				int _daysOfMonth = DateUtils.getDaysByMonth(currentMonth);
				for (int i = 1; i <= _daysOfMonth; i++) {
					String _date = currentMonth + (i < 10 ? "0" + i : i);
					if (!taskDateList.contains(_date)){
						// 判断是否与国家节假日，延迟上报截止时间
						_date = Utils.getWorkday(_date);
						taskDateList.add(_date);
					}
				}
			}
		}

		return new AppResp(taskDateList, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "用户提醒新增/修改", notes = "POST")
	@RequestMapping(value = "/remindSave", method = RequestMethod.POST)
	public AppResp save(@RequestBody Remind remind) {
		Remind remind2 = remindRepo.findOne(remind.getRemindId());
		if (null == remind2) {
			remind = remindRepo.save(remind);
		} else {
			try {
				remind2 = MergeUtil.merge(remind2, remind);
			} catch (Exception e) {
				return new AppResp(CodeDef.ERROR);
			}
			remind = remindRepo.save(remind2);
		}
		return new AppResp(remind, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "单条删除提醒信息", notes = "POST")
	@RequestMapping(value = "/remindDel", method = RequestMethod.POST)
	public AppResp delMore(@RequestBody Remind remind) {
		remindRepo.delete(remind.getRemindId());

		return new AppResp().setCode(CodeDef.SUCCESS);
	}

}
