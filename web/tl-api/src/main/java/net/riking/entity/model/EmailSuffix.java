package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;

@Entity
@Table(name = "t_email_suffix")
public class EmailSuffix {
	
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	
	@Column(name = "suffix", length = 32)
	private String suffix;
	
	//0-删除状态   1-未删除状态
	@Comment("删除标记")
	@Column(name = "delete_state")
	private String deleteState;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(String deleteState) {
		this.deleteState = deleteState;
	}
	
	
}
