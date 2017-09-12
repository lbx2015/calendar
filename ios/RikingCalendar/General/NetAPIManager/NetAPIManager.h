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





//#define ServreUrl                                 @"http://172.16.64.85:8281"//liuyao

//#define ServreUrl                                 @"http://172.16.64.96:8281"

#define ServreUrl                                   @"http://172.16.32.14:6061/tl-api"




/**
 获取验证码
 */
#define getValiCode                                 @"/getValiCode"

/**
 验证验证码
 */
#define checkValiCode                               @"/checkValiCode"


/**
 用户服务协议
 */
#define userRegisterAgree                           @"/appAboutApp/agreementHtml"


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


/**
 获取所有的提醒
 */
#define getAllRemind                                @"/synchronous/synchronousRemindToApp"

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

/**
 同步待办信息
 */
#define synchronousTodos                            @"/synchronous/synchronousTodos"


/**
 获取用户所有的待办
 */
#define getAllGtasks                                @"/synchronous/synchronousTodoToApp"

/**
 获取待办历史信息(弃用)
 */
#define getTodoHistory                              @"/Todo/getTodoHis"



/**
 同步所有信息
 */
#define synchronousAll                              @"/synchronous/synchronousAll"





/**
 获取节假日条件
 */
#define getParamUrl                                 @"/ctryHdayCrcyApp/getParam"

#define getHolidays                                 @"/ctryHdayCrcyApp/getMore"

#define vagueQuery                                  @"/ctryHdayCrcyApp/vagueQuery"


//同步工作日
#define synchronousBusinessDay                      @"/synchronous/synchronousBusinessDay"

#define synchronousDate                             @"/synchronous/synchronousDate"


/**
 获取所有的报表
 */
#define getAllReport                                @"/reportListApp/getAllReport"

/**
 显示报表详情
 */
#define showReportDetail                            @"/appAboutApp/reportHtml"




/**
 获取用户下的所有报表
 */
#define getUserRepor                                @"/appUserReport/getUserRepor"

/**
 新增用户完成报表
 {
 "appUserId": "string",
 "completeDate": "string",
 "completeId": "string",
 "isComplete": 0,
 "reportId": "string"
 }
 */
#define userReportSave                              @"/appUserReportCompleteRel/save"

/**
 获取用户所有报表完成的情况
 */
#define getUserAllCompleteReport                    @"/appUserReportCompleteRel/getAllReport"




//更新用户手机设备信息
#define IsChangeMac                                 @"/appUserApp/IsChangeMac"

//添加或者更新用户信息
#define addOrUpdate                                 @"/appUserApp/addOrUpdate"

/**
 上传用户头像
 */
#define uploadUserImage                             @"/appUserApp/upLoad"


#define aboutUrl                                    @"/appAboutApp/aboutHtml"
@end
