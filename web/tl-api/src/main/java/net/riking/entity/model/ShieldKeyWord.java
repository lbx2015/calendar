package net.riking.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import net.riking.core.annos.Comment;

@Entity
@Table(name = "T_SHIELD_KEYWORD")
public class ShieldKeyWord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1758847357327589899L;

	@Id
	@Column(name = "id")
	@GeneratedValue
	private Long id;

	@Column(name = "key_word")
	private String keyWord;

	@Comment("敏感词： 0-禁用 1-启用")
	@org.hibernate.annotations.ColumnDefault("1")
	@Column(name = "enabled", nullable = false, precision = 1)
	private Integer enabled;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

}
