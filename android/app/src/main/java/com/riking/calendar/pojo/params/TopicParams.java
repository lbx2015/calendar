package com.riking.calendar.pojo.params;

/**
 * 话题的接收参数
 */
public class TopicParams extends BaseParams {

    // 话题详情
    public String topicId;

    // 1-精华；2-问题；3-优秀回答者
    public Integer optType;
}
