package net.riking.entity.model;

import net.riking.core.entity.BaseEntity;

/**
 * 资讯收藏信息 表
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class NewsRelUnionPkId extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6365922550894229903L;

	private String userId;

	private String newsId;

	public NewsRelUnionPkId(String userId, String newsId) {
		super();
		this.userId = userId;
		this.newsId = newsId;
	}

	public NewsRelUnionPkId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newsId == null) ? 0 : newsId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewsRelUnionPkId other = (NewsRelUnionPkId) obj;
		if (newsId == null) {
			if (other.newsId != null)
				return false;
		} else if (!newsId.equals(other.newsId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
