package com.riking.calendar.pojo.params;

import java.util.List;

/**
 * 资讯类的接收参数
 *
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class UserParams extends BaseParams {

    // 是否删除： 0-删除，1-未删除
    public Integer isDeleted;

    // 手机号
    public String phone;

    // 手机Deviceid
    public String phoneDeviceid;

    // 手机类型 1-IOS;2-Android;3-其它
    public int phoneType = 2;

    public List<String> phones;

}
