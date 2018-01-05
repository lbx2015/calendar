package net.riking.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.QAComment;
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

	/***
	 * 评论点赞 返回：true-点赞；false-取消点赞
	 * @author james.you
	 * @version crateTime：2017年12月27日 下午5:51:29
	 * @used TODO
	 * @param optCommon
	 * @return true-点赞；false-取消点赞
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean commentAgree(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException;

	/**
	 * 获得需要显示的评论信息
	 * @param qaComment
	 * @param pageRequest
	 * @return
	 */
	public Page<QAComment> findAll(QAComment qaComment, PageRequest pageRequest);
}
