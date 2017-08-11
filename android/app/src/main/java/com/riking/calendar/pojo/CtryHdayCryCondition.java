package com.riking.calendar.pojo;

public class CtryHdayCryCondition {
    public CtryHdayCrcyMode _data;// 返回的数据
    public Short code; // 状态码
    public String codeDesc; // 状态码描述
    public Integer runtime = 0; // 运行时长

    @Override
    public String toString() {
        return "HolidayConditionDemo{" +
                "_data=" + _data +
                ", code=" + code +
                ", codeDesc='" + codeDesc + '\'' +
                ", runtime=" + runtime +
                '}';
    }
}
