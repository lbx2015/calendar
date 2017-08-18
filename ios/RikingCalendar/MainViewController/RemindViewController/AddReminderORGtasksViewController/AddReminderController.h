//
//  AddReminderController.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/20.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RKBaseViewController.h"
@interface AddReminderController : RKBaseViewController

@property (nonatomic, assign)NSInteger selectIndex;

@property (nonatomic, copy)void(^editSelectIndex)(NSInteger index);

@property (nonatomic, copy)void(^updateData)();

@end
