package net.riking.dao;

import java.util.Set;

import net.riking.entity.model.QueryReport;

public interface ReportSubmitCaliberDao {
	Set<QueryReport> findAllByFreeDatefromReportId(String userId,Integer week,Integer ten,Integer month,Integer season,Integer halfYear,Integer Year,Integer isWorkDay);
	Set<QueryReport> findAllfromReportId();
}
