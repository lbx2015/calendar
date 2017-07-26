package net.riking.service.repo.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import net.riking.entity.model.Sdcurrpd;
import net.riking.service.repo.SdcurrpdDao;

public class SdcurrpdRepoImpl implements SdcurrpdDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Sdcurrpd> getMore(String ccy,Date effectDate) {
		
		String sql = "From Sdcurrpd where 1=1";
		if(StringUtils.isNotEmpty(ccy)){
			sql += " and  currency =:ccy";
			
		}
		
		if(effectDate!=null){
			sql += " and  rateDate =:effectDate ";
		}
		TypedQuery<Sdcurrpd> query = null;
		if(StringUtils.isNotEmpty(ccy)&&effectDate!=null){
			query = em.createQuery(sql,Sdcurrpd.class).setParameter("ccy", ccy).setParameter("effectDate", effectDate);
		}else if(StringUtils.isNotEmpty(ccy)){
			query = em.createQuery(sql,Sdcurrpd.class).setParameter("ccy", ccy);
		}else if(effectDate!=null){
			query = em.createQuery(sql,Sdcurrpd.class).setParameter("effectDate", effectDate);
		}else{
			query = em.createQuery(sql,Sdcurrpd.class);
		}
		
		List<Sdcurrpd> resultList = query.getResultList();
		return resultList;
	}

}
