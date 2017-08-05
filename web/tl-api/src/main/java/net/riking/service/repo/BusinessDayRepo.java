package net.riking.service.repo;

import net.riking.entity.model.BusinessDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Lucky.Liu on 2017/8/05.
 */
@Repository
public interface BusinessDayRepo extends JpaRepository<BusinessDay, Long> {
}
