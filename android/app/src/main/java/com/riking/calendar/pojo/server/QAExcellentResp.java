package com.riking.calendar.pojo.server;

/**
 * 问题回答的优秀回答者
 *
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class QAExcellentResp {
    // 用户id
    public String userId;

    // 用户名
    public String userName;

    // 用户头像
    public String photoUrl;

    // 问题的回答数
    public int qanswerNum;

    // 回答的点赞数
    public String qaAgreeNum;

    // 经验值
    public int experience;

    // 是否已关注 0-未关注，1-已关注
    public int isFollow;
}
