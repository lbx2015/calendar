package net.riking.service.repo;

import net.riking.entity.model.CrimeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

/**
 * Created by bing.xun on 2017/5/24.
 */
@Repository
public interface CrimeTypeRepo extends JpaRepository<CrimeType,Long> {
    @Transactional
    @Modifying
    @Query("delete from CrimeType e where e.id in ?1")
    int deleteByIds(Set<Long> ids);

    List<CrimeType> findByIdIn(Set<Long> ids);
}
