package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("反馈表")
@Entity
@Table(name = "t_feedback")
public class FeedBack extends BaseProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5622478704716507201L;

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@JsonProperty("feedBackId")
	private String id;

	@Comment("标题")
	@Column(name = "title", length = 32)
	private String title;

	@Comment("反馈内容")
	@Column(name = "feedback_describe")
	private String feedbackDescribe;

	@Comment("是否采纳:0-未操作；1-未采纳；2-采纳")
	@Column(name = "accept")
	private Integer accept;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFeedbackDescribe() {
		return feedbackDescribe;
	}

	public void setFeedbackDescribe(String feedbackDescribe) {
		this.feedbackDescribe = feedbackDescribe;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getAccept() {
		return accept;
	}

	public void setAccept(Integer accept) {
		this.accept = accept;
	}

}
