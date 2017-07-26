package net.riking.service.repo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import net.riking.entity.report.CustomerReport;

public interface SuspiciousDao {
	Long findAllReport(Date start, Date end, String dorp, Set<String> jgbm);

	List<CustomerReport> findBySenmAndCsnm(String senm, String csnm1, Date start, Date end, Set<String> jgbm);

	List<List<String>> findReportByTosc(Date start, Date end, String tosc, Set<String> jgbm);
}
