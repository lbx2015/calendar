package com.riking.calendar.pojo.params;

import java.io.Serializable;

/**
 * 资讯类的接收参数
 *
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class NewsParams implements Serializable {

    // 用户Id
    public String userId;

    // 请求方向（up上，down下）
    public String direct;

    // 资讯信息id
    public String newsId;

    // (0-取消；1-收藏)
    public Integer enabled;

    // 请求上翻最新时间戳
//	@DateTimeFormat(pattern = "yyyyMMddHHmmssSSS")
    public String reqTimeStamp;

    // 评论内容
    public String content;

    // 屏蔽问题[1-问题;2-话题;3-屏蔽]
    public Integer objType;

    // 目标对象id
    public String objId;

}
