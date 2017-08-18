//
//  NetAPIManager.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/5.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <Foundation/Foundation.h>


#define requestUrl(url)          [NSString stringWithFormat:@"%@%@",ServreUrl,(url)]


@interface NetAPIManager : NSObject


#define ServreUrl               @"http://172.16.64.85:8281"//liuyao

//#define ServreUrl               @"http://172.16.64.96:8281"






/**
 获取验证码
 */
#define getValiCode                                 @"/getValiCode"

/**
 验证验证码
 */
#define checkValiCode                               @"/checkValiCode"

//获取系统版本
#define getappVersion                               @"/common/getappVersion"



//=====================================================提醒=============================================

//新增/修改提醒
#define saveReminder                                @"/remindApp/save"

//删除提醒
#define deleteRemind                                @"/remindApp/delMore"

//获取提醒列表
#define getReminderList                             @"/reminHisApp/getAllReport"

//同步提醒信息
#define synchronousReminds                          @"/synchronous/synchronousReminds"

//新增/修改提醒历史
#define saveRemindHistory                           @"/reminHisApp/save"

//删除提醒历史信息
#define deleteHistoryRem                            @"/reminHisApp/delMore"

//同步提醒历史
#define synchronousRemindHistory                    @"/synchronous/synchronousRemindHis"

//=====================================================待办===============================================

//保存待办信息
#define saveTodo                                    @"/Todo/save"

//获取待办信息
#define getTodo                                     @"/Todo/getTodo"

//获取待办历史信息
#define getTodoHistory                              @"/Todo/getTodoHis"









/**
 获取节假日条件
 */
#define getParamUrl                                 @"/ctryHdayCrcyApp/getParam"

#define getHolidays                                 @"/ctryHdayCrcyApp/getMore"

#define vagueQuery                                  @"/ctryHdayCrcyApp/vagueQuery"


//同步工作日
#define synchronousBusinessDay                      @"/synchronous/synchronousBusinessDay"

#define synchronousDate                             @"/synchronous/synchronousDate"
//app获取所有的报表
#define getAllReport                                @"/reportListApp/getAllReport"

#define showReportDetail                            @"/appAboutApp/reportHtml"

//app获取用户下的报表
#define getUserRepor                                @"/appUserReport/getUserRepor"

//更新用户手机设备信息
#define IsChangeMac                                 @"/appUserApp/IsChangeMac"

//添加或者更新用户信息
#define addOrUpdate                                 @"/appUserApp/addOrUpdate"

/**
 上传用户头像
 */
#define uploadUserImage                             @"/appUserApp/upLoad"


#define aboutUrl                                    @"/aboutHtml"
@end
