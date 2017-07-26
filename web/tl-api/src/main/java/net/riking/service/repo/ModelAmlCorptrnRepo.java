package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.ModelAmlCorptrn;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface ModelAmlCorptrnRepo extends JpaRepository<ModelAmlCorptrn, Long>,ModelAmlCorptrnDao{

   List<ModelAmlCorptrn> findByJyrqGreaterThanAndJyrqLessThanEqual(Date startTime, Date endTime);

   @Query("SELECT min(m.zhjyrq) from ModelAmlCorptrn m where m.jyrq<=?2 and m.jyrq >?1 and m.hzzj =?3")
   Date findMinDate(Date startTime, Date endTime, String hzzj);

   @Query("SELECT max(m.zhjyrq) from ModelAmlCorptrn m where m.jyrq<=?1 and m.hzzj =?2")
   Date findMaxDate(Date startTime,String hzzj);

   @Query("SELECT m.khzwmc from ModelAmlCorptrn m where m.jyrq>=?1 and m.jyrq<=?2 group by m.khzwmc")
   Set<String> getZwms(Date startTime, Date endTime);

   @Query("SELECT m.khywmc from ModelAmlCorptrn m where m.jyrq>=?1 and m.jyrq<=?2 group by m.khywmc")
   Set<String> getYwms(Date startTime, Date endTime);

   @Query("SELECT m.zjhm from ModelAmlCorptrn m where m.jyrq>=?1 and m.jyrq<=?2 group by m.zjhm")
   Set<String> getZjhms(Date startTime, Date endTime);

   @Query("from ModelAmlCorptrn m where m.khzwmc in ?1")
   Set<ModelAmlCorptrn> getModelAmlCorptrnWithZwm(List<String> zwms);

   @Query("from ModelAmlCorptrn m where m.khywmc in ?1")
   Set<ModelAmlCorptrn> getModelAmlCorptrnWithYwm(List<String> ywms);
   
   @Query("from ModelAmlCorptrn m where m.khbh in ?1")
   List<ModelAmlCorptrn> getByKhbh(String khbh);
   
   @Query("from ModelAmlCorptrn m where m.jylsh = ?1")
   List<ModelAmlCorptrn> getByJylsh(String jylsh);
   
   @Query("from ModelAmlCorptrn m where m.zh = ?1")
   List<ModelAmlCorptrn> getByZh(String zh);

   @Query("from ModelAmlCorptrn m where m.zjhm in ?1")
   Set<ModelAmlCorptrn> getModelAmlCorptrnWithZjhm(List<String> zjhms);
}
