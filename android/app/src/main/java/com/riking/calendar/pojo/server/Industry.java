package com.riking.calendar.pojo.server;

import com.riking.calendar.pojo.server.base.BaseProp;

public class Industry extends BaseProp {
    //    @Comment("行业/职位名称")
//    @Column(name = "name", length = 32)
    public String name;
    public String industryId;

    //    @Comment("所属行业id")
//    @Column(name = "parent_id", length = 32)
    public String parentId;

    //    @Comment("类型:0-行业；1-职位")
//    @Column(name = "data_type", length = 1)
    public Integer dataType;// 0：行业；1：职位

}
