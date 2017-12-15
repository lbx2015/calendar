package com.riking.calendar.pojo.server;

public class TopicResult {

    //    @Comment("物理主键")
//    @JsonProperty("topicId")
    public String topicId;

    //    @Comment("标题")
    public String title;

    //    @Comment("话题url")
    public String topicUrl;

    //    @Comment("关注数")
    public Integer followNum;

    //    @Comment("是否已关注 0-未关注，1-已关注")
    public Integer isFollow;

}
