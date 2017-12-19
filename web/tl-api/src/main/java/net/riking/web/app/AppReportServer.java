package net.riking.web.app;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.entity.PageQuery;
import net.riking.dao.ReportCompletedRelDao;
import net.riking.dao.repo.ReportCompletedRelRepo;
import net.riking.dao.repo.ReportSubscribeRelRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.ReportListResult;
import net.riking.entity.model.ReportSubscribeRel;
import net.riking.entity.params.ReportCompletedRelParam;
import net.riking.entity.params.ReportParams;
import net.riking.entity.params.SubscribeReportParam;
import net.riking.entity.resp.ReportCompletedRelResult;
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
	ReportCompletedRelRepo reportCompletedRelRepo;

	@Autowired
	ReportCompletedRelDao reportCompletedRelDao;

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
	public AppResp findSubscribeReportList_(@RequestBody Map<String, String> params) {
		List<ReportSubscribeRel> list = reportSubscribeRelRepo.findSubscribeReportList(params.get("userId"));
		return new AppResp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "更新用户报表订阅", notes = "POST")
	@RequestMapping(value = "/modifySubscribeReport", method = RequestMethod.POST)
	public AppResp modifySubscribeReport_(@RequestBody Map<String, Object> params) {
		SubscribeReportParam subscribeReport = Utils.map2Obj(params, SubscribeReportParam.class);

		// 先删除不在本次订阅内的reportId
		reportSubscribeRelRepo.deleteNotSubscribeByUserId(subscribeReport.getUserId(), subscribeReport.getReportIds());

		// 查找该用户剩余订阅的数据
		List<String> currentReportIds = reportSubscribeRelRepo.findByUserId(subscribeReport.getUserId());

		// 批量插入
		for (String reportId : subscribeReport.getReportIds()) {
			boolean isRn = true;
			for (String cutReportId : currentReportIds) {
				if (reportId.equals(cutReportId)) {
					// 订阅的报表已经在已订阅之内，不让其新增
					isRn = false;
				}

				if (isRn) {
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
	public AppResp findExpireTasks_(@RequestBody Map<String, Object> params) {
		ReportCompletedRelParam relParams = Utils.map2Obj(params, ReportCompletedRelParam.class);
		PageQuery page = new PageQuery();
		page.setPcount(Const.APP_PAGENO_30);
		page.setPindex(relParams.getPindex());
		List<ReportCompletedRelResult> list = reportCompletedRelDao.findExpireReportByPage(relParams.getUserId(), page);

		return new AppResp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "查询历史核销报表", notes = "POST")
	@RequestMapping(value = "/findHisCompletedTasks", method = RequestMethod.POST)
	public AppResp findHisCompletedTasks_(@RequestBody Map<String, Object> params) {
		ReportCompletedRelParam relParams = Utils.map2Obj(params, ReportCompletedRelParam.class);
		PageQuery page = new PageQuery();
		page.setPcount(Const.APP_PAGENO_30);
		page.setPindex(relParams.getPindex());
		List<ReportCompletedRelResult> list = reportCompletedRelDao.findHisCompletedReportByPage(relParams.getUserId(),
				page);

		return new AppResp(list, CodeDef.SUCCESS);
	}

}
