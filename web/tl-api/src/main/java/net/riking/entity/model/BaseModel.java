package net.riking.entity.model;

import java.io.Serializable;
import java.util.Date;


public class BaseModel implements Serializable{
	
	private static final long serialVersionUID = 3679720560629126984L;
	
	private Long id;
	private User createdBy;
	private Date createdTime;
	
	private User lastModifiedBy;
	private Date lastModifiedTime;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	
	public User getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	public User getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	public void setUserOperation(){
		Date date = new Date();
		User u = new User();
		u.setUserName("kaibing");
		if(id==null){
			createdTime = date;
			createdBy = u;
			lastModifiedTime = date;
			lastModifiedBy = u;
		}else{
			lastModifiedTime = date;
			lastModifiedBy = u;
		}
	}
}
