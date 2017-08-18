//
//  ReportModel.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/11.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ReportModel.h"

@implementation ReportModel

@end



@implementation ReportlistModel

- (void)setValue:(id)value forUndefinedKey:(NSString *)key{
    
    if ([key isEqualToString:@"id"]) {
        self.reportId = value;
    }
    
}
@end
