//
//  ReminderDatePickerView.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/27.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <UIKit/UIKit.h>




typedef enum{
    ReminderStyleShowMinute  = 0,
    ReminderStyleShowHourMinute,
    ReminderStyleShowDayHourMinute
    
}ReminderDateStyle;

@interface ReminderDatePickerView : UIView

- (void)setDateStyle:(ReminderDateStyle)datePickerStyle CompleteBlock:(void(^)(NSString *time))completeBlock;

-(void)show;

@end
