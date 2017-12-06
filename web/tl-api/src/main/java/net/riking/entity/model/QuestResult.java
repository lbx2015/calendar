package net.riking.entity.model;

import java.io.Serializable;

import net.riking.core.annos.Comment;

public class QuestResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8418351387803616527L;

	@Comment("物理主键")
	private String id;

	@Comment("标题")
	private String title;

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
