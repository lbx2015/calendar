//
//  ReminderDatePickerView.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/27.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ReminderDatePickerView.h"

#define MAXNUM        1000

typedef void(^doneBlock)(NSString *time);

@interface ReminderDatePickerView()

<UIPickerViewDelegate,UIPickerViewDataSource,UIGestureRecognizerDelegate>

{
    //数据源
    NSMutableArray *_dayArray;
    NSMutableArray *_hourArray;
    NSMutableArray *_minuteArray;
    
    
    //记录位置
    NSInteger _dayIndex;
    NSInteger _hourIndex;
    NSInteger _minuteIndex;
}


@property (nonatomic,strong)doneBlock doneBlock;
@property (nonatomic,assign)ReminderDateStyle datePickerStyle;
@property (weak, nonatomic) IBOutlet UIButton *cancelBtn;
@property (weak, nonatomic) IBOutlet UIButton *sureBtn;
@property (weak, nonatomic) IBOutlet UILabel *reminderTimeLabel;
@property (weak, nonatomic) IBOutlet UIView *pickyViewBg;
@property (copy, nonatomic) NSString *didSelectTime;
@property (nonatomic,strong) UIPickerView *reminderPickView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *pickViewBgBottomLayout;

@property (nonatomic,copy) NSString *selectTime;




- (IBAction)showPickView:(id)sender;

- (IBAction)cancelPickView:(id)sender;



@end




@implementation ReminderDatePickerView


-(instancetype)init {
    self = [super init];
    if (self) {
        self = [[[NSBundle mainBundle] loadNibNamed:NSStringFromClass([self class]) owner:self options:nil] lastObject];
    }
    return self;
}

- (void)setDateStyle:(ReminderDateStyle)datePickerStyle selectTime:(NSString *)selectTime CompleteBlock:(void(^)(NSString *time))completeBlock{
    

    self.frame=CGRectMake(0, 0, kScreenWidth, kScreenHeight);
    //点击背景是否影藏
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(dismiss)];
    tap.delegate = self;
    [self addGestureRecognizer:tap];
    self.backgroundColor = RGBA(0, 0, 0, 0);
    self.pickViewBgBottomLayout.constant=-self.frame.size.height;
    [self layoutIfNeeded];
    [[UIApplication sharedApplication].keyWindow bringSubviewToFront:self];
    [self.pickyViewBg addSubview:self.reminderPickView];
    
    self.datePickerStyle = datePickerStyle;
 
    _dayArray = [self setArray:_dayArray];
    _hourArray = [self setArray:_hourArray];
    _minuteArray = [self setArray:_minuteArray];
    
    if (self.reminderTimeLabel.text.length==0) {
        switch (self.datePickerStyle) {
            case ReminderStyleShowMinute:
                self.reminderTimeLabel.text = [NSString stringWithFormat:@"%@1%@",NSLocalizedString(@"advance_remind", nil),NSLocalizedString(@"minute", nil)];//@"提前1分";
                break;
            case ReminderStyleShowHourMinute:
                self.reminderTimeLabel.text = @"提前0时1分";
                break;
            case ReminderStyleShowDayHourMinute:
                self.reminderTimeLabel.text = @"提前0天0时1分";
                break;
            default:
                break;
        }
    }
    
    if (completeBlock) {
        self.doneBlock = ^(NSString *time) {
            completeBlock(time);
        };
    }
    
    for (int i = 0; i<365; i++) {
        
        NSString *num = [NSString stringWithFormat:@"%d",i];
        
        if (i<60) {
            
            if (0<i && i<60) {
                [_minuteArray addObject:[NSString stringWithFormat:@"%@%@",num,NSLocalizedString(@"minute", nil)]];
            }
            if (i<24) {
                [_hourArray addObject:[NSString stringWithFormat:@"%@时",num]];
            }
            
            [_dayArray addObject:[NSString stringWithFormat:@"%@天",num]];
        }
    }
    
    
    self.selectTime = selectTime;
    
}

- (UIPickerView *)reminderPickView {
    if (!_reminderPickView) {
        _reminderPickView = [[UIPickerView alloc] initWithFrame:self.pickyViewBg.bounds];
        _reminderPickView.showsSelectionIndicator = YES;
        _reminderPickView.delegate = self;
        _reminderPickView.dataSource = self;
        _reminderPickView.backgroundColor = [UIColor whiteColor];
    }
    return _reminderPickView;
}


- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    
    switch (self.datePickerStyle) {
        case ReminderStyleShowMinute:
            return 1;
            break;
        case ReminderStyleShowHourMinute:
            return 2;
            break;
        case ReminderStyleShowDayHourMinute:
            return 3;
            break;
        default:
            return 0;
            break;
    }
    
}


- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    
    switch (self.datePickerStyle) {
        case ReminderStyleShowMinute:
            return _minuteArray.count*MAXNUM;
            break;
        case ReminderStyleShowHourMinute:
            if (component==0) {
                return _minuteArray.count*MAXNUM;
            }else{
                return _hourArray.count*MAXNUM;
            }
            
        case ReminderStyleShowDayHourMinute:
            if (component==0) {
                return _minuteArray.count*MAXNUM;
            }else if (component==1){
                return _hourArray.count*MAXNUM;
            }else{
                return _dayArray.count*MAXNUM;
            }
            
        default:
            return 0;
            break;
    }
    
    
}

