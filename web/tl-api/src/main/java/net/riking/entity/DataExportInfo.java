package net.riking.entity;

import java.util.List;

public class DataExportInfo {
	private String bothOrOne;
	
	private Long branchId;

	private String date;

	private String token;

	private String userName;
	
	private List<String> branchCodes;

	public DataExportInfo() {
		super();
	}
	
	public DataExportInfo(String bothOrOne, Long branchId, String date, String token, String userName) {
		super();
		this.bothOrOne = bothOrOne;
		this.branchId = branchId;
		this.date = date;
		this.token = token;
		this.userName = userName;
	}

	public DataExportInfo(String bothOrOne, Long branchId, String date, String token, String userName,List<String> branchCodes) {
		super();
		this.bothOrOne = bothOrOne;
		this.branchId = branchId;
		this.date = date;
		this.token = token;
		this.userName = userName;
		this.branchCodes = branchCodes;
	}

	public String getBothOrOne() {
		return bothOrOne;
	}

	public void setBothOrOne(String bothOrOne) {
		this.bothOrOne = bothOrOne;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getBranchCodes() {
		return branchCodes;
	}

	public void setBranchCodes(List<String> branchCodes) {
		this.branchCodes = branchCodes;
	}
	
	
}
