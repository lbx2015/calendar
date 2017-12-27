package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Entity
@Table(name = "t_app_banner")
public class Banner extends BaseProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5370380117246181873L;

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	@Comment("横幅标题")
	@Column(name = "title", length = 100)
	private String title;

	@Comment("是否启用 0：未启用 1：启用")
	@org.hibernate.annotations.ColumnDefault("1")
	@Column(name = "enabled", length = 1)
	private String enabled;

	@Comment("是否审核 0：未审核 1：已审核 2：不通过")
	@org.hibernate.annotations.ColumnDefault("0")
	@Column(name = "is_aduit", length = 1)
	private String isAduit;

	@Comment("横幅url")
	@Column(name = "banner_url", length = 100)
	private String bannerURL;

	@Comment("友情链接")
	@Column(name = "relation_url", length = 100)
	private String relationURL;

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

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getIsAduit() {
		return isAduit;
	}

	public void setIsAduit(String isAduit) {
		this.isAduit = isAduit;
	}

	public String getBannerURL() {
		return bannerURL;
	}

	public void setBannerURL(String bannerURL) {
		this.bannerURL = bannerURL;
	}

	public String getRelationURL() {
		return relationURL;
	}

	public void setRelationURL(String relationURL) {
		this.relationURL = relationURL;
	}

}
