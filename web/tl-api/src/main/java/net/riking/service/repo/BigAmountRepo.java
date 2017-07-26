package net.riking.service.repo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.BigAmount;

@Repository
public interface BigAmountRepo extends JpaRepository<BigAmount, Long>, BigAmountDao,JpaSpecificationExecutor<BigAmount> {
	

	@Modifying
	@Transactional
	@Query("delete from BigAmount a where a.id = ?1")
	int deleteById(Long id);
	
	@Query("select count(*) from BigAmount where rpdt between ?1 and ?2 and deleteState ='1' ")
	String getAmount(Date start, Date end);

	@Query("select count(*) from BigAmount where DATEPART(YEAR,rpdt) = ?1 and MONTH(rpdt) = ?2 and deleteState ='1' ")
	String getCountByLast6M();

	BigAmount findByTicdAndRpdt(String Tcid, Date rpdt);
	
	@Query("select b.tcac from BigAmount b where tcac like ?1 and (b.flowState='PRE_VERIFY' or b.flowState='PRE_EXPORT' or b.flowState='PRE_EXPORTOVER' or b.flowState='PRE_STORAGE')  and b.deleteState='1' group by b.tcac")
	List<String>findLikeTcac(String tcac);

	//@Query("select new net.riking.entity.model.BigAmount(b.id,p.curJobState) from BigAmount b JOIN b.job AS p where b.rpdt=?1 and b.deleteState=?2")
	@Query("select new net.riking.entity.model.BigAmount(b.id,b.flowState) from BigAmount b where b.rpdt=?1 and b.deleteState=?2")
	List<BigAmount> getByRpdtAndDeleteState(Date rpdt,String deleteState);

	@Query("select ticd from BigAmount where (tstm  between ?1 and ?2) and deleteState ='1'")
	Set<String> findTicdByTstm(Date start,Date end);

	@Transactional
	@Modifying
	@Query("update BigAmount r set r = ?1 where deleteState ='1'")
	void updateAmlBigAmount(BigAmount amlBigAmount);

	//@Query("select b.nowSubmitBatch from BigAmount b left join b.job j  where b.rpdt =?1  and b.nowSubmitBatch is not NULL and b.deleteState ='1' and j.curJobState=?2 group by b.nowSubmitBatch order by b.nowSubmitBatch ")
	@Query("select b.nowSubmitBatch from BigAmount b where b.rpdt =?1  and b.nowSubmitBatch is not NULL and b.deleteState ='1' and b.flowState=?2 group by b.nowSubmitBatch order by b.nowSubmitBatch ")
	List<String> getSumbit(Date rpdt, String curJobState);
	
	//@Query("select b.nowSubmitBatch from BigAmount b left join b.job j  where b.nowSubmitBatch is not NULL and b.deleteState ='1' and j.curJobState=?1 group by b.nowSubmitBatch order by b.nowSubmitBatch ")
	@Query("select b.nowSubmitBatch from BigAmount b where b.nowSubmitBatch is not NULL and b.deleteState ='1' and b.flowState=?1 group by b.nowSubmitBatch order by b.nowSubmitBatch ")
	List<String> getSumbit(String curJobState);

	//@Query("select b.csnm from BigAmount b left join b.job j  where b.rpdt = ?1  and b.reportType  <> 'D' and b.deleteState ='1' and j.curJobState=?2 group by b.csnm ")
	@Query("select b.csnm from BigAmount b where b.rpdt = ?1  and b.reportType  <> 'D' and b.deleteState ='1' and b.flowState=?2 group by b.csnm ")
	List<String> getCsnm(Date rpdt, String curJobState);

	@Query("select b.csnm from BigAmount b where b.rpdt >?1  and b.rpdt <=?2  and b.jgbm in ?3  and b.deleteState ='1' group by b.csnm ")
	List<String> getAllCsnm(Date start, Date end,Set<String> jgbm);
	
	//@Query("select b.id from BigAmount b left join b.job j where b.rpdt = ?1 and b.nowSubmitBatch =?2 and b.deleteState ='1'  and j.curJobState=?3")
	@Query("select b.id from BigAmount b where b.rpdt = ?1 and b.nowSubmitBatch =?2 and b.deleteState ='1'  and b.flowState=?3")
	Set<Long> findIdBySubitBatch(Date rpdt, String nowSubmitBatch, String curJobState);

//	@Transactional
//	@Modifying
//	@Query(" update BigAmount set aptp='01' where id in?1")
//	int updateReportAgain(Set<Long> ids);

