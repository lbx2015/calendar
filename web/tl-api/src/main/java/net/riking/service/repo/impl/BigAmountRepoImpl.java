package net.riking.service.repo.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.riking.entity.model.QueryResult;
import net.riking.entity.report.CustomerReport;
import net.riking.service.repo.BigAmountDao;

public class BigAmountRepoImpl implements BigAmountDao {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Long findAllReport(Date start, Date end, String bwzt, Set<String> jgbm) {
		String sql = "select count(*) from BigAmount where deleteState='1'AND  flowState ='PRE_STORAGE' ";
		if (null != start) {
			sql = sql + "and rpdt >=:start ";
		}
		if (null != end) {
			sql = sql + "and rpdt <=:end ";
		}
		if (null != bwzt && !"".equals(bwzt)) {
			sql = sql + "and bwzt = '" + bwzt + "'";
		}
		if (null != jgbm && jgbm.size() > 0) {
			sql = sql + "and jgbm in :jgbm ";
		}
		Query query = em.createQuery(sql);
		if (null != start) {
			query.setParameter("start", start);
		}
		if (null != end) {
			query.setParameter("end", end);
		}
		if (null != jgbm && jgbm.size() > 0) {
			query.setParameter("jgbm", jgbm);
		}
		Long result = (Long) query.getSingleResult();
		return result;
	}

	@Override
	public List<CustomerReport> findByCtnmAndCsnm(String ctnm, String csnm, Date start, Date end, Set<String> jgbm) {
		String sql1 = "select new net.riking.entity.report.CustomerReport(csnm,ctnm) from BigAmount where deleteState ='1' AND  flowState ='PRE_STORAGE' ";
		if (null != start) {
			sql1 = sql1 + "and rpdt >=:start ";
		}
		if (null != end) {
			sql1 = sql1 + "and rpdt <=:end ";
		}
		if (null != jgbm && jgbm.size() > 0) {
			sql1 = sql1 + "and jgbm in :jgbm ";
		}
		sql1 = sql1 + " GROUP BY ctnm,csnm";
		Query query1 = em.createQuery(sql1);
		if (null != start) {
			query1.setParameter("start", start);
		}
		if (null != end) {
			query1.setParameter("end", end);
		}
		if (null != jgbm && jgbm.size() > 0) {
			query1.setParameter("jgbm", jgbm);
		}
		@SuppressWarnings("unchecked")
		List<CustomerReport> list1 = query1.getResultList();
		String sql2 = "select new net.riking.entity.report.CustomerReport(csnm,count(*),crtp,sum(crat)) from BigAmount where deleteState ='1' AND  flowState ='PRE_STORAGE' ";
		if (null != start) {
			sql2 = sql2 + "and rpdt >=:start ";
		}
		if (null != end) {
			sql2 = sql2 + "and rpdt <=:end ";
		}
		if (null != jgbm && jgbm.size() > 0) {
			sql2 = sql2 + "and jgbm in :jgbm ";
		}
		sql2 = sql2 + " GROUP BY crtp,csnm";
		Query query2 = em.createQuery(sql2);
		if (null != start) {
			query2.setParameter("start", start);
		}
		if (null != end) {
			query2.setParameter("end", end);
		}
		if (null != jgbm && jgbm.size() > 0) {
			query2.setParameter("jgbm", jgbm);
		}
		@SuppressWarnings("unchecked")
		List<CustomerReport> list2 = query2.getResultList();
		for (int i = 0; i < list1.size(); i++) {
			CustomerReport customerReport1 = (CustomerReport) list1.get(i);
			for (int j = 0; j < list2.size(); j++) {
				CustomerReport customerReport2 = list2.get(j);
				if (customerReport1.getNumber().equals(customerReport2.getNumber())) {
					customerReport2.setName(customerReport1.getName());
				}
			}
		}
		return list2;
	}

	@Override
	public List<QueryResult> findReportByCrcd(Date start, Date end, Set<String> jgbm) {
		String sql = "select new net.riking.entity.model.QueryResult(crcd,count(*)) from BigAmount where deleteState='1'AND flowState ='PRE_STORAGE'  ";
		if (null != start) {
			sql = sql + "and rpdt >=:start ";
		}
		if (null != end) {
			sql = sql + "and rpdt <=:end ";
		}
		if (null != jgbm && jgbm.size() > 0) {
			sql = sql + "and jgbm in :jgbm ";
		}
		sql = sql + " GROUP BY crcd";
		Query query = em.createQuery(sql);
		if (null != start) {
			query.setParameter("start", start);
		}
		if (null != end) {
			query.setParameter("end", end);
		}
		if (null != jgbm && jgbm.size() > 0) {
			query.setParameter("jgbm", jgbm);
		}
		@SuppressWarnings("unchecked")
		List<QueryResult> result = query.getResultList();
		return result;
	}

}
