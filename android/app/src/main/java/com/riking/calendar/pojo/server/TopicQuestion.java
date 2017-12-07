package com.riking.calendar.pojo.server;


import com.riking.calendar.pojo.server.base.BaseAuditProp;

/**
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
//@Comment("话题的问题表")
//@Entity
//@Table(name = "t_topic_question")
public class TopicQuestion extends BaseAuditProp {

    //    @Comment("物理主键")
//    @Id
//    @Column(name = "id", length = 32)
//    @GenericGenerator(name = "system-uuid", strategy = "uuid")
//    @GeneratedValue(generator = "system-uuid")
//    @JsonProperty("topicQuestionId")
    public String topicQuestionId;

    //    @Comment("标题")
//    @Column(name = "title", length = 512, nullable = false)
    public String title;

    //    @Comment("内容")
//    @Lob
//    @Column(name = "content", nullable = false)
    public String content;

    //    @Comment("话题主键： fk t_topic (最多3个，用','分隔开)") // 最多3个
//    @Column(name = "topic_id", length = 128, nullable = false)
    public String topicId;

    //    @Comment("用户主键: fk t_app_user")
//    @Column(name = "user_id", length = 32)
    public String userId;

    // 用户名
//    @Transient
    public String userName;

    // 用户头像Url
//    @Transient
    public String photoUrl;

    // 用户关注数
//    @Transient
    public int followNum;

    // 用户回答数
//    @Transient
    public int answerNum;

    // 经验值
//    @Transient
    public int experience;

    //    @Transient
//    @Comment("是否已关注 0-未关注，1-已关注")
    public int isFollow;

}
