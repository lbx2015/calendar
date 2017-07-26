package net.riking.service.repo;

import net.riking.entity.model.SuspiciousPropDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bing.xun on 2017/4/6.
 */
@Repository
public interface SuspiciousPropDictRepo extends JpaRepository<SuspiciousPropDict, Long> {
}
