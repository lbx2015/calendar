package net.riking.service.repo.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import net.riking.entity.model.BaseIndvCust;
import net.riking.service.repo.BaseIndvCustDao;

public class BaseIndvCustRepoImpl implements BaseIndvCustDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<BaseIndvCust> getMore(String khbh) {

		String sql = "select t from BaseIndvCust t where 1=1";
		if (StringUtils.isNotEmpty(khbh)) {
			sql += " and  t.khbh like'%" + khbh + "%' ";
		}
		TypedQuery<BaseIndvCust> query = em.createQuery(sql, BaseIndvCust.class);
		List<BaseIndvCust> resultList = query.getResultList();
		return resultList;
	}

}
