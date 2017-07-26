package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.SusAttachment;

@Repository
public interface SusAttachmentRepo extends JpaRepository<SusAttachment, Long>,JpaSpecificationExecutor<SusAttachment> {
	
	public List<SusAttachment> findBySusId(Long susId);
	
	@Query("from SusAttachment where susId in ?1")
	public List<SusAttachment> findBySusIdIn(Set<Long> susIds);
}
