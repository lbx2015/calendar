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

@Entity
@Table(name = "T_BASE_TRN")
public class BaseTrn extends BaseFlowJob implements ILog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;// 瀹㈡埛缂栧彿

	/** 交易流水号*/
	@Comment("交易流水号")
	@Column(name = "jylsh",length = 256)
	private String jylsh;
	
	/** 分行代码*/
	@Comment("分行代码")
	@Column(name = "fhdm",length = 50)
	private String fhdm;
	
	/** 客户编号*/
	@Comment("客户编号")
	@Column(name = "khbh",length = 50)
	private String khbh;
	
	/** 业务编号*/
	@Comment("业务编号")
	@Column(name = "ywbh",length = 256)
	private String ywbh;
	
	/** 币种*/
	@Comment("币种")
	@Column(name = "bz",length = 3)
	private String bz;
	
	/** 账号*/
	@Comment("账号")
	@Column(name = "zh",length = 64)
	private String zh;
	
	/** 账户分类*/
	@Comment("账户分类")
	@Column(name = "zhfl",length = 16)
	private String zhfl;
	
	/** 借贷标记*/
	@Comment("借贷标记")
	@Column(name = "jdbj",length = 2)
	private String jdbj;
	
	/** 交易日期*/
	@Comment("交易日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "jyrq")
	private Date jyrq;
	
	/** 生效日期*/
	@Comment("生效日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "sxrq")
	private Date sxrq;
	
	/** 交易金额*/
	@Comment("交易金额")
	@Column(name = "jyje",length=22)
	private BigDecimal jyje;
	
	/** 原始交易金额（未扣费）*/
	@Comment("原始交易金额（未扣费）")
	@Column(name = "ysjyje",length=20)
	private BigDecimal ysjyje;
	
	/** 美金金额）*/
	@Comment("美金金额")
	@Column(name = "mjje",length=22)
	private BigDecimal mjje;
	
	/** 产品类型*/
	@Comment("产品类型")
	@Column(name = "cplx",length = 15)
	private String cplx;
	
	/** 交易编码*/
	@Comment("交易编码")
	@Column(name = "jybm",length = 6)
	private String jybm;
	
	/** 交易描述*/
	@Comment("交易描述")
	@Column(name = "jyms",length = 100)
	private String jyms;
	
	/** 交易对手银行机构代码*/
	@Comment("交易对手银行机构代码")
	@Column(name = "jydsyhjgdm",length = 64)
	private String jydsyhjgdm;
	
	/** 交易对手银行名称*/
	@Comment("交易对手银行名称")
	@Column(name = "jydsyhmc",length = 64)
	private String jydsyhmc;
	
	/** 交易对手银行国家代码*/
	@Comment("交易对手银行国家代码")
	@Column(name = "jydsyhgjdm",length = 9)
	private String jydsyhgjdm;
	
	/** 交易对手名称*/
	@Comment("交易对手名称")
	@Column(name = "jydsmc",length = 35)
	private String jydsmc;
	
	/** 交易对手国家代码*/
	@Comment("交易对手国家代码")
	@Column(name = "jydsgjdm",length = 3)
	private String jydsgjdm;
	
	/** 交易对手账号*/
	@Comment("交易对手账号")
	@Column(name = "jydszh",length = 15)
	private String jydszh;
	
	/** 交易对手类型*/
	@Comment("交易对手类型")
	@Column(name = "jydslx",length = 2)
	private String jydslx;
	
	/** 支付方式*/
	@Comment("支付方式")
	@Column(name = "zffs",length = 1)
	private String zffs;
	
	/** 本行扣费金额*/
	@Comment("本行扣费金额")
	@Column(name = "bhkfje",length=20)
	private BigDecimal bhkfje;
	
	/** 对手行扣费金额*/
	@Comment("对手行扣费金额")
	@Column(name = "dshkfje",length=20)
	private BigDecimal dshkfje;
	
	/** 换汇或购汇或结汇汇率*/
	@Comment("换汇或购汇或结汇汇率")
	@Column(name = "fxrt",length=20)
	private BigDecimal fxrt;
	
	/** 人民币账号或外币账号（BOP时使用）*/
	@Comment("人民币账号或外币账号（BOP时使用）")
	@Column(name = "fxacod",length = 64)
	private String fxacod;
	
	/** 原始币种*/
	@Comment("原始币种")
	@Column(name = "ysbz",length = 8)
	private String ysbz;
	
	/** 原币金额*/
	@Comment("原始金额")
	@Column(name = "ybje",length=20)
	private BigDecimal ybje;
	
	/** 购汇或结汇金额*/
	@Comment("购汇或结汇金额")
	@Column(name = "ghjhje",length=22)
	private BigDecimal ghjhje;
	
	/** 现汇金额*/
	@Comment("现汇金额")
	@Column(name = "xhje",length=22)
	private BigDecimal xhje;
	
	/** 其他账号*/
	@Comment("其他账号")
	@Column(name = "qtzh",length = 20)
	private String qtzh;
	
	/** 其他金额*/
	@Comment("其他金额")
	@Column(name = "qyje",length=22)
	private BigDecimal qyje;
	
	/** BOP标识*/
	@Comment("BOP标识")
	@Column(name = "bopbj",length = 5)
	private String bopbj;
	
	/**反洗钱标识*/
	@Comment("反洗钱标识")
	@Column(name = "amlbj",length = 2)
	private String amlbj;
	
	/**JWJN标识*/
	@Comment("JWJN标识")
	@Column(name = "jwjnbj",length = 2)
	private String jwjnbj;
	
	/** 收付款方匹配号类型 */
	@Comment("收款方匹配号类型")
	@Column(name = "sfkfpphlx",length=500)
	private String sfkfpphlx;
	
	/** 收付款方匹配号 */
	@Comment("收付款方匹配号")
	@Column(name = "sfkfpph",length=500)
	private String sfkfpph;
	
	/** 交易方式 */
	@Comment("交易方式")
	@Column(name = "jyfs",length=6)
	private String jyfs;
	
	/** 资金用途 */
	@Comment("资金用途")
	@Column(name = "zjyt",length=128)
	private String zjyt;
	
	/** 非柜台交易方式*/
	@Comment("非柜台交易方式")
	@Column(name = "fgtjyfs",length=32)
	private String fgtjyfs;
	
	/** 非柜台交易方式的设备代码 */
	@Comment("非柜台交易方式得设备代码")
	@Column(name = "fgtjyfssbdm",length=500)
	private String fgtjyfssbdm;
	
	/** 银行与支付机构之间的业务交易编码 */
	@Comment("银行与支付机构之间的业务交易编码")
	@Column(name = "yhyzfjgzjdywbm",length=500)
	private String yhyzfjgzjdywbm;
	
	/** 交易信息备注1 */
	@Comment("交易信息备注1")
	@Column(name = "jyxxbz1",length=64)
	private String jyxxbz1;
	
	/** 交易信息备注2 */
	@Comment("交易信息备注2")
	@Column(name = "jyxxbz2",length=64)
	private String jyxxbz2;
	
	/** 金融机构与客户的关系 */
	@Comment("金融机构与客户的关系")
	@Column(name = "jrjgykhdgx",length=2)
	private String jrjgykhdgx;
	
	/** 交易发生地 */
	@Comment("交易发生地")
	@Column(name = "jyfsd",length=9)
	private String jyfsd;
	
	/** 代办人证件类型*/
	@Comment("代办人证件类型")
	@Column(name = "dbrzjlx",length=32)
	private String dbrzjlx;
	
	/** 代办人证件号码*/
	@Comment("代办人证件号码")
	@Column(name = "dbrzjhm",length=40)
	private String dbrzjhm;
	
	/** 代办人名称*/
	@Comment("代办人名称")
	@Column(name = "dbrmc",length=128)
	private String dbrmc;
	
	/** 代办人国家*/
	@Comment("代办人国家")
	@Column(name = "dbrgj",length=3)
	private String dbrgj;
	
	/** 对方金融机构网点代码类型 */
	@Comment("对方金融机构网点代码类型")
	@Column(name = "jydswdlx",length=2)
	private String jydswdlx;
	
	/** 交易对手身份证件/证明文件类型 */
	@Comment("交易对手身份证件/证明文件类型")
	@Column(name = "jydszjlx",length=6)
	private String jydszjlx;
	
	/** 交易对手身份证件/证明文件号码 */
	@Comment("交易对手身份证件/证明文件号码")
	@Column(name = "jydszjhm",length=128)
	private String jydszjhm;
	
	/** 交易对手账户类型 */
	@Comment("交易对手账户类型")
	@Column(name = "jydszhlx",length=6)
	private String jydszhlx;
	
	/**数据日期*/
	@Comment("数据日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "sjrq")
	private Date sjrq;
	
	/** 有效标志 0 不可用    1 可用*/
	@Comment("有效标志")
	@Column(name = "enabled",length = 2)
	private Integer enabled;
	
	/** 审核状态*/
	@Comment("审核状态")
	@Column(name = "confirmStatus",length = 8)
	private String confirmStatus;
	
	/** 刪除状态 0 刪除， 1 未刪除*/
	@Comment("删除状态")
	@Column(name = "delState",length = 2)
	private String delState;
	
	/**查询开始时间*/
	@Comment("查询开始时间")
	@Transient
	private Date starts;
	
	/**查询结束时间*/
	@Comment("查询结束时间")
	@Transient
	private Date ends;
	
	/**机构编码*/
	@Comment("机构编码")
	@Column(name = "jgbm",length= 32)
	private String jgbm;
	
	/** 最后修改时间*/
	@Column(name = "SYNC_LAST_TIME")
	private Long syncLastTime;
	

	public Long getSyncLastTime() {
		return syncLastTime;
	}

	public void setSyncLastTime(Long syncLastTime) {
		this.syncLastTime = syncLastTime;
	}

	public Date getStarts() {
		return starts;
	}

	public void setStarts(Date starts) {
		this.starts = starts;
	}

	public Date getEnds() {
		return ends;
	}

	public void setEnds(Date ends) {
		this.ends = ends;
	}

	public String getDelState() {
		return delState;
	}

	public void setDelState(String delState) {
		this.delState = delState;
	}

	public Long getId() {
		return id;
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

	public String getJrjgykhdgx() {
		return jrjgykhdgx;
	}

	public void setJrjgykhdgx(String jrjgykhdgx) {
		this.jrjgykhdgx = jrjgykhdgx;
	}

	public String getJyfsd() {
		return jyfsd;
	}

	public void setJyfsd(String jyfsd) {
		this.jyfsd = jyfsd;
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

	public void setId(Long id) {
		this.id = id;
	}

	public String getJylsh() {
		return jylsh;
	}

	public void setJylsh(String jylsh) {
		this.jylsh = jylsh;
	}

	public String getFhdm() {
		return fhdm;
	}

	public void setFhdm(String fhdm) {
		this.fhdm = fhdm;
	}

	public String getKhbh() {
		return khbh;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
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

	
	public String getJydswdlx() {
		return jydswdlx;
	}

	public void setJydswdlx(String jydswdlx) {
		this.jydswdlx = jydswdlx;
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

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getZh() {
		return zh;
	}

	public void setZh(String zh) {
		this.zh = zh;
	}

	public String getZhfl() {
		return zhfl;
	}

	public void setZhfl(String zhfl) {
		this.zhfl = zhfl;
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

	public String getCplx() {
		return cplx;
	}

	public void setCplx(String cplx) {
		this.cplx = cplx;
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

	public String getJydsyhjgdm() {
		return jydsyhjgdm;
	}

	public void setJydsyhjgdm(String jydsyhjgdm) {
		this.jydsyhjgdm = jydsyhjgdm;
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

	public String getFxacod() {
		return fxacod;
	}

	public void setFxacod(String fxacod) {
		this.fxacod = fxacod;
	}

	public String getYsbz() {
		return ysbz;
	}

	public void setYsbz(String ysbz) {
		this.ysbz = ysbz;
	}

	public BigDecimal getYbje() {
		return ybje;
	}

	public void setYbje(BigDecimal ybje) {
		this.ybje = ybje;
	}

	public BigDecimal getGhjhje() {
		return ghjhje;
	}

	public void setGhjhje(BigDecimal ghjhje) {
		this.ghjhje = ghjhje;
	}

	public BigDecimal getXhje() {
		return xhje;
	}

	public void setXhje(BigDecimal xhje) {
		this.xhje = xhje;
	}

	public String getQtzh() {
		return qtzh;
	}

	public void setQtzh(String qtzh) {
		this.qtzh = qtzh;
	}

	public BigDecimal getQyje() {
		return qyje;
	}

	public void setQyje(BigDecimal qyje) {
		this.qyje = qyje;
	}

	public String getBopbj() {
		return bopbj;
	}

	public void setBopbj(String bopbj) {
		this.bopbj = bopbj;
	}

	public String getAmlbj() {
		return amlbj;
	}

	public void setAmlbj(String amlbj) {
		this.amlbj = amlbj;
	}

	public String getJwjnbj() {
		return jwjnbj;
	}

	public void setJwjnbj(String jwjnbj) {
		this.jwjnbj = jwjnbj;
	}

	public Date getSjrq() {
		return sjrq;
	}

	public void setSjrq(Date sjrq) {
		this.sjrq = sjrq;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}
	
	public String getJgbm() {
		return jgbm;
	}

	public void setJgbm(String jgbm) {
		this.jgbm = jgbm;
	}

	@Override
	public String toString() {
		return "BaseTrn [id=" + id + ", jylsh=" + jylsh + ", fhdm=" + fhdm + ", khbh=" + khbh + ", ywbh=" + ywbh
				+ ", bz=" + bz + ", zh=" + zh + ", zhfl=" + zhfl + ", jdbj=" + jdbj + ", jyrq=" + jyrq + ", sxrq="
				+ sxrq + ", jyje=" + jyje + ", ysjyje=" + ysjyje + ", mjje=" + mjje + ", cplx=" + cplx + ", jybm="
				+ jybm + ", jyms=" + jyms + ", jydsyhjgdm=" + jydsyhjgdm + ", jydsyhmc=" + jydsyhmc + ", jydsyhgjdm="
				+ jydsyhgjdm + ", jydsmc=" + jydsmc + ", jydsgjdm=" + jydsgjdm + ", jydszh=" + jydszh + ", jydslx="
				+ jydslx + ", zffs=" + zffs + ", bhkfje=" + bhkfje + ", dshkfje=" + dshkfje + ", fxrt=" + fxrt
				+ ", fxacod=" + fxacod + ", ysbz=" + ysbz + ", ybje=" + ybje + ", ghjhje=" + ghjhje + ", xhje=" + xhje
				+ ", qtzh=" + qtzh + ", qyje=" + qyje + ", bopbj=" + bopbj + ", amlbj=" + amlbj + ", jwjnbj=" + jwjnbj
				+ ", sjrq=" + sjrq + ", enabled=" + enabled + ", confirmStatus=" + confirmStatus + "]";
	}

	@Override
	public LogConfig getLogConfig() {
		return new LogConfig("id"); //;
	}

	
}
