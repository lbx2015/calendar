package net.riking.entity.risk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_BASE_KYC_RISKFACTOR")
public class RiskFactor {

	/** 系统ID */
	@Id
	@Column(name = "id")
	@GeneratedValue
	private Long id;

	/** 风险代码 */
	@Column(name = "factor_code")
	private String factorCode;

	/** 风险名称 */
	@Column(name = "factor_name", length = 50)
	private String factorName;

	/** 权重 */
	@Column(name = "weights", length = 22)
	private Integer weights;

	/** 父节点,为0表示为一级节点 */
	@Column(name = "parent_factor_code")
	private String parentFactorCode;

	/** 分行代码 */
	@Column(name = "branch_id")
	private Integer branchId;

	/** 客户表中的属性 */
	@Column(name = "corptrn_prop")
	private String corptrnProp;

	@Column(name = "confirm_status")
	private String confirmStatus;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private String createdTime;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Column(name = "last_modified_time")
	private String lastModifiedTime;

	@Column(name = "confirm_by")
	private String Confirm_By;

	@Column(name = "confirm_time")
	private String confirmTime;

	/** 风险对公对私区分 1为对公2为对私 */
	@Column(name = "risk_type")
	private String risktype;

	public String getRisktype() {
		return risktype;
	}

	public void setRisktype(String risktype) {
		this.risktype = risktype;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFactorCode() {
		return factorCode;
	}

	public void setFactorCode(String factorCode) {
		this.factorCode = factorCode;
	}

	public String getFactorName() {
		return factorName;
	}

	public void setFactorName(String factorName) {
		this.factorName = factorName;
	}

	public Integer getWeights() {
		return weights;
	}

	public void setWeights(Integer weights) {
		this.weights = weights;
	}

	public String getParentFactorCode() {
		return parentFactorCode;
	}

	public void setParentFactorCode(String parentFactorCode) {
		this.parentFactorCode = parentFactorCode;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public String getCorptrnProp() {
		return corptrnProp;
	}

	public void setCorptrnProp(String corptrnProp) {
		this.corptrnProp = corptrnProp;
	}

	public String getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getConfirm_By() {
		return Confirm_By;
	}

	public void setConfirm_By(String confirm_By) {
		Confirm_By = confirm_By;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
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
		RiskFactor other = (RiskFactor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RiskFactor [id=" + id + ", factorCode=" + factorCode + ", factorName=" + factorName + ", weights="
				+ weights + ", parentFactorCode=" + parentFactorCode + ", branchId=" + branchId + ", corptrnProp="
				+ corptrnProp + ", confirmStatus=" + confirmStatus + ", createdBy=" + createdBy + ", createdTime="
				+ createdTime + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedTime=" + lastModifiedTime
				+ ", Confirm_By=" + Confirm_By + ", confirmTime=" + confirmTime + "]";
	}

}
