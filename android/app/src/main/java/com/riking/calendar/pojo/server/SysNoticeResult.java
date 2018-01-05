package com.riking.calendar.pojo.server;

import java.util.Date;

public class SysNoticeResult {
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
    //数据类型0-系统信息；1-被邀请回答的邀请；2-问题回答被点赞或收藏；3-问题被关注；4-被关注的用户；5-评论被点赞；6-问题回答的被评论；7-评论的回复和回复的被回复
    public int dataType = -1;
    //是否已读：0-未读; 1-已读
    public Integer isRead;
    //翻页时间戳
//	@DateTimeFormat(pattern = "yyyyMMddHHmmssSSS")
//	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
//    public String reqTimeStamp;
    //创建时间
    public Date createdTime;
}
