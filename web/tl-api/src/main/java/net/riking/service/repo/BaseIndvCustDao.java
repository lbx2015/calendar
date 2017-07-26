package net.riking.service.repo;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.riking.entity.model.BaseIndvCust;

@Repository
public interface BaseIndvCustDao {
	
	List<BaseIndvCust> getMore(String khbh);
	
}
