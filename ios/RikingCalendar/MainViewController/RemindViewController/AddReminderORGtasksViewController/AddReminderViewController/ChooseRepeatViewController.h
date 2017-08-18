//
//  ChooseRepeatViewController.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/26.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseTabViewController.h"
#import "ReminderModel.h"
//typedef  void(^chooseRepeatTime)(NSString * repeatTime);

@interface ChooseRepeatViewController : RKBaseTabViewController

@property (nonatomic,copy)NSString *repeatTimeStr;

@property (nonatomic,strong)ReminderModel *reminderModel;

@property (nonatomic,copy)void(^chooseRepeatTime)(NSString * repeatTime);


//- (void)chooseRepeatTime:(void(^)(NSString *time))time;

@end
