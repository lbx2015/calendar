package com.riking.calendar.pojo.server;

public class Industry {

    //行业/职位表
//	@Id
//	@GeneratedValue
//	@Column(name = "id")
    public Long id;

    //	@Comment("行业/职位名称")
//	@Column(name = "name", length = 32)
    public String name;

    //	@Comment("父id")
//	@Column(name = "parent_id", length = 32)
    public Long parentId;

    //	@Comment("所属类型")
//	@Column(name = "type", length = 1)
    public Integer type;//0：行业；1：职位

}
