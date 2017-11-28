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
@Comment("问题回答点赞和收藏表")
@Entity
@IdClass(QAnswerRelUnionPkId.class)
@Table(name = "t_qa_rel")
public class QAnswerRel extends BaseEntity {

	private static final long serialVersionUID = 7996371062026840698L;

	@Id
	@Comment("操作人主键  ")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Id
	@Comment("目标对象主键")
	@Column(name = "qa_id", nullable = false)
	private String qaId;

	@Comment("数据类型：数据类型：1-点赞；2-收藏；3-屏蔽")
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

	public String getQaId() {
		return qaId;
	}

	public void setQaId(String qaId) {
		this.qaId = qaId;
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
