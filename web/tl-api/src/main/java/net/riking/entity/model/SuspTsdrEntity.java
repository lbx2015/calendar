package net.riking.entity.model;



/**
 * 可疑客户：收入：支出   实体对象
 * @author mf.teng
 *
 */
public class SuspTsdrEntity {
	/**可疑交易客户*/
	private String csnm;
	/**收入*/
	private Number income;
	/**支出*/
	private Number outlay;
	
	
	public SuspTsdrEntity(String csnm, Number income2, Number outlay2) {
		super();
		this.csnm = csnm;
		this.income = income2;
		this.outlay = outlay2;
	}
	public String getCsnm() {
		return csnm;
	}
	public void setCsnm(String csnm) {
		this.csnm = csnm;
	}
	public Number getIncome() {
		return income;
	}
	public void setIncome(Integer income) {
		this.income = income;
	}
	public Number getOutlay() {
		return outlay;
	}
	public void setOutlay(Integer outlay) {
		this.outlay = outlay;
	}
	@Override
	public String toString() {
		return "SuspTsdrEntity [csnm=" + csnm + ", income=" + income + ", outlay=" + outlay + "]";
	}
	
	
	
}
