package net.riking.service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.DataReport;
@Repository
public interface DataExportRepo extends JpaRepository<DataReport, Long>, JpaSpecificationExecutor<DataReport>{

	@Query("from DataReport  where modular=?1 and state='1' order by showLevel")
	List<DataReport> findByModular(String Modular );
}
