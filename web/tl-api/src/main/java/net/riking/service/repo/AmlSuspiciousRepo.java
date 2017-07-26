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

import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.AmlSuspiciousQuery;

@Repository
public interface AmlSuspiciousRepo extends JpaRepository<AmlSuspicious, Long>, SuspiciousDao,JpaSpecificationExecutor<AmlSuspicious> {

	@Query("select ticd from AmlSuspicious where (tstm  between ?1 and ?2) and deleteState ='1'")
	Set<String> findTicdByTstm(Date start,Date end);

	@Query("from AmlSuspicious where Ticd = ?1 and rpdt = ?2 and deleteState ='1'")
	AmlSuspicious findAmlSuspiciousByTicdandRpdt(String Ticd,Date rpdt);
	
	//@Query("select new net.riking.entity.model.AmlSuspiciousQuery(b.ctnm,b.ctid) from  AmlSuspicious b left join b.job j  where j.curJobState in ?1 and b.jgbm in ?2 and b.deleteState ='1' group by b.ctid,b.ctnm ")
	@Query("select new net.riking.entity.model.AmlSuspiciousQuery(b.ctnm,b.ctid) from  AmlSuspicious b where b.flowState in ?1 and b.jgbm in ?2 and b.deleteState ='1' group by b.ctid,b.ctnm")
	List<AmlSuspiciousQuery> findAllCtid(Set<String> status,List<String> branchCodes);

	//@Query("select new net.riking.entity.model.AmlSuspiciousQuery(b.ctnm,b.ctid) from  AmlSuspicious b left join b.job j  where j.curJobState in ?1 and b.deleteState ='1' group by b.ctid,b.ctnm ")
	@Query("select new net.riking.entity.model.AmlSuspiciousQuery(b.ctnm,b.ctid) from  AmlSuspicious b where b.flowState in ?1 and b.deleteState ='1' group by b.ctid,b.ctnm")
	List<AmlSuspiciousQuery> findAllCtid(Set<String> status);
	
	@Query("from AmlSuspicious where id in ?1")
	List<AmlSuspicious> findAmlSuspicious(Set<Long> ids);	
	
//	@Transactional
//	@Modifying
//	@Query("delete from AmlSuspicious where id in ?1 ")
//	int updateMore(Set<Long> ids);
	
	@Transactional
	@Modifying
	@Query("update AmlSuspicious set deleteState='2' where id in ?1")
	int deleteAmlSuspicious(Set<Long> ids);
	
	//@Query("select b.nowSubmitBatch from AmlSuspicious b left join b.job j  where b.rpdt = ?1  and b.nowSubmitBatch is not NULL and b.deleteState ='1' and j.curJobState=?2 group by b.nowSubmitBatch order by b.nowSubmitBatch ")
	@Query("select b.nowSubmitBatch from AmlSuspicious b where b.rpdt = ?1  and b.nowSubmitBatch is not NULL and b.deleteState ='1' and b.flowState=?2 group by b.nowSubmitBatch order by b.nowSubmitBatch ")
	List<String> getSumbit(Date rpdt,String curJobState);

	
	//@Query("select b.id from AmlSuspicious b left join b.job j where b.rpdt = ?1 and b.nowSubmitBatch =?2 and b.deleteState ='1'  and j.curJobState=?3")
	@Query("select b.id from AmlSuspicious b where b.rpdt = ?1 and b.nowSubmitBatch =?2 and b.deleteState ='1'  and b.flowState=?3")
	Set<Long> findIdBySubitBatch(Date rpdt,String nowSubmitBatch,String curJobState);
	
	
//	@Transactional
//	@Modifying
//	@Query(" update AmlSuspicious set aptp='01' where id in?1")
//	int updateReportAgain(Set<Long> ids);
	
//	@Transactional
//	@Modifying
//	@Query("update AmlSuspicious e set e.tkms = ?1 ,e.dorp=?2 ,e.ssds=?3,e.aptp = ?4  where e.id in ?5 ")
//	int approveMore(String tkms ,  String dorp, String ssds, String aptp,Set<Long> set);
	
//	@Transactional
//	@Modifying
//	@Query("update AmlSuspicious e set e.tkms = ?1 ,e.dorp=?2 ,e.ssds=?3  where e.id in ?4")
//	int updateMore(String tkms ,  String dorp, String ssds, Set<Long> set);
	
	@Query("from  AmlSuspicious  where id in ?1 and deleteState ='1' ")
	List<AmlSuspicious> findByIdIn(Set<Long> ids);
	
