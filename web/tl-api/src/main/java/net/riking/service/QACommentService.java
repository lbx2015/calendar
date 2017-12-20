package net.riking.service;

import java.util.List;

import net.riking.entity.model.QACommentResult;

public interface QACommentService {

	/**
	 * 找到用户的评论
	 * @param userId
	 * @param pageBegin
	 * @param pageCount
	 * @return
	 */
	public List<QACommentResult> findByUserId(String userId, Integer pageBegin, Integer pageCount);
}
