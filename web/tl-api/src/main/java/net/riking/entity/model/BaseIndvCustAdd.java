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

@Entity
@Table(name = "T_BASE_INDV_CUST_ADD")
public class BaseIndvCustAdd {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;// 客户编号

	/** 客户编号 */
	@Column(name = "khbh",length = 50)
	private String khbh;
	
	/** 名族 */
	@Column(name = "mz",length = 10)
	private String mz;
	
	/** 性别*/
	@Column(name = "xb",length = 1)
	private String xb;
	
	/** 出生日期*/
	@Temporal(TemporalType.DATE)
	@Column(name = "csrq", length = 8)
	private Date csrq;
	
	/** 婚姻状况*/
	@Column(name = "hyzk",length = 2)
	private String hyzk;
	
	/** 最高学历*/
	@Column(name = "zgxl",length = 2)
	private String zgxl;
	
	/** 最高学位*/
	@Column(name = "zgxw",length = 1)
	private String zgxw;
	
	/** 邮件*/
	@Column(name = "yj",length = 100)
	private String yj;
	
	/** 住宅电话*/
	@Column(name = "zzdh",length = 20)
	private String zzdh;
	
	/** 移动电话*/
	@Column(name = "yddh",length = 20)
	private String yddh;
	
	/** 职业*/
	@Column(name = "zy",length = 1)
	private String zy;
	
	/** 单位名称*/
	@Column(name = "dwmc",length = 200)
	private String dwmc;
	
	/** 单位所属行业*/
	@Column(name = "dwsshy",length = 5)
	private String dwsshy;
	
	/** 单位地址*/
	@Column(name = "dwdz",length = 300)
	private String dwdz;
	
	/** 单位邮编*/
	@Column(name = "dwyb",length = 10)
	private String dwyb;
	
	/** 单位电话*/
	@Column(name = "dwdh",length = 20)
	private String dwdh;
	
	/** 本单位工作起始日期*/
	@Temporal(TemporalType.DATE)
	@Column(name = "gzqsrq", length = 8)
	private Date gzqsrq;
	
	/** 职务*/
	@Column(name = "zw",length = 1)
	private String zw;
	
	/** 职称*/
	@Column(name = "zc",length = 1)
	private String zc;
	
	/** 个人年收入*/
	@Column(name = "grnsr", length = 20)
	private BigDecimal grnsr;
	
	/** 工资账户*/
	@Column(name = "gzzh",length = 30)
	private String gzzh;
	
	/** 账户开户行*/
	@Column(name = "zhkhh",length = 100)
	private String zhkhh;
	
	/** 居住状况*/
	@Column(name = "jzzk",length = 1)
	private String jzzk;
	
	/** 户籍地址*/
	@Column(name = "hjdz",length = 300)
	private String hjdz;
	
	/** 户籍地址邮编*/
	@Column(name = "hjyzbm",length = 10)
	private String hjyzbm;
	
	/** 居住地址*/
	@Column(name = "jzdz",length = 300)
	private String jzdz;
	
	/** 邮政编码*/
	@Column(name = "yzbm",length = 10)
	private String yzbm;
	
	/** 配偶姓名*/
	@Column(name = "poxm",length = 50)
	private String poxm;
	
	/** 配偶证件类型*/
	@Column(name = "pozjlx",length = 2)
	private String pozjlx;
	
	/** 配偶证件号码*/
	@Column(name = "pozjhm",length = 20)
	private String pozjhm;
	
	/** 配偶单位名称*/
	@Column(name = "podwmc",length = 200)
	private String podwmc;
	
	/** 配偶联系人电话*/
	@Column(name = "podh",length = 20)
	private String podh;
	
	/** 本行员工标识*/
	@Column(name = "bhygbz",length = 1)
	private String bhygbz;
	
	/** 本行黑名单标识*/
	@Column(name = "bhhmdbz",length = 1)
	private String bhhmdbz;
	
	/** 黑名单日期*/
	@Temporal(TemporalType.DATE)
	@Column(name = "hbdrq",length=8)
	private Date hbdrq;
	
