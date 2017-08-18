//
//  LocalNotificationManager.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/14.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LocalNotificationManager : NSObject

+ (LocalNotificationManager *)shareManager;


/**
 取消所有通知
 */
- (void)cancelAllLocalNotifications;

#pragma mark - 取消某一条通知

/**
 取消某一条通知

 @param key key description
 @param value value description
 */
- (void)cancelLocalNotificationsWithKey:(NSString *)key value:(NSString *)value;


- (NSDateComponents *)InitializationWithDate:(NSDate *)date;

#pragma mark - 添加通知

/**
 添加通知

 @param alertBody alertBody description
 @param userInfo userInfo description
 @param repeatInterVal repeatInterVal description
 @param date date description
 */
- (void)scheduleNotificationWithAlertBody:(NSString *)alertBody userInfo:(NSDictionary *)userInfo repeatInterVal:(NSCalendarUnit)repeatInterVal fireDate:(NSDate*)date;
@end
