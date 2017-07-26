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
@Table(name = "T_BASE_AIF")
public class BaseAif extends BaseFlowJob implements ILog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;// 瀹㈡埛缂栧彿

	/** 客户号 */
	@Comment("客户号")
	@Column(name = "khbh", length = 50)
	private String khbh;

	/** 币种 */
	@Comment("币种")
	@Column(name = "bz", length = 3)
	private String bz;

	/** 账号 */
	@Comment("账号")
	@Column(name = "zh", length = 16)
	private String zh;

	/** 分行代码 */
	@Comment("分行代码")
	@Column(name = "fhdm", length = 50)
	private String fhdm;

	/** 利率 */
	@Comment("利率")
	@Column(name = "ll", length = 20)
	private BigDecimal ll;

	/** 可用余额 */
	@Comment("可用余额")
	@Column(name = "kyye", length = 20)
	private BigDecimal kyye;

	/** 帐户会计余额 */
	@Comment("账户会计余额")
	@Column(name = "kjye", length = 20)
	private BigDecimal kjye;

	/** 最后交易日期 */
	@Comment("最后交易日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "zhjyrq", length = 8)
	private Date zhjyrq;

	/** 内部账户类型 */
	@Comment("内部账户类型")
	@Column(name = "nbzhlx", length = 8)
	private String nbzhlx;

	/** 监管账户类型 */
	@Comment("监管账户类型")
	@Column(name = "zhlx", length = 6)
	private String zhlx;

	/** 开户日期 */
	@Comment("开户日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "khrq", length = 8)
	private Date khrq;

	/** 销户日期 */
	@Comment("销户日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "ghrq", length = 8)
	private Date ghrq;

	/** 限额类型 */
	@Comment("限额类型")
	@Column(name = "xelx", length = 1)
	private String xelx;

	/** 限额金额 */
	@Comment("限额金额")
	@Column(name = "xeje", length = 20)
	private BigDecimal xeje;

	/** 核准件编号 */
	@Comment("核准件编号")
	@Column(name = "hzjbh", length = 16)
	private String hzjbh;

	/** 数据日期 */
	@Comment("数据日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "sjrq", length = 8)
	private Date sjrq;

	/** 有效标志 */
	@Comment("有效标志")
	@Column(name = "enabled", length = 1)
	private String enabled;

	/** 银行卡类型 */
	@Comment("银行卡类型")
	@Column(name = "yhklx", length = 2)
	private String yhklx;

	/** 银行卡号码 */
	@Comment("银行卡号码")
	@Column(name = "yhkhm", length = 64)
	private String yhkhm;

	/** 审核状态 */
	@Comment("审核状态")
	@Column(name = "confirmStatus",length = 8)
	private String confirmStatus;

	/** 查询开始时间 */
	@Comment("查询开始时间")
	@Transient
	private Date starts;

	/** 查询结束时间 */
	@Comment("查询结束时间")
	@Transient
	private Date ends;

	/** 机构编码 */
	@Comment("机构编码")
	@Column(name = "jgbm", length = 32)
	private String jgbm;

	@Column(name = "old_Id")
	private Long oldId;
	
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

	public Long getOldId() {
		return oldId;
	}

	public void setOldId(Long oldId) {
		this.oldId = oldId;
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

	public String getFhdm() {
		return fhdm;
	}

	public void setFhdm(String fhdm) {
		this.fhdm = fhdm;
	}

	public BigDecimal getLl() {
		return ll;
	}

	public void setLl(BigDecimal ll) {
		this.ll = ll;
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

	public String getNbzhlx() {
		return nbzhlx;
	}

	public void setNbzhlx(String nbzhlx) {
		this.nbzhlx = nbzhlx;
	}

	public String getZhlx() {
		return zhlx;
	}

	public void setZhlx(String zhlx) {
		this.zhlx = zhlx;
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

	public BigDecimal getXeje() {
		return xeje;
	}

	public void setXeje(BigDecimal xeje) {
		this.xeje = xeje;
	}

	public String getHzjbh() {
		return hzjbh;
	}

	public void setHzjbh(String hzjbh) {
		this.hzjbh = hzjbh;
	}

	public Date getSjrq() {
		return sjrq;
	}

	public void setSjrq(Date sjrq) {
		this.sjrq = sjrq;
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

	@Override
	public LogConfig getLogConfig() {
		return new LogConfig("id"); // ;
	}

	public String getJgbm() {
		return jgbm;
	}

	public void setJgbm(String jgbm) {
		this.jgbm = jgbm;
	}

}
