package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.BaseIndvCustAdd;

@Repository
public interface BaseIndvCustAddRepo extends JpaRepository<BaseIndvCustAdd, Long> {
	
	
}
