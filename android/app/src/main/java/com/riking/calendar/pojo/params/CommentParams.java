package com.riking.calendar.pojo.params;

import com.google.gson.Gson;

/**
 * App版本获取接口参数
 *
 * @author james.you
 * @version crateTime：2017年11月28日 下午2:39:26
 * @used TODO
 */
public class CommentParams {

    // 用户id
    public String userId;

    // 被回复用户id
    public String toUserId;

    // 评论ID
    public String commentId;

    // 1-回答类；2-资讯类
    public Integer objType;

    // 回复Id
    public String replyId;

    // 内容
    public String content;

    // 0-取消；1-赞同
    public Integer enabled;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
