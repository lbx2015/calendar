package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_AML_DataReport")
public class DataReport {
	@Id
	@Column(name = "id")
	@GeneratedValue
	private Long id;

	/** 所属模块 */
	@Column(name = "modular", length = 24)
	private String modular;

	/** 字段名 */
	@Column(name = "nameKey", length = 50)
	private String nameKey;

	/** 显示中文名 */
	@Column(name = "paraName", length = 50)
	private String paraName;

	/** 显示顺序 */
	@Column(name = "showLevel")
	private int showLevel;

	/** 显示的状态 */
	@Column(name = "state", length = 2)
	private String state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModular() {
		return modular;
	}

	public void setModular(String modular) {
		this.modular = modular;
	}

	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

	public String getParaName() {
		return paraName;
	}

	public void setParaName(String paraName) {
		this.paraName = paraName;
	}

	public int getShowLevel() {
		return showLevel;
	}

	public void setShowLevel(int showLevel) {
		this.showLevel = showLevel;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
