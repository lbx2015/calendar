package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.riking.core.annos.Comment;
import net.riking.core.entity.ILog;
import net.riking.core.entity.LogConfig;

@Comment("黑白名单")
@Entity
@Table(name = "T_BASE_KYC_BlackWhiteList")
public class BlackWhiteList implements ILog {
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -1982598544370949428L;

	@Id
	@Comment("id")
	@Column(name = "id")
	@GeneratedValue
	private Long id;

	/** 证件类型 */
	@Comment("证件类型 ")
	@Column(name="zjlx",length = 50)
	private String zjlx;

	/** 证件号码 */
	@Comment("证件号码")
	@Column(name="zjhm",length = 50)
	private String zjhm;

	/** 账号 */
	@Comment("账号")
	@Column(name="zh",length = 50)
	private String zh;

	/** 卡号/存折号 */
	@Comment("卡号/存折号")
	@Column(name="kczh",length = 50)
	private String kczh;

	/** 电话 */
	@Comment("电话")
	@Column(name="dh",length = 20)
	private String dh;

	/** 手机号 */
	@Comment("手机号")
	@Column(name="sjh",length = 20)
	private String sjh;

	/** IP地址 */
	@Comment("IP地址")
	@Column(name="ipdz",length = 20)
	private String ipdz;

	/** 地址 */
	@Comment("地址")
	@Column(name="dz",length = 150)
	private String dz;

	/** 国籍 */
	@Comment("国籍")
	@Column(name="gj",length = 10)
	private String gj;

	/** 中文名称 */
	@Comment("中文名称")
	@Column(name="zwmc",length = 100)
	private String zwmc;

	/** 中文名称拼音 */
	@Comment("中文名称拼音")
	@Column(name="zwmcpy",length = 100)
	private String zwmcpy;

	/** 中文名称繁体 */
	@Comment("中文名称繁体")
	@Column(name="zwmcft",length = 100)
	private String zwmcft;

	/** 中文姓 */
	@Comment("中文姓")
	@Column(name="zwx",length = 50)
	private String zwx;

	/** 中文名 */
	@Comment("中文名")
	@Column(name="zwm",length = 50)
	private String zwm;

	/** 中文姓拼音 */
	@Comment("中文姓拼音")
	@Column(name="zwxpy",length = 50)
	private String zwxpy;

	/** 中文名拼音 */
	@Comment("中文名拼音")
	@Column(name="zwmp",length = 50)
	private String zwmp;

	/** 中文姓繁体 */
	@Comment("中文姓繁体")
	@Column(name="zwxf",length = 50)
	private String zwxf;

	/** 中文名繁体 */
	@Comment("中文名繁体")
	@Column(name="zwmf",length = 50)
	private String zwmf;

	/** 英文名称 */
	@Comment("英文名称")
	@Column(name="ywmc",length = 50)
	private String ywmc;

	/** 英文姓 */
	@Comment("英文姓")
	@Column(name="ywx",length = 50)
	private String ywx;

	/** 英文名 */
	@Comment("英文名")
	@Column(name="ywm",length = 50)
	private String ywm;

	/** 名单类型 */
	@Comment("名单类型")
	@Column(name="hmdlx",length = 10)
	private String hmdlx;

	/** 名单来源 */
	@Comment("名单来源")
	@Column(name="mdly",length = 10)
	private String mdly;

	/** 状态 */
	@Comment("状态 ")
	@Column(name="zt",length = 1)
	private String zt;

	/** 更新时间 */
	@Comment("更新时间")
	@Temporal(TemporalType.DATE)
	@Column(name="gxsj")
	private Date gxsj;

	/** 创建人 */
	@Comment("创建人")
	@Column(name="cjr",length = 20)
	private String cjr;

	/** 修改人 */
	@Comment("修改人")
	@Column(name="xgr",length = 20)
	private String xgr;

	/** 审核人 */
	@Comment("审核人")
	@Column(name="shr",length = 20)
	private String shr;

	/** 创建时间 */
	@Comment("创建时间")
	@Temporal(TemporalType.DATE)
	@Column(name="cjsj")
	private Date cjsj;

	/** 修改时间 */
	@Comment("修改时间")
	@Temporal(TemporalType.DATE)
	@Column(name="xgsj")
	private Date xgsj;

	/** 审核时间 */
	@Comment("审核时间")
	@Temporal(TemporalType.DATE)
	@Column(name="shsj")
	private Date shsj;

	/** 事件描述/罪名 */
	@Comment("事件描述/罪名")
	@Column(name="sjmx",length = 500)
	private String sjmx;

	/** 学历 */
	@Comment("学历")
	@Column(name="xl",length = 100)
	private String xl;

	/** 立案单位 */
	@Comment("立案单位")
	@Column(name="ladw",length = 100)
	private String ladw;

	/** 涉案号/通缉号 */
	@Comment("涉案号/通缉号")
	@Column(name="sah",length = 100)
	private String sah;

	/** 原工作单位/公司 */
	@Comment("原工作单位/公司")
	@Column(name="ygzdw",length = 100)
	private String ygzdw;
	
	/** 性别 */
	@Comment("立案单位")
	@Column(name="xb",length = 2)
	private String xb;

	/** 外逃所持证照信息 */
	@Comment("立案单位")
	@Column(name="wtzjxx",length = 2)
	private String wtzjxx;
	
