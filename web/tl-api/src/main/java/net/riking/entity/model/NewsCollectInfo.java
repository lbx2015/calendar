package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

@Comment("资讯收藏信息 表")
@Entity
@Table(name = "t_news_collect_info")
public class NewsCollectInfo extends BaseProp {

	private static final long serialVersionUID = -5750505701863092076L;
	
	@Comment("操作人主键  ")
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Comment("目标对象主键")
	@Column(name = "news_id", nullable = false)
	private String newsId;

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

}
