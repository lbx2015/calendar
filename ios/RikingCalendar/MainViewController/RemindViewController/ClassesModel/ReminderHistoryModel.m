//
//  ReminderHistoryModel.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/7.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ReminderHistoryModel.h"

@implementation ReminderHistoryModel


+ (NSDictionary *)describeColumnDict{
    LKDBColumnDes *account = [LKDBColumnDes new];
    account.primaryKey = YES;
    account.columnName = @"loactionId";
    
    LKDBColumnDes *name = [[LKDBColumnDes alloc] initWithgeneralFieldWithAuto:NO unique:NO isNotNull:YES check:nil defaultVa:nil];
    
    LKDBColumnDes *noField = [LKDBColumnDes new];
    noField.useless = YES;
    
    return @{@"remindHisId":account,@"name":name,@"noField":noField};
}


@end
