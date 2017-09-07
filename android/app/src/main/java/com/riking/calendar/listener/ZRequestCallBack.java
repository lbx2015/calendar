package com.riking.calendar.listener;

/**
 * Created by zw.zhang on 2017/9/6.
 */

public abstract class ZRequestCallBack {
    public int successCount = 0;

    public abstract void success();

    public abstract void fail();
}
