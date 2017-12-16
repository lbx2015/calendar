package net.riking.entity.params;

import net.riking.core.entity.PageQuery;

/**
 * 报表核销参数
 * @author james.you
 * @version crateTime：2017年11月29日 下午3:16:14
 * @used TODO
 */
public class ReportCompletedRelParam extends PageQuery{
	
	private String userId;// 用户Id

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