	/** 黑名单原因*/
	@Column(name = "hmdyy",length = 400)
	private String hmdyy;
	
	/** 农户标识*/
	@Column(name = "nhbz",length = 1)
	private String nhbz;
	
	/** 家庭月收入*/
	@Column(name = "jtysr",length=20)
	private BigDecimal jtysr;
	
	/** 单位性质*/
	@Column(name = "dwxz",length = 30)
	private String dwxz;
	
	/** 客户状态*/
	@Column(name = "khzt",length = 1)
	private String khzt;
	
	/** 数据日期*/
	@Temporal(TemporalType.DATE)
	@Column(name = "sjrq",length=8)
	private Date sjrq;
	
	/** 数据导入日期*/
	@Temporal(TemporalType.DATE)
	@Column(name = "drrq",length=8)
	private Date drrq;
	
	/** 分行代码 */
	@Column(name = "fhdm",length =32)
	private String fhdm;
	
	/** 年龄*/
	@Column(name = "nl",length =32)
	private String nl;
	
	/** 反洗钱交易监测记录 */
	@Column(name = "amljcjl",length =3)
	private String amljcjl;
	
	
	/** 是否有不良记录 */
	@Column(name = "sfbljl",length =3)
	private String sfbljl;
	
	
	/**产品和服务风险 */
	@Column(name = "cphfwfx",length =3)
	private String cphfwfx;
		
	/** 主要交易对手所在地 */
	@Column(name = "zyjydsszd",length =3)
	private String zyjydsszd;

	/** 非面对面交易类型*/
	@Column(name = "fmdmjylx",length =3)
	private String fmdmjylx;
		
	/** 跨境标识 */
	@Column(name = "kjbs",length =3)
	private String kjbs;
		
	/**代理交易 */
	@Column(name = "dljy",length =3)
	private String dljy;
	
	/** 特殊业务类型的交易频率 */
	@Column(name = "tsywlxdjypl",length =3)
	private String tsywlxdjypl;
	
	/** 合作渠道关系*/
	@Column(name = "hzqdgx",length =3)
	private String hzqdgx;
	
	/** 信息公开程度*/
	@Column(name = "xxgkcd",length =3)
	private String xxgkcd;

	/**国籍所在地*/
	@Column(name = "gjszd",length= 3)
	private String gjszd;
	
	
	public String getGjszd() {
		return gjszd;
	}

	public void setGjszd(String gjszd) {
		this.gjszd = gjszd;
	}

	public String getFhdm() {
		return fhdm;
	}

