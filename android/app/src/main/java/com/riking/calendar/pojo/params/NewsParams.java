package com.riking.calendar.pojo.params;

import com.riking.calendar.util.ZPreference;

import java.io.Serializable;

/**
 * 资讯类的接收参数
 *
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class NewsParams extends BaseParams implements Serializable {

    // 请求方向（up上，down下）
    public String direct;

    // 资讯信息id
    public String newsId;

    // (0-取消；1-收藏)
    public int enabled = 0;

    // 请求上翻最新时间戳
//	@DateTimeFormat(pattern = "yyyyMMddHHmmssSSS")
    public String reqTimeStamp;

    // 评论内容
    public String content;

    // 屏蔽问题[1-问题;2-话题;3-屏蔽]
    public int objType;

    // 目标对象id
    public String objId;

}
