//
//  NSCalendar+SFHelper.m
//  NMWStarter
//
//  Created by 陈少华 on 2017/1/6.
//  Copyright © 2017年 juqitech. All rights reserved.
//

#import "NSCalendar+SFHelper.h"

@implementation NSCalendar (SFHelper)

- (BOOL)isDate:(NSDate *)date1 equalToDate:(NSDate *)date2 toCalendarUnit:(NSCalendarUnit)unit
{
    switch (unit) {
        case NSCalendarUnitMonth:
            return [self isDate:date1 equalToDate:date2 toUnitGranularity:NSCalendarUnitMonth];
            break;
        case NSCalendarUnitWeekOfYear:
            return [self isDate:date1 equalToDate:date2 toUnitGranularity:NSCalendarUnitYear];
            break;
        case NSCalendarUnitDay:
            return [self isDate:date1 inSameDayAsDate:date2];
            break;
        default:
            return NO;
    }

    return NO;
}

- (NSDate *)beginingOfMonthOfDate:(NSDate *)date
{
    if (!date) return nil;
    NSDateComponents *components = [self components:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay|NSCalendarUnitHour fromDate:date];
    components.day = 1;
    return [self dateFromComponents:components];
}

- (NSDate *)endOfMonthOfDate:(NSDate *)date
{
    if (!date) return nil;
    NSDateComponents *components = [self components:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay|NSCalendarUnitHour fromDate:date];
    components.month++;
    components.day = 0;
    return [self dateFromComponents:components];
}

- (NSDate *)beginingOfWeekOfDate:(NSDate *)date
{
    if (!date) return nil;
    NSDateComponents *weekdayComponents = [self components:NSCalendarUnitWeekday fromDate:date];
    NSDateComponents *components = [NSDateComponents new];
    components.day = - (weekdayComponents.weekday - self.firstWeekday);
    components.day = (components.day-7) % 7;
    NSDate *beginningOfWeek = [self dateByAddingComponents:components toDate:date options:0];
    beginningOfWeek = [self dateByIgnoringTimeComponentsOfDate:beginningOfWeek];
    components.day = NSIntegerMax;
    return beginningOfWeek;
}

- (NSDate *)endOfWeekOfDate:(NSDate *)date
{
    if (!date) return nil;
    NSDateComponents *weekdayComponents = [self components:NSCalendarUnitWeekday fromDate:date];
    NSDateComponents *components = [NSDateComponents new];
    components.day = - (weekdayComponents.weekday - self.firstWeekday);
    components.day = (components.day-7) % 7 + 6;
    NSDate *endOfWeek = [self dateByAddingComponents:components toDate:date options:0];
    endOfWeek = [self dateByIgnoringTimeComponentsOfDate:endOfWeek];
    components.day = NSIntegerMax;
    return endOfWeek;
}

- (NSDate *)middleOfWeekFromDate:(NSDate *)date
{
    if (!date) return nil;
    NSDateComponents *weekdayComponents = [self components:NSCalendarUnitWeekday fromDate:date];
    NSDateComponents *componentsToSubtract = [NSDateComponents new];
    componentsToSubtract.day = - (weekdayComponents.weekday - self.firstWeekday) + 3;
    NSDate *middleOfWeek = [self dateByAddingComponents:componentsToSubtract toDate:date options:0];
    NSDateComponents *components = [self components:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay|NSCalendarUnitHour fromDate:middleOfWeek];
    middleOfWeek = [self dateFromComponents:components];
    componentsToSubtract.day = NSIntegerMax;
    return middleOfWeek;
}

- (NSInteger)yearOfDate:(NSDate *)date
{
    if (!date) return NSNotFound;
    NSDateComponents *component = [self components:NSCalendarUnitYear fromDate:date];
    return component.year;
}

- (NSInteger)monthOfDate:(NSDate *)date
{
    if (!date) return NSNotFound;
    NSDateComponents *component = [self components:NSCalendarUnitMonth
                                                    fromDate:date];
    return component.month;
}

- (NSInteger)dayOfDate:(NSDate *)date
{
    if (!date) return NSNotFound;
    NSDateComponents *component = [self components:NSCalendarUnitDay
                                                    fromDate:date];
    return component.day;
}

- (NSInteger)weekdayOfDate:(NSDate *)date
{
    if (!date) return NSNotFound;
    NSDateComponents *component = [self components:NSCalendarUnitWeekday fromDate:date];
    return component.weekday;
}

- (NSInteger)weekOfDate:(NSDate *)date
{
    if (!date) return NSNotFound;
    NSDateComponents *component = [self components:NSCalendarUnitWeekOfYear fromDate:date];
    return component.weekOfYear;
}

