package com.riking.calendar.pojo.server;

import com.riking.calendar.pojo.server.base.BaseAuditProp;


/**
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
//@Comment("行业资讯表")
//@Entity
//@Table(name = "t_news")
public class News extends BaseAuditProp {
    public String newsId;

    //    @Comment("资讯标题")
//    @Column(name = "title", length = 128)
    public String title;

    //    @Comment("封面位置：right；center")
//    @Lob
//    @Column(name = "seat", length = 10)
    public String seat;

    //    @Comment("多个封面URL，';'分隔")
//    @Lob
//    @Column(name = "cover_urls", length = 255)
    public String coverUrls;

    //    @Comment("资讯内容")
//    @Lob
//    @Column(name = "content")
    public String content;

    //    @Comment("发布单位")
//    @Column(name = "issued", length = 255, nullable = false)
    public String issued;

    //    @Transient
    public int experience;

    //    @Transient
    public int grade;

    // 用户名
//    @Transient
    public String userName;

    // 评论数
//    @Transient
    public int commentNumber;

    // 用户头像Url
//    @Transient
    public String photoUrl;

    // 是否收藏（0-未收藏，1-已收藏）
//    @Transient
    public int isCollect;
}
