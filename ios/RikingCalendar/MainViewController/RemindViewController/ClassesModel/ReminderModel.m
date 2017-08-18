//
//  ReminderModel.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/1.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ReminderModel.h"
#import "LKDBTool.h"
@implementation ReminderModel


+ (NSDictionary *)describeColumnDict{
    LKDBColumnDes *account = [LKDBColumnDes new];
    account.primaryKey = YES;
    account.columnName = @"loactionId";
    
    LKDBColumnDes *name = [[LKDBColumnDes alloc] initWithgeneralFieldWithAuto:NO unique:NO isNotNull:YES check:nil defaultVa:nil];
    
    LKDBColumnDes *noField = [LKDBColumnDes new];
    noField.useless = YES;
    
    return @{@"reminderId":account,@"name":name,@"noField":noField};
}

@end