-(CGFloat)pickerView:(UIPickerView *)pickerView rowHeightForComponent:(NSInteger)component {
    return 35;
}

- (CGFloat)pickerView:(UIPickerView *)pickerView widthForComponent:(NSInteger)component{
    
    return [UIScreen mainScreen].bounds.size.width/3;
}


-(UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view {
    
    
    for(UIView *singleLine in pickerView.subviews)
    {
        if (singleLine.frame.size.height < 1)
        {
            singleLine.backgroundColor = dt_line_color;
        }
        
    }
    
    UILabel *customLabel = (UILabel *)view;
    if (!customLabel) {
        customLabel = [[UILabel alloc] init];
        customLabel.textAlignment = NSTextAlignmentCenter;
        [customLabel setFont:[UIFont systemFontOfSize:19]];
    }
    
    NSString *title;
    
    switch (self.datePickerStyle) {
        case ReminderStyleShowMinute:
            title = _minuteArray[row%_minuteArray.count];
            break;
        case ReminderStyleShowHourMinute:
            if (component==0) {
                title = _minuteArray[row%_minuteArray.count];
            }else{
                title = _hourArray[row%_hourArray.count];
            }
            
        case ReminderStyleShowDayHourMinute:
            if (component==0) {
                title = _minuteArray[row%_minuteArray.count];
            }else if (component==1){
                title = _hourArray[row%_minuteArray.count];
            }else{
                title = _dayArray[row%_minuteArray.count];
            }
            
        default:
            title = @"";
            break;
    }
    
    customLabel.text = title;
    customLabel.font = [UIFont systemFontOfSize:19];
    return customLabel;
    
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    
    NSString *timeStr = @"";
    
    switch (self.datePickerStyle) {
        case ReminderStyleShowMinute:
            timeStr = _minuteArray[row%_minuteArray.count];
            break;
        case ReminderStyleShowHourMinute:
            if (component==0) {
                _hourIndex = row%_hourArray.count;
            }else{
                _minuteIndex = row%_minuteArray.count;
            }
            timeStr = [NSString stringWithFormat:@"%@%@",_hourArray[_hourIndex],_minuteArray[_minuteIndex]];
        case ReminderStyleShowDayHourMinute:
            if (component==0) {
                _dayIndex = row%_dayArray.count;
            }else if (component==1){
                _hourIndex = row%_hourArray.count;
            }else{
                _minuteIndex = row%_minuteArray.count;
            }
            
            timeStr = [NSString stringWithFormat:@"%@%@%@",_dayArray[_dayIndex],_hourArray[_hourIndex],_minuteArray[_minuteIndex]];
            
        default:
            
            break;
    }
    
    self.reminderTimeLabel.text = [NSString stringWithFormat:@"%@%@",NSLocalizedString(@"advance_remind", nil),timeStr];
    _didSelectTime = timeStr;
}


- (void)setSelectTime:(NSString *)selectTime{
    
    _selectTime = selectTime;
    
    switch (self.datePickerStyle) {
        case ReminderStyleShowMinute:
            
            if ([_selectTime intValue]>0) {
                _didSelectTime = [NSString stringWithFormat:@"%@%@",_selectTime,NSLocalizedString(@"minute", nil)];
            }else{
                _didSelectTime = [NSString stringWithFormat:@"1%@",NSLocalizedString(@"minute", nil)];
            }
            
            [self.reminderPickView selectRow:[_selectTime intValue]>0?([_selectTime intValue]-1)+_minuteArray.count*(MAXNUM/2) : [_selectTime intValue]+_minuteArray.count*(MAXNUM/2) inComponent:0 animated:NO];
            break;
        case ReminderStyleShowHourMinute:
            break;
        case ReminderStyleShowDayHourMinute:
            break;
        default:
            
            break;
    }
}

#pragma mark - Action
-(void)show {
    
    [[UIApplication sharedApplication].keyWindow addSubview:self];
    [UIView animateWithDuration:.3 animations:^{
        self.backgroundColor = RGBA(0, 0, 0, 0.4);
        self.pickViewBgBottomLayout.constant=0;
        [self layoutIfNeeded];
        
    }];
}
-(void)dismiss {
    [UIView animateWithDuration:.3 animations:^{
        self.backgroundColor = RGBA(0, 0, 0, 0);
        self.pickViewBgBottomLayout.constant = -self.frame.size.height;
        [self layoutIfNeeded];
    } completion:^(BOOL finished) {
        [self.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
        [self removeFromSuperview];
    }];
}




- (NSMutableArray *)setArray:(id)mutableArray
{
    if (mutableArray)
        [mutableArray removeAllObjects];
    else
        mutableArray = [NSMutableArray array];
    return mutableArray;
}

#pragma mark - UIGestureRecognizerDelegate
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if( [touch.view isDescendantOfView:self.pickyViewBg]) {
        return NO;
    }
    return YES;
}
- (IBAction)showPickView:(id)sender {
    self.doneBlock([_didSelectTime stringByReplacingOccurrencesOfString:[NSString stringWithFormat:@"%@",NSLocalizedString(@"minute", nil)] withString:@""]);
    [self dismiss];
}

- (IBAction)cancelPickView:(id)sender {
    
    [self dismiss];
}
@end
