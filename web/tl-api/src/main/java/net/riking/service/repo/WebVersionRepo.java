package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.riking.entity.model.WebVersion;

public interface WebVersionRepo extends JpaRepository<WebVersion, String>, JpaSpecificationExecutor<WebVersion> {

}