	/** 别名、曾用名、护照名 */
	@Comment("立案单位")
	@Column(name="bm",length = 2)
	private String bm;
	
	/** 是否回溯 1.未回溯 2.已回溯 */
	@Comment("是否回溯 1.未回溯 2.已回溯")
	@Column(name="ishs",length = 1)
	private Byte ishs;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getZjlx() {
		return zjlx;
	}

	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}

	public String getZjhm() {
		return zjhm;
	}

	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
	}

	public String getZh() {
		return zh;
	}

	public void setZh(String zh) {
		this.zh = zh;
	}

	public String getKczh() {
		return kczh;
	}

	public void setKczh(String kczh) {
		this.kczh = kczh;
	}

	public String getDh() {
		return dh;
	}

	public void setDh(String dh) {
		this.dh = dh;
	}

	public String getSjh() {
		return sjh;
	}

	public void setSjh(String sjh) {
		this.sjh = sjh;
	}

	public String getIpdz() {
		return ipdz;
	}

	public void setIpdz(String ipdz) {
		this.ipdz = ipdz;
	}

	public String getDz() {
		return dz;
	}

	public void setDz(String dz) {
		this.dz = dz;
	}

	public String getGj() {
		return gj;
	}

	public void setGj(String gj) {
		this.gj = gj;
	}

	public String getZwmc() {
		return zwmc;
	}

	public void setZwmc(String zwmc) {
		this.zwmc = zwmc;
	}

	public String getZwmcpy() {
		return zwmcpy;
	}

	public void setZwmcpy(String zwmcpy) {
		this.zwmcpy = zwmcpy;
	}

	public String getZwmcft() {
		return zwmcft;
	}

	public void setZwmcft(String zwmcft) {
		this.zwmcft = zwmcft;
	}

	public String getZwx() {
		return zwx;
	}

	public void setZwx(String zwx) {
		this.zwx = zwx;
	}

	public String getZwm() {
		return zwm;
	}

	public void setZwm(String zwm) {
		this.zwm = zwm;
	}

	public String getZwxpy() {
		return zwxpy;
	}

	public void setZwxpy(String zwxpy) {
		this.zwxpy = zwxpy;
	}

	public String getZwmp() {
		return zwmp;
	}

	public void setZwmp(String zwmp) {
		this.zwmp = zwmp;
	}

	public String getZwxf() {
		return zwxf;
	}

	public void setZwxf(String zwxf) {
		this.zwxf = zwxf;
	}

	public String getZwmf() {
		return zwmf;
	}

	public void setZwmf(String zwmf) {
		this.zwmf = zwmf;
	}

	public String getYwmc() {
		return ywmc;
	}

	public void setYwmc(String ywmc) {
		this.ywmc = ywmc;
	}

	public String getYwx() {
		return ywx;
	}

	public void setYwx(String ywx) {
		this.ywx = ywx;
	}

	public String getYwm() {
		return ywm;
	}

	public void setYwm(String ywm) {
		this.ywm = ywm;
	}

	public String getHmdlx() {
		return hmdlx;
	}

	public void setHmdlx(String hmdlx) {
		this.hmdlx = hmdlx;
	}

	public String getMdly() {
		return mdly;
	}

	public void setMdly(String mdly) {
		this.mdly = mdly;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public Date getGxsj() {
		return gxsj;
	}

	public void setGxsj(Date gxsj) {
		this.gxsj = gxsj;
	}

	public String getCjr() {
		return cjr;
	}

	public void setCjr(String cjr) {
		this.cjr = cjr;
	}

	public String getXgr() {
		return xgr;
	}

	public void setXgr(String xgr) {
		this.xgr = xgr;
	}

	public String getShr() {
		return shr;
	}

	public void setShr(String shr) {
		this.shr = shr;
	}

	public Date getCjsj() {
		return cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	public Date getXgsj() {
		return xgsj;
	}

	public void setXgsj(Date xgsj) {
		this.xgsj = xgsj;
	}

	public Date getShsj() {
		return shsj;
	}

	public void setShsj(Date shsj) {
		this.shsj = shsj;
	}

	public String getSjmx() {
		return sjmx;
	}

	public void setSjmx(String sjmx) {
		this.sjmx = sjmx;
	}

	public String getXl() {
		return xl;
	}

	public void setXl(String xl) {
		this.xl = xl;
	}

	public String getLadw() {
		return ladw;
	}

	public void setLadw(String ladw) {
		this.ladw = ladw;
	}

	public String getSah() {
		return sah;
	}

	public void setSah(String sah) {
		this.sah = sah;
	}

	public String getYgzdw() {
		return ygzdw;
	}

	public void setYgzdw(String ygzdw) {
		this.ygzdw = ygzdw;
	}

	public Byte getIshs() {
		return ishs;
	}

	public void setIshs(Byte ishs) {
		this.ishs = ishs;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getWtzjxx() {
		return wtzjxx;
	}

	public void setWtzjxx(String wtzjxx) {
		this.wtzjxx = wtzjxx;
	}

	public String getBm() {
		return bm;
	}

	public void setBm(String bm) {
		this.bm = bm;
	}

	@Override
	public LogConfig getLogConfig() {
		return new LogConfig("id"); //
	}
}
