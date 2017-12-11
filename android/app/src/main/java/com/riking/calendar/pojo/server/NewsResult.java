package com.riking.calendar.pojo.server;

import java.util.Date;

public class NewsResult {

    //	@Comment("物理主键")
//	@JsonProperty("newsId")
    public String newsId;

    //	@Comment("标题")
    public String title;

    //	@Comment("创建时间")
//	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
    public Date createdTime;

}
