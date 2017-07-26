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
@Table(name = "T_MODEL_AML_INDITRN")
public class ModelAmlInditrn {

	@Id
	@Column(name = "id")
	@GeneratedValue
	private Long id;

	/** 证件类型 */
	@Column(name = "zjlx",length = 6)
	private String zjlx;

	/** 客户号 */
	@Column(name = "khbh",length = 32)
	private String khbh;

	/** 客户身份证件/证明文件号码 */
	@Column(name = "zjhm",length = 128)
	private String zjhm;

	/** 客户英文名称 */
	@Column(name = "khywmc",length = 512 )
	private String khywmc;

	/** 客户中文名称 */
	@Column(name = "khzwmc",length = 512)
	private String khzwmc;

	/** 客户地区代码 */
	@Column(name = "khdqdm",length = 6)
	private String khdqdm;

	/** 客户国籍 */
	@Column(name = "khgj",length = 3)
	private String khgj;

	/** 通讯地址 */
	@Column(name = "txdz",length = 512)
	private String txdz;

	/** 邮政编码 */
	@Column(name = "yzbm",length = 10)
	private String yzbm;

	/** 民族 */
	@Column(name = "mz",length = 10)
	private String mz;

	/** 性别 */
	@Column(name = "xb",length = 1)
	private String xb;

	/** 出生日期（YYYYMMDD） */
	@Temporal(TemporalType.DATE)
	@Column(name = "csrq")
	private Date csrq;

	/** 婚姻状况 */
	//bug private String hyzk; 
	@Column(name = "hyzk",length = 2)
	private String hyzk;

	/** 最高学历 */
	@Column(name = "zgxl",length = 2)
	private String zgxl;

	/** 最高学位 */
	@Column(name = "zgxw",length = 1)
	private String zgxw;

	/** 职业 */
	@Column(name = "zy",length = 1)
	private String zy;

	/** 单位名称 */
	@Column(name = "dwmc",length = 200)
	private String dwmc;

	/** 单位所属行业 */
	@Column(name = "dwsshy",length = 5)
	private String dwsshy;

	/** 本单位工作起始年份 */
	@Temporal(TemporalType.DATE)
	@Column(name = "gzqsrq")
	private Date gzqsrq;

	/** 单位所属行业 */
	@Column(name = "grnsr", length = 20)
	private BigDecimal grnsr;

	/** 工资账户 */
	@Column(name = "gzzh",length = 30)
	private String gzzh;

	/** 工资账户开户行 */
	@Column(name = "zhkhh",length = 100)
	private String zhkhh;

	/** 居住状况 */
	@Column(name = "jzzk",length = 1)
	private String jzzk;

	/** 户籍地址 */
	@Column(name = "hjdz",length = 300)
	private String hjdz;

	/** 户籍地址邮编 */
	@Column(name = "hjyzbm",length = 10)
	private String hjyzbm;

	/** 居住地址 */
	@Column(name = "jzdz",length = 300)
	private String jzdz;

	/** 配偶姓名 */
	@Column(name = "poxm",length = 50)
	private String poxm;
	
	/** 配偶证件号码 */
	@Column(name = "pozjhm",length = 20)
	private String pozjhm;

	/** 配偶证件类型 */
	@Column(name = "pozjlx",length = 2)
	private String pozjlx;

	/** 配偶单位名称 */
	@Column(name = "podwmc",length = 200)
	private String podwmc;

	/** 配偶联系人电话 */
	@Column(name = "podh",length = 20)
	private String podh;

	/** 本行员工标识 */
	@Column(name = "bhygbz",length = 1)
	private String bhygbz;

	/** 农户标识 */
	@Column(name = "nhbz",length = 1)
	private String nhbz;

	/** 家庭月收入 */
	@Column(name = "jtysr", length = 20)
	private BigDecimal jtysr;

	/** 单位性质 */
	@Column(name = "dwxz",length = 30)
	private String dwxz;

	/** 客户状态 */
	@Column(name = "khzt",length = 1)
	private String khzt;

	/** 交易流水号 */
	@Column(name = "jylsh",length = 256)
	private String jylsh;

	/** 业务编号 */
	@Column(name = "ywbh",length = 256)
	private String ywbh;

	/** 币种 */
	@Column(name = "bz",length = 3)
	private String bz;

	/** 帐目编号(账号) */
	@Column(name = "zh",length = 64)
	private String zh;

