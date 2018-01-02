package net.riking.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_APP_QUESTIONKEYWORD")
public class QuestionKeyWord implements Serializable {

	private static final long serialVersionUID = 6201480562449024982L;

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;

	/** 关键字 */
	@Column(name = "KEY_WORD")
	private String keyWord;

	/** 对应话题id */
	@Column(name = "TOPIC_ID")
	private String topicId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}


}
