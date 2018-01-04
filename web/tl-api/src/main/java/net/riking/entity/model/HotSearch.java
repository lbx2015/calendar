package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("热门搜索表")
@Entity
@Table(name = "t_hot_search")
public class HotSearch extends BaseEntity {

	private static final long serialVersionUID = -4138765413765191901L;

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@JsonProperty("hotSearchId")
	private String id;

	@Comment("标题")
	@Column(name = "title", length = 255, nullable = false)
	private String title;

	@Comment("被搜索次数")
	@org.hibernate.annotations.ColumnDefault("0")
	@Column(name = "search_count", nullable = false)
	private Integer searchCount;

	public HotSearch() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public HotSearch(String title) {
		super();
		this.title = title;
	}

	public HotSearch(String id, String title, Integer searchCount) {
		super();
		this.id = id;
		this.title = title;
		this.searchCount = searchCount;
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

	public Integer getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Integer searchCount) {
		this.searchCount = searchCount;
	}

}
