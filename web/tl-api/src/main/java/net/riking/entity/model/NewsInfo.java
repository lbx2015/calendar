package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Lob;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

public class NewsInfo extends BaseProp {

	private static final long serialVersionUID = 4238557791903463912L;
	
	@Comment("标题")
	@Column(name = "title", length = 512, nullable = false)
	private String title;
	
	@Comment("内容")
	@Lob
	@Column(name = "content", nullable = false)
	private String content;
	
	@Comment("作者")
	@Column(name = "author", length = 512, nullable = false)
	private String author;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
}
