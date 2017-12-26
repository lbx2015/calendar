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

    // 他人的用户Id
    public String toUserId;

    // 验证码
    public String verifyCode;

    // 邮箱
    public String email;

    // 是否删除： 0-删除，1-未删除
    public Integer isDeleted;

    // 手机号
    public String phone;

    // 手机Deviceid
    public String phoneDeviceid;

    // 手机类型 1-IOS;2-Android;3-其它
    public Integer phoneType;

    public List<String> phones;

    public Integer pindex = 0; //
    public Integer pcount = 30;//
    public String sort;// like: id_asc|name_desc
}
