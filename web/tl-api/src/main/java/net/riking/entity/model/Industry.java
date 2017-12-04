package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("行业职务表")
@Entity
@Table(name = "t_industry")
public class Industry extends BaseProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3940163346158751094L;

	@Comment("行业/职位名称")
	@Column(name = "name", length = 32)
	private String name;

	@Comment("所属行业id")
	@Column(name = "parent_id", length = 32)
	private String parentId;

	@Comment("类型:0-行业；1-职位")
	@Column(name = "data_type", length = 1)
	private Integer dataType;// 0：行业；1：职位

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

}
