package net.riking.entity.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_BASE_KYC_RiskFactorConfig")
public class RiskFactorConfig {

	/**
	 * 
	 */

	@Id
	@Column(name = "id")
	@GeneratedValue
	private Long id;// 系统ID

	@Column(name = "factor_code")
	private String factorCode;// 父级风险因子代码

	@Column(name = "rank", length = 50)
	private String rank;// 等级

	@Column(name = "score", precision = 22, scale = 2)
	private BigDecimal score;// 分数

	@Column(name = "Factor_Code_Value", length = 50)
	private String factorCodeValue;// 属性代码值

	@Column(name = "Factor_Code_Name", length = 255)
	private String factorCodeName;// 属性代码名称

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

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public String getFactorCodeValue() {
		return factorCodeValue;
	}

	public void setFactorCodeValue(String factorCodeValue) {
		this.factorCodeValue = factorCodeValue;
	}

	public String getFactorCodeName() {
		return factorCodeName;
	}

	public void setFactorCodeName(String factorCodeName) {
		this.factorCodeName = factorCodeName;
	}

	@Override
	public String toString() {
		return "RiskFactorConfig [id=" + id + ", factorCode=" + factorCode + ", rank=" + rank + ", score=" + score
				+ ", factorCodeValue=" + factorCodeValue + ", factorCodeName=" + factorCodeName + "]";
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
		RiskFactorConfig other = (RiskFactorConfig) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
