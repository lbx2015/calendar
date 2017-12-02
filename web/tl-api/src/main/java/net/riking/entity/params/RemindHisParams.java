package net.riking.entity.params;

import java.io.Serializable;

/**
 * 提醒的历史参数
 * 
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class RemindHisParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6952211991459875719L;

	// 用户Id
	private String remindHisId;

	public String getRemindHisId() {
		return remindHisId;
	}

	public void setRemindHisId(String remindHisId) {
		this.remindHisId = remindHisId;
	}

}
