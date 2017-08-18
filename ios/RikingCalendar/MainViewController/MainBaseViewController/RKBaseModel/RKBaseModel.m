//
//  RKBaseModel.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/1.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseModel.h"

@implementation RKBaseModel


- (void)setValue:(id)value forUndefinedKey:(NSString *)key  {
    if([key isEqualToString:@"id"]){
        NSLog(@"这是一个id关键字");

    }
       
}

@end
