package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("话题关注表")
@Entity
@IdClass(TopicRelUnionPkId.class)
@Table(name = "t_topic_rel")
public class TopicRel extends BaseEntity {

	private static final long serialVersionUID = 944410490844286677L;

	@Id
	@Comment("fk t_app_user")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Id
	@Comment("fk t_topic_info")
	@Column(name = "topic_id", nullable = false)
	private String topicId;

	@Comment("数据类型：0-关注；3-屏蔽")
	@org.hibernate.annotations.ColumnDefault("0")
	@Column(name = "data_type", insertable = false, nullable = false, precision = 1)
	private Integer dataType;

	@Comment("创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@org.hibernate.annotations.CreationTimestamp
	@Column(name = "created_time", insertable = false, updatable = false, nullable = false, columnDefinition = "datetime default now()")
	private Date createdTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
