package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

@Entity
@Table(name = "t_industry")
public class Industry extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -68360963358945117L;

	//行业/职位表
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Comment("行业/职位名称")
	@Column(name = "name", length = 32)
	private String name;
	
	@Comment("父id")
	@Column(name = "parent_id", length = 32)
	private Long parentId;
	
	@Comment("所属类型")
	@Column(name = "type", length = 1)
	private Integer type;//0：行业；1：职位


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	

}
