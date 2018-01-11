package com.riking.calendar.pojo.server;

import com.google.gson.Gson;

import java.util.Date;

public class SysNoticeResult {
    public String userId;
    //消息通知id
    public String noticeId;
    //消息通知标题
    public String title;
    //来自某用户对象的用户昵称
    public String fromUserName;
    //用户头像
    public String userPhotoUrl;
    //根据依据dataType存储对象id
    public String objId;
    //消息内容
    public String content;
    // 数据类型0-系统信息；1-被邀请回答的邀请；2-问题回答被点赞或收藏；3-资讯的收藏；4-问题的屏蔽；5-问题，话题，用户的关注；6-评论点赞；7-通讯录的邀请 ；8-问题回答的评论；
    //9-资讯的评论发布；10-评论的回复和回复的回复；
    public int dataType = -1;
    //是否已读：0-未读; 1-已读
    public Integer isRead;
    //翻页时间戳
//	@DateTimeFormat(pattern = "yyyyMMddHHmmssSSS")
//	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
//    public String reqTimeStamp;
    //创建时间
    public Date createdTime;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}