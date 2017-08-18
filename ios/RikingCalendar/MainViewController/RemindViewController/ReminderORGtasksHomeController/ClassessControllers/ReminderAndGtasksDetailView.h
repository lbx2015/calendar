//
//  ReminderAndGtasksDetailView.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/8.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <UIKit/UIKit.h>



@interface ReminderAndGtasksDetailView : UIView

- (void)setDetailViewWithModel:(RKBaseModel *)model type:(int)type clickBtnType:(void(^)(int buttonType))clickBtnType;//type:1:提醒;2:待办

//显示
-(void)show;
@end
