package net.riking.service.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.BaseReceipt;

@Repository
public interface BaseReceiptRepo extends JpaRepository<BaseReceipt, Long>, JpaSpecificationExecutor<BaseReceipt> {
	@Transactional
	@Modifying
	@Query("update BaseReceipt set hzState='02' where id = ?1 ")
	int updateByHzState(Long id);

	List<BaseReceipt> findByHzmc(String hzmc);
}
