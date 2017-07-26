package net.riking.entity.model;

import java.math.BigDecimal;

/**
 * 图表查询
 * @author xy.wu
 *
 */
public class QueryResult {

	private String name;
	private Long value;
	private BigDecimal money;
	
	public QueryResult(){}
	
	public QueryResult(String name, Long value) {
		this.name = name;
		this.value = value;
	}
	
	public QueryResult(Long value, BigDecimal money) {
		this.money = money;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

}