	/** 可用余额 */
	@Column(name = "kyye", length = 20)
	private BigDecimal kyye;

	/** 会计余额 */
	@Column(name = "kjye", length = 20)
	private BigDecimal kjye;

	/** 最后交易日期 */
	@Temporal(TemporalType.DATE)
	@Column(name = "zhjyrq")
	private Date zhjyrq;

	/** 账户类型 */
	@Column(name = "zhlx",length = 6)
	private String zhlx;

	/** 监管机构类型 */
	@Column(name = "jgjgzhlx",length = 6)
	private String jgjgzhlx;

	/** 开户日期 */
	@Temporal(TemporalType.DATE)
	@Column(name = "khrq")
	private Date khrq;

	/** 关户日期 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ghrq")
	private Date ghrq;

	/** 限额类型 */
	@Column(name = "xelx",length = 1)
	private String xelx;

	/** 账户限额 */
	@Column(name = "zhxe", length = 20)
	private BigDecimal zhxe;

	/** 借贷标记 */
	@Column(name = "jdbj",length = 2)
	private String jdbj;

	/** 交易日期 */
	@Temporal(TemporalType.DATE)
	@Column(name = "jyrq")
	private Date jyrq;

	/** 生效日期 */
	@Temporal(TemporalType.DATE)
	@Column(name = "sxrq")
	private Date sxrq;

	/** 交易金额 */
	@Column(name = "jyje", length = 20)
	private BigDecimal jyje;

	/** 原始交易金额 */
	@Column(name = "ysjyje", length = 20)
	private BigDecimal ysjyje;

	/** 美金金额 */
	@Column(name = "mjje", length = 20)
	private BigDecimal mjje;

	/** 业务类型 */
	@Column(name = "ywlx",length = 6)
	private String ywlx;

	/** 交易编码 */
	@Column(name = "jybm",length = 16)
	private String jybm;

	/** 交易描述 */
	@Column(name = "jyms",length = 100)
	private String jyms;

	/** 交易对手银行机构代码 */
	@Column(name = "jydsyhdm",length = 64)
	private String jydsyhdm;

	/** 交易对手银行名称 */
	@Column(name = "jydsyhmc",length = 64)
	private String jydsyhmc;

	/** 交易对手银行国家代码 */
	@Column(name = "jydsyhgjdm",length = 9)
	private String jydsyhgjdm;

	/** 交易对手名称 */
	@Column(name = "jydsmc",length = 128)
	private String jydsmc;

	/** 交易对手国家代码 */
	@Column(name = "jydsgjdm",length = 3)
	private String jydsgjdm;

	/** 交易对手账号 */
	@Column(name = "jydszh",length = 15)
	private String jydszh;

	/** 交易对手类型 */
	@Column(name = "jydslx",length = 2)
	private String jydslx;

	/** 支付方式 */
	@Column(name = "zffs",length = 1)
	private String zffs;

	/** 本行扣费金额 */
	@Column(name = "bhkfje", length = 20)
	private BigDecimal bhkfje;

	/** 对手行扣费金额 */
	@Column(name = "dshkfje", length = 20)
	private BigDecimal dshkfje;

	/** 以结汇收到的款项或购汇付出的汇款 */
	@Column(name = "fxrt", length = 20)
	private BigDecimal fxrt;

	/** 人民币账号或外币账号（BOP时使用）换汇时需要 */
	@Column(name = "fxAcod",length = 64)
	private String fxAcod;

	@Column(name = "hzzj",length = 60)
	private String hzzj;

	/** 收付款方匹配号类型 */
	@Column(name = "sfkfpphlx",length = 500)
	private String sfkfpphlx;

	/** 收付款方匹配号 */
	@Column(name = "sfkfpph",length = 500)
	private String sfkfpph;

	/** 非柜台交易方式 */
	@Column(name = "fgtjyfs",length = 32)
	private String fgtjyfs;

	/** 非柜台交易方式的设备代码 */
	@Column(name = "fgtjyfssbdm",length = 500)
	private String fgtjyfssbdm;

	/** 银行与支付机构之间的业务交易编码 */
	@Column(name = "yhyzfjgzjdywbm",length = 500)
	private String yhyzfjgzjdywbm;

	/** 交易信息备注1 */
	@Column(name = "jyxxbz1",length = 64)
	private String jyxxbz1;

	/** 交易信息备注2 */
	@Column(name = "jyxxbz2",length = 64)
	private String jyxxbz2;

