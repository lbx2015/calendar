package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.riking.entity.model.AppVersion;

public interface AppVersionRepo extends JpaRepository<AppVersion, String>, JpaSpecificationExecutor<AppVersion>{

}
