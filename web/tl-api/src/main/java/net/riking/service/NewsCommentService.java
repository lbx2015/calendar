package net.riking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.riking.entity.model.NewsComment;

public interface NewsCommentService {

	/**
	 * 获得需要显示的评论信息
	 * @param qaComment
	 * @param pageRequest
	 * @return
	 */
	public Page<NewsComment> findAll(NewsComment newsComment, PageRequest pageRequest);
}
