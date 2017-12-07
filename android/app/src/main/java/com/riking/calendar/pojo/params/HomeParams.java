package com.riking.calendar.pojo.params;


/**
 * 主页的接收参数
 *
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class HomeParams extends BaseParams {

    // 请求方向（up上，down下）
    public String direct;

    // 请求上翻最新时间戳
//	@DateTimeFormat(pattern = "yyyyMMddHHmmssSSS")
    //Server is Date format
    public String reqTimeStamp;

    // 1-问题；2-话题
    public int objType = 1;

    // 目标对象id
    public String objId;

    // 0-屏蔽；1-显示
    public int enabled=1;

}
