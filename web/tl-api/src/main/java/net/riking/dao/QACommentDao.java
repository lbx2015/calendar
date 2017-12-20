package net.riking.dao;

import java.util.List;

import net.riking.entity.model.QACommentResult;

public interface QACommentDao {
	/**
	 * 找到用户的评论
	 * @param userId
	 * @param pageBegin
	 * @param pageCount
	 * @return
	 */
	public List<QACommentResult> findByUserId(String userId, Integer pageBegin, Integer pageCount);
}
