package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppLog;

@Repository
public interface AppLogInfoRepo extends JpaRepository<AppLog, String>, JpaSpecificationExecutor<AppLog>{

}
