//
//  GtaskHistoryViewController.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/3.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseTabViewController.h"

@interface GtaskHistoryViewController : RKBaseTabViewController


@property (nonatomic,copy)void(^editGtask)(BOOL isEdit);

@end
