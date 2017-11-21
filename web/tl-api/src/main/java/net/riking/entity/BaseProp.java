package net.riking.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import net.riking.core.annos.Comment;

@MappedSuperclass
public class BaseProp implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8133896068470367793L;


	@Comment("物理主键")
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	
	
	@Comment("创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@org.hibernate.annotations.CreationTimestamp
	@Column(name="created_time",insertable=false,updatable = false , nullable = false, columnDefinition="datetime default now()")
	private Date createdTime;

	@Comment("最后修改时间")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@org.hibernate.annotations.UpdateTimestamp  
	@Column(name="last_modified_time",insertable=false, nullable = false, columnDefinition="datetime default now()")
	private Date lastModifiedTime;

	@Comment("数据是否有效 0有效，1无效")
	@org.hibernate.annotations.ColumnDefault("0")  
	@Column(name="invalid",insertable=false, nullable=false, precision=1)
	private Integer invalid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public Integer getInvalid() {
		return invalid;
	}

	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
	}
	
	
	
}
