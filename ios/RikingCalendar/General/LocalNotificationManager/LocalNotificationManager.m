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
    
    locationNotification.timeZone=[NSTimeZone defaultTimeZone];
    //执行本地推送
    [[UIApplication sharedApplication] scheduleLocalNotification:locationNotification];
    
}

#pragma mark - 添加本地待办通知
- (void)addGtaskLocationNotificationWithGtasksModel:(GtasksModel *)gtasksModel{
    
    if ([Utils timeSwitchTimestamp:gtasksModel.strDate andFormatter:@"yyyyMMddHHmm"]>[Utils timeSwitchTimestamp:[Utils transformDateWithFormatter:@"yyyyMMddHHmm" date:[NSDate date]] andFormatter:@"yyyyMMddHHmm"]) {
        
        NSMutableDictionary *gtasksDict = [gtasksModel mj_keyValues];
        [gtasksDict removeObjectForKey:@"columeNames"];
        [gtasksDict removeObjectForKey:@"columeTypes"];
        [gtasksDict removeObjectForKey:@"propertyNames"];
        [gtasksDict removeObjectForKey:@"pk"];
        
        [[LocalNotificationManager shareManager] scheduleNotificationWithAlertBody:gtasksModel.content userInfo:gtasksDict repeatInterVal:0 fireDate:[Utils getDataString:gtasksModel.strDate formatter:@"yyyyMMddHHmm"]];
    }
}


