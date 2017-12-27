package net.riking.service;

import net.riking.entity.model.MQOptCommon;

public interface QAInviteService {

	/**
	 * 保存邀请信息
	 * @param optCommon
	 * @return
	 */
	public void saveQaInvite(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException;

}
