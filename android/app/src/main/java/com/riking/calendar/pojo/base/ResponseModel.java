package com.riking.calendar.pojo.base;

import com.google.gson.Gson;

/**
 * Created by zw.zhang on 2017/8/17.
 */

public class ResponseModel<T> {
    public T _data;// 返回的数据
    public Short code; // 状态码
    public String codeDesc; // 状态码描述
    public Integer runtime = 0; // 运行时长

    @Override
    public String toString() {
        Gson s = new Gson();
        return s.toJson(this);
    }
}
