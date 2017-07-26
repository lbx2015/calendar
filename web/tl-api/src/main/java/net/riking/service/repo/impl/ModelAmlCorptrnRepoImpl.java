package net.riking.service.repo.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import net.riking.entity.model.BaseAif;
import net.riking.entity.model.BaseCorpCust;
import net.riking.entity.model.BaseTrn;
import net.riking.entity.model.ModelAmlCorptrn;
import net.riking.service.repo.ModelAmlCorptrnDao;
import org.apache.commons.lang3.StringUtils;

public class ModelAmlCorptrnRepoImpl implements ModelAmlCorptrnDao {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<ModelAmlCorptrn> get(String rule,Date dadt) {
		String rule1 = rule + " and (dadt=:dadt)";
		String hql = "From ModelAmlCorptrn where" + rule1;
		System.out.println(hql);
		Query query = em.createQuery(hql).setParameter("dadt", dadt);
		List<ModelAmlCorptrn> list = query.getResultList();
		return list;
	}

	@Override
	public List<ModelAmlCorptrn> getWithTime(String rule,Date startDate ,Date endDate){
		String rule1 ="";
		if(StringUtils.isNotBlank(rule)){
			 rule1 = rule + " and jyrq<=:endDate and jyrq >:startDate";
		}else{
			rule1 = "jyrq<=:endDate and jyrq >:startDate";
		}
		String hql = "From ModelAmlCorptrn where " + rule1;
		TypedQuery<ModelAmlCorptrn> setParameter = em.createQuery(hql,ModelAmlCorptrn.class).setParameter("endDate", endDate).setParameter("startDate",startDate);
		List<ModelAmlCorptrn> list = setParameter.getResultList();
		return list;
	}

	@Override
	@Transactional
	public int updateCorpByAif(BaseAif baseAif) {
		String hql = " update ModelAmlCorptrn set kyye=:kyye,kjye=:kjye,zhjyrq=:zhjyrq,zhlx=:zhlx,jgjgzhlx=:jgjgzhlx,khrq=:khrq,ghrq=:ghrq,xelx=:xelx,zhxe=:zhxe,yhklx=:yhklx,yhkhm=:yhkhm,khbh=:khbh where zh=:zh " ;
		Query query = em.createQuery(hql);
		query.setParameter("kyye", baseAif.getKyye());
		query.setParameter("kjye", baseAif.getKjye());
		query.setParameter("zhjyrq", baseAif.getZhjyrq());
		query.setParameter("zhlx", baseAif.getNbzhlx());
		query.setParameter("jgjgzhlx", baseAif.getZhlx());
		query.setParameter("khrq", baseAif.getKhrq());
		query.setParameter("ghrq", baseAif.getGhrq());
		query.setParameter("xelx", baseAif.getXelx());
		query.setParameter("zhxe", baseAif.getXeje());
		query.setParameter("yhklx", baseAif.getYhklx());
		query.setParameter("yhkhm", baseAif.getYhkhm());
		query.setParameter("khbh", baseAif.getKhbh());
		query.setParameter("zh", baseAif.getZh());
		int list = query.executeUpdate();  
		return list;
	}

