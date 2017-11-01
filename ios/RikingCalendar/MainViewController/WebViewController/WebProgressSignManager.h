//
//  WebProgressSignManager.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/22.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface WebProgressSignManager : NSObject

+ (WebProgressSignManager *)shareManager;

@property (nonatomic,strong)NSMutableDictionary *htmlUrlDict;


@end
