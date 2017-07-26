package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "CusRateInfo")
@Table(name = "T_CUSTOMER_RATEINFO")
public class CusRateInfo {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;

	@Column(name = "csnm")
	private String csnm;// 客户号

	@Column(name = "ctid")
	private String ctid;// 证件号码

	@Column(name = "enName")
	private String enName;// 中文名称

	@Column(name = "riskprop1")
	private String riskprop1;

	@Column(name = "riskprop2")
	private String riskprop2;

	@Column(name = "riskprop3")
	private String riskprop3;

	@Column(name = "riskprop4")
	private String riskprop4;

	@Column(name = "riskprop5")
	private String riskprop5;

	@Column(name = "riskprop6")
	private String riskprop6;

	@Column(name = "riskprop7")
	private String riskprop7;

	@Column(name = "riskprop8")
	private String riskprop8;

	@Column(name = "riskprop9")
	private String riskprop9;

	@Column(name = "riskprop10")
	private String riskprop10;

	@Column(name = "riskprop11")
	private String riskprop11;

	@Column(name = "riskprop12")
	private String riskprop12;

	@Column(name = "riskprop13")
	private String riskprop13;

	@Column(name = "riskprop14")
	private String riskprop14;

	@Column(name = "riskprop15")
	private String riskprop15;

	@Column(name = "riskprop16")
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

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
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
		CusRateInfo other = (CusRateInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CusRateInfo [id=" + id + ", csnm=" + csnm + ", ctid=" + ctid + ", enName=" + enName + ", riskprop1="
				+ riskprop1 + ", riskprop2=" + riskprop2 + ", riskprop3=" + riskprop3 + ", riskprop4=" + riskprop4
				+ ", riskprop5=" + riskprop5 + ", riskprop6=" + riskprop6 + ", riskprop7=" + riskprop7 + ", riskprop8="
				+ riskprop8 + ", riskprop9=" + riskprop9 + ", riskprop10=" + riskprop10 + ", riskprop11=" + riskprop11
				+ ", riskprop12=" + riskprop12 + ", riskprop13=" + riskprop13 + ", riskprop14=" + riskprop14
				+ ", riskprop15=" + riskprop15 + ", riskprop16=" + riskprop16 + "]";
	}

}
