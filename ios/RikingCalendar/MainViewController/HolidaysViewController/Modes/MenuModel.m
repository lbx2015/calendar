//
//  MenuModel.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/9.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "MenuModel.h"

@implementation MenuModel

- (void)setValue:(id)value forUndefinedKey:(NSString *)key  {
    if([key isEqualToString:@"id"]){
        
        self.menuId = value;
        NSLog(@"这是一个id关键字");
    }
    
}

@end

@implementation CriteriaModel

- (void)setValue:(id)value forUndefinedKey:(NSString *)key  {
    if([key isEqualToString:@"id"]){
        
        NSLog(@"这是一个id关键字");
    }
    
}

@end
