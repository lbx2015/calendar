package net.riking.entity.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.riking.core.annos.Comment;
import net.riking.core.entity.ILog;
import net.riking.core.entity.LogConfig;
import net.riking.core.entity.model.BaseFlowJob;

@Comment("对私客户")
@Entity
@Table(name = "T_BASE_INDV_CUST")
public class BaseIndvCust extends BaseFlowJob implements ILog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;// 客户编号

	/** 客户编号 */
	@Comment("客户编号")
	@Column(name = "khbh",length = 32)
	private String khbh;// @Column(name = "fxdjfs")
	
	/** 客户风险等级分数*/
	@Comment("客户风险等级分数")
	@Column(name = "fxdjfs")
	private BigDecimal fxdjfs;
	
	/** 客户风险等级*/
	@Comment("客户风险等级")
	@Column(name = "khfxdj",length = 1)
	private String khfxdj;
	
	/** 证件类型*/
	@Comment("证件类型")
	@Column(name = "zjlx",length = 6)
	private String zjlx;
	
	/** 评级时间*/
	@Comment("评级时间")
	@Column(name = "riskTime")
	private String riskTime;//
	
	/** 证件号码*/
	@Comment("评级号码")
	@Column(name = "zjhm",length = 128)
	private String zjhm;
	
	/** 客户英文名称*/
	@Comment("客户英文名称")
	@Column(name = "khywmc",length = 512)
	private String khywmc;
	
	/** 客户中文名称*/
	@Comment("客户中文名称")
	@Column(name = "khzwmc",length = 512)
	private String khzwmc;
	
	/** 分行代码*/
	@Comment("分行代码")
	@Column(name = "fhdm",length = 50)
	private String fhdm;
	
	/** 客户地区代码*/
	@Comment("客户地区代码")
	@Column(name = "khdqdm",length = 8)
	private String khdqdm;
	
	/** 客户国籍*/
	@Comment("客户国籍")
	@Column(name = "khgj",length = 3)
	private String khgj;
	
	/** 通讯地址*/
	@Comment("通讯地址")
	@Column(name = "txdz",length = 512)
	private String txdz;
	
	/** 客户联系方式*/
	@Comment("客户联系方式")
	@Column(name = "lxfs",length= 500)
	private String lxfs;
	
	/**客户开户日期 */
	@Comment("客户开户日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "khkhrq",length = 14)
	private Date khkhrq;
	
	/** 客户销户日期*/
	@Comment("客户销户日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "khxhrq",length = 14)
	private Date khxhrq;
	
	/** 邮政编码*/
	@Comment("邮政编码")
	@Column(name = "yzbm",length = 10)
	private String yzbm;
	
	/** 数据日期*/
	@Comment("数据日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "sjrq")
	private Date sjrq;
	
	/** 导入日期*/
	@Comment("导入日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "drrq")
	private Date drrq;
	
	/**机构编码*/
	@Comment("机构编码")
	@Column(name = "jgbm",length= 32)
	private String jgbm;
	
	@OneToOne(fetch = FetchType.EAGER)
    private BaseIndvCustAdd baseIndvCustAdd;
	
	/** 有效标志*/
	@Comment("有效标志")
	@Column(name = "enabled",length = 1)
	private String enabled;
	
	/** 审核状态*/
	@Comment("审核状态")
	@Column(name = "confirmStatus",length = 8)
	private String confirmStatus;
	
	@Column(name = "old_Id")
	private Long oldId;
	
	/** 联系电话*/
	@Column(name = "lxdh",length=64)
	private String lxdh;
	
	/** 最后修改时间*/
	@Column(name = "SYNC_LAST_TIME")
	private Long syncLastTime;
	/** 刪除状态 0 刪除， 1 未刪除*/
	
	@Transient
	private String yddhOrZzdh;
	@Transient
	private String zy;
	@Transient
	private String zjlxsm;
	
	
	
	
	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public Long getOldId() {
		return oldId;
	}

	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public BigDecimal getFxdjfs() {
		return fxdjfs;
	}

	public void setFxdjfs(BigDecimal fxdjfs) {
		this.fxdjfs = fxdjfs;
	}

	public String getKhfxdj() {
		return khfxdj;
	}

	public void setKhfxdj(String khfxdj) {
		this.khfxdj = khfxdj;
	}

	public String getRiskTime() {
		return riskTime;
	}

	public void setRiskTime(String riskTime) {
		this.riskTime = riskTime;
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

	public String getKhbh() {
		return khbh;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
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

	public BaseIndvCustAdd getBaseIndvCustAdd() {
		return baseIndvCustAdd;
	}

	public void setBaseIndvCustAdd(BaseIndvCustAdd baseIndvCustAdd) {
		this.baseIndvCustAdd = baseIndvCustAdd;
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

	public String getJgbm() {
		return jgbm;
	}

	public void setJgbm(String jgbm) {
		this.jgbm = jgbm;
	}

	public Long getSyncLastTime() {
		return syncLastTime;
	}

	public void setSyncLastTime(Long syncLastTime) {
		this.syncLastTime = syncLastTime;
	}
	
	

	public String getYddhOrZzdh() {
		return yddhOrZzdh;
	}

	public void setYddhOrZzdh(String yddhOrZzdh) {
		this.yddhOrZzdh = yddhOrZzdh;
	}

	public String getZy() {
		return zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
	}

	public String getZjlxsm() {
		return zjlxsm;
	}

	public void setZjlxsm(String zjlxsm) {
		this.zjlxsm = zjlxsm;
	}

	@Override
	public LogConfig getLogConfig() {
		return new LogConfig("id"); //;
	}
}