	@Override
	@Transactional
	public int updateCorpByTrn(BaseTrn BaseTrn) {
		String hql = "update ModelAmlCorptrn set ywbh=:ywbh,jyfs=:jyfs,bz=:bz,zh=:zh,"
				+ "jdbj=:jdbj,jyrq=:jyrq,sxrq=:sxrq,jyje=:jyje,ysjyje=:ysjyje,mjje=:mjje,ywlx=:ywlx,"
				+ "jybm=:jybm,jyms=:jyms,jydsyhdm=:jydsyhdm,jydsyhmc=:jydsyhmc,jydsyhgjdm=:jydsyhgjdm,"
				+ "jydsmc=:jydsmc,jydsgjdm=:jydsgjdm,jydszh=:jydszh,jydslx=:jydslx,zffs=:zffs,"
				+ "bhkfje=:bhkfje,dshkfje=:dshkfje,fxrt=:fxrt,fxAcod=:fxAcod,sfkfpphlx=:sfkfpphlx,"
				+ "sfkfpph=:sfkfpph,zjyt=:zjyt,jydswdlx=:jydswdlx,jydszjlx=:jydszjlx,jydszjhm=:jydszjhm,"
				+ "jydszhlx=:jydszhlx,fgtjyfs=:fgtjyfs,fgtjyfssbdm=:fgtjyfssbdm,yhyzfjgzjdywbm=:yhyzfjgzjdywbm,"
				+ "jyxxbz1=:jyxxbz1,jyxxbz2=:jyxxbz2,jrjgykhdgx=:jrjgykhdgx,jyfsd=:jyfsd,jgbm=:jgbm,khbh=:khbh where jylsh=:jylsh " ;
		Query query = em.createQuery(hql);
		query.setParameter("jylsh", BaseTrn.getJylsh());
		query.setParameter("ywbh", BaseTrn.getYwbh());
		query.setParameter("jyfs", BaseTrn.getJyfs());
		query.setParameter("bz", BaseTrn.getBz());
		query.setParameter("zh", BaseTrn.getZh());
		if("2".equals(BaseTrn.getJdbj())){
			query.setParameter("jdbj","C");
		}else if("1".equals(BaseTrn.getJdbj())){
			query.setParameter("jdbj","D");
		}else{
			query.setParameter("jdbj", BaseTrn.getJdbj());
		}
		query.setParameter("jyrq", BaseTrn.getJyrq());
		query.setParameter("sxrq", BaseTrn.getSxrq());
		query.setParameter("jyje", BaseTrn.getJyje());
		query.setParameter("ysjyje", BaseTrn.getYsjyje());
		query.setParameter("mjje", BaseTrn.getMjje());
		query.setParameter("ywlx", BaseTrn.getCplx());
		query.setParameter("jybm", BaseTrn.getJybm());
		query.setParameter("jyms", BaseTrn.getJyms());
		query.setParameter("jydsyhdm", BaseTrn.getJydsyhjgdm());
		query.setParameter("jydsyhmc", BaseTrn.getJydsyhmc());
		query.setParameter("jydsyhgjdm", BaseTrn.getJydsyhgjdm());
		query.setParameter("jydsmc", BaseTrn.getJydsmc());
		query.setParameter("jydsgjdm", BaseTrn.getJydsgjdm());
		query.setParameter("jydszh", BaseTrn.getJydszh());
		query.setParameter("jydslx", BaseTrn.getJydslx());
		query.setParameter("zffs", BaseTrn.getZffs());
		query.setParameter("bhkfje", BaseTrn.getBhkfje());
		query.setParameter("dshkfje", BaseTrn.getDshkfje());
		query.setParameter("fxrt", BaseTrn.getFxrt());
		query.setParameter("fxAcod", BaseTrn.getFxacod());
		query.setParameter("sfkfpphlx", BaseTrn.getSfkfpphlx());
		query.setParameter("sfkfpph", BaseTrn.getSfkfpph());
		query.setParameter("zjyt", BaseTrn.getZjyt());
		query.setParameter("jydswdlx", BaseTrn.getJydswdlx());
		query.setParameter("jydszjlx", BaseTrn.getJydszjlx());
		query.setParameter("jydszjhm", BaseTrn.getJydszjhm());
		query.setParameter("jydszhlx", BaseTrn.getJydszhlx());
		query.setParameter("fgtjyfs", BaseTrn.getFgtjyfs());
		query.setParameter("fgtjyfssbdm", BaseTrn.getFgtjyfssbdm());
		query.setParameter("yhyzfjgzjdywbm", BaseTrn.getYhyzfjgzjdywbm());
		query.setParameter("jyxxbz1", BaseTrn.getJyxxbz1());
		query.setParameter("jyxxbz2", BaseTrn.getJyxxbz2());
		query.setParameter("jrjgykhdgx", BaseTrn.getJrjgykhdgx());
		query.setParameter("jyfsd", BaseTrn.getJyfsd());
		query.setParameter("khbh", BaseTrn.getKhbh());
		query.setParameter("jgbm", BaseTrn.getJgbm());
		int list = query.executeUpdate();  
		return list;
	}

