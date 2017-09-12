//
//  ReminderModel.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/1.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "LKDBModel.h"

@interface ReminderModel : LKDBModel

@property (nonatomic, copy)     NSString    *reminderId;
@property (nonatomic, copy)     NSString    *userId;
@property (nonatomic, copy)     NSString    *content;//内容
@property (nonatomic, copy)     NSString    *strDate;//提醒时间
@property (nonatomic, copy)     NSString    *startTime;//全天事件提醒时间
@property (nonatomic, assign)   int         isAllday;//是否开启全天提醒
@property (nonatomic, assign)   int         beforeTime;//提醒提前时间
@property (nonatomic, copy)     NSString    *endTime;//结束时间
@property (nonatomic, assign)   int         repeatFlag;//重复标识（0-不重复；1-法定工作日；2-法定节假日；3-其它）
@property (nonatomic, copy)     NSString    *repeatValue;//重复值(例如0,1,2,3,4,5,6)
@property (nonatomic, assign)   int         currWeek;//当前星期数（0,6）
@property (nonatomic, assign)   int         clientType;//客户端类型 1:iOS 2:android 3 web
@property (nonatomic, assign)   int         isRemind;//是否提醒;0-不提醒；1-提醒
@property (nonatomic, assign)   int         syncStatus;//同步的状态0:同步,1未同步
@property (nonatomic, assign)   int         deleteState;//0不删除,1删除
@property (nonatomic, assign)   int         loactionStatus;//0:本地和服务器都有,1:只有本地有
@end
