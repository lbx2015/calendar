package net.riking.entity.model;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

public class QuestResult extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8418351387803616527L;

	@Comment("物理主键")
	@JsonProperty("questionId")
	private String id;

	@Comment("标题")
	private String title;

	@Comment("内容")
	private String content;

	// 用户名
	@Transient
	private String userName;

	// 用户头像Url
	@Transient
	private String photoUrl;

	public QuestResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuestResult(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
