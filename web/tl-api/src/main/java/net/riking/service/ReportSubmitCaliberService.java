package net.riking.service;

import java.util.List;
import java.util.Set;

import net.riking.entity.model.QueryReport;

public interface ReportSubmitCaliberService {

	Set<QueryReport> findAllByFreeDatefromReportId(String userId,Integer week,Integer ten,Integer month,Integer season,Integer halfYear,Integer Year,Integer isWorkDay);
	Set<QueryReport> findAllfromReportId();
	int  updateDelayDateAfter(String type,String remarks);
	int  updateDelayDateBefer(String type,String remarks,String frequency);
	//查询所有的报表
	List<QueryReport> findAllReport();
	
}
