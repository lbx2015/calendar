package net.riking.service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.riking.entity.model.CusRateInfo;

public interface CusRateInfoRepo extends JpaRepository<CusRateInfo, Long>{
	
	@Query("from  CusRateInfo r where r.csnm = ?1 ")
	List<CusRateInfo> getOneByCsnm(String csnm);
	@Query("from  CusRateInfo r where r.csnm = ?1 ")
	CusRateInfo findByCsnm(String csnm);
	
}
