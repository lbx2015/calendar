//
//  RemindAndGtasksDBManager.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/16.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RemindAndGtasksDBManager.h"


static RemindAndGtasksDBManager *manager = nil;
@implementation RemindAndGtasksDBManager


+ (RemindAndGtasksDBManager *)shareManager
{
    static dispatch_once_t oneToken;
    dispatch_once(&oneToken,^{
        manager = [[self alloc] init];
        
    });
    return manager;
}




#pragma mark - 获取今天的提醒
- (NSMutableArray *)getRemindArrayWithDate:(NSDate *)date{

    NSString *todayStr = [Utils transformDate:date dateFormatStyle:DateFormatYearMonthDayHourMinute];
    NSString *todayWeekDay = [self weekdayStringFromDate:date];
    NSString *sql = [NSString stringWithFormat:@"select * from ReminderModel a where repeatFlag=0 and strDate='%@' union select * from ReminderModel a where (repeatFlag=3 and repeatValue like '%%%@%%' and strDate<='%@')  order by startTime",todayStr,todayWeekDay,todayStr];
    
    NSMutableArray *todyReminderArray = [NSMutableArray arrayWithArray:[ReminderModel selectDataWithSql:sql]];
    
    
    //今天非工作日
    NSMutableArray *isWorkDayArray = [NSMutableArray arrayWithArray:[CalendarModel selectDataWithSql:[NSString stringWithFormat:@"select *from CalendarModel where date = '%@'",todayStr]]];
    CalendarModel *cModel = [isWorkDayArray lastObject];
    
    NSString *isNotWorkSql = @"";
    if ([cModel.remark isEqualToString:@"1"]) {
        isNotWorkSql = @"select *from ReminderModel where repeatFlag = 1 order by startTime";
    }else{
        isNotWorkSql = @"select *from ReminderModel where repeatFlag = 0 order by startTime";
    }
    
    NSMutableArray *isWorkRemindArray = [NSMutableArray arrayWithArray:[ReminderModel selectDataWithSql:isNotWorkSql]];
    
    RKLog(@"%@",sql);
    NSMutableArray *allDayReminderArray = [NSMutableArray array];
    if (todyReminderArray.count>0 || isWorkRemindArray.count>0) {
        NSMutableArray *newTodyReminderArray = [NSMutableArray array];
        for (int i = 0; i<todyReminderArray.count; i++) {
            
            ReminderModel *model = todyReminderArray[i];
            if (model.isAllday) {
                [allDayReminderArray addObject:model];
            }else{
                [newTodyReminderArray addObject:model];
            }
            
        }
        
        //把非工作日和工作日的加进来
        for (int k = 0; k<isWorkRemindArray.count; k++) {
            
            [newTodyReminderArray addObject:isWorkRemindArray[k]];
        }
        
        NSArray *newArray = [self arraySortDescOrAscWithType:0 sortArray:newTodyReminderArray];
        
        for (int j = 0; j<newArray.count; j++) {
            
            [allDayReminderArray addObject:newArray[j]];
        }
        
    }
    
    return allDayReminderArray;
    
}

#pragma mark - 获取明天的提醒
- (NSMutableArray *)getTomorrowRemindArray{
    
    //明天
    NSString *nextDay = [self GetTomorrowDay:[NSDate date]];
    NSArray *nextDayArray = [ReminderModel selectDataWithSql:[NSString stringWithFormat:@"select * from ReminderModel a where repeatFlag=0 and strDate='%@' union select * from ReminderModel a where (repeatFlag=3 and repeatValue like '%%%@%%'and strDate<='%@') or repeatFlag=2 order by startTime",nextDay,[NSString stringWithFormat:@"%d",[[self weekdayStringFromDate:[NSDate date]] intValue]+1],nextDay]];
    
    
    
    //明天非工作日
    NSMutableArray *isWorkDayArray = [NSMutableArray arrayWithArray:[CalendarModel selectDataWithSql:[NSString stringWithFormat:@"select *from CalendarModel where date = '%@'",nextDay]]];
    CalendarModel *cModel = [isWorkDayArray lastObject];
    NSString *isNotWorkSql = @"";
    if ([cModel.remark isEqualToString:@"1"]) {
        isNotWorkSql = @"select *from ReminderModel where repeatFlag = 1 order by startTime";
    }else{
        isNotWorkSql = @"select *from ReminderModel where repeatFlag = 0 order by startTime";
    }
    
    NSMutableArray *isWorkRemindArray = [NSMutableArray arrayWithArray:[ReminderModel selectDataWithSql:isNotWorkSql]];
    
    NSMutableArray *allDayReminderArray = [NSMutableArray array];
    if (nextDayArray.count>0 || isWorkDayArray.count>0) {
        
        NSMutableArray *newTodyReminderArray = [NSMutableArray array];
        for (int i = 0; i<nextDayArray.count; i++) {
            
            ReminderModel *model = nextDayArray[i];
            if (model.isAllday) {
                [allDayReminderArray addObject:model];
            }else{
                [newTodyReminderArray addObject:model];
            }
            
        }
        
        //把非工作日和工作日的加进来
        for (int k = 0; k<isWorkRemindArray.count; k++) {
            
            [newTodyReminderArray addObject:isWorkRemindArray[k]];
        }
        
        NSArray *newArray = [self arraySortDescOrAscWithType:0 sortArray:newTodyReminderArray];
        
        for (int j = 0; j<newArray.count; j++) {
            
            [allDayReminderArray addObject:newArray[j]];
        }
        
    }
    
    return allDayReminderArray;
    
}

