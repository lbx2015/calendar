package com.riking.calendar.pojo.params;

/**
 * 搜索列表类的接收参数
 *
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class SearchParams extends BaseParams {

    // 问题Id
    public String tqId;

    // 显示操作类型：0-不显示状态；1-显示关注/订阅/收藏状态；2-显示邀请状态
    public int showOptType = 1;

    // 搜索类型： 1-报表；2-话题；3-人脉；4-资讯；5-问题
    public int objType = 1;

    // 关键字
    public String keyWord;

}
