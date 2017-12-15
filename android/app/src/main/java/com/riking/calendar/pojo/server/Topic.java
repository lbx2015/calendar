package com.riking.calendar.pojo.server;

import com.riking.calendar.pojo.server.base.BaseAuditProp;

/**
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
//@Comment("话题信息 表")
//@Entity
//@Table(name = "t_topic")
public class Topic extends BaseAuditProp {

    //    @Comment("物理主键")
//    @Id
//    @Column(name = "id", length = 32)
//    @GenericGenerator(name = "system-uuid", strategy = "uuid")
//    @GeneratedValue(generator = "system-uuid")
//    @JsonProperty("topicId")
    public String topicId;

    //    @Comment("标题")
//    @Column(name = "title", length = 255, nullable = false)
    public String title;

    //    @Comment("内容")
//    @Lob
//    @Column(name = "content", nullable = false)
    public String content;

    //    @Comment("话题url")
//    @Lob
//    @Column(name = "topic_url", nullable = false)
    public String topicUrl;

    //    @Transient
//    @Comment("创建人名称")
    public String userName;

    //    @Transient
//    @Comment("关注数")
    public int followNum;

    //    @Transient
//    @Comment("用户头像")
    public String photoUrl;

    //    @Transient
//    @Comment("是否已关注 0-未关注，1-已关注")
    public int isFollow;
}