	/** 数据日期 */
	@Temporal(TemporalType.DATE)
	@Column(name = "dadt")
	private Date dadt;

	/** 机构编码 */
	private String jgbm;

	/** 金融机构网点与大额交易的关系 */
	private String jrjgwdydegx;

	/** 对方金融机构网点代码类型 */
	private String jydswdlx;

	/** 报告机构编码 */
	@Column(name = "bgjgbm",length = 14)
	private String bgjgbm;

	/** 网点代码 */
	@Column(name = "wddm",length = 16)
	private String wddm;

	/** 金融机构与客户关系 */
	@Column(name = "jrjgykhdgx",length = 2)
	private String jrjgykhdgx;

	/** 联系方式 */
	@Column(name = "lxfs",length = 512)
	private String lxfs;

	/** 客户开户日期 */
	@Temporal(TemporalType.DATE)
	@Column(name = "khkhrq")
	private Date khkhrq;

	/** 客户销户日期 */
	@Temporal(TemporalType.DATE)
	@Column(name = "khxhrq")
	private Date khxhrq;

	/** 银行卡类型 */
	@Column(name = "yhklx",length = 2)
	private String yhklx;

	/** 银行卡号码 */
	@Column(name = "yhkhm",length = 64)
	private String yhkhm;

	/** 代办人名称 */
	@Column(name = "dbrmc",length = 128)
	private String dbrmc;

	/** 代办人证件类型 */
	@Column(name = "dbrzjlx",length = 32)
	private String dbrzjlx;

	/** 代办人证件号码 */
	@Column(name = "dbrzjhm",length = 40)
	private String dbrzjhm;

	/** 代办人国籍 */
	@Column(name = "dbrgj",length = 3)
	private String dbrgj;

	/** 交易发生地 */
	@Column(name = "jyfsd",length = 9)
	private String jyfsd;

	/** 交易方式 */
	@Column(name = "jyfs",length = 6)
	private String jyfs;

	/** 资金来源及用途 */
	@Column(name = "zjyt",length = 128)
	private String zjyt;

	/** 交易对手证件类型 */
	@Column(name = "jydszjlx",length =6)
	private String jydszjlx;

	/** 交易对手证件号码 */
	@Column(name = "jydszjhm",length =128)
	private String jydszjhm;

	/** 交易对手账户类型 */
	@Column(name = "jydszhlx",length = 6)
	private String jydszhlx;

	/** 居民非居民 */
	@Column(name = "sfjm", length = 2)
	private String sfjm;

	/** 钞汇标识  1-现钞  2-现汇*/
	@Column(name = "chbz", length = 1)
	private String chbz;
	
	/** 联系电话*/
	@Column(name = "lxdh", length = 64)
	private String lxdh;

	
	
	public String getPozjhm() {
		return pozjhm;
	}

	public void setPozjhm(String pozjhm) {
		this.pozjhm = pozjhm;
	}

	public String getChbz() {
		return chbz;
	}

