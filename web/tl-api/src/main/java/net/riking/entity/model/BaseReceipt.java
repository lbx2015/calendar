package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "T_BASE_RECEIPT")
public class BaseReceipt {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;

	/** 回执对应报文名称 */
	@Column(name = "bwmc", length = 40)
	private String bwmc;

	/** 原客户号 */
	@Column(name = "old_csnm", length = 40)
	private String oldcsnm;

	/** 原交易时间 */
	@Temporal(TemporalType.DATE)
	@Column(name = "old_tstm")
	private Date oldtstm;

	/** 原特征代码 */
	@Column(name = "old_crcd", length = 40)
	private String oldcrcd;

	/** 原业务标识号 */
	@Column(name = "old_ticd", length = 40)
	private String oldticd;

	/** 回执内容 */
	@Column(name = "xgnr", length = 4000)
	private String xgnr;

	/** 回执类型 */
	@Column(name = "hzlx", length = 40)
	private String hzlx;

	/** 回执名称 */
	@Column(name = "hzmc", length = 80)
	private String hzmc;

	/** 回执状态 */
	@Column(name = "hzState", length = 40)
	private String hzState;

	/** 回执导入时间 */
	@Column(name = "hzsj")
	private Date hzsj;

	/** 原数据ID */
	@Column(name = "old_Id")
	private Long oldId;

	public Long getOldId() {
		return oldId;
	}

	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}

	public String getHzmc() {
		return hzmc;
	}

	public void setHzmc(String hzmc) {
		this.hzmc = hzmc;
	}

	public String getHzState() {
		return hzState;
	}

	public void setHzState(String hzState) {
		this.hzState = hzState;
	}

	public Date getHzsj() {
		return hzsj;
	}

	public void setHzsj(Date hzsj) {
		this.hzsj = hzsj;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBwmc() {
		return bwmc;
	}

	public void setBwmc(String bwmc) {
		this.bwmc = bwmc;
	}

	public String getOldcsnm() {
		return oldcsnm;
	}

	public void setOldcsnm(String oldcsnm) {
		this.oldcsnm = oldcsnm;
	}

	public Date getOldtstm() {
		return oldtstm;
	}

	public void setOldtstm(Date oldtstm) {
		this.oldtstm = oldtstm;
	}

	public String getOldcrcd() {
		return oldcrcd;
	}

	public void setOldcrcd(String oldcrcd) {
		this.oldcrcd = oldcrcd;
	}

	public String getOldticd() {
		return oldticd;
	}

	public void setOldticd(String oldticd) {
		this.oldticd = oldticd;
	}

	public String getXgnr() {
		return xgnr;
	}

	public void setXgnr(String xgnr) {
		this.xgnr = xgnr;
	}

	public String getHzlx() {
		return hzlx;
	}

	public void setHzlx(String hzlx) {
		this.hzlx = hzlx;
	}

	@Override
	public String toString() {
		return "BaseReceipt [id=" + id + ", bwmc=" + bwmc + ", oldcsnm=" + oldcsnm + ", oldtstm=" + oldtstm
				+ ", oldcrcd=" + oldcrcd + ", oldticd=" + oldticd + ", xgnr=" + xgnr + ", hzlx=" + hzlx + ", hzmc="
				+ hzmc + ", hzState=" + hzState + ", hzsj=" + hzsj + "]";
	}

}
