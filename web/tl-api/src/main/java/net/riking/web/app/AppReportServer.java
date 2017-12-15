package net.riking.web.app;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.dao.repo.ReportSubscribeRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.ReportListResult;
import net.riking.entity.model.ReportResult;
import net.riking.entity.model.ReportSubscribeRel;
import net.riking.entity.params.ReportParams;
import net.riking.entity.params.SubscribeReportParam;
import net.riking.service.ReportAgenceFrencyService;
import net.riking.service.ReportService;
import net.riking.service.ReportSubmitCaliberService;
import net.riking.service.SysDataService;
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
	SysDataService sysDataservice;

	@Autowired
	ReportSubmitCaliberService reportSubmitCaliberService;

	@Autowired
	ReportAgenceFrencyService reportAgenceFrencyService;

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
		SubscribeReportParam subscribeReport = Utils.map2Obj(params, SubscribeReportParam.class);
		
		//先删除不在本次订阅内的reportId
		reportSubscribeRelRepo.deleteNotSubscribeByUserId(subscribeReport.getUserId(), subscribeReport.getReportIds());
		
		//查找该用户剩余订阅的数据
		List<String> currentReportIds = reportSubscribeRelRepo.findByUserId(subscribeReport.getUserId());
		
		// 批量插入
		for (String reportId : subscribeReport.getReportIds()) {
			boolean isRn = true;
			for(String cutReportId : currentReportIds){
				if(reportId.equals(cutReportId)){
					//订阅的报表已经在已订阅之内，不让其新增
					isRn = false;
				}
				
				if(isRn){
					ReportSubscribeRel rel = new ReportSubscribeRel();
					rel.setReportId(reportId);
					rel.setUserId(subscribeReport.getUserId());
					reportSubscribeRelRepo.save(rel);
				}
				
			}
		}

		return new AppResp(CodeDef.SUCCESS);
	}

	
	@ApiOperation(value = "查询逾期报表", notes = "POST")
	@RequestMapping(value = "/findExpireTasks", method = RequestMethod.POST)
	public AppResp findExpireTasks_(@RequestParam("userId") String userId) {
		List<ReportSubscribeRel> list = reportSubscribeRelRepo.findSubscribeReportList(userId);
		
		
		return new AppResp(list, CodeDef.SUCCESS);
	}

}
