package net.riking.dao.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.ShieldKeyWord;

@Repository
public interface ShieldKeyWordRepo extends JpaRepository<ShieldKeyWord, Long>, JpaSpecificationExecutor<ShieldKeyWord> {

	@Query("select keyWord from ShieldKeyWord where enabled=1")
	Set<String> findAllKeyWord();

}
