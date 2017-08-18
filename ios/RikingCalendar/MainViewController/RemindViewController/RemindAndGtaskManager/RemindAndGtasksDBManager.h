//
//  RemindAndGtasksDBManager.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/16.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface RemindAndGtasksDBManager : NSObject

+ (RemindAndGtasksDBManager *)shareManager;

#pragma mark - 获取提醒根据时间
- (NSMutableArray *)getRemindArrayWithDate:(NSDate *)date;

#pragma mark - 获取明天的提醒
- (NSMutableArray *)getTomorrowRemindArray;

#pragma mark - 获取未来的提醒
- (NSMutableArray *)getFutureRemindArray;

#pragma mark - 获取所有待办
- (NSMutableArray *)getAllGtasksArray;

#pragma mark - 获取所有待办历史
- (NSMutableArray *)getAllGtasksHistory;

#pragma mark - 明天的时间
- (NSString *)GetTomorrowDay:(NSDate *)aDate;
@end
