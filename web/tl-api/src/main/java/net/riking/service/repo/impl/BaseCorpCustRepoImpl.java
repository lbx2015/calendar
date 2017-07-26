package net.riking.service.repo.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import net.riking.entity.model.BaseCorpCust;
import net.riking.entity.model.BaseTrn;
import net.riking.service.repo.BaseCorpCustDao;

public class BaseCorpCustRepoImpl implements BaseCorpCustDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<BaseCorpCust> getMore(String khbh) {
		
		String sql = "select t from BaseCorpCust t where t.enabled='1'";
		if(StringUtils.isNotEmpty(khbh)){
			sql += " and  t.khbh like'%"+ khbh+"%' ";
		}
		
		TypedQuery<BaseCorpCust> createQuery = em.createQuery(sql,BaseCorpCust.class);
		List<BaseCorpCust> resultList = createQuery.getResultList();
		return resultList;
	}

	@Override
	public List<String> getMore(List<String> csnms) {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.khzwmc from BaseCorpCust t where t.enabled='1' and  confirmStatus='101002'  and t.khbh in (");
		for (int i = 0; i < csnms.size(); i++) {
			if (i==csnms.size()-1) {
				sql.append("'"+csnms.get(i)+"') order by khbh");
			}else{
				sql.append("'"+csnms.get(i)+"',");
			}
		}
		TypedQuery<String> createQuery = em.createQuery(sql.toString(),String.class);
		List<String> resultList = createQuery.getResultList();
		return resultList;
	}

	@Override
	public List<BaseCorpCust> getBykhbh(String khbh) {
		String sql ="select t.khbh from BaseCorpCust t where t.enabled='1' and  t.khbh='"+khbh+"'";
		TypedQuery<BaseCorpCust> createQuery = em.createQuery(sql,BaseCorpCust.class);
		List<BaseCorpCust> resultList = createQuery.getResultList();
		return resultList;
	}

	@Override
	public List<BaseCorpCust> getByshtyzxdm(String shtxxydm) {
		String sql ="select t.khzjhm from BaseCorpCust t where t.enabled='1' and  t.khzjhm='"+shtxxydm+"'";
		TypedQuery<BaseCorpCust> createQuery = em.createQuery(sql,BaseCorpCust.class);
		List<BaseCorpCust> resultList = createQuery.getResultList();
		return resultList;
	}

	@Override
	public List<BaseCorpCust> getByyhzh(String yhzh) {
		String sql ="select t.yhzh from BaseCorpCust t where t.enabled='1' and  t.yhzh='"+yhzh+"'";
		TypedQuery<BaseCorpCust> createQuery = em.createQuery(sql,BaseCorpCust.class);
		List<BaseCorpCust> resultList = createQuery.getResultList();
		return resultList;
	}

	@Override
	public List<BaseCorpCust> getBynsrzbh(String nsrsbm) {
		String sql ="select t.nsrsbm from BaseCorpCust t where t.enabled='1' and  t.nsrsbm='"+nsrsbm+"'";
		TypedQuery<BaseCorpCust> createQuery = em.createQuery(sql,BaseCorpCust.class);
		List<BaseCorpCust> resultList = createQuery.getResultList();
		return resultList;
	}

	@Override
	public  List<BaseTrn> getMoreBaseTrn() {
		String sql = "select t from BaseTrn t where job is null and enabled='1' and delState='1' ";
		TypedQuery<BaseTrn> createQuery = em.createQuery(sql,BaseTrn.class);
		List<BaseTrn> resultList = createQuery.getResultList();
		return resultList;
	}

}
