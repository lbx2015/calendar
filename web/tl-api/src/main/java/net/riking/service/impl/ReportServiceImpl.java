package net.riking.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.config.Const;
import net.riking.core.entity.PageQuery;
import net.riking.dao.ReportCompletedRelDao;
import net.riking.dao.ReportDao;
import net.riking.dao.repo.ReportCompletedRelRepo;
import net.riking.dao.repo.ReportSubmitCaliberRepo;
import net.riking.dao.repo.ReportSubscribeRelRepo;
import net.riking.dao.repo.SysDaysRepo;
import net.riking.entity.model.ReportCompletedRel;
import net.riking.entity.model.ReportFrequency;
import net.riking.entity.model.ReportListResult;
import net.riking.entity.model.ReportResult;

import net.riking.entity.model.ReportSubmitCaliber;
import net.riking.entity.model.ReportSubscribeRel;
import net.riking.entity.model.ReportTypeListResult;
import net.riking.entity.model.SysDays;
import net.riking.entity.resp.CurrentReportTaskResp;
import net.riking.entity.resp.ReportCompletedRelResult;
import net.riking.service.ReportService;
import net.riking.util.DateUtils;
import net.riking.util.RedisUtil;
import net.riking.util.Utils;

@Service("reportService")
public class ReportServiceImpl implements ReportService {
	protected final transient Logger logger = LogManager.getLogger(getClass());
	@Autowired
	ReportDao reportDao;
	@Autowired
	ReportCompletedRelDao reportCompletedRelDao;
	@Autowired
	ReportCompletedRelRepo reportCompletedRelRepo;
	@Autowired
	ReportSubscribeRelRepo reportSubscribeRelRepo;
	@Autowired
	ReportSubmitCaliberRepo reportSubmitCaliberRepo;
	@Autowired
	SysDaysRepo sysDaysRepo;

	@Override
	public List<ReportFrequency> findAppUserReportById(String userId) {
		// TODO Auto-generated method stub
		return reportDao.findAppUserReportById(userId);
	}
	
	@Override
	public List<ReportResult> getReportResultByParam(String reportName){
		return reportDao.getAllReportByParams(reportName);
	}

	@Override
	public List<ReportListResult> getReportByParam(String reportName, String userId) {
		// TODO Auto-generated method stub

		List<ReportResult> list = reportDao.getAllReportByParams(reportName, userId);

		List<ReportTypeListResult> typeListResults = new ArrayList<ReportTypeListResult>();

		List<ReportListResult> reportListResults = new ArrayList<ReportListResult>();

		for (int i = 0; i < list.size(); i++) {
			// 塞ReportTypeListResult第二层数据
			if (typeListResults.size() == 0) {
				setReportTypeListResult(list, i, typeListResults);
			}
			for (int j = 0; j < typeListResults.size(); j++) {
				// 把报表类型相等并且报表所属模块相等的归一类
				if (list.get(i).getReportType().toUpperCase().equals(typeListResults.get(j).getAgenceCode())
						&& list.get(i).getModuleType().equals(typeListResults.get(j).getModuleType())) {
					typeListResults.get(j).getList().add(list.get(i));
				} else {
					if (j == typeListResults.size() - 1) {
						setReportTypeListResult(list, i, typeListResults);
					}
				}
				// 塞ReportListResult第三层数据,在第二层数据塞完才执行
				if (list.size() - 1 == i) {
					if (reportListResults.size() == 0) {
						setReportListResult(typeListResults, j, reportListResults);
					}
					for (int k = 0; k < reportListResults.size(); k++) {
						if (reportListResults.get(k).getAgenceCode().toUpperCase()
								.equals(typeListResults.get(j).getAgenceCode())) {
							reportListResults.get(k).getList().add(typeListResults.get(j));
						} else {
							if (k == reportListResults.size() - 1) {
								setReportListResult(typeListResults, j, reportListResults);
							}
						}
					}
				}
			}
		}

		return reportListResults;
	}

	private void setReportListResult(List<ReportTypeListResult> typeListResults, int j,
			List<ReportListResult> reportListResults) {
		ReportListResult reportListResult = new ReportListResult();
		reportListResult.setAgenceCode(typeListResults.get(j).getAgenceCode());
		List<ReportTypeListResult> results = new ArrayList<ReportTypeListResult>();
		results.add(typeListResults.get(j));
		reportListResult.setList(results);
		reportListResults.add(reportListResult);
	}

	private void setReportTypeListResult(List<ReportResult> list, int i, List<ReportTypeListResult> typeListResults) {
		ReportTypeListResult typeListResult = new ReportTypeListResult();
		typeListResult.setAgenceCode(list.get(i).getReportType());
		typeListResult.setModuleType(list.get(i).getModuleType());
		typeListResult.setModuleTypeName(list.get(i).getModuleTypeName());
		List<ReportResult> results = new ArrayList<ReportResult>();
		results.add(list.get(i));
		typeListResult.setList(results);
		typeListResults.add(typeListResult);
	}

