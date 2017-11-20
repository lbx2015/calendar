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
 * @version crateTime：2017年11月14日 下午4:52:39
 * @used 话题表
 */
@Entity
@Table(name = "t_topic")
public class Topic {
	
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	
	@Comment("话题名称")
	@Column(name = "topic_name", length = 512)
	private String topicName;//话题名称
	
	@Comment("话题描述")
	@Column(name = "topic_describe", length = 512)
	private String topicDescribe;//话题描述
	
	@Comment("话题图片")
	@Column(name = "topic_photo", length = 256)
	private String topicPhoto;//话题图片
	
	@Comment("是否删除")
	@Column(name = "delete_state", length = 1)
	private String deleteState;//删除 0：已删除 1：未删除
	
	@Comment("创建时间")
	@Column(name = "create_time", length = 16)
	private String createTime;//创建时间 
	
	@Comment("关注这个话题的总人数")
	@Column(name = "sum_user", length = 16)
	private String sumUser;//关注这个话题的总人数
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getTopicDescribe() {
		return topicDescribe;
	}

	public void setTopicDescribe(String topicDescribe) {
		this.topicDescribe = topicDescribe;
	}

	public String getTopicPhoto() {
		return topicPhoto;
	}

	public void setTopicPhoto(String topicPhoto) {
		this.topicPhoto = topicPhoto;
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
	
}
