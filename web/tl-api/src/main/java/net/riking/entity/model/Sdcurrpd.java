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

import net.riking.core.annos.Comment;

@Entity
@Table(name = "T_Sdcurrpd")
@Comment("汇率")
public class Sdcurrpd {

	@Id
	@Column(name = "id")
	@GeneratedValue
	@Comment("系统ID")
	private Long id;// 系统ID

	// 币种
	@Comment("币种")
	@Column(name = "A6CYCD", length = 3)
	private String currency;
	// 汇率
	@Comment("汇率")
	@Column(name = "A6SPRT",length =22)
	private BigDecimal rate;
	// 乘/除
	@Comment("乘、除")
	@Column(name = "A6MDIN", length = 8)
	private String method;
	// 汇率日期
	@Comment("汇率日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECTIVEDATE")

	private Date rateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Date getRateDate() {
		return rateDate;
	}

	public void setRateDate(Date rateDate) {
		this.rateDate = rateDate;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return "Sdcurrpd [id=" + id + ", currency=" + currency + ", rate=" + rate + ", rateDate=" + rateDate + "]";
	}

}
