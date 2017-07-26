package net.riking.service.repo.impl;

import net.riking.entity.model.BaseAif;
import net.riking.entity.model.BaseIndvCust;
import net.riking.entity.model.BaseTrn;
import net.riking.entity.model.ModelAmlInditrn;
import net.riking.service.repo.ModelAmlInditrnDao;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by bing.xun on 2017/5/26.
 */
public class ModelAmlInditrnRepoImpl implements ModelAmlInditrnDao {
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<ModelAmlInditrn> getWithTime(String rule, Date startDate, Date endDate) {
		String rule1 = "";
		if (StringUtils.isNotBlank(rule)) {
			rule1 = rule + " and jyrq<=:endDate and jyrq >:startDate";
		} else {
			rule1 = "jyrq<=:endDate and jyrq >:startDate";
		}
		String hql = "From ModelAmlInditrn where " + rule1;
		TypedQuery<ModelAmlInditrn> setParameter = em.createQuery(hql, ModelAmlInditrn.class)
				.setParameter("endDate", endDate).setParameter("startDate", startDate);
		List<ModelAmlInditrn> list = setParameter.getResultList();
		return list;
	}
	@Override
	@Transactional
	public int updateIndvByAif(BaseAif baseAif) {
		String hql = " update ModelAmlInditrn set kyye=:kyye,kjye=:kjye,zhjyrq=:zhjyrq,zhlx=:zhlx,jgjgzhlx=:jgjgzhlx,khrq=:khrq,ghrq=:ghrq,xelx=:xelx,zhxe=:zhxe,yhklx=:yhklx,yhkhm=:yhkhm,khbh=:khbh where zh=:zh " ;
		System.out.println(hql);
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
	public int updateIndvByTrn(BaseTrn BaseTrn) {
		String hql = "update ModelAmlInditrn set ywbh=:ywbh,jyfs=:jyfs,bz=:bz,zh=:zh,"
				+ "jdbj=:jdbj,jyrq=:jyrq,sxrq=:sxrq,jyje=:jyje,ysjyje=:ysjyje,mjje=:mjje,ywlx=:ywlx,"
				+ "jybm=:jybm,jyms=:jyms,jydsyhdm=:jydsyhdm,jydsyhmc=:jydsyhmc,jydsyhgjdm=:jydsyhgjdm,"
				+ "jydsmc=:jydsmc,jydsgjdm=:jydsgjdm,jydszh=:jydszh,jydslx=:jydslx,zffs=:zffs,dadt=:dadt,"
				+ "bhkfje=:bhkfje,dshkfje=:dshkfje,fxrt=:fxrt,fxAcod=:fxAcod,sfkfpphlx=:sfkfpphlx,"
				+ "sfkfpph=:sfkfpph,zjyt=:zjyt,jydswdlx=:jydswdlx,jydszjlx=:jydszjlx,jydszjhm=:jydszjhm,"
				+ "jydszhlx=:jydszhlx,fgtjyfs=:fgtjyfs,fgtjyfssbdm=:fgtjyfssbdm,yhyzfjgzjdywbm=:yhyzfjgzjdywbm,"
				+ "jyxxbz1=:jyxxbz1,jyxxbz2=:jyxxbz2,jrjgykhdgx=:jrjgykhdgx,jyfsd=:jyfsd,jgbm=:jgbm,"
				+ "dbrzjlx=:dbrzjlx,dbrzjhm=:dbrzjhm,dbrmc=:dbrmc,dbrgj=:dbrgj,khbh=:khbh  where jylsh=:jylsh" ;
		Query query = em.createQuery(hql);
		query.setParameter("jylsh", BaseTrn.getJylsh());
		query.setParameter("ywbh", BaseTrn.getYwbh());
		query.setParameter("jyfs", BaseTrn.getJyfs());
		query.setParameter("bz", BaseTrn.getBz());
		query.setParameter("zh", BaseTrn.getZh());
		query.setParameter("jdbj", BaseTrn.getJdbj());
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
		query.setParameter("jgbm", BaseTrn.getJgbm());
		query.setParameter("dadt", BaseTrn.getSxrq());
		query.setParameter("dbrzjlx", BaseTrn.getDbrzjlx());
		query.setParameter("dbrzjhm", BaseTrn.getDbrzjhm());
		query.setParameter("dbrmc", BaseTrn.getDbrmc());
		query.setParameter("dbrgj", BaseTrn.getDbrgj());
		query.setParameter("khbh", BaseTrn.getKhbh());
		int list = query.executeUpdate();  
		return list;
	}
	@Override
	@Transactional
	public int updateIndvByIndv(BaseIndvCust baseIndvCust) {
		String hql = " update ModelAmlInditrn set zjlx=:zjlx,zjhm=:zjhm,khywmc=:khywmc,khzwmc=:khzwmc,"
				+ "khdqdm=:khdqdm,khgj=:khgj,txdz=:txdz,yzbm=:yzbm,mz=:mz,xb=:xb,"
				+ "csrq=:csrq,hyzk=:hyzk,zgxl=:zgxl,zgxw=:zgxw,zy=:zy,dwmc=:dwmc,"
				+ "dwsshy=:dwsshy,gzqsrq=:gzqsrq,grnsr=:grnsr,gzzh=:gzzh,zhkhh=:zhkhh,jzzk=:jzzk,"
				+ "hjdz=:hjdz,hjyzbm=:hjyzbm,jzdz=:jzdz,sfjm=:sfjm,"
				+ "poxm=:poxm,pozjlx=:pozjlx,pozjhm=:pozjhm,podwmc=:podwmc,podh=:podh,"
				+ "bhygbz=:bhygbz,nhbz=:nhbz,jtysr=:jtysr,dwxz=:dwxz,khzt=:khzt,lxfs=:lxfs,"
				+ "khkhrq=:khkhrq,khxhrq=:khxhrq,lxdh=:lxdh where khbh=:khbh " ;
		Query query = em.createQuery(hql);
		query.setParameter("zjlx", baseIndvCust.getZjlx());
		query.setParameter("zjhm", baseIndvCust.getZjhm());
		query.setParameter("khywmc", baseIndvCust.getKhywmc());
		query.setParameter("khzwmc", baseIndvCust.getKhzwmc());
		query.setParameter("khdqdm", baseIndvCust.getKhdqdm());
		query.setParameter("khgj", baseIndvCust.getKhgj());
		query.setParameter("txdz", baseIndvCust.getTxdz());
		query.setParameter("yzbm", baseIndvCust.getYzbm());
		query.setParameter("mz", baseIndvCust.getBaseIndvCustAdd().getMz());
		query.setParameter("xb", baseIndvCust.getBaseIndvCustAdd().getXb());
		query.setParameter("csrq", baseIndvCust.getBaseIndvCustAdd().getCsrq());
		query.setParameter("hyzk", baseIndvCust.getBaseIndvCustAdd().getHyzk());
		query.setParameter("zgxl", baseIndvCust.getBaseIndvCustAdd().getZgxl());
		query.setParameter("zgxw", baseIndvCust.getBaseIndvCustAdd().getZgxw());
		query.setParameter("zy", baseIndvCust.getBaseIndvCustAdd().getZy());
		query.setParameter("dwmc", baseIndvCust.getBaseIndvCustAdd().getDwmc());
		query.setParameter("dwsshy", baseIndvCust.getBaseIndvCustAdd().getDwsshy());
		query.setParameter("gzqsrq", baseIndvCust.getBaseIndvCustAdd().getGzqsrq());
		query.setParameter("grnsr", baseIndvCust.getBaseIndvCustAdd().getGrnsr());
		query.setParameter("gzzh", baseIndvCust.getBaseIndvCustAdd().getGzzh());
		query.setParameter("zhkhh", baseIndvCust.getBaseIndvCustAdd().getZhkhh());
		query.setParameter("jzzk", baseIndvCust.getBaseIndvCustAdd().getJzzk());
		query.setParameter("hjdz", baseIndvCust.getBaseIndvCustAdd().getHjdz());
		query.setParameter("hjyzbm", baseIndvCust.getBaseIndvCustAdd().getHjyzbm());
		query.setParameter("jzdz", baseIndvCust.getBaseIndvCustAdd().getJzdz());
		query.setParameter("poxm", baseIndvCust.getBaseIndvCustAdd().getPoxm());
		query.setParameter("pozjlx", baseIndvCust.getBaseIndvCustAdd().getPozjlx());
		query.setParameter("pozjhm", baseIndvCust.getBaseIndvCustAdd().getPozjhm());
		query.setParameter("podwmc", baseIndvCust.getBaseIndvCustAdd().getPodwmc());
		query.setParameter("podh", baseIndvCust.getBaseIndvCustAdd().getPodh());
		query.setParameter("bhygbz", baseIndvCust.getBaseIndvCustAdd().getBhygbz());
		query.setParameter("nhbz", baseIndvCust.getBaseIndvCustAdd().getNhbz());
		query.setParameter("jtysr", baseIndvCust.getBaseIndvCustAdd().getJtysr());
		query.setParameter("dwxz", baseIndvCust.getBaseIndvCustAdd().getDwxz());
		query.setParameter("khzt", baseIndvCust.getBaseIndvCustAdd().getKhzt());
		query.setParameter("lxfs", baseIndvCust.getLxfs());
		query.setParameter("khkhrq", baseIndvCust.getKhkhrq());
		query.setParameter("khxhrq", baseIndvCust.getKhxhrq());
		query.setParameter("lxdh", baseIndvCust.getLxdh());
		query.setParameter("khbh", baseIndvCust.getKhbh());
		if ("CHN".equals(baseIndvCust.getKhgj())) {
			query.setParameter("sfjm", "Y");
		}else {
			query.setParameter("sfjm", "N");
		}
		int list = query.executeUpdate();  
		return list;
	}
}
