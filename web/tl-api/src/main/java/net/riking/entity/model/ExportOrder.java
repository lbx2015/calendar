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
@Table(name = "T_AML_ExportOrder")
public class ExportOrder {

	@Id
	@Column(name = "id")
	@GeneratedValue
	private Long id;

	/** 机构编号 */
	@Column(name = "branch_Code", length = 255)
	private String branchCode;
	/** 系统时间 */
	@Temporal(TemporalType.DATE)
	@Column(name = "sys_Date", length = 8)
	private Date sysDate;

	@Column(name = "order_Num")
	private Integer orderNum;

	@Column(name = "order_Type")
	private String orderType;

	@Column(name = "is_Null_Report")
	private String isNullReport;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public Date getSysDate() {
		return sysDate;
	}

	public void setSysDate(Date sysDate) {
		this.sysDate = sysDate;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getIsNullReport() {
		return isNullReport;
	}

	public void setIsNullReport(String isNullReport) {
		this.isNullReport = isNullReport;
	}
}
