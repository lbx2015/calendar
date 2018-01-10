package com.riking.calendar.pojo.server;

/**
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
//@Comment("热门搜索表")
//@Entity
//@Table(name = "t_hot_search")
public class HotSearch {
    //	@Comment("物理主键")
//	@Id
//	@Column(name = "id", length = 32)
//	@GenericGenerator(name = "system-uuid", strategy = "uuid")
//	@GeneratedValue(generator = "system-uuid")
//	@JsonProperty("hotSearchId")
    public String id;
    //	@Comment("标题")
//	@Column(name = "title", length = 255, nullable = false)
    public String title;
    //	@Comment("被搜索次数")
//	@org.hibernate.annotations.ColumnDefault("0")
//	@Column(name = "search_count", nullable = false)
    public Integer searchCount;
}
