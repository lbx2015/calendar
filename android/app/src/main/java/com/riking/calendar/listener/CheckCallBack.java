package com.riking.calendar.listener;

import com.riking.calendar.pojo.AppVersionResult;

/**
 * Created by zw.zhang on 2017/9/11.
 */

public interface CheckCallBack{//检测成功或者失败的相关接口
    void onSuccess(AppVersionResult updateInfo);
    void onError();
}