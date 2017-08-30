package net.riking.entity.model;

public class AppVersionResult {

	private String type;
	private String msg;
	private String versionNumber;
	public AppVersionResult(){
		super();
	}
	
	public AppVersionResult(String type, String msg ,String versionNumber) {
		this.type = type;
		this.msg = msg;
		this.versionNumber = versionNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	
}
