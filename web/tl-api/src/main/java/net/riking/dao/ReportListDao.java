package net.riking.dao;

import java.util.List;

import net.riking.entity.model.AppUserReportRel;

public interface ReportListDao {
	List<AppUserReportRel> findAppUserReportById(String userId);

}
