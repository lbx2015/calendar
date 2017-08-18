//
//  NSCalendar+SFHelper.h
//  NMWStarter
//
//  Created by 陈少华 on 2017/1/6.
//  Copyright © 2017年 juqitech. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSCalendar (SFHelper)

- (BOOL)isDate:(NSDate *)date1 equalToDate:(NSDate *)date2 toCalendarUnit:(NSCalendarUnit)unit;

- (NSDate *)beginingOfMonthOfDate:(NSDate *)date;

- (NSDate *)endOfMonthOfDate:(NSDate *)date;

- (NSDate *)beginingOfWeekOfDate:(NSDate *)date;

- (NSDate *)endOfWeekOfDate:(NSDate *)date;

- (NSDate *)middleOfWeekFromDate:(NSDate *)date;

- (NSInteger)yearOfDate:(NSDate *)date;

- (NSInteger)monthOfDate:(NSDate *)date;

- (NSInteger)dayOfDate:(NSDate *)date;

- (NSInteger)weekdayOfDate:(NSDate *)date;

- (NSInteger)weekOfDate:(NSDate *)date;

- (NSDate *)dateByIgnoringTimeComponentsOfDate:(NSDate *)date;

- (NSDate *)tomorrowOfDate:(NSDate *)date;

- (NSDate *)yesterdayOfDate:(NSDate *)date;

- (NSDate *)dateWithYear:(NSInteger)year month:(NSInteger)month day:(NSInteger)day;

- (NSDate *)dateByAddingYears:(NSInteger)years toDate:(NSDate *)date;

- (NSDate *)dateBySubstractingYears:(NSInteger)years fromDate:(NSDate *)date;

- (NSDate *)dateByAddingMonths:(NSInteger)months toDate:(NSDate *)date;

- (NSDate *)dateBySubstractingMonths:(NSInteger)months fromDate:(NSDate *)date;

- (NSDate *)dateByAddingWeeks:(NSInteger)weeks toDate:(NSDate *)date;

- (NSDate *)dateBySubstractingWeeks:(NSInteger)weeks fromDate:(NSDate *)date;

- (NSDate *)dateByAddingDays:(NSInteger)days toDate:(NSDate *)date;

- (NSDate *)dateBySubstractingDays:(NSInteger)days fromDate:(NSDate *)date;

- (NSInteger)yearsFromDate:(NSDate *)fromDate toDate:(NSDate *)toDate;

- (NSInteger)monthsFromDate:(NSDate *)fromDate toDate:(NSDate *)toDate;

- (NSInteger)weeksFromDate:(NSDate *)fromDate toDate:(NSDate *)toDate;

- (NSInteger)daysFromDate:(NSDate *)fromDate toDate:(NSDate *)toDate;

@end
