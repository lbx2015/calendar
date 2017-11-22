package net.riking.dao;

import java.util.List;

import net.riking.entity.model.ReportFrequency;

public interface ReportListDao {
	List<ReportFrequency> findAppUserReportById(String userId);

}
