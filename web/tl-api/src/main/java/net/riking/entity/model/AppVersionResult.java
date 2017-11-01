package net.riking.entity.model;

public class AppVersionResult {

	private String type;
	private String msg;
	private String versionNumber;
	private String apkUrl;
	public AppVersionResult(){
		super();
	}
	
	public AppVersionResult(String type, String msg ,String versionNumber,String apkUrl) {
		this.type = type;
		this.msg = msg;
		this.versionNumber = versionNumber;
		this.apkUrl= apkUrl;
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

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	
}
