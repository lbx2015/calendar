package net.riking.service;

import java.util.List;

import net.riking.entity.model.MQOptCommon;
import net.riking.entity.model.News;

public interface NewsService {

	/**
	 * 根据userId找到收藏的资讯
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<News> findCollectNews(String userId, int start, int pageCount);

	/**
	 * 
	 * @param coverUrls
	 * @return
	 */
	public String concatCoverUrls(String coverUrls);

	public void newsCollect(MQOptCommon common) throws IllegalArgumentException, IllegalAccessException;

	public void newsCommentPub(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException;

	public void moveFile(News news);
}
