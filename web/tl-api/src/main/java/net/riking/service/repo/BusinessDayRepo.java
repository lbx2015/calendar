package net.riking.service.repo;

import net.riking.entity.model.BusinessDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bing.xun on 2017/6/14.
 */
@Repository
public interface BusinessDayRepo extends JpaRepository<BusinessDay, Long> {
}
