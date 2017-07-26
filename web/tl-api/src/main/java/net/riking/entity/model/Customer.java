package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "T_MODEL_CUSTOMER")
public class Customer {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;// 客户编号

	@Column(name = "csnm")
	private String csnm;// 客户号

	@Column(name = "ctid")
	private String ctid;// 证件号码

	@Column(name = "customerType")
	private String customerType;// 客户类型

	@Column(name = "country")
	private String country;// 国籍

	@Column(name = "enName")
	private String enName;// 客户中文名称

	@Column(name = "englishName")
	private String englishName;// 客户英文名称

	@Column(name = "branchCode")
	private String branchCode;// 分行代码

	@Column(name = "areaCode")
	private String areaCode;// 客户地区代码

	@Column(name = "cctz")
	private String cctz; // 客户集团所在地

	@Column(name = "lind")
	private String lind; // 客户行业代码

	@Column(name = "sfCode")
	private String sfCode;// 社会信用代码

	@Column(name = "enCode")
	private String enCode;// 组织机构代码

	@Column(name = "postCode")
	private String postCode;// 邮编

	@Column(name = "address")
	private String address;// 所在地址(中文)

	@Column(name = "enScale")
	private String enScale;// 企业规模

	@Column(name = "status")
	private String status;// 客户状态

	@Column(name = "riskRank")
	private String riskRank;// 风险等级

	@Column(name = "riskTime")
	private String riskTime;// 评级时间 2017/4/10

	@OneToOne(fetch = FetchType.EAGER)
	private CusRateInfo cusRateInfo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCsnm() {
		return csnm;
	}

	public void setCsnm(String csnm) {
		this.csnm = csnm;
	}

	public String getCtid() {
		return ctid;
	}

	public void setCtid(String ctid) {
		this.ctid = ctid;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCctz() {
		return cctz;
	}

	public void setCctz(String cctz) {
		this.cctz = cctz;
	}

	public String getLind() {
		return lind;
	}

	public void setLind(String lind) {
		this.lind = lind;
	}

	public String getSfCode() {
		return sfCode;
	}

	public void setSfCode(String sfCode) {
		this.sfCode = sfCode;
	}

	public String getEnCode() {
		return enCode;
	}

	public void setEnCode(String enCode) {
		this.enCode = enCode;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEnScale() {
		return enScale;
	}

	public void setEnScale(String enScale) {
		this.enScale = enScale;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRiskRank() {
		return riskRank;
	}

	public void setRiskRank(String riskRank) {
		this.riskRank = riskRank;
	}

	public String getRiskTime() {
		return riskTime;
	}

	public void setRiskTime(String riskTime) {
		this.riskTime = riskTime;
	}

	public CusRateInfo getCusRateInfo() {
		return cusRateInfo;
	}

	public void setCusRateInfo(CusRateInfo cusRateInfo) {
		this.cusRateInfo = cusRateInfo;
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
		Customer other = (Customer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getName() {
		return StringUtils.isEmpty(getEnName()) ? getEnglishName() : getEnName();
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", csnm=" + csnm + ", ctid=" + ctid + ", customerType=" + customerType
				+ ", country=" + country + ", enName=" + enName + ", englishName=" + englishName + ", branchCode="
				+ branchCode + ", areaCode=" + areaCode + ", cctz=" + cctz + ", lind=" + lind + ", sfCode=" + sfCode
				+ ", enCode=" + enCode + ", postCode=" + postCode + ", address=" + address + ", enScale=" + enScale
				+ ", status=" + status + ", riskRank=" + riskRank + ", riskTime=" + riskTime + "]";
	}

}