	public void setChbz(String chbz) {
		this.chbz = chbz;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	public String getSfjm() {
		return sfjm;
	}

	public void setSfjm(String sfjm) {
		this.sfjm = sfjm;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getZjlx() {
		return zjlx;
	}

	public String getJydswdlx() {
		return jydswdlx;
	}

	public void setJydswdlx(String jydswdlx) {
		this.jydswdlx = jydswdlx;
	}

	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}

	public String getKhbh() {
		return khbh;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}

	public String getZjhm() {
		return zjhm;
	}

	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
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

	public String getKhdqdm() {
		return khdqdm;
	}

	public void setKhdqdm(String khdqdm) {
		this.khdqdm = khdqdm;
	}

	public String getKhgj() {
		return khgj;
	}

	public void setKhgj(String khgj) {
		this.khgj = khgj;
	}

	public String getTxdz() {
		return txdz;
	}

	public void setTxdz(String txdz) {
		this.txdz = txdz;
	}

	public String getYzbm() {
		return yzbm;
	}

	public void setYzbm(String yzbm) {
		this.yzbm = yzbm;
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

	public Date getGzqsrq() {
		return gzqsrq;
	}

	public void setGzqsrq(Date gzqsrq) {
		this.gzqsrq = gzqsrq;
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

	public String getJylsh() {
		return jylsh;
	}

	public void setJylsh(String jylsh) {
		this.jylsh = jylsh;
	}

	public String getYwbh() {
		return ywbh;
	}

	public void setYwbh(String ywbh) {
		this.ywbh = ywbh;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getZh() {
		return zh;
	}

	public void setZh(String zh) {
		this.zh = zh;
	}

	public BigDecimal getKyye() {
		return kyye;
	}

	public void setKyye(BigDecimal kyye) {
		this.kyye = kyye;
	}

	public BigDecimal getKjye() {
		return kjye;
	}

	public void setKjye(BigDecimal kjye) {
		this.kjye = kjye;
	}

	public Date getZhjyrq() {
		return zhjyrq;
	}

	public void setZhjyrq(Date zhjyrq) {
		this.zhjyrq = zhjyrq;
	}

	public String getZhlx() {
		return zhlx;
	}

	public void setZhlx(String zhlx) {
		this.zhlx = zhlx;
	}

	public String getJgjgzhlx() {
		return jgjgzhlx;
	}

	public void setJgjgzhlx(String jgjgzhlx) {
		this.jgjgzhlx = jgjgzhlx;
	}

	public Date getKhrq() {
		return khrq;
	}

	public void setKhrq(Date khrq) {
		this.khrq = khrq;
	}

	public Date getGhrq() {
		return ghrq;
	}

	public void setGhrq(Date ghrq) {
		this.ghrq = ghrq;
	}

	public String getXelx() {
		return xelx;
	}

	public void setXelx(String xelx) {
		this.xelx = xelx;
	}

	public BigDecimal getZhxe() {
		return zhxe;
	}

	public void setZhxe(BigDecimal zhxe) {
		this.zhxe = zhxe;
	}

	public String getJdbj() {
		return jdbj;
	}

	public void setJdbj(String jdbj) {
		this.jdbj = jdbj;
	}

	public Date getJyrq() {
		return jyrq;
	}

	public void setJyrq(Date jyrq) {
		this.jyrq = jyrq;
	}

	public Date getSxrq() {
		return sxrq;
	}

	public void setSxrq(Date sxrq) {
		this.sxrq = sxrq;
	}

	public BigDecimal getJyje() {
		return jyje;
	}

	public void setJyje(BigDecimal jyje) {
		this.jyje = jyje;
	}

	public BigDecimal getYsjyje() {
		return ysjyje;
	}

	public void setYsjyje(BigDecimal ysjyje) {
		this.ysjyje = ysjyje;
	}

	public BigDecimal getMjje() {
		return mjje;
	}

	public void setMjje(BigDecimal mjje) {
		this.mjje = mjje;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getJybm() {
		return jybm;
	}

	public void setJybm(String jybm) {
		this.jybm = jybm;
	}

	public String getJyms() {
		return jyms;
	}

	public void setJyms(String jyms) {
		this.jyms = jyms;
	}

	public String getJydsyhdm() {
		return jydsyhdm;
	}

	public void setJydsyhdm(String jydsyhdm) {
		this.jydsyhdm = jydsyhdm;
	}

	public String getJydsyhmc() {
		return jydsyhmc;
	}

	public void setJydsyhmc(String jydsyhmc) {
		this.jydsyhmc = jydsyhmc;
	}

	public String getJydsyhgjdm() {
		return jydsyhgjdm;
	}

	public void setJydsyhgjdm(String jydsyhgjdm) {
		this.jydsyhgjdm = jydsyhgjdm;
	}

	public String getJydsmc() {
		return jydsmc;
	}

	public void setJydsmc(String jydsmc) {
		this.jydsmc = jydsmc;
	}

	public String getJydsgjdm() {
		return jydsgjdm;
	}

	public void setJydsgjdm(String jydsgjdm) {
		this.jydsgjdm = jydsgjdm;
	}

	public String getJydszh() {
		return jydszh;
	}

	public void setJydszh(String jydszh) {
		this.jydszh = jydszh;
	}

	public String getJydslx() {
		return jydslx;
	}

	public void setJydslx(String jydslx) {
		this.jydslx = jydslx;
	}

	public String getZffs() {
		return zffs;
	}

	public void setZffs(String zffs) {
		this.zffs = zffs;
	}

	public BigDecimal getBhkfje() {
		return bhkfje;
	}

	public void setBhkfje(BigDecimal bhkfje) {
		this.bhkfje = bhkfje;
	}

	public BigDecimal getDshkfje() {
		return dshkfje;
	}

	public void setDshkfje(BigDecimal dshkfje) {
		this.dshkfje = dshkfje;
	}

	public BigDecimal getFxrt() {
		return fxrt;
	}

	public void setFxrt(BigDecimal fxrt) {
		this.fxrt = fxrt;
	}

	public String getFxAcod() {
		return fxAcod;
	}

	public void setFxAcod(String fxAcod) {
		this.fxAcod = fxAcod;
	}

	public Date getDadt() {
		return dadt;
	}

	public void setDadt(Date dadt) {
		this.dadt = dadt;
	}

	public String getSfkfpphlx() {
		return sfkfpphlx;
	}

	public void setSfkfpphlx(String sfkfpphlx) {
		this.sfkfpphlx = sfkfpphlx;
	}

	public String getSfkfpph() {
		return sfkfpph;
	}

	public void setSfkfpph(String sfkfpph) {
		this.sfkfpph = sfkfpph;
	}

	public String getFgtjyfs() {
		return fgtjyfs;
	}

	public void setFgtjyfs(String fgtjyfs) {
		this.fgtjyfs = fgtjyfs;
	}

	public String getFgtjyfssbdm() {
		return fgtjyfssbdm;
	}

	public void setFgtjyfssbdm(String fgtjyfssbdm) {
		this.fgtjyfssbdm = fgtjyfssbdm;
	}

	public String getYhyzfjgzjdywbm() {
		return yhyzfjgzjdywbm;
	}

	public void setYhyzfjgzjdywbm(String yhyzfjgzjdywbm) {
		this.yhyzfjgzjdywbm = yhyzfjgzjdywbm;
	}

	public String getJyxxbz1() {
		return jyxxbz1;
	}

	public void setJyxxbz1(String jyxxbz1) {
		this.jyxxbz1 = jyxxbz1;
	}

	public String getJyxxbz2() {
		return jyxxbz2;
	}

	public void setJyxxbz2(String jyxxbz2) {
		this.jyxxbz2 = jyxxbz2;
	}

	public String getHzzj() {
		return hzzj;
	}

	public void setHzzj(String hzzj) {
		this.hzzj = hzzj;
	}

	public String getJgbm() {
		return jgbm;
	}

	public void setJgbm(String jgbm) {
		this.jgbm = jgbm;
	}

	public String getJrjgwdydegx() {
		return jrjgwdydegx;
	}

	public void setJrjgwdydegx(String jrjgwdydegx) {
		this.jrjgwdydegx = jrjgwdydegx;
	}

	public String getBgjgbm() {
		return bgjgbm;
	}

	public void setBgjgbm(String bgjgbm) {
		this.bgjgbm = bgjgbm;
	}

	public String getWddm() {
		return wddm;
	}

	public void setWddm(String wddm) {
		this.wddm = wddm;
	}

	public String getJrjgykhdgx() {
		return jrjgykhdgx;
	}

	public void setJrjgykhdgx(String jrjgykhdgx) {
		this.jrjgykhdgx = jrjgykhdgx;
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

	public String getYhklx() {
		return yhklx;
	}

	public void setYhklx(String yhklx) {
		this.yhklx = yhklx;
	}

	public String getYhkhm() {
		return yhkhm;
	}

	public void setYhkhm(String yhkhm) {
		this.yhkhm = yhkhm;
	}

	public String getDbrmc() {
		return dbrmc;
	}

	public void setDbrmc(String dbrmc) {
		this.dbrmc = dbrmc;
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

	public String getDbrgj() {
		return dbrgj;
	}

	public void setDbrgj(String dbrgj) {
		this.dbrgj = dbrgj;
	}

	public String getJyfsd() {
		return jyfsd;
	}

	public void setJyfsd(String jyfsd) {
		this.jyfsd = jyfsd;
	}

	public String getJyfs() {
		return jyfs;
	}

	public void setJyfs(String jyfs) {
		this.jyfs = jyfs;
	}

	public String getZjyt() {
		return zjyt;
	}

	public void setZjyt(String zjyt) {
		this.zjyt = zjyt;
	}

	public String getJydszjlx() {
		return jydszjlx;
	}

	public void setJydszjlx(String jydszjlx) {
		this.jydszjlx = jydszjlx;
	}

	public String getJydszjhm() {
		return jydszjhm;
	}

	public void setJydszjhm(String jydszjhm) {
		this.jydszjhm = jydszjhm;
	}

	public String getJydszhlx() {
		return jydszhlx;
	}

	public void setJydszhlx(String jydszhlx) {
		this.jydszhlx = jydszhlx;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelAmlInditrn other = (ModelAmlInditrn) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