	public void setFhdm(String fhdm) {
		this.fhdm = fhdm;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getAmljcjl() {
		return amljcjl;
	}

	public void setAmljcjl(String amljcjl) {
		this.amljcjl = amljcjl;
	}

	public String getSfbljl() {
		return sfbljl;
	}

	public void setSfbljl(String sfbljl) {
		this.sfbljl = sfbljl;
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

	public String getHzqdgx() {
		return hzqdgx;
	}

	public void setHzqdgx(String hzqdgx) {
		this.hzqdgx = hzqdgx;
	}

	public String getXxgkcd() {
		return xxgkcd;
	}

	public void setXxgkcd(String xxgkcd) {
		this.xxgkcd = xxgkcd;
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

	public String getMz() {
		return mz;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public Date getCsrq() {
		return csrq;
	}

	public void setCsrq(Date csrq) {
		this.csrq = csrq;
	}

	public String getHyzk() {
		return hyzk;
	}

	public void setHyzk(String hyzk) {
		this.hyzk = hyzk;
	}

	public String getZgxl() {
		return zgxl;
	}

	public void setZgxl(String zgxl) {
		this.zgxl = zgxl;
	}

	public String getZgxw() {
		return zgxw;
	}

	public void setZgxw(String zgxw) {
		this.zgxw = zgxw;
	}

	public String getYj() {
		return yj;
	}

	public void setYj(String yj) {
		this.yj = yj;
	}

	public String getZzdh() {
		return zzdh;
	}

	public void setZzdh(String zzdh) {
		this.zzdh = zzdh;
	}

	public String getYddh() {
		return yddh;
	}

	public void setYddh(String yddh) {
		this.yddh = yddh;
	}

	public String getZy() {
		return zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getDwsshy() {
		return dwsshy;
	}

	public void setDwsshy(String dwsshy) {
		this.dwsshy = dwsshy;
	}

	public String getDwdz() {
		return dwdz;
	}

	public void setDwdz(String dwdz) {
		this.dwdz = dwdz;
	}

	public String getDwyb() {
		return dwyb;
	}

	public void setDwyb(String dwyb) {
		this.dwyb = dwyb;
	}

	public String getDwdh() {
		return dwdh;
	}

	public void setDwdh(String dwdh) {
		this.dwdh = dwdh;
	}

	public Date getGzqsrq() {
		return gzqsrq;
	}

	public void setGzqsrq(Date gzqsrq) {
		this.gzqsrq = gzqsrq;
	}

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	public String getZc() {
		return zc;
	}

	public void setZc(String zc) {
		this.zc = zc;
	}

	public BigDecimal getGrnsr() {
		return grnsr;
	}

	public void setGrnsr(BigDecimal grnsr) {
		this.grnsr = grnsr;
	}

	public String getGzzh() {
		return gzzh;
	}

	public void setGzzh(String gzzh) {
		this.gzzh = gzzh;
	}

	public String getZhkhh() {
		return zhkhh;
	}

	public void setZhkhh(String zhkhh) {
		this.zhkhh = zhkhh;
	}

	public String getJzzk() {
		return jzzk;
	}

	public void setJzzk(String jzzk) {
		this.jzzk = jzzk;
	}

	public String getHjdz() {
		return hjdz;
	}

	public void setHjdz(String hjdz) {
		this.hjdz = hjdz;
	}

	public String getHjyzbm() {
		return hjyzbm;
	}

	public void setHjyzbm(String hjyzbm) {
		this.hjyzbm = hjyzbm;
	}

	public String getJzdz() {
		return jzdz;
	}

	public void setJzdz(String jzdz) {
		this.jzdz = jzdz;
	}

	public String getYzbm() {
		return yzbm;
	}

	public void setYzbm(String yzbm) {
		this.yzbm = yzbm;
	}

	public String getPoxm() {
		return poxm;
	}

	public void setPoxm(String poxm) {
		this.poxm = poxm;
	}

	public String getPozjlx() {
		return pozjlx;
	}

	public void setPozjlx(String pozjlx) {
		this.pozjlx = pozjlx;
	}

	public String getPozjhm() {
		return pozjhm;
	}

	public void setPozjhm(String pozjhm) {
		this.pozjhm = pozjhm;
	}

	public String getPodwmc() {
		return podwmc;
	}

	public void setPodwmc(String podwmc) {
		this.podwmc = podwmc;
	}

	public String getPodh() {
		return podh;
	}

	public void setPodh(String podh) {
		this.podh = podh;
	}

	public String getBhygbz() {
		return bhygbz;
	}

	public void setBhygbz(String bhygbz) {
		this.bhygbz = bhygbz;
	}

	public String getBhhmdbz() {
		return bhhmdbz;
	}

	public void setBhhmdbz(String bhhmdbz) {
		this.bhhmdbz = bhhmdbz;
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

	public String getNhbz() {
		return nhbz;
	}

	public void setNhbz(String nhbz) {
		this.nhbz = nhbz;
	}

	public BigDecimal getJtysr() {
		return jtysr;
	}

	public void setJtysr(BigDecimal jtysr) {
		this.jtysr = jtysr;
	}

	public String getDwxz() {
		return dwxz;
	}

	public void setDwxz(String dwxz) {
		this.dwxz = dwxz;
	}

	public String getKhzt() {
		return khzt;
	}

	public void setKhzt(String khzt) {
		this.khzt = khzt;
	}

	public Date getSjrq() {
		return sjrq;
	}

	public void setSjrq(Date sjrq) {
		this.sjrq = sjrq;
	}

	public Date getDrrq() {
		return drrq;
	}

	public void setDrrq(Date drrq) {
		this.drrq = drrq;
	}

	
	
}
