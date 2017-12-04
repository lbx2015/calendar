package com.riking.calendar.pojo;

/**
 * Created by zw.zhang on 2017/8/10.
 */

public class ModelPropDict {
    public Long id;

    /**
     * 所属类名
     */
//    @Column(name = "TABLENAME")
    public String clazz;

    /**
     * 对应字段
     */
//    @Column(name = "FIELD")
    public String field;

    /**
     * 对应的键
     */
//    @Column(name = "KE")
    public String ke;

    /**
     * 对应的值
     */
//    @Column(name = "VALU")
    public String valu;

    @Override
    public String toString() {
        return "ModelPropDict{" +
                "userId=" + id +
                ", clazz='" + clazz + '\'' +
                ", field='" + field + '\'' +
                ", ke='" + ke + '\'' +
                ", valu='" + valu + '\'' +
                '}';
    }
}
