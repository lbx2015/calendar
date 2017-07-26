package net.riking.entity.model;

public class AmlSuspiciousQuery {
	// 为统计查询而封装的类，str为所需要查出来的字符串类型，num为统计的数量
	private String str;
	private Long num1;
	private Long num2;
	private String ctid;

	public AmlSuspiciousQuery(String str, Long num1) {
		super();
		this.str = str;
		this.num1 = num1;
	}

	public AmlSuspiciousQuery(String str, Long num1, Long num2) {
		super();
		this.str = str;
		this.num1 = num1;
		this.num2 = num2;
	}

	public AmlSuspiciousQuery(String str,  Long num1, Long num2,String ctid) {
		super();
		this.str = str;
		this.ctid = ctid;
		this.num1 = num1;
		this.num2 = num2;
	}
	
	public AmlSuspiciousQuery(String str,  Long num1, String ctid) {
		super();
		this.str = str;
		this.ctid = ctid;
		this.num1 = num1;
	}
	
	public AmlSuspiciousQuery(String str, String ctid) {
		super();
		this.str = str;
		this.ctid = ctid;
	}

	public String getCtid() {
		return ctid;
	}

	public void setCtid(String ctid) {
		this.ctid = ctid;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public Long getNum1() {
		return num1;
	}

	public void setNum1(Long num1) {
		this.num1 = num1;
	}

	public Long getNum2() {
		return num2;
	}

	public void setNum2(Long num2) {
		this.num2 = num2;
	}

	@Override
	public String toString() {
		return "AmlSuspiciousQuery [str=" + str + ", num1=" + num1 + ", num2=" + num2 + ", ctid=" + ctid + "]";
	}

	
}
