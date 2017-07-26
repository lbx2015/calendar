package net.riking.service.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.AmlSuspiciousQuery;

public interface RiskDataReport extends JpaRepository<AmlSuspicious, Long>{
	
	@Query("select new net.riking.entity.model.AmlSuspiciousQuery(max(ctnm), sum(case when tsdr = '01' then 1 else 0 end) as income,sum(case when tsdr = '02' then 1 else 0 end) as outlay,ctid) from AmlSuspicious where rpdt between ?1 and ?2 and deleteState ='1' GROUP BY ctid")
	List<AmlSuspiciousQuery> getCust(Date start ,Date end);
	
	@Query("select new net.riking.entity.model.AmlSuspiciousQuery(sevc, count(id)) from AmlSuspicious where rpdt between ?1 and ?2 and deleteState ='1' GROUP BY sevc ")
	List<AmlSuspiciousQuery> getWork(Date start ,Date end);
	
	@Query("select new net.riking.entity.model.AmlSuspiciousQuery(stnt,count(id)) from AmlSuspicious where rpdt between ?1 and ?2 and deleteState ='1' GROUP BY stnt ")
	List<AmlSuspiciousQuery> getArea(Date start ,Date end);
	
	@Query("select new net.riking.entity.model.AmlSuspiciousQuery(tstp, count(id)) from AmlSuspicious where rpdt between ?1 and ?2 and deleteState ='1' GROUP BY tstp ")
	List<AmlSuspiciousQuery> getWay(Date start ,Date end);
	
	@Query("select new net.riking.entity.model.AmlSuspiciousQuery(cfrc, count(id)) from AmlSuspicious where rpdt between ?1 and ?2 and deleteState ='1' and deleteState ='1' GROUP BY cfrc ")
	List<AmlSuspiciousQuery> getFlow(Date start ,Date end);
	
	@Query("select count(id) from AmlSuspicious where rpdt between ?1 and ?2 and deleteState ='1' AND stcr like %?3%")
	long getRules(Date start ,Date end,String stcr);
	
	@Query("select a.stcr  from AmlSuspicious a where a.rpdt between ?1 and ?2 and a.deleteState ='1' ")
	List<String> getRule(Date start ,Date end);
	
}
