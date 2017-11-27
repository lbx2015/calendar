package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseAuditProp;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("话题信息 表")
@Entity
@Table(name = "t_topic")
public class Topic extends BaseAuditProp {

	private static final long serialVersionUID = -4138765413765191901L;

	@Comment("标题")
	@Column(name = "title", length = 255, nullable = false)
	private String title;

	@Comment("内容")
	@Lob
	@Column(name = "content", nullable = false)
	private String content;

	@Transient
	@Comment("创建人名称")
	private String userName;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
