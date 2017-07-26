package net.riking.service.repo;

import net.riking.entity.model.ModelAmlInditrn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by bing.xun on 2017/5/26.
 */
public interface ModelAmlInditrnRepo extends JpaRepository<ModelAmlInditrn, Long> ,ModelAmlInditrnDao{
    List<ModelAmlInditrn> findByJyrqGreaterThanEqualAndJyrqLessThanEqual(Date startTime, Date endTime);

    @Query("SELECT min(m.zhjyrq) from ModelAmlInditrn m where m.jyrq<=?2 and m.jyrq >?1 and m.hzzj =?3")
    Date findMinDate(Date startTime, Date endTime, String hzzj);

    @Query("SELECT max(m.zhjyrq) from ModelAmlInditrn m where m.jyrq<=?1 and m.hzzj =?2")
    Date findMaxDate(Date startTime,String hzzj);

    @Query("SELECT m.khzwmc from ModelAmlInditrn m where m.jyrq>=?1 and m.jyrq<=?2 group by m.khzwmc")
    Set<String> getZwms(Date startTime, Date endTime);

    @Query("SELECT m.khywmc from ModelAmlInditrn m where m.jyrq>=?1 and m.jyrq<=?2 group by m.khywmc")
    Set<String> getYwms(Date startTime, Date endTime);

    @Query("SELECT m.zjhm from ModelAmlInditrn m where m.jyrq>=?1 and m.jyrq<=?2 group by m.zjhm")
    Set<String> getZjhms(Date startTime, Date endTime);

    @Query("from ModelAmlInditrn m where m.khzwmc in ?1")
    Set<ModelAmlInditrn> getModelAmlInditrnWithZwm(List<String> zwms);

    @Query("from ModelAmlInditrn m where m.khywmc in ?1")
    Set<ModelAmlInditrn> getModelAmlInditrnWithYwm(List<String> ywms);
    
    @Query("from ModelAmlInditrn m where m.khbh in ?1")
    List<ModelAmlInditrn> getByKhbh(String khbh);

    @Query("from ModelAmlInditrn m where m.zjhm in ?1")
    Set<ModelAmlInditrn> getModelAmlInditrnWithZjhm(List<String> zjhms);
}
