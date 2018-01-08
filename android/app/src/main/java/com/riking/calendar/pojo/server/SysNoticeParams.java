package com.riking.calendar.pojo.server;

import com.riking.calendar.pojo.params.BaseParams;

import java.util.Date;

/**
 * 消息通知接收参数
 *
 * @author james.you
 * @version crateTime：2018年1月3日 下午8:01:15
 * @used TODO
 */
public class SysNoticeParams extends BaseParams {

    //多个消息id，以逗号分隔
    private String noticeIds;

    //单个消息id
    private String noticeId;

    //删除内容是否有系统消息：1-有；0-无
    private Integer haveSysInfo;

    //翻页时间戳
//	@DateTimeFormat(pattern = "yyyyMMddHHmmssSSS")
//	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
    private Date reqTimeStamp;


}
