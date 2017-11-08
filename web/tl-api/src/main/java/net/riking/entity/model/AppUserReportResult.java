package net.riking.entity.model;

import java.util.List;

/**
 * 
 * @author tao.yuan
 * @version crateTime：2017年11月7日 下午4:01:44
 * @used TODO
 */
public class AppUserReportResult {
	
	private String userId;
	
	private List<String> list;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}


}
