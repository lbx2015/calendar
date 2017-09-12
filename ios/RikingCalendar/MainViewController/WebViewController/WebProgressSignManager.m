//
//  WebProgressSignManager.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/22.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "WebProgressSignManager.h"
static WebProgressSignManager *manager = nil;
@implementation WebProgressSignManager



+ (WebProgressSignManager *)shareManager
{
    static dispatch_once_t oneToken;
    dispatch_once(&oneToken,^{
        manager = [[self alloc] init];
        manager.htmlUrlDict = [NSMutableDictionary dictionary];
    });
    return manager;
}
@end
