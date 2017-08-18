//
//  LocalNotificationManager.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/14.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "LocalNotificationManager.h"

static LocalNotificationManager *manager = nil;

@implementation LocalNotificationManager

+ (LocalNotificationManager *)shareManager
{
    static dispatch_once_t oneToken;
    dispatch_once(&oneToken,^{
        manager = [[self alloc] init];
        
    });
    return manager;
}


#pragma mark - 取消所有通知
- (void)cancelAllLocalNotifications{
    [[UIApplication sharedApplication] cancelAllLocalNotifications];
}


#pragma mark - 取消某一条通知
- (void)cancelLocalNotificationsWithKey:(NSString *)key value:(NSString *)value
{
    NSArray *arrLocalNotifis = [[UIApplication sharedApplication] scheduledLocalNotifications];//获取所有本地通知
    for (UILocalNotification *localNoti in arrLocalNotifis)//遍历
    {
        NSDictionary *userInfo = localNoti.userInfo;//获取通知附带的信息
        if (userInfo)
        {
            if ([(NSString *)[userInfo objectForKey:key]isEqualToString:value])//前面保存在userInfo中的内容
            {
                [[UIApplication sharedApplication] cancelLocalNotification:localNoti];//这句代码会删除所有本地通知中的特定一条，并且不会再推送这一条
                RKLog(@"取消通知name:%@==%@",key,value);
            }
        }
    }
}


- (NSDateComponents *)InitializationWithDate:(NSDate *)date
{
    NSCalendar *calendar=[[NSCalendar alloc]initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    NSDateComponents *comps = [[NSDateComponents alloc] init];
    NSInteger unitFlags = NSCalendarUnitEra | NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay | NSCalendarUnitHour | NSCalendarUnitMinute | NSCalendarUnitSecond | NSCalendarUnitWeekOfYear | NSCalendarUnitWeekday | NSCalendarUnitWeekdayOrdinal | NSCalendarUnitQuarter;
    comps = [calendar components:unitFlags fromDate:date];
    
    return comps;
}


#pragma mark - 添加通知
- (void)scheduleNotificationWithAlertBody:(NSString *)alertBody userInfo:(NSDictionary *)userInfo repeatInterVal:(NSCalendarUnit)repeatInterVal fireDate:(NSDate*)date{
    
    //初始化
    UILocalNotification *locationNotification = [[UILocalNotification alloc]init];
    locationNotification.fireDate =date;
    //设置重复周期
    locationNotification.repeatInterval = repeatInterVal;
    //设置通知的音乐
    locationNotification.soundName = UILocalNotificationDefaultSoundName;
    //设置通知内容
    locationNotification.alertBody = alertBody;
    //设置推送实体
    locationNotification.userInfo = userInfo;
    //应用程序图标右上角显示的消息数
    locationNotification.applicationIconBadgeNumber=1;
    //执行本地推送
    [[UIApplication sharedApplication] scheduleLocalNotification:locationNotification];
    
}

@end