	@Transactional
	@Modifying
	@Query("update BigAmount set deleteState='2' where id in ?1")
	int deleteAmlBigAmount(Set<Long> ids);

//	@Query("from BigAmount where csnm=?1 and rpdt=?2 and tsdr='01' and deleteState ='1'")
//	List<BigAmount> findallByCsnmS(String csnm, Date rpdt);

//	@Query("from BigAmount where csnm=?1 and rpdt=?2 and tsdr='02' and deleteState ='1'")
//	List<BigAmount> findallByCsnmF(String csnm, Date rpdt);

	@Query("select new net.riking.entity.model.BigAmount(b.crtp,b.crat,b.tsdr) from BigAmount b where b.ctid=?1 and b.rpdt = ?2 and b.deleteState ='1' and b.crcd=?3 and b.submitType ='Y'")
	List<BigAmount> findallByCtidAndRpdt(String ctid, Date rpdt,String crcd);

	@Query("select new net.riking.entity.model.BigAmount(b.crtp,b.crat,b.tsdr,b.ctid,b.crcd) from BigAmount b where b.rpdt = ?1 and b.deleteState ='1' and b.submitType ='Y'")
	List<BigAmount> findallByRpdt(Date rpdt);

//	@Query("select count(b.crtp) from BigAmount b WHERE b.csnm=?1 and b.rpdt=?2 and b.tsdr='01' and deleteState ='1' group by  b.crtp ")
//	Set<Long> getFirsByCsnmCnyS(String csnm, Date rpdt);

//	@Query("select count(b.crtp) from BigAmount b WHERE b.csnm=?1 and b.rpdt=?2 and b.tsdr='02' and deleteState ='1' group by  b.crtp ")
//	Set<Long> getFirsCsnmCnyF(String csnm, Date rpdt);

//	@Query("select sum(crat) from BigAmount WHERE csnm=?1 and rpdt=?2 and tsdr='01' and deleteState ='1'")
//	List<BigDecimal> findByCsnmCratS(String csnm, Date rpdt);

//	@Query("select sum(crat) from BigAmount WHERE csnm=?1 and rpdt=?2 and tsdr='02' and deleteState ='1'")
//	List<BigDecimal> findByCsnmCratF(String csnm, Date rpdt);

//	@Query("select id from BigAmount WHERE csnm=?1 and rpdt=?2 and tsdr='01' and deleteState ='1' ")
//	Set<Long> findFirstByCsnmIdS(String csnm, Date rpdt);

//	@Query("select id from BigAmount WHERE csnm=?1 and rpdt=?2 and tsdr='02' and deleteState ='1' ")
//	Set<Long> findFirstByCsnmIdF(String csnm, Date rpdt);

	@Transactional
	@Modifying
	@Query("update BigAmount set sfmz='01' where id in ?1 and deleteState ='1'")
	int updateBySfmzMz(Set<Long> ids);

	@Transactional
	@Modifying
	@Query("update BigAmount set sfmz='02' where id in ?1 and deleteState ='1'")
	int updateBySfmzBmz(Set<Long> ids);

	@Query("from  BigAmount  where id in ?1 and deleteState ='1' ")
	List<BigAmount> findByIdIn(Set<Long> ids);
	
	List<BigAmount> findByTicdAndTstmBetweenAndDeleteStateAndIdNot(String ticd, Date start,Date end,String deleteState, Long id);

	//@Query("from BigAmount b left join b.job j  WHERE j.id=?1")
	@Query("from BigAmount  WHERE jobId=?1")
	BigAmount getByJobId(String jobId);

	@Query("select count(b.id) from BigAmount b  WHERE b.flowState is not NULL and b.rpdt between ?1 and ?2 and b.jgbm in ?3 and b.deleteState ='1' ")
	Integer findByRpdtbetween(Date date, Date date2,List<String> jgbm);
	
	@Query("select count(b.id) from BigAmount b  WHERE b.flowState is not NULL and b.rpdt >=?1 and b.jgbm in ?2 and b.deleteState ='1' ")
	Integer findByRpdtStart(Date date,List<String> jgbm);
	
