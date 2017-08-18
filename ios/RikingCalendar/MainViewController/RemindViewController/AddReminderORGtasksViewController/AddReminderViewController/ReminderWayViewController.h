//
//  ReminderWayViewController.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/7.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseTabViewController.h"
#import "ReminderModel.h"
@interface ReminderWayViewController : RKBaseTabViewController

@property (nonatomic,assign)BOOL isEdit;

@property (nonatomic,strong)ReminderModel *reminderModel;

@property (nonatomic,copy) void(^reminderWay)(ReminderModel *model);

@end