	@Override
	@Transactional
	public int updateCorpByCorp(BaseCorpCust BaseCorpCust) {
		String hql = " update ModelAmlCorptrn set khywmc=:khywmc,khzwmc=:khzwmc,khdqdm=:khdqdm,jyszdgj=:jyszdgj,zbszdgj=:zbszdgj,"
				+ "khhydm=:khhydm,zjhm=:zjhm,khlx=:khlx,jzdz=:jzdz,yzbm=:yzbm,jjlx=:jjlx,qygm=:qygm,"
				+ "khzt=:khzt,tsjjlx=:tsjjlx,fxdj=:fxdj,zczbbz=:zczbbz,zczbje=:zczbje,zcsdzb=:zcsdzb,dbrzjlx=:dbrzjlx,"
				+ "dbrzjhm=:dbrzjhm,dbrmc=:dbrmc,dbrgj=:dbrgj,lxfs=:lxfs,khkhrq=:khkhrq,khxhrq=:khxhrq,frxm=:frxm,"
				+ "frzjlx=:frzjlx,frzjhm=:frzjhm,gdmc=:gdmc,gdzjlx=:gdzjlx,gdzjhm=:gdzjhm,sfjm=:sfjm,lxdh=:lxdh,zjlx=:zjlx where khbh=:khbh " ;
		Query query = em.createQuery(hql);
		query.setParameter("khywmc", BaseCorpCust.getKhywmc());
		query.setParameter("khzwmc", BaseCorpCust.getKhzwmc());
		query.setParameter("khdqdm", BaseCorpCust.getKhdqdm());
		query.setParameter("jyszdgj", BaseCorpCust.getJyszdgj());
		query.setParameter("zbszdgj", BaseCorpCust.getZbszdgj());
		query.setParameter("khhydm", BaseCorpCust.getKhhydm());
		query.setParameter("zjlx", BaseCorpCust.getKhzjlx());
		query.setParameter("zjhm", BaseCorpCust.getKhzjhm());
		query.setParameter("khlx", BaseCorpCust.getKhlx());
		query.setParameter("jzdz", BaseCorpCust.getTxdz());
		query.setParameter("yzbm", BaseCorpCust.getTxyzbm());
		query.setParameter("fxdj", BaseCorpCust.getKhfxdj());
		query.setParameter("qygm", BaseCorpCust.getQygm());
		query.setParameter("khzt", BaseCorpCust.getKhzt());
		query.setParameter("tsjjlx", BaseCorpCust.getTsjjlx());
		query.setParameter("jjlx", BaseCorpCust.getJjlx());
		query.setParameter("zczbbz", BaseCorpCust.getZczbbz());
		query.setParameter("zczbje", BaseCorpCust.getZczbje());
		query.setParameter("zcsdzb", BaseCorpCust.getSszbje());
		query.setParameter("dbrzjlx", BaseCorpCust.getDbrzjlx());
		query.setParameter("dbrzjhm", BaseCorpCust.getDbrzjhm());
		query.setParameter("dbrmc", BaseCorpCust.getDbrmc());
		query.setParameter("dbrgj", BaseCorpCust.getDbrgj());
		query.setParameter("lxfs", BaseCorpCust.getLxfs());
		query.setParameter("khkhrq", BaseCorpCust.getKhkhrq());
		query.setParameter("khxhrq", BaseCorpCust.getKhxhrq());
		query.setParameter("frxm", BaseCorpCust.getFrxm());
		query.setParameter("frzjlx", BaseCorpCust.getFrzjlx());
		query.setParameter("frzjhm", BaseCorpCust.getFrzjhm());
		query.setParameter("gdmc", BaseCorpCust.getGdmc());
		query.setParameter("gdzjlx", BaseCorpCust.getGdzjlx());
		query.setParameter("gdzjhm", BaseCorpCust.getGdzjhm());
		query.setParameter("sfjm", BaseCorpCust.getJmbz());
		query.setParameter("lxdh", BaseCorpCust.getLxdh());
		query.setParameter("khbh", BaseCorpCust.getKhbh());
		int list = query.executeUpdate();  
		return list;
	}
}

