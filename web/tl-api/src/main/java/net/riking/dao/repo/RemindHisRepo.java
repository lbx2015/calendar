package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.RemindHis;
/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月9日 上午10:33:17
 * @used TODO
 */
@Repository
public interface RemindHisRepo extends JpaRepository<RemindHis, String>, JpaSpecificationExecutor<RemindHis> {
	

	List<RemindHis> findByUserId(String userId);
}
