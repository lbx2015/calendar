package net.riking.entity.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.riking.core.annos.Comment;
import net.riking.core.entity.ILog;
import net.riking.core.entity.LogConfig;
import net.riking.core.entity.model.BaseFlowJob;
@Comment("对公客户")
@Entity
@Table(name = "T_BASE_CORP_CUST")
public class BaseCorpCust extends BaseFlowJob implements ILog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;
	
	@Comment("客户编号")
	@Column(name = "khbh",length = 32)
	private String khbh;// 
	
	@Comment("客户中文名称")
	@Column(name = "khywmc",length =512)
	private String khywmc;
	
	@Comment("客户英文名称")
	@Column(name = "khzwmc",length = 512)
	private String khzwmc;
	
	@Comment("分行代码")
	@Column(name = "fhdm",length = 50)
	private String fhdm;
	
	@Comment("客户地区代码")
	@Column(name = "khdqdm",length =6)
	private String khdqdm;
	
	@Comment("经营所在地国家")
	@Column(name = "jyszdgj",length=3)
	private String jyszdgj;
	
	@Comment("总部所在地国家")
	@Column(name = "zbszdgj",length = 3)
	private String zbszdgj;
	
	@Comment("集团公司编号")
	@Column(name = "jtgsbh",length =50)
	private String jtgsbh;
	
	@Comment("集团公司名称")
	@Column(name = "jtgsmc",length =200)
	private String jtgsmc;
	
	@Comment("客户行业代码")
	@Column(name = "khhydm",length =6)
	private String khhydm;
	
	/*客户证件号码*/
	@Comment("客户证件号码")
	@Column(name = "khzjhm",length=32)
	private String khzjhm;

	@Comment("居民标识")
	@Column(name = "jmbz",length =3)
	private String jmbz;

	@Comment("客户类型")
	@Column(name = "khlx",length = 10)
	private String khlx;

	@Comment("注册日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "zcrq",length=8)
	private Date zcrq;

	@Comment("注册地址")
	@Column(name = "zcdz",length = 300)
	private String zcdz;
	
	/** 客户联系方式*/
	@Comment("客户联系方式")
	@Column(name = "lxfs",length= 500)
	private String lxfs;
	
	/** 客户证件类型*/
	@Comment(" 客户证件类型")
	@Column(name = "khzjlx",length= 6)
	private String khzjlx;
	
	/** 联系电话*/
	@Comment("联系电话")
	@Column(name = "lxdh",length= 64)
	private String lxdh;
	
	/**客户开户日期 */
	@Comment("客户开户日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "khkhrq")
	private Date khkhrq;
	
	/** 客户销户日期*/
	@Comment("客户销户日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "khxhrq")
	private Date khxhrq;
	
	/** 法人姓名*/
	@Comment("法人姓名")
	@Column(name = "frxm",length= 32)
	private String frxm;
	
	/** 法人证件类型*/
	@Comment("法人证件类型")
	@Column(name = "frzjlx",length= 32)
	private String frzjlx;
	
	/** 法人证件号码*/
	@Comment("法人证件号码")
	@Column(name = "frzjhm",length=128)
	private String frzjhm;
	
	/** 股东名称*/
	@Comment("股东名称")
	@Column(name = "gdmc",length= 128)
	private String gdmc;
	
	/** 股东证件类型*/
	@Comment("股东证件类型")
	@Column(name = "gdzjlx",length= 32)
	private String gdzjlx;
	
	/**股东证件号码*/
	@Comment("股东证件号码")
	@Column(name = "gdzjhm",length=40)
	private String gdzjhm;
	
	/**代办人证件类型*/
	@Comment("代办人证件类型")
	@Column(name = "dbrzjlx",length= 32)
	private String dbrzjlx;
	
	/**代办人证件号码*/
	@Comment("代办人证件号码")
	@Column(name = "dbrzjhm",length= 40)
	private String dbrzjhm;
	
	/**代办人名称*/
	@Comment("代办人名称")
	@Column(name = "dbrmc",length= 32)
	private String dbrmc;
	
	/**代办人国家*/
	@Comment("代办人国家")
	@Column(name = "dbrgj",length= 3)
	private String dbrgj;
	
	/**产品和服务风险*/
	@Comment("产品和服务风险")
	@Column(name = "cphfwfx",length= 3)
	private String cphfwfx;
	
	/**主要交易对手所在地*/
	@Comment("主要交易对手所在地")
	@Column(name = "zyjydsszd",length= 3)
	private String zyjydsszd;
	
	/**非面对面交易类型*/
	@Comment("非面对面交易类型")
	@Column(name = "fmdmjylx",length= 3)
	private String fmdmjylx;
	
	/**跨境标识*/
	@Comment("跨境标识")
	@Column(name = "kjbs",length= 3)
	private String kjbs;
	
	/**代理交易*/
	@Comment("代理交易")
	@Column(name = "dljy",length= 3)
	private String dljy;
	
	/**特殊业务类型的交易频率*/
	@Comment("特殊业务类型的交易频率")
	@Column(name = "tsywlxdjypl",length= 3)
	private String tsywlxdjypl;
	
	/**反洗钱交易监测记录*/
	@Comment("反洗钱交易监测记录")
	@Column(name = "amljcjl",length= 3)
	private String amljcjl;
	
	
	/**受益人地区风险（公司客户实际受益人、实际控制人的国籍、住所）*/
	@Comment("受益人地区风险（公司客户实际受益人、实际控制人的国籍、住所）")
	@Column(name = "syrdqfx",length= 3)
	private String syrdqfx;
	
	/**国籍所在地*/
	@Comment("国籍所在地")
	@Column(name = "gjszd",length= 3)
	private String gjszd;
	/**黑名单日期*/
	@Comment("黑名单日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "hbdrq",length = 8)
	private Date hbdrq;
	
	/**黑名单原因*/
	@Comment("黑名单原因")
	@Column(name = "hmdyy",length= 400)
	private String hmdyy;
	
	/**黑名单原因*/
	@Comment("")
	@Column(name = "gqhkzqdjg",length= 3)
	private String gqhkzqdjg;
	
	/**机构编码*/
	@Comment("机构编码")
	@Column(name = "jgbm",length= 32)
	private String jgbm;
	

	@Comment("注册邮政编码")
	@Column(name = "zcyzbm",length = 10)
	private String zcyzbm;

	@Comment("通讯地址")
	@Column(name = "txdz",length = 300)
	private String txdz;

	@Comment("通讯邮政编码")
	@Column(name = "txyzbm",length = 10)
	private String txyzbm;

	@Comment("企业出资人经济成分")
	@Column(name = "czrjjcf",length = 5)
	private String czrjjcf;

	@Comment("经济类型")
	@Column(name = "jjlx",length = 3)
	private String jjlx;

	@Comment("企业规模")
	@Column(name = "qygm",length = 1)
	private String qygm;

	@Comment("特殊经济区类型")
	@Column(name = "tsjjlx",length = 8)
	private String tsjjlx;

	@Comment("授信额度")
	@Column(name = "sxed", length = 20)
	private BigDecimal sxed;

	@Comment("客户状态")
	@Column(name = "khzt",length = 1)
	private String khzt;

	@Comment("")
	@Temporal(TemporalType.DATE)
	@Column(name = "pfrj", length = 8)
	private Date pfrj;

	@Comment("是否破产")
	@Column(name = "sfpc",length=2)
	private String sfpc;
	
	/** swiftCode*/
	@Comment("swiftCode")
	@Column(name = "swiftCode")
	private String swiftCode;

	@Comment("金融机构代码")
	@Column(name = "jrjgdm",length = 20)
	private String jrjgdm;

	@Comment("")
	@Column(name = "khyh",length = 64)
	private String khyh;

	@Comment("")
	@Column(name = "yhzh",length = 64)
	private String yhzh;

	@Comment("")
	@Column(name = "nsrsbm",length = 32)
	private String nsrsbm;

	@Comment("")
	@Column(name = "cwlxr",length = 40)
	private String cwlxr;

	@Comment("")
	@Column(name = "cwlxdz",length = 400)
	private String cwlxdz;

	@Comment("")
	@Column(name = "spr",length = 64)
	private String spr;

	@Comment("")
	@Column(name = "nsrlb",length = 2)
	private String nsrlb;

	@Comment("")
	@Column(name = "sfglf",length = 1)
	private String sfglf;

	@Comment("")
	@Column(name = "khfxdj",length = 1)
	private String khfxdj;

	@Comment("")
	@Column(name = "zzm",length = 20)
	private String zzm;

	@Comment("")
	@Column(name = "zczbbz",length = 3)
	private String zczbbz;

	@Comment("")
	@Column(name = "zczbje", length = 20)
	private BigDecimal zczbje;

	@Comment("")
	@Column(name = "sszbje", length = 20)
	private BigDecimal sszbje;

	@Comment("")
	@Temporal(TemporalType.DATE)
	@Column(name = "sjrq", length = 8)
	private Date sjrq;

	@Comment("")
	@Column(name = "hzqdgx",length = 3)
	private String hzqdgx;

	@Comment("")
	@Column(name = "sfbljl",length = 3)
	private String sfbljl;

	@Comment("")
	@Column(name = "riskTime")
	private String riskTime;// 璇勭骇鏃堕棿 2017/4/10

	@Comment("")
	@Column(name = "fxdjfs")
	private BigDecimal fxdjfs;

	@Comment("")
	@Column(name = "enabled",length = 1)
	private String enabled;

	@Comment("")
	@Column(name = "confirmStatus",length = 8)
	private String confirmStatus;

	@Comment("")
	@Column(name = "accountMainType",length = 8)
	private String accountMainType;
	
	@Comment("")
	@Column(name = "old_Id")
	private Long oldId;
	
	/** 最后修改时间*/
	@Column(name = "SYNC_LAST_TIME")
	private Long syncLastTime;
	
	@Transient
	private String zjlxsm;
	
	public String getZjlxsm() {
		return zjlxsm;
	}

	public void setZjlxsm(String zjlxsm) {
		this.zjlxsm = zjlxsm;
	}

	public String getGqhkzqdjg() {
		return gqhkzqdjg;
	}

	public void setGqhkzqdjg(String gqhkzqdjg) {
		this.gqhkzqdjg = gqhkzqdjg;
	}

	public String getJgbm() {
		return jgbm;
	}

	public void setJgbm(String jgbm) {
		this.jgbm = jgbm;
	}

	public String getKhzjhm() {
		return khzjhm;
	}

	public void setKhzjhm(String khzjhm) {
		this.khzjhm = khzjhm;
	}

	public String getKhzjlx() {
		return khzjlx;
	}

	public void setKhzjlx(String khzjlx) {
		this.khzjlx = khzjlx;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getCphfwfx() {
		return cphfwfx;
	}

	public void setCphfwfx(String cphfwfx) {
		this.cphfwfx = cphfwfx;
	}

	public String getZyjydsszd() {
		return zyjydsszd;
	}

	public void setZyjydsszd(String zyjydsszd) {
		this.zyjydsszd = zyjydsszd;
	}

	public String getFmdmjylx() {
		return fmdmjylx;
	}

	public void setFmdmjylx(String fmdmjylx) {
		this.fmdmjylx = fmdmjylx;
	}

	public String getKjbs() {
		return kjbs;
	}

	public void setKjbs(String kjbs) {
		this.kjbs = kjbs;
	}

	public String getDljy() {
		return dljy;
	}

	public void setDljy(String dljy) {
		this.dljy = dljy;
	}

	public String getTsywlxdjypl() {
		return tsywlxdjypl;
	}

	public void setTsywlxdjypl(String tsywlxdjypl) {
		this.tsywlxdjypl = tsywlxdjypl;
	}

	public String getAmljcjl() {
		return amljcjl;
	}

	public void setAmljcjl(String amljcjl) {
		this.amljcjl = amljcjl;
	}

	public Date getHbdrq() {
		return hbdrq;
	}

	public void setHbdrq(Date hbdrq) {
		this.hbdrq = hbdrq;
	}

	public String getHmdyy() {
		return hmdyy;
	}

	public void setHmdyy(String hmdyy) {
		this.hmdyy = hmdyy;
	}

	public Long getOldId() {
		return oldId;
	}

	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}

	public String getLxfs() {
		return lxfs;
	}

	public void setLxfs(String lxfs) {
		this.lxfs = lxfs;
	}

	public Date getKhkhrq() {
		return khkhrq;
	}

	public void setKhkhrq(Date khkhrq) {
		this.khkhrq = khkhrq;
	}

	public Date getKhxhrq() {
		return khxhrq;
	}

	public void setKhxhrq(Date khxhrq) {
		this.khxhrq = khxhrq;
	}

	public String getFrxm() {
		return frxm;
	}

	public void setFrxm(String frxm) {
		this.frxm = frxm;
	}

	public String getFrzjlx() {
		return frzjlx;
	}

	public void setFrzjlx(String frzjlx) {
		this.frzjlx = frzjlx;
	}

	public String getFrzjhm() {
		return frzjhm;
	}

	public void setFrzjhm(String frzjhm) {
		this.frzjhm = frzjhm;
	}

	public String getGdmc() {
		return gdmc;
	}

	public void setGdmc(String gdmc) {
		this.gdmc = gdmc;
	}

	public String getGdzjlx() {
		return gdzjlx;
	}

	public void setGdzjlx(String gdzjlx) {
		this.gdzjlx = gdzjlx;
	}

	public String getGdzjhm() {
		return gdzjhm;
	}

	public void setGdzjhm(String gdzjhm) {
		this.gdzjhm = gdzjhm;
	}

	public String getDbrzjlx() {
		return dbrzjlx;
	}

	public void setDbrzjlx(String dbrzjlx) {
		this.dbrzjlx = dbrzjlx;
	}

	public String getDbrzjhm() {
		return dbrzjhm;
	}

	public void setDbrzjhm(String dbrzjhm) {
		this.dbrzjhm = dbrzjhm;
	}

	public String getDbrmc() {
		return dbrmc;
	}

	public void setDbrmc(String dbrmc) {
		this.dbrmc = dbrmc;
	}

	public String getDbrgj() {
		return dbrgj;
	}

	public void setDbrgj(String dbrgj) {
		this.dbrgj = dbrgj;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKhbh() {
		return khbh;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}

	public String getKhywmc() {
		return khywmc;
	}

	public void setKhywmc(String khywmc) {
		this.khywmc = khywmc;
	}

	public String getKhzwmc() {
		return khzwmc;
	}

	public void setKhzwmc(String khzwmc) {
		this.khzwmc = khzwmc;
	}

	public String getFhdm() {
		return fhdm;
	}

	public void setFhdm(String fhdm) {
		this.fhdm = fhdm;
	}

	public String getKhdqdm() {
		return khdqdm;
	}

	public void setKhdqdm(String khdqdm) {
		this.khdqdm = khdqdm;
	}

	public String getJyszdgj() {
		return jyszdgj;
	}

	public void setJyszdgj(String jyszdgj) {
		this.jyszdgj = jyszdgj;
	}

	public String getZbszdgj() {
		return zbszdgj;
	}

	public void setZbszdgj(String zbszdgj) {
		this.zbszdgj = zbszdgj;
	}

	public String getJtgsbh() {
		return jtgsbh;
	}

	public void setJtgsbh(String jtgsbh) {
		this.jtgsbh = jtgsbh;
	}

	public String getJtgsmc() {
		return jtgsmc;
	}

	public void setJtgsmc(String jtgsmc) {
		this.jtgsmc = jtgsmc;
	}

	public String getKhhydm() {
		return khhydm;
	}

	public void setKhhydm(String khhydm) {
		this.khhydm = khhydm;
	}


	public String getJmbz() {
		return jmbz;
	}

	public void setJmbz(String jmbz) {
		this.jmbz = jmbz;
	}

	public String getKhlx() {
		return khlx;
	}

	public void setKhlx(String khlx) {
		this.khlx = khlx;
	}

	public Date getZcrq() {
		return zcrq;
	}

	public void setZcrq(Date zcrq) {
		this.zcrq = zcrq;
	}

	public String getZcdz() {
		return zcdz;
	}

	public void setZcdz(String zcdz) {
		this.zcdz = zcdz;
	}

	public String getZcyzbm() {
		return zcyzbm;
	}

	public void setZcyzbm(String zcyzbm) {
		this.zcyzbm = zcyzbm;
	}

	public String getTxdz() {
		return txdz;
	}

	public void setTxdz(String txdz) {
		this.txdz = txdz;
	}

	public String getTxyzbm() {
		return txyzbm;
	}

	public void setTxyzbm(String txyzbm) {
		this.txyzbm = txyzbm;
	}

	public String getCzrjjcf() {
		return czrjjcf;
	}

	public void setCzrjjcf(String czrjjcf) {
		this.czrjjcf = czrjjcf;
	}

	public String getJjlx() {
		return jjlx;
	}

	public void setJjlx(String jjlx) {
		this.jjlx = jjlx;
	}

	public String getQygm() {
		return qygm;
	}

	public void setQygm(String qygm) {
		this.qygm = qygm;
	}

	public String getTsjjlx() {
		return tsjjlx;
	}

	public void setTsjjlx(String tsjjlx) {
		this.tsjjlx = tsjjlx;
	}

	public BigDecimal getSxed() {
		return sxed;
	}

	public void setSxed(BigDecimal sxed) {
		this.sxed = sxed;
	}

	public String getSyrdqfx() {
		return syrdqfx;
	}

	public void setSyrdqfx(String syrdqfx) {
		this.syrdqfx = syrdqfx;
	}

	public String getKhzt() {
		return khzt;
	}

	public void setKhzt(String khzt) {
		this.khzt = khzt;
	}

	public Date getPfrj() {
		return pfrj;
	}

	public void setPfrj(Date pfrj) {
		this.pfrj = pfrj;
	}

	public String getSfpc() {
		return sfpc;
	}

	public void setSfpc(String sfpc) {
		this.sfpc = sfpc;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getJrjgdm() {
		return jrjgdm;
	}

	public void setJrjgdm(String jrjgdm) {
		this.jrjgdm = jrjgdm;
	}

	public String getKhyh() {
		return khyh;
	}

	public void setKhyh(String khyh) {
		this.khyh = khyh;
	}

	public String getYhzh() {
		return yhzh;
	}

	public void setYhzh(String yhzh) {
		this.yhzh = yhzh;
	}

	public String getNsrsbm() {
		return nsrsbm;
	}

	public void setNsrsbm(String nsrsbm) {
		this.nsrsbm = nsrsbm;
	}

	public String getCwlxr() {
		return cwlxr;
	}

	public void setCwlxr(String cwlxr) {
		this.cwlxr = cwlxr;
	}

	public String getCwlxdz() {
		return cwlxdz;
	}

	public void setCwlxdz(String cwlxdz) {
		this.cwlxdz = cwlxdz;
	}

	public String getSpr() {
		return spr;
	}

	public void setSpr(String spr) {
		this.spr = spr;
	}

	public String getNsrlb() {
		return nsrlb;
	}

	public void setNsrlb(String nsrlb) {
		this.nsrlb = nsrlb;
	}

	public String getSfglf() {
		return sfglf;
	}

	public void setSfglf(String sfglf) {
		this.sfglf = sfglf;
	}

	public String getKhfxdj() {
		return khfxdj;
	}

	public void setKhfxdj(String khfxdj) {
		this.khfxdj = khfxdj;
	}

	public String getZzm() {
		return zzm;
	}

	public void setZzm(String zzm) {
		this.zzm = zzm;
	}

	public String getAccountMainType() {
		return accountMainType;
	}

	public void setAccountMainType(String accountMainType) {
		this.accountMainType = accountMainType;
	}

	public String getZczbbz() {
		return zczbbz;
	}

	public void setZczbbz(String zczbbz) {
		this.zczbbz = zczbbz;
	}

	public BigDecimal getZczbje() {
		return zczbje;
	}

	public void setZczbje(BigDecimal zczbje) {
		this.zczbje = zczbje;
	}

	public String getGjszd() {
		return gjszd;
	}

	public void setGjszd(String gjszd) {
		this.gjszd = gjszd;
	}

	public BigDecimal getSszbje() {
		return sszbje;
	}

	public void setSszbje(BigDecimal sszbje) {
		this.sszbje = sszbje;
	}

	public Date getSjrq() {
		return sjrq;
	}

	public void setSjrq(Date sjrq) {
		this.sjrq = sjrq;
	}

	public String getHzqdgx() {
		return hzqdgx;
	}

	public void setHzqdgx(String hzqdgx) {
		this.hzqdgx = hzqdgx;
	}

	public String getSfbljl() {
		return sfbljl;
	}

	public void setSfbljl(String sfbljl) {
		this.sfbljl = sfbljl;
	}

	public String getRiskTime() {
		return riskTime;
	}

	public void setRiskTime(String riskTime) {
		this.riskTime = riskTime;
	}

	public BigDecimal getFxdjfs() {
		return fxdjfs;
	}

	public void setFxdjfs(BigDecimal fxdjfs) {
		this.fxdjfs = fxdjfs;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public Long getSyncLastTime() {
		return syncLastTime;
	}

	public void setSyncLastTime(Long syncLastTime) {
		this.syncLastTime = syncLastTime;
	}

	@Override
	public String toString() {
		return "BaseCorpCust [id=" + id + ", khbh=" + khbh + ", khywmc=" + khywmc + ", khzwmc=" + khzwmc + ", fhdm="
				+ fhdm + ", khdqdm=" + khdqdm + ", jyszdgj=" + jyszdgj + ", zbszdgj=" + zbszdgj + ", jtgsbh=" + jtgsbh
				+ ", jtgsmc=" + jtgsmc + ", khhydm=" + khhydm + ", khzjhm=" + khzjhm + ", jmbz=" + jmbz + ", khlx="
				+ khlx + ", zcrq=" + zcrq + ", zcdz=" + zcdz + ", lxfs=" + lxfs + ", khzjlx=" + khzjlx + ", lxdh="
				+ lxdh + ", khkhrq=" + khkhrq + ", khxhrq=" + khxhrq + ", frxm=" + frxm + ", frzjlx=" + frzjlx
				+ ", frzjhm=" + frzjhm + ", gdmc=" + gdmc + ", gdzjlx=" + gdzjlx + ", gdzjhm=" + gdzjhm + ", dbrzjlx="
				+ dbrzjlx + ", dbrzjhm=" + dbrzjhm + ", dbrmc=" + dbrmc + ", dbrgj=" + dbrgj + ", cphfwfx=" + cphfwfx
				+ ", zyjydsszd=" + zyjydsszd + ", fmdmjylx=" + fmdmjylx + ", kjbs=" + kjbs + ", dljy=" + dljy
				+ ", tsywlxdjypl=" + tsywlxdjypl + ", amljcjl=" + amljcjl + ", hbdrq=" + hbdrq + ", hmdyy=" + hmdyy
				+ ", zcyzbm=" + zcyzbm + ", txdz=" + txdz + ", txyzbm=" + txyzbm + ", czrjjcf=" + czrjjcf + ", jjlx="
				+ jjlx + ", qygm=" + qygm + ", tsjjlx=" + tsjjlx + ", sxed=" + sxed + ", khzt=" + khzt + ", pfrj="
				+ pfrj + ", sfpc=" + sfpc + ", swiftCode=" + swiftCode + ", jrjgdm=" + jrjgdm + ", khyh=" + khyh
				+ ", yhzh=" + yhzh + ", nsrsbm=" + nsrsbm + ", cwlxr=" + cwlxr + ", cwlxdz=" + cwlxdz + ", spr=" + spr
				+ ", nsrlb=" + nsrlb + ", sfglf=" + sfglf + ", khfxdj=" + khfxdj + ", zzm=" + zzm + ", zczbbz=" + zczbbz
				+ ", zczbje=" + zczbje + ", sszbje=" + sszbje + ", sjrq=" + sjrq + ", hzqdgx=" + hzqdgx + ", sfbljl="
				+ sfbljl + ", riskTime=" + riskTime + ", fxdjfs=" + fxdjfs + ", enabled=" + enabled + ", confirmStatus="
				+ confirmStatus + ", oldId=" + oldId
				+ "]";
	}
	
	 @Override
	    public LogConfig getLogConfig() {
	        return new LogConfig("id");
	    }
	
}
