//
//  WSDatePickerView.h
//  WSDatePicker
//
//  Created by iMac on 17/2/23.
//  Copyright © 2017年 zws. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NSDate+Extension.h"

typedef enum{
    DateStyleShowYearMonthDayHourMinute  = 0,
    DateStyleShowMonthDayHourMinute,
    DateStyleShowYearMonthDay,
    DateStyleShowMonthDay,
    DateStyleShowHourMinute,
    DateStyleShowMonthOrShowYearMonthDay,
    DateStyleShowYear
}WSDateStyle;


@interface WSDatePickerView : UIView

@property (nonatomic,strong)UIColor *doneButtonColor;//按钮颜色
@property (nonatomic,assign)BOOL isShouWeek;//是佛显示周;
@property (nonatomic, retain) NSDate *scrollToDate;//滚到指定日期
@property (nonatomic, retain) NSDate *maxLimitDate;//限制最大时间（没有设置默认9999）
@property (nonatomic, retain) NSDate *minLimitDate;//限制最小时间（没有设置默认0）
@property (weak, nonatomic) IBOutlet UIButton *Cancel;
@property (weak, nonatomic) IBOutlet UIButton *SureBtn;
@property (nonatomic,strong) void(^disMiss)();


- (instancetype)init;

- (void)setDateStyle:(WSDateStyle)datePickerStyle CompleteBlock:(void(^)(NSDate *))completeBlock;

- (void)setDateStyle:(WSDateStyle)datePickerStyle isMonth:(BOOL)isMonth completeBlock:(void(^)(NSDate *date,BOOL isMonth))completeBlock;

- (void)show;

-(void)dismiss;

@end

