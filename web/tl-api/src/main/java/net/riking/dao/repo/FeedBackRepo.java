package net.riking.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.FeedBack;

/**
 * 
 * 〈反馈表〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface FeedBackRepo extends JpaRepository<FeedBack, String>, JpaSpecificationExecutor<FeedBack> {

}
