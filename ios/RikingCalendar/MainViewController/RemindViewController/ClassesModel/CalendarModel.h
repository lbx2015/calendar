//
//  CalendarModel.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/16.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "LKDBModel.h"

@interface CalendarModel : LKDBModel
@property (nonatomic,copy)NSString *dateId;//日期ID
@property (nonatomic,copy)NSString *date;//日期
@property (nonatomic,copy)NSString *remark;//1:工作日;0:非工作日
@property (nonatomic,copy)NSString *weekday;//(1-7)星期
@end
