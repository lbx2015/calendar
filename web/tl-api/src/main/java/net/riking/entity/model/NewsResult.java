package net.riking.entity.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.riking.core.annos.Comment;

public class NewsResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8418351387803616527L;

	@Comment("物理主键")
	private String id;

	@Comment("标题")
	private String title;

	@Comment("创建时间")
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	private Date createdTime;

	public NewsResult(String id, String title, Date createdTime) {
		super();
		this.id = id;
		this.title = title;
		this.createdTime = createdTime;
	}

	public NewsResult() {
		super();
		// TODO Auto-generated constructor stub
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
