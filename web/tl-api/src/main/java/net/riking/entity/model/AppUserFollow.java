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
 * @version crateTime：2017年11月15日 下午1:56:42
 * @used 用户关注表
 */
@Entity
@Table(name = "t_appuser_follow")
public class AppUserFollow {
	
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	@Comment("用户id")
	@Column(name = "user_id", length = 32)
	private String userId;//用户id
	
	@Comment("关注的用户id")
	@Column(name = "to_user_id", length = 32)
	private String toUserId;//关注的用户id
	
	@Comment("关注状态")
	@Column(name = "follow_state", length = 1)
	private String followState;//关注状态
	

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

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getFollowState() {
		return followState;
	}

	public void setFollowState(String followState) {
		this.followState = followState;
	}
	
	
	
	
}
