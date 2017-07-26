package net.riking.service.repo;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Sdcurrpd;

@Repository
public interface SdcurrpdRepo extends JpaRepository<Sdcurrpd, Long>, JpaSpecificationExecutor<Sdcurrpd> {
	
	@Modifying
	@Transactional
	@Query("delete from Sdcurrpd b where b.rateDate between ?1 and ?2")
	int deleteByRateDate(Date startDt,Date endDate);
	
	@Query("from Sdcurrpd b where b.rateDate = ?1 ")
	List<Sdcurrpd> findByRateDate(Date rateDt);

	@Query("from Sdcurrpd b where b.rateDate <= ?1")
	List<Sdcurrpd> findByRateDateBefor(Date end);
	
	@Query("from Sdcurrpd b where b.currency=?1 and b.method = ?2 and b.rateDate = ?3")
	Sdcurrpd getSdcurrpd(String currency,String method,Date RateDate);
}