#pragma mark - 获取未来的提醒
- (NSMutableArray *)getFutureRemindArray{
    
    //以后
    NSArray *afterArray = [ReminderModel selectDataWithSql:[NSString stringWithFormat:@"select * from ReminderModel a where strDate>'%@' or repeatFlag <>0",[self GetTomorrowDay:[NSDate date]]]];
    NSMutableArray *allDayReminderArray = [NSMutableArray array];
    if (afterArray.count>0) {
        
        NSMutableArray *newTodyReminderArray = [NSMutableArray array];
        for (int i = 0; i<afterArray.count; i++) {
            
            ReminderModel *model = afterArray[i];
            if (model.isAllday) {
                [allDayReminderArray addObject:model];
            }else{
                [newTodyReminderArray addObject:model];
            }
            
        }
        
        if (newTodyReminderArray.count>0) {
            for (int k = 0; k<newTodyReminderArray.count; k++) {
                [allDayReminderArray addObject:newTodyReminderArray[k]];
            }
        }
    }
    return allDayReminderArray;
}




#pragma mark - 获取所有待办
- (NSMutableArray *)getAllGtasksArray{
    NSArray *remindArray = [GtasksModel findByCriteria:[NSString stringWithFormat:@" WHERE isOpen = '1' and isComplete = '0' order by strDate"]];
    
    NSArray *gtaskArray = [GtasksModel findByCriteria:[NSString stringWithFormat:@" WHERE isOpen = '0' and isComplete = '0' order by appCreatedTime"]];
    
    
    NSMutableArray *todayGtasksArray = [NSMutableArray array];
    
    if (remindArray.count>0 || gtaskArray.count>0) {
        for (int i=0; i<remindArray.count; i++) {
            [todayGtasksArray addObject:remindArray[i]];
            
            GtasksModel *gModel = remindArray[i];
            RKLog(@"%@",gModel.content);
        }
        for (int k=0; k<gtaskArray.count; k++) {
            [todayGtasksArray addObject:gtaskArray[k]];
        }
        
    }

    return todayGtasksArray;
}

#pragma mark - 获取所有待办历史
- (NSMutableArray *)getAllGtasksHistory{
    
    NSArray *array = [GtasksModel findByCriteria:[NSString stringWithFormat:@" WHERE isComplete = '1' order by completeDate"]];
    //遍历时，时间相同的装在同一个数组中，先取_dataArray[0]分一组
    NSMutableArray *gtasksArray = [NSMutableArray array];
    if (array.count>0) {
        
        NSMutableArray *currentArr=[NSMutableArray array];
        [currentArr addObject:array[0]];
        
        [gtasksArray addObject:currentArr];
        
        if(array.count>1)
        {
            for (int i=1;i<array.count;i++)
            {
                //取上一组元素并获取上一组元素的比较日期
                NSMutableArray *preArr=[gtasksArray objectAtIndex:gtasksArray.count-1];
                
                GtasksModel *gModel = [preArr objectAtIndex:0];
                
                NSString *pretime = [gModel.completeDate substringToIndex:8];
                
                //取当前遍历的字典中的日期
                GtasksModel *currentModel = [array objectAtIndex:i];
                
                NSString *time = [currentModel.completeDate substringToIndex:8];
                
                //如果遍历当前字典的日期和上一组元素中日期相同则把当前字典分类到上一组元素中
                if([time isEqualToString:pretime])
                {
                    [currentArr addObject:currentModel];
                }
                //如果当前字典的日期和上一组元素日期不同，则重新开新的一组，把这组放入到分类数组中
                else
                {
                    currentArr=[NSMutableArray array];
                    
                    [currentArr addObject:gModel];
                    
                    [gtasksArray addObject:currentArr];
                    
                }
            }
        }
        
    }
    
    return gtasksArray;
    
}



- (NSArray *)arraySortDescOrAscWithType:(int)type sortArray:(NSArray *)sortArray{
    
    //type:0降序,1升序
    NSArray *result = [sortArray sortedArrayUsingComparator:^NSComparisonResult(id  _Nonnull obj1, id  _Nonnull obj2) {
        
        NSLog(@"%@~%@",obj1,obj2); //3~4 2~1 3~1 3~2
        
        ReminderModel *mode1 = (ReminderModel *)obj1;
        ReminderModel *mode2 = (ReminderModel *)obj1;
        
        if (type==0) {
            return [mode1.startTime compare:mode2.startTime]; //升序
        }else{
            return [mode2.startTime compare:mode1.startTime]; //升序
        }
        
        
    }];
    
    return result;
    
}

- (NSString *)GetTomorrowDay:(NSDate *)aDate {
    NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    NSDateComponents *components = [gregorian components:NSCalendarUnitWeekday | NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay fromDate:aDate];
    [components setDay:([components day]+1)]; NSDate *beginningOfWeek = [gregorian dateFromComponents:components];
    NSDateFormatter *dateday = [[NSDateFormatter alloc] init];
    [dateday setDateFormat:@"yyyyMMdd"];
    return [dateday stringFromDate:beginningOfWeek];
}


- (NSString*)weekdayStringFromDate:(NSDate*)inputDate {
    
    NSArray *weekdays = [NSArray arrayWithObjects: [NSNull null], @"7", @"1", @"2", @"3", @"4", @"5", @"6", nil];
    
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    
    NSTimeZone *timeZone = [[NSTimeZone alloc] initWithName:@"Asia/Shanghai"];
    
    [calendar setTimeZone: timeZone];
    
    NSCalendarUnit calendarUnit = NSCalendarUnitWeekday;
    
    NSDateComponents *theComponents = [calendar components:calendarUnit fromDate:inputDate];
    
    return [weekdays objectAtIndex:theComponents.weekday];
    
}


@end
