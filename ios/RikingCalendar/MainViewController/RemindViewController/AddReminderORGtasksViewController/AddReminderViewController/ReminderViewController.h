//
//  ReminderViewController.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/20.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseViewController.h"
#import "ReminderModel.h"
@interface ReminderViewController : RKBaseViewController

@property (nonatomic,assign)BOOL isEdit;

@property (nonatomic,strong)ReminderModel *reminderMode;

@property (nonatomic,strong)void(^updateReminder)();

/**
 保存提醒
 */
- (void)doSaveReminder;

@end
