package com.riking.calendar.pojo.params;

/**
 * 问题的接收参数
 *
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class TQuestionParams extends BaseParams {

    // 话题下面问题Id
    public String tqId;

    // (标题)
    public String title;

    // (话题id,逗号','分隔，最三个)
    public String topicId;

    // (1-问题；2-话题；3-用户)
    public int objType;

    // (目标对象ID)
    public String attentObjId;

    // (0-取消；1-关注)
    public int enabled;

    // 问题回答Id
    public String questAnswerId;

}
