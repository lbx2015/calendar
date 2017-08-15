package com.riking.calendar.pojo;

import com.google.gson.Gson;

public class QueryReportModel {
    public QueryReportContainerList _data;// 返回的数据
    public Short code; // 状态码
    public String codeDesc; // 状态码描述
    public Integer runtime = 0; // 运行时长

    @Override
    public String toString() {
        Gson s = new Gson();
        return s.toJson(this);
    }
}