	List<AmlSuspicious> findByTicdAndTstmBetweenAndDeleteState(String ticd,Date start,Date end,String deletesate);
	
	List<AmlSuspicious> findByTicdAndTstmBetweenAndDeleteStateAndIdNot(String ticd,Date start,Date end,String deleteState, Long id);
	
	//@Query("select count(a.id) from AmlSuspicious a left join a.job j WHERE a.rpdt >=?1 and j.curJobState in ?2 and  a.jgbm in ?3 and a.deleteState ='1' ")
	@Query("select count(a.id) from AmlSuspicious a  WHERE a.rpdt >=?1 and a.flowState in ?2 and  a.jgbm in ?3 and a.deleteState ='1' ")
	Integer findByRpdtStart(Date date,Set<String> status,List<String> jgbm);
	
	//@Query("select count(a.id) from AmlSuspicious a left join a.job j WHERE a.rpdt <=?1 and j.curJobState in ?2 and  a.jgbm in ?3 and a.deleteState ='1' ")
	@Query("select count(a.id) from AmlSuspicious a WHERE a.rpdt <=?1 and a.flowState in ?2 and  a.jgbm in ?3 and a.deleteState ='1' ")
	Integer findByRpdtEnd(Date date,Set<String> status,List<String> jgbm);
	
	//@Query("select count(a.id) from AmlSuspicious a left join a.job j WHERE j.curJobState in ?1 and  a.jgbm in ?2 and a.deleteState ='1' ")
	@Query("select count(a.id) from AmlSuspicious a WHERE a.flowState in ?1 and  a.jgbm in ?2 and a.deleteState ='1' ")
	Integer findByRpdtbetween(Set<String> status,List<String> jgbm);
	
	//@Query("select count(a.id) from AmlSuspicious a left join a.job j WHERE a.rpdt between ?1 and ?2 and j.curJobState in ?3 and  a.jgbm in ?4 and a.deleteState ='1' ")
	@Query("select count(a.id) from AmlSuspicious a WHERE a.rpdt between ?1 and ?2 and a.flowState in ?3 and  a.jgbm in ?4 and a.deleteState ='1' ")
	Integer findByRpdtbetween(Date date ,Date date2 ,Set<String> status,List<String> jgbm);
	
	@Query("select count(a.id) from AmlSuspicious a  WHERE a.flowState is not NULL and a.rpdt >=?1 and  a.jgbm in ?3  and a.deleteState ='1' ")
	Integer findByRpdtStarts(Date date,List<String> jgbm);
	
	@Query("select count(a.id) from AmlSuspicious a  WHERE a.flowState is not NULL and a.rpdt <=?1 and  a.jgbm in ?2  and a.deleteState ='1' ")
	Integer findByRpdtEnds(Date date,List<String> jgbm);
	
	@Query("select count(a.id) from AmlSuspicious a WHERE a.flowState is not NULL and a.rpdt between ?1 and ?2  and  a.jgbm in ?3 and a.deleteState ='1' ")
	Integer findByRpdtbetweens(Date date ,Date date2,List<String> jgbm);
	
	@Query("select count(a.id) from AmlSuspicious a WHERE a.flowState is not NULL and a.jgbm in ?1 and a.deleteState ='1' ")
	Integer findByRpdts(List<String> jgbm);
	
	//@Query(" from AmlSuspicious a left join a.job j  WHERE j.id=?1")
	@Query(" from AmlSuspicious a WHERE a.jobId=?1")
	AmlSuspicious getByJobId(String jobId);
	
	@Query("select b.id from AmlSuspicious b where b.tname = ?1 and b.deleteState =?2 and b.flowState='PRE_EXPORTOVER' ")
	Set<Long> findByTnameAndDeleteState(String tname,String deleteState);

	@Query("select new net.riking.entity.model.AmlSuspicious(b.id,p.curJobState) from AmlSuspicious b JOIN b.job AS p where b.rpdt=?1 and b.deleteState=?2")
	List<AmlSuspicious> getByRpdtAndDeleteState(Date rpdt,String deleteState);

	@Query("select max(a.torp) from AmlSuspicious a WHERE a.seid =?1 ")
	Integer getMaxTorp(String seid);

	@Query("select a.csnm1 from AmlSuspicious a  where a.rpdt >?1  and a.rpdt <=?2 and a.jgbm in ?3   and a.deleteState ='1' group by a.csnm1 ")
	List<String> getAllCsnm(Date start,Date end,Set<String> jgbm);
}
