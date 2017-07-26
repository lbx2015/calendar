package net.riking.entity.report;

import java.math.BigDecimal;

/**
 * Created by bing.xun on 2017/7/11.
 */
public class CustomerReport {

    //客户名
    private String name;

    //客户号
    private String number;

    //笔数
    private Long total;

    //币种
    private String bz;

    //金额
    private BigDecimal je;

    public CustomerReport(){

	}

    public CustomerReport(String number,Long total,String bz, BigDecimal je) {
    	this.number = number;
		this.total = total;
		this.bz = bz;
		this.je = je;
	}
    
    public CustomerReport(String number,String name) {
    	this.number = number;
		this.name = name;
	}
    
    public CustomerReport(String number,String name,Long total,String bz, BigDecimal je) {
    	this.number = number;
    	this.name = name;
		this.total = total;
		this.bz = bz;
		this.je = je;
	}
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public BigDecimal getJe() {
		return je;
	}

	public void setJe(BigDecimal je) {
		this.je = je;
	}
    
    
}
