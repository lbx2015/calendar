package net.riking.entity.params;

import net.riking.core.entity.PageQuery;

/**
 * 报表核销参数
 * @author james.you
 * @version crateTime：2017年11月29日 下午3:16:14
 * @used TODO
 */
public class ReportCompletedRelParam extends PageQuery{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4792743477807916643L;

	private String userId;// 用户Id
	
	private String currentMonth;//当前月份yyyyMM

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}

}