#pragma mark - 添加本地提醒通知
- (void)addLocalNotifisWithRemindModel:(ReminderModel *)remindModel{
    
    NSMutableDictionary *param = [NSMutableDictionary dictionaryWithDictionary:[remindModel mj_keyValues]];
    [param removeObjectForKey:@"propertyNames"];
    [param removeObjectForKey:@"pk"];
    [param removeObjectForKey:@"columeTypes"];
    [param removeObjectForKey:@"columeNames"];
    
    NSString *curronDateStr = remindModel.strDate;
    NSString *startTimeStr = [NSString stringWithFormat:@"%@%@",remindModel.startTime,@"00"];
    NSDate *curronDate = [Utils getDataString:[NSString stringWithFormat:@"%@%@",curronDateStr,startTimeStr] formatter:@"yyyyMMddHHmmss"];
    NSInteger beforeTime = 0;
    
    if (remindModel.isAllday) {
        
        if (isAllDayRemindTime) {
            curronDate = [Utils getDataString:[NSString stringWithFormat:@"%@%@",[remindModel.strDate substringToIndex:8],[NSString stringWithFormat:@"%@%@",isAllDayRemindTime,@"00"]] formatter:@"yyyyMMddHHmmss"];
        }else{
            curronDate = [Utils getDataString:[NSString stringWithFormat:@"%@%@",[remindModel.strDate substringToIndex:8],[NSString stringWithFormat:@"%@%@",@"0800",@"00"]] formatter:@"yyyyMMddHHmmss"];
        }
        
    }
    if (remindModel.beforeTime>0) {
        beforeTime = remindModel.beforeTime*60;
        
        NSInteger selectTimestamp = [Utils timeSwitchTimestamp:[Utils transformDateWithFormatter:@"yyyyMMddHHmmss" date:curronDate] andFormatter:@"yyyyMMddHHmmss"] - beforeTime;
        NSDate *beforeDate = [Utils getDataString:[Utils timestampSwitchTime:selectTimestamp andFormatter:@"yyyyMMddHHmmss"] formatter:@"yyyyMMddHHmmss"];
        curronDate = beforeDate;
    }
    NSCalendar *calendar=[[NSCalendar alloc]initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    NSDateComponents *comps = [[NSDateComponents alloc] init];
    NSInteger unitFlags = NSCalendarUnitEra | NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay | NSCalendarUnitHour | NSCalendarUnitMinute | NSCalendarUnitSecond | NSCalendarUnitWeekOfYear | NSCalendarUnitWeekday | NSCalendarUnitWeekdayOrdinal | NSCalendarUnitQuarter;
    comps = [calendar components:unitFlags fromDate:curronDate];
    
    if (remindModel.repeatFlag == 0) {
        
//        RKLog(@"%@",[Utils transformDateWithFormatter:@"yyyyMMddHHmmss" date:curronDate]);
//        RKLog(@"%@",[Utils transformDateWithFormatter:@"yyyyMMddHHmmss" date:[NSDate date]]);
        
        if (![self WhetherTimeIsOverWithTime:curronDate]) {
            
            [self scheduleNotificationWithAlertBody:remindModel.content userInfo:param repeatInterVal:0 fireDate:curronDate];
        }
    }else if (remindModel.repeatFlag==1){
        
        NSArray *dayData = [CalendarModel selectDataWithSql:[NSString stringWithFormat:@"select *from CalendarModel where date>='%@' and remark='1' order by date",[Utils getCurrentTimeWithDay]]];
        
        if (dayData.count>0) {
            
            for (int i = 0; i<dayData.count; i++) {
                
                //工作日只注册最近的5天的通知
                if (i<5) {

                    CalendarModel *cModel = dayData[i];
                    NSString *remindDateStr = [NSString stringWithFormat:@"%@%@",cModel.date,[Utils transformDateWithFormatter:@"HHmmss" date:curronDate]];
                    NSDate *remindDate = [Utils getDataString:remindDateStr formatter:@"yyyyMMddHHmmss"];
                    
                    if (![self WhetherTimeIsOverWithTime:remindDate]) {
                        [self scheduleNotificationWithAlertBody:remindModel.content userInfo:param repeatInterVal:0 fireDate:remindDate];
                    }
                }
            }
        }
        
        
    }else if (remindModel.repeatFlag==2){
        
        
        NSArray *dayData = [CalendarModel selectDataWithSql:[NSString stringWithFormat:@"select *from CalendarModel where date>='%@' and remark='0' order by date",[Utils getCurrentTimeWithDay]]];
        
        if (dayData.count>0) {
            
            for (int i = 0; i<dayData.count; i++) {
                
                //节假日日只注册最近的3天的通知
                if (i<3) {
                    CalendarModel *cModel = dayData[i];
                    NSString *remindDateStr = [NSString stringWithFormat:@"%@%@",cModel.date,[Utils transformDateWithFormatter:@"HHmmss" date:curronDate]];
                    NSDate *remindDate = [Utils getDataString:remindDateStr formatter:@"yyyyMMddHHmmss"];
                    
                    if (![self WhetherTimeIsOverWithTime:remindDate]) {
                        [self scheduleNotificationWithAlertBody:remindModel.content userInfo:param repeatInterVal:0 fireDate:remindDate];
                    }
                }
            }
        }
        
        
    }else{
        
        
        NSArray *array = [NSArray arrayWithArray:[remindModel.repeatValue componentsSeparatedByString:@","]];
        
        if ([remindModel.repeatValue containsString:@"1,2,3,4,5,6,7"] && array.count==7){
            //每天重复
            [self scheduleNotificationWithAlertBody:remindModel.content userInfo:param repeatInterVal:NSCalendarUnitDay fireDate:curronDate];
            
        }else{
            for (int i=0; i<7; i++) {
                //i==0,当天的时间
                NSDate *myDate = [curronDate dateByAddingTimeInterval:3600 * 24 * i];
                NSString *weekDay = [Utils getWeekDayWithDate:myDate];
                
                if ([remindModel.repeatValue rangeOfString:weekDay].location!=NSNotFound) {
                    //NSCalendarUnitWeekday            = kCFCalendarUnitWeekday,
                    [self scheduleNotificationWithAlertBody:remindModel.content userInfo:param repeatInterVal:kCFCalendarUnitWeek fireDate:myDate];
                }
            }
        }
        
    }
    
    
    
    
    
    
    //        for (int newWeekDay =2; newWeekDay<=6; newWeekDay++) {
    //
    //            NSInteger temp = 0;
    //            NSInteger days = 0;
    //
    //            temp = newWeekDay - comps.weekday;
    //            days = (temp >= 0 ? temp : temp + 7);
    //
    //            NSDate *newFireDate = [[calendar dateFromComponents:comps] dateByAddingTimeInterval:3600 * 24 * days];
    //            [self scheduleNotificationWithItem:_rModel.content fireDate:newFireDate];
    //
    //
    //        }
    
    
    
    
    //        NSCalendar *calendar=[[NSCalendar alloc]initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    //        NSDateComponents *comps = [[NSDateComponents alloc] init]; NSInteger unitFlags = NSCalendarUnitEra | NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay | NSCalendarUnitHour | NSCalendarUnitMinute | NSCalendarUnitSecond | NSCalendarUnitWeekOfYear | NSCalendarUnitWeekday | NSCalendarUnitWeekdayOrdinal | NSCalendarUnitQuarter;
    //        comps = [calendar components:unitFlags fromDate:[NSDate date]];
    //        for (int i = 0; i <10; i++) {
    //            NSDate *newFireDate = [calendar dateFromComponents:comps];//[ dateByAddingTimeInterval:3*i];
    //            UILocalNotification *newNotification = [[UILocalNotification alloc] init];
    //            if (newNotification) {
    //
    //                newNotification.fireDate = newFireDate;
    //                newNotification.alertBody = @"哈哈"; newNotification.soundName = @"呵呵";
    //                newNotification.applicationIconBadgeNumber=1;//应用程序图标右上角显示的消息数
    //            newNotification.alertAction = @"查看闹钟"; newNotification.userInfo =@{@"id":@1,@"user":@"Kenshin Cui"};//绑定到通知上的其他附加信息;
    //            [[UIApplication sharedApplication] scheduleLocalNotification:newNotification];
    //        }
    //
    //    }
    
    
    
    
    
    
    //        // 申请通知权限
    //        [[UNUserNotificationCenter currentNotificationCenter] requestAuthorizationWithOptions:(UNAuthorizationOptionAlert | UNAuthorizationOptionSound | UNAuthorizationOptionBadge) completionHandler:^(BOOL granted, NSError * _Nullable error) {
    //            // A Boolean value indicating whether authorization was granted. The value of this parameter is YES when authorization for the requested options was granted. The value is NO when authorization for one or more of the options is denied.
    //            if (granted) {
    //                // 1、创建通知内容，注：这里得用可变类型的UNMutableNotificationContent，否则内容的属性是只读的
    //                UNMutableNotificationContent *content = [[UNMutableNotificationContent alloc] init];
    //                // 标题
    //                content.title = @"通知到啦";
    //                //次标题 content.subtitle = @"柯梵办公室通知";
    //                //内容
    //                content.body = _rModel.content;
    //                // app显示通知数量的角标
    //                content.badge = @1;
    //                // 通知的提示声音，这里用的默认的声音
    //                content.sound = [UNNotificationSound defaultSound];
    //
    //                // 附件 可以是音频、图片、视频 这里是一张图片
    //                //                NSURL *imageUrl = [[NSBundle mainBundle] URLForResource:@"jianglai" withExtension:@"jpg"];
    //                //                UNNotificationAttachment *attachment = [UNNotificationAttachment attachmentWithIdentifier:@"imageIndetifier" URL:imageUrl options:nil error:nil];
    //                //                content.attachments = @[attachment];
    //                // 标识符
    //                content.categoryIdentifier = _rModel.reminderId;
    //                // 2、创建通知触发
    //                /* 触发器分三种：
    //                 UNTimeIntervalNotificationTrigger : 在一定时间后触发，如果设置重复的话，timeInterval不能小于60
    //                 UNCalendarNotificationTrigger : 在某天某时触发，可重复
    //                 UNLocationNotificationTrigger : 进入或离开某个地理区域时触发 */
    //
    //                NSCalendar *calendar=[[NSCalendar alloc]initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    //                NSDateComponents *comps = [[NSDateComponents alloc] init];
    //                NSInteger unitFlags = NSCalendarUnitEra | NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay | NSCalendarUnitHour | NSCalendarUnitMinute | NSCalendarUnitSecond | NSCalendarUnitWeekOfYear | NSCalendarUnitWeekday | NSCalendarUnitWeekdayOrdinal | NSCalendarUnitQuarter;
    //                comps = [calendar components:unitFlags fromDate:curronDate];
    //                comps.weekday = 2|3|4;
    //                comps.year = curronDate.year;
    //                comps.month = curronDate.month;
    //                comps.hour = curronDate.hour;
    //                comps.minute = curronDate.minute;
    //                comps.second = 0;
    //                UNCalendarNotificationTrigger *trigger2 = [UNCalendarNotificationTrigger triggerWithDateMatchingComponents:comps repeats:YES];
    //
    //                // 3、创建通知请求
    //                UNNotificationRequest *notificationRequest = [UNNotificationRequest requestWithIdentifier:@"KFGroupNotification" content:content trigger:trigger2];
    //                // 4、将请求加入通知中心
    //                [[UNUserNotificationCenter currentNotificationCenter] addNotificationRequest:notificationRequest withCompletionHandler:^(NSError * _Nullable error) { if (error == nil) { NSLog(@"已成功加推送%@",notificationRequest.identifier);
    //                }
    //                }];
    //            }
    //        }];
    
    
    
}

#pragma mark - 查询所有已注册的本地推送
- (NSArray *)queryAllSystemNotifications{
    return [[UIApplication sharedApplication] scheduledLocalNotifications];
}

#pragma mark - 对比，是否过期
- (void)compareFiretime:(UILocalNotification *)notification needRemove:(void(^)(UILocalNotification * item))needRemove{
    
    NSComparisonResult result = [notification.fireDate compare:[NSDate date]];
    
    if (result == NSOrderedAscending && notification.repeatInterval !=0) {
        needRemove(notification);
    }
    
}


- (BOOL)WhetherTimeIsOverWithTime:(NSDate *)time{
    
    BOOL ret = YES;
    if ([Utils timeSwitchTimestamp:[Utils transformDateWithFormatter:@"yyyyMMddHHmmss" date:time] andFormatter:@"yyyyMMddHHmmss"]>[Utils timeSwitchTimestamp:[Utils transformDateWithFormatter:@"yyyyMMddHHmmss" date:[NSDate date]] andFormatter:@"yyyyMMddHHmmss"]) {
        
        ret = NO;
    }
    return ret;
}

@end