	@Query("select count(b.id) from BigAmount b  WHERE b.flowState is not NULL and b.rpdt <=?1 and b.jgbm in ?2 and b.deleteState ='1' ")
	Integer findByRpdtEnd(Date date,List<String> jgbm);
	
	@Query("select count(b.id) from BigAmount b  WHERE b.flowState is not NULL and b.jgbm in ?1 and b.deleteState ='1' ")
	Integer findByRpdtStart(List<String> jgbm);

	//@Query("select count(b.id) from BigAmount b left join b.job j  WHERE b.rpdt between ?1 and ?2 and j.curJobState in ?3 and b.jgbm in ?4 and b.deleteState ='1' ")
	@Query("select count(b.id) from BigAmount b WHERE b.rpdt between ?1 and ?2 and b.flowState in ?3 and b.jgbm in ?4 and b.deleteState ='1' ")
	Integer findByRpdtbetweenDateAndStatus(Date date, Date date2, Set<String> status,List<String> jgbm);
	
	//@Query("select count(b.id) from BigAmount b left join b.job j  WHERE  b.rpdt >=?1   and j.curJobState in ?2 and  b.jgbm in ?3 and b.deleteState ='1' ")
	@Query("select count(b.id) from BigAmount b  WHERE  b.rpdt >=?1   and b.flowState in ?2 and  b.jgbm in ?3 and b.deleteState ='1' ")
	Integer findByRpdtStartAndStatus(Date date, Set<String> status,List<String> jgbm);
	
	//@Query("select count(b.id) from BigAmount b left join b.job j  WHERE j.curJobState in ?1 and b.deleteState ='1' and b.jgbm in ?2 ")
	@Query("select count(b.id) from BigAmount b  WHERE b.flowState in ?1 and b.deleteState ='1' and b.jgbm in ?2 ")
	Integer findByRpdtDate( Set<String> status,List<String> jgbm);
	
	//@Query("select count(b.id) from BigAmount b left join b.job j  WHERE  b.rpdt <=?1   and j.curJobState in ?2 and b.jgbm in ?3 and b.deleteState ='1' ")
	@Query("select count(b.id) from BigAmount b  WHERE  b.rpdt <=?1   and b.flowState in ?2 and b.jgbm in ?3 and b.deleteState ='1' ")
	Integer findByRpdtEndAndStatus(Date date, Set<String> status,List<String> jgbm);

	//@Query("select count(b.id) from BigAmount b left join b.job j  WHERE b.rpdt = ?1  and b.deleteState =?2 and b.submitType=?3  and j.curJobState in ?4 ")
	@Query("select count(b.id) from BigAmount b WHERE b.rpdt = ?1  and b.deleteState =?2 and b.submitType=?3  and b.flowState in ?4 ")
	Integer findNotPass(Date date, String deleteState, String submitType, Set<String> status);
	
	//@Query("select b.id from BigAmount b left join b.job j  where b.tname = ?1 and b.deleteState =?2 and j.curJobState='PRE_EXPORTOVER' ")
	@Query("select b.id from BigAmount b  where b.tname = ?1 and b.deleteState =?2 and b.flowState='PRE_EXPORTOVER' ")
	Set<Long> findByTnameAndDeleteState(String tname,String deleteState);
	
	@Query("select b.id from BigAmount b  where b.ticd = ?1 and b.rpdt = ?2 and b.crcd=?3 and  b.deleteState =?4")
	Long findByTicdAndRpdtAndCrcdAndDeleteStates(String ticd,Date rpdt,String crcd,String deleteState);
	
	//@Query("select b.id from BigAmount b left join b.job j  where b.ticd = ?1 and b.rpdt = ?2 and b.crcd=?3 and  b.deleteState =?4 and j.curJobState='PRE_STORAGE'" )
	@Query("select b.id from BigAmount b  where b.ticd = ?1 and b.rpdt = ?2 and b.crcd=?3 and  b.deleteState =?4 and b.flowState='PRE_STORAGE'" )
	Long findByTicdAndRpdtAndCrcdAndDeleteState(String ticd,Date rpdt,String crcd,String deleteState);
}
