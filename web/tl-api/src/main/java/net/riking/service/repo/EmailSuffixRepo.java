package net.riking.service.repo;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.EmailSuffix;

@Repository
public interface EmailSuffixRepo extends JpaRepository<EmailSuffix, String>, JpaSpecificationExecutor<EmailSuffix>{

	@Transactional
	@Modifying
	@Query("update EmailSuffix set deleteState = '0'  where id in ?1")
	int deleteByIds(Set<String> ids);
}
