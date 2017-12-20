package net.riking.web.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.entity.PageQuery;
import net.riking.dao.repo.RemindRepo;
import net.riking.dao.repo.ReportCompletedRelRepo;
import net.riking.dao.repo.ReportSubscribeRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.Remind;
import net.riking.entity.model.ReportCompletedRel;
import net.riking.entity.model.ReportListResult;
import net.riking.entity.model.ReportResult;
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
		//获取订阅关联表
		List<ReportSubscribeRel> reportSubcribeRelList = reportSubscribeRelRepo.findSubscribeReportList(reportParams.getUserId());
		List<ReportListResult> reportListResult = reportService.getReportByParam(reportParams.getReportName());
		
		for(ReportListResult rl : reportListResult){
			List<ReportResult> reportList = rl.getList();
			for(ReportResult r : reportList){
				for(ReportSubscribeRel rel : reportSubcribeRelList){
					if(r.getReportId().equals(rel.getReportId())){
						r.setIsSubscribe("1");//已订阅
					}
				}
			}
			
		}
		
		return new AppResp(reportListResult, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "查询用户订阅的报表", notes = "POST")
	@RequestMapping(value = "/findSubscribeReportList", method = RequestMethod.POST)
	public AppResp findSubscribeReportList_(@RequestParam("userId") String userId) {
		List<ReportSubscribeRel> list = reportSubscribeRelRepo.findSubscribeReportList(userId);
		return new AppResp(list, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "更新用户报表订阅", notes = "POST")
	@RequestMapping(value = "/modifySubscribeReport", method = RequestMethod.POST)
	public AppResp modifySubscribeReport_(@RequestBody Map<String, Object> params) {
		SubscribeReportParam relParam = Utils.map2Obj(params, SubscribeReportParam.class);
		String[] arr = {};
		if(StringUtils.isNotBlank(relParam.getReportIds())){
			arr = relParam.getReportIds().split(",");
		}
		String currentDate = DateUtils.getDate("yyyyMMdd");
		reportService.addReportTaskByUserSubscribe(relParam.getUserId(), arr, currentDate);
		return new AppResp(CodeDef.SUCCESS);
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
		List<CurrentReportTaskResp> list = reportService.findCurrentTasks(relParams.getUserId(), relParams.getCurrentDate());
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
		
		if(relParams.getIsCompleted() == 0){//未完成
			reportCompletedRelRepo.updateNotComplated(relParams.getUserId(), relParams.getReportId(), relParams.getSubmitStartTime(), relParams.getSubmitEndTime());
			
		}else{//完成
			String currentDate = DateUtils.getDate("yyyyMMddHHmm");
			
			//未到核销时间，给予提示
			if(Integer.parseInt(currentDate) < Integer.parseInt(relParams.getSubmitStartTime())){
				return new AppResp(CodeDef.EMP.REPORT_NOTTO_COMPLETEDATE, CodeDef.EMP.REPORT_NOTTO_COMPLETEDATE_DESC);
			}
			
			reportCompletedRelRepo.updateComplated(relParams.getUserId(), relParams.getReportId(), relParams.getSubmitStartTime(), relParams.getSubmitEndTime(), relParams.getCompletedDate());  
			//移除闹钟设置记录
			Remind entity = new Remind();
			entity.setRemindId(relParams.getRemindId());
			remindRepo.delete(entity);
			
		}
		
		return new AppResp(CodeDef.SUCCESS);
	}
	
	/**
	 * 获取当天之后的打点日期[userId]
	 * @param params
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "获取当月任务日期", notes = "POST")
	@RequestMapping(value = "/getTaskDate", method = RequestMethod.POST)
	public AppResp getTaskDate_(@RequestBody Map<String, Object> params) throws ParseException {
		ReportCompletedRelParam relParams = Utils.map2Obj(params, ReportCompletedRelParam.class);
		String currentDate = DateUtils.getDate("yyyyMM");
		List<ReportCompletedRel> taskList = reportCompletedRelRepo.findTasksByUserId(relParams.getUserId(), relParams.getCurrentMonth());
		
		//有任务的日期
		List<String> taskDateList = new ArrayList<String>();
//		Map<Integer, String> mTaskDate = new HashMap<Integer, String>();
		for(ReportCompletedRel data : taskList){
			String submitEndDate = data.getSubmitEndTime().substring(0, 6);
			if(Integer.parseInt(submitEndDate) == Integer.parseInt(currentDate)){
				//如果是当月情况，添加时间
				String submitEndDateDay = data.getSubmitEndTime().substring(6, 8);
				for(int i=1; i <= Integer.parseInt(submitEndDateDay);  i++){
					String _date = submitEndDate + (i < 10 ? "0" + i : i);
					if(!taskDateList.contains(_date))
						taskDateList.add(_date);
					
				}
			}else{
				//非当月，则添加当月所有日期
				int _daysOfMonth = DateUtils.getDaysByMonth(currentDate);
				for(int i = 0; i < _daysOfMonth; i++){
					String _date = submitEndDate + (i < 10 ? "0" + i : i);
					if(!taskDateList.contains(_date))
						taskDateList.add(_date);
					
//					if(!mTaskDate.containsKey(i))
//						mTaskDate.put(i, _date);
				}
			}
		}
		
		return new AppResp(taskDateList, CodeDef.SUCCESS);
	}
	
	private List<String> getAllTheDateOftheMonth(Date date) {
		List<String> list = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		int month = cal.get(Calendar.MONTH);
		SimpleDateFormat sdf = null;
		while (cal.get(Calendar.MONTH) == month) {
			sdf = new SimpleDateFormat("yyyyMMdd");
			list.add(sdf.format(cal.getTime()));
			cal.add(Calendar.DATE, 1);
		}
		return list;
	}

}
