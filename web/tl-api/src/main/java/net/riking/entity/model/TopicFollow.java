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
 * @version crateTime：2017年11月15日 上午10:45:51
 * @used 话题关注表
// */
@Entity
@Table(name = "t_topic_follow")
public class TopicFollow {
	
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	
	@Comment("话题id")
	@Column(name = "topic_id", length = 32)
	private String topicId;//话题id
	
	@Comment("用户id")
	@Column(name = "user_id", length = 32)
	private String userId;//用户id
	
	@Comment("创建时间")
	@Column(name = "create_time", length = 16)
	private String createTime;//创建时间/关注话题的时间
	
	@Comment("删除标志")
	@Column(name = "delete_state", length = 1)
	private String deleteState;//删除标志 0：删除 ； 1：未删除
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setDeleteState(String deleteState) {
		this.deleteState = deleteState;
	}
	
	public String getDeleteState() {
		return deleteState;
	}

	public void setDeleteStatus(String deleteState) {
		this.deleteState = deleteState;
	}

}
