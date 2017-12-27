package net.riking.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.SysNotice;

@Repository
public interface SysNoticeRepo extends JpaRepository<SysNotice, String>, JpaSpecificationExecutor<SysNotice> {
	

}