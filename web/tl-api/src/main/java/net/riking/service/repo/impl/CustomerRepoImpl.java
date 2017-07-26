package net.riking.service.repo.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import net.riking.entity.model.Customer;
import net.riking.service.repo.CustomerDao;

public class CustomerRepoImpl implements CustomerDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Customer> getMore(String riskRank, String csnm, String name) {
		String sql_1 = "";
		String sql_2 = "";
		String sql_3 = "";
		String sql = "select t from Customer t where ";
		String all = "";
		if(StringUtils.isNotEmpty(riskRank)){
			sql_1 = " t.riskRank="+ riskRank+" and";
		}
		if(StringUtils.isNotEmpty(csnm)){
			sql_2 = " t.csnm="+csnm+" and";
		}
		if(StringUtils.isNotEmpty(name)){
			sql_3 = " (t.enName like '%" + name + "%' or t.englishName like '%" + name + "%' )";
		}
		if(StringUtils.isEmpty(riskRank) && StringUtils.isEmpty(csnm) && StringUtils.isEmpty(name)){
			sql = "From Customer";
		}
		all = sql + sql_1 + sql_2 + sql_3;
		TypedQuery<Customer> createQuery = em.createQuery(all,Customer.class);
		List<Customer> resultList = createQuery.getResultList();
		return resultList;
	}


}
