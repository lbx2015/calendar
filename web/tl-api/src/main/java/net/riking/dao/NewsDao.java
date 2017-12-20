package net.riking.dao;

import java.util.List;

import net.riking.entity.model.News;

public interface NewsDao {

	/**
	 * 收藏的资讯
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<News> findCollectNews(String userId, int begin, int pageCount);
}
