package net.riking.service.repo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import net.riking.entity.model.QueryResult;
import net.riking.entity.report.CustomerReport;

public interface BigAmountDao {
	Long findAllReport(Date start, Date end, String bwzt, Set<String> jgbm);

	List<CustomerReport> findByCtnmAndCsnm(String ctnm, String csnm, Date start, Date end, Set<String> jgbm);
	
	
	List<QueryResult> findReportByCrcd(Date start, Date end,Set<String> jgbm);
}
