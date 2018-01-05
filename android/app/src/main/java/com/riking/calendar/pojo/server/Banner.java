package com.riking.calendar.pojo.server;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
//@Entity
//@Table(name = "t_app_banner")
public class Banner {

//	@Comment("物理主键")
//	@Id
//	@Column(name = "id", length = 32)
//	@GenericGenerator(name = "system-uuid", strategy = "uuid")
//	@GeneratedValue(generator = "system-uuid")
	public String id;

//	@Comment("横幅标题")
//	@Column(name = "title", length = 100)
	public String title;

//	@Comment("是否启用 0：未启用 1：启用")
//	@org.hibernate.annotations.ColumnDefault("1")
//	@Column(name = "enabled", length = 1)
	public String enabled;

//	@Comment("是否审核 0：未审核 1：已审核 2：不通过")
//	@org.hibernate.annotations.ColumnDefault("0")
//	@Column(name = "is_aduit", length = 1)
	public String isAduit;

//	@Comment("横幅url")
//	@Column(name = "banner_url", length = 100)
	public String bannerURL;

//	@Comment("友情链接")
//	@Column(name = "relation_url", length = 100)
	public String relationURL;
}
