package net.riking.service;

import java.util.List;

import net.riking.entity.model.AppUserReportRel;

public interface ReportLisService {
	
	List<AppUserReportRel> findAppUserReportById(String userId);

}
