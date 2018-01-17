package com.riking.calendar.pojo.server.base;

/**
 * Created by zw.zhang on 2018/1/16.
 */

public class BaseUser {
    // 其他用户ID
    public String userId;
    // 是否已关注   未注册： -2-未邀请 ， -1 - 已邀请  ， 已注册 ： 0-未关注  ，1 -已关注 ， 2- 互相关注
    public int isFollow;
}
