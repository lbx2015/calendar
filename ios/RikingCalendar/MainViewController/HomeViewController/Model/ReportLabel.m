//
//  ReportLabel.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/11.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ReportLabel.h"

@implementation ReportLabel

+ (NSDictionary *)describeColumnDict{
    
    LKDBColumnDes *account = [LKDBColumnDes new];
    account.primaryKey = YES;
    account.columnName = @"id";
    
    LKDBColumnDes *name = [[LKDBColumnDes alloc] initWithgeneralFieldWithAuto:NO unique:NO isNotNull:YES check:nil defaultVa:nil];
    
    LKDBColumnDes *noField = [LKDBColumnDes new];
    noField.useless = YES;
    
    return @{@"reportId":account,@"name":name,@"noField":noField};
}

@end
