package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;
import net.riking.entity.MyDateFormat;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("系统通知表")
@Entity
@Table(name = "t_sys_notice")
public class SysNotice extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4523950779896146571L;

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	@Comment("通知标题")
	@Column(name = "title", length = 255)
	private String title;

	@Comment("通知内容")
	@Column(name = "content", length = 500)
	private String content;
	
	@Comment("数据类型0-系统信息；1-被邀请回答的邀请；2-问题回答被点赞或收藏；3-问题被关注；4-被关注的用户；5-评论被点赞；6-问题回答的被评论；7-评论的回复和回复的被回复")
	@Column(name = "data_type", length = 2)
	private Integer dataType;
	
	@Comment("依据data_type存储对象id")
	@Column(name = "obj_id", length = 32)
	private String objId;
	
	@Comment("来自某用户对象的信息，系统代表：SYS")
	@Column(name = "from_user_id", length = 32)
	private String fromUserId;
	
	@Comment("通知用户对象")
	@Column(name = "notice_user_id", length = 32)
	private String noticeUserId;
	
	@Comment("创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@MyDateFormat(pattern = "yyyyMMddHHmmssSSS")
	@org.hibernate.annotations.CreationTimestamp
	@Column(name = "created_time", insertable = false, updatable = false, nullable = false, columnDefinition = "datetime default now()")
	private Date createdTime;

	public SysNotice() {
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getNoticeUserId() {
		return noticeUserId;
	}

	public void setNoticeUserId(String noticeUserId) {
		this.noticeUserId = noticeUserId;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

}
