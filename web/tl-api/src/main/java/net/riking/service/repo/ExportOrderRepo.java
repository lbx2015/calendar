package net.riking.service.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.riking.entity.model.ExportOrder;

public interface ExportOrderRepo extends JpaRepository<ExportOrder, Long> {
	/*
	 * @Transactional(readOnly=true)
	 * 
	 * @Query(
	 * "select MAX(o.orderNum)FROM ExportOrder o WHERE o.branchCode = ?1 AND o.sysDate=?2  and o.orderType =?3  "
	 * ) public ExportOrder getOrderNum(String branchCode, Date date, String
	 * orderType);
	 */

	@Query("select MAX(o.orderNum) FROM ExportOrder o WHERE o.branchCode = ?1 AND o.sysDate=?2  and o.orderType =?3  ")
	public Integer getOrderNum(String branchCode, Date date, String orderType);
}
