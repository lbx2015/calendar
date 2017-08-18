//
//  CalendarModel.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/16.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "CalendarModel.h"

@implementation CalendarModel

+ (NSDictionary *)describeColumnDict{
    
    LKDBColumnDes *account = [LKDBColumnDes new];
    account.primaryKey = YES;
    account.columnName = @"id";
    
    LKDBColumnDes *name = [[LKDBColumnDes alloc] initWithgeneralFieldWithAuto:NO unique:NO isNotNull:YES check:nil defaultVa:nil];
    
    LKDBColumnDes *noField = [LKDBColumnDes new];
    noField.useless = YES;
    
    return @{@"dateId":account,@"name":name,@"noField":noField};
}

@end