	@Override
	public List<ReportCompletedRelResult> findExpireReportByPage(String userId, PageQuery pageQuery) {
		// TODO Auto-generated method stub
		return reportCompletedRelDao.findExpireReportByPage(userId, pageQuery);
	}

	@Override
	public List<ReportCompletedRelResult> findHisCompletedReportByPage(String userId, PageQuery pageQuery) {
		// TODO Auto-generated method stub
		return reportCompletedRelDao.findHisCompletedReportByPage(userId, pageQuery);
	}

	@Override
	public List<CurrentReportTaskResp> findCurrentTasks(String userId, String currentDate) {
		// TODO Auto-generated method stub
		return reportCompletedRelDao.findCurrentTasks(userId, currentDate);
	}

	@Override
	@Transactional
	public boolean addReportTaskByUserSubscribe(String userId, String[] reportIds, String currentDate) {
		// TODO Auto-generated method stub
		String _year = currentDate.substring(0, 4);
		String _month = currentDate.substring(4, 6);
		//String _day = currentDate.substring(6, 8);
		
		//先删除不在本次订阅内的reportId
		reportSubscribeRelRepo.deleteNotSubscribeByUserId(userId, reportIds);
		//删除在该次订阅的时间范围内用户核销相关数据
		reportCompletedRelRepo.deleteSubscriptTask(userId, reportIds, currentDate);
		//查找该用户剩余订阅的数据
		List<String> currentReportIds = reportSubscribeRelRepo.findByUserId(userId);
		
		List<String> reportIdList = new ArrayList<String>();
		// 批量插入
		for (String reportId : reportIds) {
			boolean isRn = true;
			for(String cutReportId : currentReportIds){
				if(reportId.equals(cutReportId)){
					//订阅的报表已经在已订阅之内，不让其新增
					isRn = false;
				}
			}
			if(isRn){
				//保存该次订阅的数据
				ReportSubscribeRel rel = new ReportSubscribeRel();
				rel.setReportId(reportId);
				rel.setUserId(userId);
				reportSubscribeRelRepo.save(rel);
				reportIdList.add(reportId);
			}
		}
		
		//处理新增核销任务
		for(String reportId : reportIdList){
			//新增用户订阅报表的核销任务
			ReportSubmitCaliber reportSubmitCaliber = reportSubmitCaliberRepo.findByReportId(reportId);
			if(reportSubmitCaliber != null){
				String[] arr_month = reportSubmitCaliber.getSubmitMonth().split(",");
				
				for(String month : arr_month){
					if(Integer.parseInt(month) == Integer.parseInt(_month)){
						//上报开始时间
						String submitStartTime = "";
						//上报截止时间
						String submitEndTime = "";
						Date date = null;
						//延后日期时间
						String afterDateStr = "";
						switch (reportSubmitCaliber.getFrequency()) {
							case 0://日
							case 1://周
							case 2://旬
							case 3://月
							case 4://季
								date = DateUtils.parseDate(_year + _month + "01");
								submitStartTime = _year + _month + "01";
								break;
							case 5://半年
								if(Integer.parseInt(_month) < 7){//添加上半年的半年报
									date = DateUtils.parseDate(_year + "0601");
									submitStartTime = _year + "0601";
								}else{//添加上半年的半年报
									date = DateUtils.parseDate(_year + "1201");
									submitStartTime = _year + "1201";
								}
								break;
							case 6://年
								date = DateUtils.parseDate(_year  + "12" + "01");
								submitStartTime = _year + "1201";
								break;
						}
					
						//根据天数得到日期yyyyMMdd
						afterDateStr = DateUtils.getDateByDays(date, reportSubmitCaliber.getDelayDates() - 1 );
						
						//未来日期>=当前日期，新增任务
						if(Integer.parseInt(currentDate) <= Integer.parseInt(afterDateStr)){
							submitStartTime += "0000";
							
							//判断是否与国家节假日，延迟上报截止时间
							afterDateStr = Utils.getWorkday(afterDateStr);
							logger.info("afterDateStr={}", afterDateStr);
							
							submitEndTime = afterDateStr + reportSubmitCaliber.getSubmitTime();
							logger.info("submitStartTime={}, submitEndTime={}", submitStartTime, submitEndTime);
							
							ReportCompletedRel data = reportCompletedRelRepo.findByOne(userId, reportId, submitStartTime, submitEndTime);
							if(data == null){
								data = new ReportCompletedRel();
								data.setUserId(userId);
								data.setReportId(reportId);
								data.setIsCompleted(0);
								//yyyyMMddHHmm
								data.setSubmitStartTime(submitStartTime);
								data.setSubmitEndTime(submitEndTime);
								reportCompletedRelRepo.save(data);
							}
						}
					}
					
				}
				
			}
		}
		
		return true;
	}
	
}
