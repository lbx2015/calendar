package net.riking.service.repo;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.riking.entity.model.Customer;
@Repository
public interface CustomerDao {
	List<Customer> getMore(String riskRank,String csnm,String name);
	
}
