package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.EmailSuffix;

@Repository
public interface EmailSuffixRepo extends JpaRepository<EmailSuffix, String>, JpaSpecificationExecutor<EmailSuffix> {
	/**
	 * 获取有效数据
	 * @param phone
	 * @return
	 */
	@Query(" from EmailSuffix where isDeleted = 1 ")
	List<EmailSuffix> findInvalidDataByIsDeleted();

}
