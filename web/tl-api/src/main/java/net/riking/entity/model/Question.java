package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;

/**
 * 
 * @author tao.yuan
 * @version crateTime：2017年11月15日 下午2:13:22
 * @used 问题表
 */
@Entity
@Table(name = "t_question")
public class Question {
	
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	
	@Comment("所属话题id")
	@Column(name = "topic_id", length = 32)
	private String topicId;//所属话题id
	
	@Comment("问题名称")
	@Column(name = "question_name", length = 512)
	private String questionName;//问题名称
	
	@Comment("问题内容")
	@Column(name = "question_content", length = 512)
	private String questionContent;//问题内容
	
	@Comment("问题图片")
	@Column(name = "question_photo", length = 256)
	private String questionPhoto;//问题图片

	@Comment("是否删除")
	@Column(name = "delete_state", length = 1)
	private String deleteState;//删除 0：已删除 1：未删除
	
	@Comment("创建时间")
	@Column(name = "create_time", length = 16)
	private String createTime;//创建时间 
	
	@Comment("关注这个问题题的总人数")
	@Column(name = "sum_user", length = 16)
	private String sumUser;//关注这个问题的总人数
	
	@Comment("总回答数")
	@Column(name = "sum_answer", length = 16)
	private String sumAnswer;//总回答数


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	public String getQuestionContent() {
		return questionContent;
	}

	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}

	public String getQuestionPhoto() {
		return questionPhoto;
	}

	public void setQuestionPhoto(String questionPhoto) {
		this.questionPhoto = questionPhoto;
	}

	public String getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(String deleteState) {
		this.deleteState = deleteState;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getSumUser() {
		return sumUser;
	}

	public void setSumUser(String sumUser) {
		this.sumUser = sumUser;
	}

	public String getSumAnswer() {
		return sumAnswer;
	}

	public void setSumAnswer(String sumAnswer) {
		this.sumAnswer = sumAnswer;
	}
	
}
