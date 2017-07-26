package net.riking.entity.model;

import org.apache.commons.lang3.StringUtils;

public class CusRateEntity {

	private Long id;

	private String csnm;// 客户号

	private String ctid;// 证件号码
	
	private String name;//客户名称

	private String enName;// 客户中文名称

	private String customerType;// 客户类型

	private String country;// 国籍

	private String englishName;// 客户英文名称

	private String branchCode;// 分行代码

	private String areaCode;// 客户地区代码

	private String cctz; // 客户集团所在地

	private String lind; // 客户行业代码

	private String sfCode;// 社会信用代码

	private String enCode;// 组织机构代码

	private String postCode;// 邮编

	private String address;// 所在地址(中文)

	private String enScale;// 企业规模

	private String status;// 客户状态

	private String riskRank;// 风险等级

	private String riskTime;// 评级时间 2017/4/10

	private String riskprop1;

	private String riskprop2;

	private String riskprop3;

	private String riskprop4;

	private String riskprop5;

	private String riskprop6;

	private String riskprop7;

	private String riskprop8;

	private String riskprop9;

	private String riskprop10;

	private String riskprop11;

	private String riskprop12;

	private String riskprop13;

	private String riskprop14;

	private String riskprop15;

	private String riskprop16;

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

	public String getName() {
		return StringUtils.isEmpty(getEnName()) ? getEnglishName() :getEnName();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
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

	public String getRiskprop1() {
		return riskprop1;
	}

	public void setRiskprop1(String riskprop1) {
		this.riskprop1 = riskprop1;
	}

	public String getRiskprop2() {
		return riskprop2;
	}

	public void setRiskprop2(String riskprop2) {
		this.riskprop2 = riskprop2;
	}

	public String getRiskprop3() {
		return riskprop3;
	}

	public void setRiskprop3(String riskprop3) {
		this.riskprop3 = riskprop3;
	}

	public String getRiskprop4() {
		return riskprop4;
	}

	public void setRiskprop4(String riskprop4) {
		this.riskprop4 = riskprop4;
	}

	public String getRiskprop5() {
		return riskprop5;
	}

	public void setRiskprop5(String riskprop5) {
		this.riskprop5 = riskprop5;
	}

	public String getRiskprop6() {
		return riskprop6;
	}

	public void setRiskprop6(String riskprop6) {
		this.riskprop6 = riskprop6;
	}

	public String getRiskprop7() {
		return riskprop7;
	}

	public void setRiskprop7(String riskprop7) {
		this.riskprop7 = riskprop7;
	}

	public String getRiskprop8() {
		return riskprop8;
	}

	public void setRiskprop8(String riskprop8) {
		this.riskprop8 = riskprop8;
	}

	public String getRiskprop9() {
		return riskprop9;
	}

	public void setRiskprop9(String riskprop9) {
		this.riskprop9 = riskprop9;
	}

	public String getRiskprop10() {
		return riskprop10;
	}

	public void setRiskprop10(String riskprop10) {
		this.riskprop10 = riskprop10;
	}

	public String getRiskprop11() {
		return riskprop11;
	}

	public void setRiskprop11(String riskprop11) {
		this.riskprop11 = riskprop11;
	}

	public String getRiskprop12() {
		return riskprop12;
	}

	public void setRiskprop12(String riskprop12) {
		this.riskprop12 = riskprop12;
	}

	public String getRiskprop13() {
		return riskprop13;
	}

	public void setRiskprop13(String riskprop13) {
		this.riskprop13 = riskprop13;
	}

	public String getRiskprop14() {
		return riskprop14;
	}

	public void setRiskprop14(String riskprop14) {
		this.riskprop14 = riskprop14;
	}

	public String getRiskprop15() {
		return riskprop15;
	}

	public void setRiskprop15(String riskprop15) {
		this.riskprop15 = riskprop15;
	}

	public String getRiskprop16() {
		return riskprop16;
	}

	public void setRiskprop16(String riskprop16) {
		this.riskprop16 = riskprop16;
	}

	@Override
	public String toString() {
		return "CusRateEntity [id=" + id + ", csnm=" + csnm + ", ctid=" + ctid + ", name=" + name + ", enName=" + enName
				+ ", customerType=" + customerType + ", country=" + country + ", englishName=" + englishName
				+ ", branchCode=" + branchCode + ", areaCode=" + areaCode + ", cctz=" + cctz + ", lind=" + lind
				+ ", sfCode=" + sfCode + ", enCode=" + enCode + ", postCode=" + postCode + ", address=" + address
				+ ", enScale=" + enScale + ", status=" + status + ", riskRank=" + riskRank + ", riskTime=" + riskTime
				+ ", riskprop1=" + riskprop1 + ", riskprop2=" + riskprop2 + ", riskprop3=" + riskprop3 + ", riskprop4="
				+ riskprop4 + ", riskprop5=" + riskprop5 + ", riskprop6=" + riskprop6 + ", riskprop7=" + riskprop7
				+ ", riskprop8=" + riskprop8 + ", riskprop9=" + riskprop9 + ", riskprop10=" + riskprop10
				+ ", riskprop11=" + riskprop11 + ", riskprop12=" + riskprop12 + ", riskprop13=" + riskprop13
				+ ", riskprop14=" + riskprop14 + ", riskprop15=" + riskprop15 + ", riskprop16=" + riskprop16 + "]";
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
		CusRateEntity other = (CusRateEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
