package net.riking.service.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.AmlSuspiciousQuery;

public interface CustomerDataReportRepo extends JpaRepository<AmlSuspicious, Long>{
	@Query("select new net.riking.entity.model.AmlSuspiciousQuery(cfrc,count(cfrc)) from AmlSuspicious where rpdt between ?1 and ?2 and ctid=?3 and deleteState ='1' GROUP BY cfrc ")
	List<AmlSuspiciousQuery> getArea(Date start,Date end,String ctid);
	
	@Query("select new net.riking.entity.model.AmlSuspiciousQuery(tstp, count(id)) from AmlSuspicious where rpdt between ?1 and ?2 and ctid=?3 and deleteState ='1' GROUP BY tstp ")
	List<AmlSuspiciousQuery> getWay(Date start,Date end,String ctid);
	
	@Query("select new net.riking.entity.model.AmlSuspiciousQuery(cfrc, count(id)) from AmlSuspicious where rpdt between ?1 and ?2 and ctid=?3 and deleteState ='1' and deleteState ='1' GROUP BY cfrc ")
	List<AmlSuspiciousQuery> getFlow(Date start,Date end,String ctid);
	/*
	@Query("select count(id) from AmlSuspicious where rpdt between ?1 and ?2 and deleteState ='1' AND stcr like %?3% and ctid=?4")
	long getRules(Date start,Date end,String stcr,String ctid);*/
	
	@Query("select a.stcr  from AmlSuspicious a where a.rpdt between ?1 and ?2 and a.ctid=?3 and a.deleteState ='1' ")
	List<String> getRule(Date start,Date end,String ctid);
}