- (NSDate *)dateByIgnoringTimeComponentsOfDate:(NSDate *)date
{
    if (!date) return nil;
    NSDateComponents *components = [self components:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay fromDate:date];
    return [self dateFromComponents:components];
}

- (NSDate *)tomorrowOfDate:(NSDate *)date
{
    if (!date) return nil;
    NSDateComponents *components = [self components:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay|NSCalendarUnitHour fromDate:date];
    components.day++;
    components.hour = 0;
    return [self dateFromComponents:components];
}

- (NSDate *)yesterdayOfDate:(NSDate *)date
{
    if (!date) return nil;
    NSDateComponents *components = [self components:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay|NSCalendarUnitHour fromDate:date];
    components.day--;
    components.hour = 0;
    return [self dateFromComponents:components];
}

- (NSDate *)dateWithYear:(NSInteger)year month:(NSInteger)month day:(NSInteger)day
{
    NSDateComponents *components = [NSDateComponents new];
    components.year = year;
    components.month = month;
    components.day = day;
    components.hour = 0;
    NSDate *date = [self dateFromComponents:components];
    components.year = NSIntegerMax;
    components.month = NSIntegerMax;
    components.day = NSIntegerMax;
    components.hour = NSIntegerMax;
    return date;
}

- (NSDate *)dateByAddingYears:(NSInteger)years toDate:(NSDate *)date
{
    if (!date) return nil;
    NSDateComponents *components = [NSDateComponents new];
    components.year = years;
    NSDate *d = [self dateByAddingComponents:components toDate:date options:0];
    components.year = NSIntegerMax;
    return d;
}

- (NSDate *)dateBySubstractingYears:(NSInteger)years fromDate:(NSDate *)date
{
    if (!date) return nil;
    return [self dateByAddingYears:-years toDate:date];
}

- (NSDate *)dateByAddingMonths:(NSInteger)months toDate:(NSDate *)date
{
    if (!date) return nil;
    NSDateComponents *components = [NSDateComponents new];
    components.month = months;
    NSDate *d = [self dateByAddingComponents:components toDate:date options:0];
    components.month = NSIntegerMax;
    return d;
}

- (NSDate *)dateBySubstractingMonths:(NSInteger)months fromDate:(NSDate *)date
{
    return [self dateByAddingMonths:-months toDate:date];
}

- (NSDate *)dateByAddingWeeks:(NSInteger)weeks toDate:(NSDate *)date
{
    if (!date) return nil;
    NSDateComponents *components = [NSDateComponents new];
    components.weekOfYear = weeks;
    NSDate *d = [self dateByAddingComponents:components toDate:date options:0];
    components.weekOfYear = NSIntegerMax;
    return d;
}

- (NSDate *)dateBySubstractingWeeks:(NSInteger)weeks fromDate:(NSDate *)date
{
    return [self dateByAddingWeeks:-weeks toDate:date];
}

- (NSDate *)dateByAddingDays:(NSInteger)days toDate:(NSDate *)date
{
    if (!date) return nil;
    NSDateComponents *components = [NSDateComponents new];
    components.day = days;
    NSDate *d = [self dateByAddingComponents:components toDate:date options:0];
    components.day = NSIntegerMax;
    return d;
}

- (NSDate *)dateBySubstractingDays:(NSInteger)days fromDate:(NSDate *)date
{
    return [self dateByAddingDays:-days toDate:date];
}

- (NSInteger)yearsFromDate:(NSDate *)fromDate toDate:(NSDate *)toDate
{
    NSDateComponents *components = [self components:NSCalendarUnitYear
                                                     fromDate:fromDate
                                                       toDate:toDate
                                                      options:0];
    return components.year;
}

- (NSInteger)monthsFromDate:(NSDate *)fromDate toDate:(NSDate *)toDate
{
    NSDateComponents *components = [self components:NSCalendarUnitMonth
                                                     fromDate:fromDate
                                                       toDate:toDate
                                                      options:0];
    return components.month;
}

- (NSInteger)weeksFromDate:(NSDate *)fromDate toDate:(NSDate *)toDate
{
    NSDateComponents *components = [self components:NSCalendarUnitWeekOfYear
                                                     fromDate:fromDate
                                                       toDate:toDate
                                                      options:0];
    return components.weekOfYear;
}

- (NSInteger)daysFromDate:(NSDate *)fromDate toDate:(NSDate *)toDate
{
    NSDateComponents *components = [self components:NSCalendarUnitDay
                                                     fromDate:fromDate
                                                       toDate:toDate
                                                      options:0];
    return components.day;
}

@end
