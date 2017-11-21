package net.riking.service;

import java.util.List;

import net.riking.entity.model.ReportFrequency;

public interface ReportLisService {
	
	List<ReportFrequency> findAppUserReportById(String userId);

}
