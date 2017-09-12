//
//  ReminderViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/20.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ReminderViewController.h"
#import "WSDatePickerView.h"
#import "ChooseRepeatViewController.h"
#import "ReminderDatePickerView.h"
#import "ReminderModel.h"
#import "MJExtension.h"
#import "ReminderWayViewController.h"
#import <UserNotifications/UserNotifications.h>
#define MAX_LIMIT_NUMS      10000000

#define spaceHeight         50

@interface ReminderViewController ()
<UITextViewDelegate>
{
    ReminderModel *_rModel;
    BOOL _isShow;
}
@property (nonatomic, strong) IQTextView *reminderTxetView;
@property (nonatomic, strong) UIView *textViewBg;
@property (nonatomic, strong) UIView *chooseTime;
@property (nonatomic, strong) UILabel *chooseTimeLabel;
@property (nonatomic, strong) UILabel *reminderLabel;//提醒方式
@property (nonatomic, strong) UILabel *repeatBtnLabel;
@property (nonatomic, strong) UIAlertView *showAllDayTime;
@end

@implementation ReminderViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self setRightButtonWithTitle:@[NSLocalizedString(@"done", nil)]];
    NSString *languageName = [[[NSUserDefaults standardUserDefaults] objectForKey:@"AppleLanguages"] objectAtIndex:0];
    RKLog(@"%@",languageName);
    
    [self initData];
    [self createMainUI];
    
}

- (void)initData{
    
    _rModel = [[ReminderModel alloc]init];
    if (_isEdit) {
        _rModel  = self.reminderMode;
    }else{
        _rModel.content = @"";
        _rModel.isAllday = NO;
        _rModel.repeatFlag = 0;
        _rModel.repeatValue = @"0";
        _rModel.isRemind = 1;
        _rModel.strDate = [Utils getCurrentTimeWithDateStyle:DateFormatYearMonthDay01];
        _rModel.startTime = [Utils getCurrentTimeWithDateStyle:DateFormatHourMinute01];
    }

}


#pragma mark - 创建主UI
- (void)createMainUI{
    //提醒内容
    self.textViewBg = [[UIView alloc]init];
    self.textViewBg.layer.cornerRadius = 5;
    self.textViewBg.layer.borderWidth = 1;
    self.textViewBg.layer.borderColor = [UIColor hex_colorWithHex:@"#d9d9d9"].CGColor;
    [self setViewShadowWithView:self.textViewBg];
    [self.view addSubview:self.textViewBg];
    
    __weak typeof (self) weakSelf = self;
    
    @KKWeak(self);
    [self.textViewBg mas_makeConstraints:^(MASConstraintMaker *make) {
        @KKStrong(self);
        //        make.edges.mas_equalTo(UIEdgeInsetsMake(15, 5, 0, 5));
        //        make.edges.mas_equalTo(UIEdgeInsetsMake(15, 5, 0, 5));
        //        make.edges.equalTo(self.view).with.insets(UIEdgeInsetsMake(15, 5, 0, 5));
        
        make.top.mas_equalTo(self.view).offset(15);
        make.left.mas_equalTo(self.view).offset(5);
        make.right.mas_equalTo(self.view).offset(-5);
        make.height.mas_equalTo(spaceHeight);
        
    }];
    
    [self.textViewBg mas_updateConstraints:^(MASConstraintMaker *make) {
        
    }];
    
    self.reminderTxetView = [[IQTextView alloc]init];
    self.reminderTxetView.placeholder = NSLocalizedString(@"remind_placeholder", nil);
    self.reminderTxetView.font = threeClassTextFont;
    self.reminderTxetView.delegate = self;
    self.reminderTxetView.text = _rModel.content;
    [self.textViewBg addSubview:self.reminderTxetView];
    
    [self.reminderTxetView mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.edges.mas_equalTo(UIEdgeInsetsMake(5, 5, 5, 5));
        
    }];
    
    
    
    self.chooseTime = [[UIView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(self.textViewBg.frame)+15, kScreenWidth, 100.5)];
    [self setViewShadowWithView:self.chooseTime];
    [self.view addSubview:self.chooseTime];
    
    //    //提醒时间
    UIButton *btn = [self createButtonFrame:CGRectMake(15, 0, 29, spaceHeight) normalImage:@"programme_chooseTime" selectImage:nil isBackgroundImage:NO title:nil target:self action:@selector(chooseTimeAction)];
    [self.chooseTime addSubview:btn];
    
    self.chooseTimeLabel = [self createLabelFrame:CGRectMake(CGRectGetMaxX(btn.frame)+15, 0, kScreenWidth-CGRectGetMaxX(btn.frame)-15-25, spaceHeight) text:nil font:threeClassTextFont textColor:[UIColor hex_colorWithHex:@"#323232"] textAlignment:NSTextAlignmentLeft];
    
    
    if (!_rModel.isAllday) {
        self.chooseTimeLabel.text = [Utils transformDate:[NSString stringWithFormat:@"%@%@",_rModel.strDate,_rModel.startTime] dateFormatStyle:DateFormatYearMonthDayHourMinute1];
    }else{
        self.chooseTimeLabel.text = [Utils transformDate:_rModel.strDate dateFormatStyle:DateFormatYearMonthDayWithChinese];
    }
    self.chooseTimeLabel.userInteractionEnabled = YES;
    [self.chooseTimeLabel addGestureRecognizer: [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(chooseTimeAction)]];
    [self.chooseTime addSubview:self.chooseTimeLabel];
    
    UIButton *btn01 = [self createButtonFrame:CGRectMake(CGRectGetMaxX(self.chooseTimeLabel.frame)+5, 0, kScreenWidth-15-9, spaceHeight) normalImage:@"btn_normalImage_next" selectImage:nil isBackgroundImage:NO title:nil target:self action:@selector(chooseTimeAction)];
    [self.chooseTime addSubview:btn01];
    
    UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(CGRectGetMaxX(btn.frame)+15, CGRectGetMaxY(btn.frame), kScreenWidth-CGRectGetMaxX(btn.frame)+15, 0.5)];
    lineView.themeMap = @{kThemeMapKeyColorName : line_lightgrey_color};
    [self.chooseTime addSubview:lineView];
    
    [self.chooseTime mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.top.equalTo(weakSelf.textViewBg.mas_bottom).offset(15);
        
        //        //设置宽度(1)
        //        make.left.equalTo(weakSelf.view).offset(0);
        //        make.right.equalTo(weakSelf.view).offset(0);
        //        //设置宽度(2)
        //        make.width.equalTo(weakSelf.view).multipliedBy(0);
        //设置宽度(3)
        make.width.equalTo(weakSelf.view);
        
        make.height.mas_equalTo(100.5);
        
    }];
    
    
    [btn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(weakSelf.chooseTime).offset(15);
        make.top.equalTo(weakSelf.chooseTime).offset(0);
        make.width.mas_equalTo(btn.imageView.image.size.width);
        make.height.mas_equalTo(spaceHeight);
    }];
    
    [self.chooseTimeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.left.equalTo(btn.mas_right).offset(15);
        make.top.equalTo(weakSelf.chooseTime).offset(0);
        make.right.mas_equalTo(-24);
        make.height.mas_equalTo(spaceHeight);
        
    }];
    
    [btn01 mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.top.equalTo(weakSelf.chooseTime).offset(0);
        make.right.equalTo(weakSelf.chooseTime).offset(-15);
        make.height.mas_equalTo(spaceHeight);
        make.width.mas_equalTo(btn01.imageView.image.size.width);
    }];
    
    
    [lineView mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.top.equalTo(weakSelf.chooseTime).offset(spaceHeight);
        make.left.equalTo(btn.mas_right).offset(15);
        make.right.equalTo(weakSelf.chooseTime).offset(0);
        make.height.mas_equalTo(0.5);
        
    }];
    
    
    
    UIButton *allDayBtn = [self createButtonFrame:CGRectMake(0, 0, 0, 0) normalImage:@"programme_allday" selectImage:nil isBackgroundImage:NO title:nil target:self action:@selector(chooseAllDay)];
    
    [self.chooseTime addSubview:allDayBtn];
    
    
    //全天
    UILabel *chooseAllDay = [self createLabelFrame:CGRectMake(CGRectGetMaxX(btn.frame)+15, 0, kScreenWidth-CGRectGetMaxX(btn.frame)-15-25, spaceHeight) text:NSLocalizedString(@"remind_allDay", nil) font:threeClassTextFont textColor:[UIColor hex_colorWithHex:@"#323232"] textAlignment:NSTextAlignmentLeft];
    chooseAllDay.userInteractionEnabled = YES;
    [chooseAllDay addGestureRecognizer: [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(chooseAllDay)]];
    [self.chooseTime addSubview:chooseAllDay];
    
    //开关,默认大小51x31
    UISwitch *switchView = [[UISwitch alloc]init];
    switchView.on = _rModel.isAllday;
    [switchView addTarget:self action:@selector(chooseAllDay) forControlEvents:UIControlEventValueChanged]; // 开关事件切换通知
    [switchView setOnTintColor:[UIColor hex_colorWithHex:@"#29a1f7"]];
    [self.chooseTime addSubview: switchView];
    
    
    [allDayBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.left.equalTo(weakSelf.chooseTime).offset(15);
        make.top.equalTo(lineView.mas_bottom).offset(0);
        make.width.mas_equalTo(allDayBtn.imageView.image.size.width);
        make.height.mas_equalTo(spaceHeight);
        
    }];
    
    
    [chooseAllDay mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.left.equalTo(allDayBtn.mas_right).offset(15);
        make.top.equalTo(lineView.mas_bottom).offset(0);
        make.height.mas_equalTo(spaceHeight);
        make.right.mas_equalTo(60);
        
    }];
    
    [switchView mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.top.equalTo(lineView.mas_bottom).offset(9.5);
        make.right.equalTo(weakSelf.chooseTime).offset(-15);
        make.height.mas_equalTo(spaceHeight);
        make.width.mas_equalTo(51);
        
    }];
    
    
    
    //是否重复
    //背景
    UIView *repeatView = [[UIView alloc]init];
    [self setViewShadowWithView:repeatView];
    [self.view addSubview:repeatView];
    

    //提醒方式图片
    UIButton *reminderBtn = [self createButtonFrame:CGRectMake(15, 0, 29, spaceHeight) normalImage:@"remind_way" selectImage:nil isBackgroundImage:NO title:nil target:self action:@selector(reminderTime)];
    [repeatView addSubview:reminderBtn];
    
    
    //提醒时间
    self.reminderLabel = [self createLabelFrame:CGRectMake(0, 0, 20, 30) text:@"" font:threeClassTextFont textColor:[UIColor hex_colorWithHex:@"#323232"] textAlignment:NSTextAlignmentLeft];
    self.reminderLabel.userInteractionEnabled = YES;
    [self.reminderLabel addGestureRecognizer: [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(reminderTime)]];
    [repeatView addSubview:self.reminderLabel];
    
    if (_rModel.isRemind==0) {
        self.reminderLabel.text = NSLocalizedString(@"not_remind", nil);
    }else{
        self.reminderLabel.text = _rModel.beforeTime>0?[NSString stringWithFormat:@"%@%d%@",NSLocalizedString(@"advance_remind", nil),_rModel.beforeTime,NSLocalizedString(@"minute", nil)] : NSLocalizedString(@"punctual_remind", nil);
    }
    
    //向右箭头
    UIButton *reminderNextBtn = [self createButtonFrame:CGRectMake(0, 0, 30, 30) normalImage:@"btn_normalImage_next" selectImage:nil isBackgroundImage:NO title:nil target:self action:nil];
    [repeatView addSubview:reminderNextBtn];
    
    
    //分割线
    UIView *reminderLineView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 30, 1)];
    reminderLineView.themeMap = @{kThemeMapKeyColorName : line_lightgrey_color};
    [repeatView addSubview:reminderLineView];
    
    
    UIButton *repeatBtn = [self createButtonFrame:CGRectMake(0, 0, 20, 30) normalImage:@"programme_ repeat" selectImage:nil isBackgroundImage:NO title:nil target:self action:@selector(chooseRepeat)];
    [repeatView addSubview:repeatBtn];
    
    
    self.repeatBtnLabel = [self createLabelFrame:CGRectMake(0, 0, 0, 0) text:[self getRepeatString:_rModel.repeatFlag repeat_value:_rModel.repeatValue] font:threeClassTextFont textColor:[UIColor hex_colorWithHex:@"#323232"] textAlignment:NSTextAlignmentLeft];
    self.repeatBtnLabel.userInteractionEnabled = YES;
    [self.repeatBtnLabel addGestureRecognizer: [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(chooseRepeat)]];
    [repeatView addSubview:self.repeatBtnLabel];
    
    UIButton *btn02 = [self createButtonFrame:CGRectMake(CGRectGetMaxX(self.chooseTimeLabel.frame)+5, 0, kScreenWidth-15-9, spaceHeight) normalImage:@"btn_normalImage_next" selectImage:nil isBackgroundImage:NO title:nil target:self action:@selector(chooseRepeat)];
    [repeatView addSubview:btn02];
    
    
    [repeatView mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.top.equalTo(weakSelf.chooseTime.mas_bottom).offset(15);
        
        //        //设置宽度(1)
        //        make.left.equalTo(weakSelf.view).offset(0);
        //        make.right.equalTo(weakSelf.view).offset(0);
        //        //设置宽度(2)
        //        make.width.equalTo(weakSelf.view).multipliedBy(0);
        //设置宽度(3)
        make.width.equalTo(weakSelf.view);
        
        make.height.mas_equalTo(100.5);
        
    }];
    
    
    [reminderBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(repeatView).offset(15);
        make.top.equalTo(repeatView).offset(0);
        make.width.mas_equalTo(reminderBtn.imageView.image.size.width);
        make.height.mas_equalTo(spaceHeight);
    }];
    
    [self.reminderLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.left.equalTo(reminderBtn.mas_right).offset(15);
        make.top.equalTo(repeatView).offset(0);
        make.right.mas_equalTo(-24);
        make.height.mas_equalTo(spaceHeight);
        
    }];
    
    [reminderNextBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.top.equalTo(repeatView).offset(0);
        make.right.equalTo(repeatView).offset(-15);
        make.height.mas_equalTo(spaceHeight);
        make.width.mas_equalTo(reminderNextBtn.imageView.image.size.width);
    }];
    
    
    [reminderLineView mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.top.equalTo(repeatView).offset(spaceHeight);
        make.left.equalTo(reminderBtn.mas_right).offset(15);
        make.right.equalTo(repeatView).offset(0);
        make.height.mas_equalTo(0.5);
        
    }];
    
    
    [repeatBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.left.equalTo(repeatView).offset(15);
        make.top.equalTo(reminderLineView).offset(0);
        make.width.mas_equalTo(repeatBtn.imageView.image.size.width);
        make.height.mas_equalTo(spaceHeight);
        
    }];
    
    [self.repeatBtnLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.top.equalTo(reminderLineView).offset(0);
        make.left.equalTo(repeatBtn.mas_right).offset(15);
        make.height.mas_equalTo(spaceHeight);
        make.right.equalTo(repeatView).offset(-24);
        
    }];
    
    
    [btn02 mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.top.equalTo(reminderLineView).offset(0);
        make.right.equalTo(repeatView).offset(-15);
        make.height.mas_equalTo(spaceHeight);
        make.width.mas_equalTo(btn02.imageView.image.size.width);
    }];
    
}




#pragma mark - 选择提醒时间
- (void)chooseTimeAction{
    RKLog(@"请选择时间");
    
    //放弃第一响应者,收起键盘
    [self.reminderTxetView resignFirstResponder];
    
    WSDatePickerView *datepicker = [[WSDatePickerView alloc] init];
    datepicker.isShouWeek = YES;
    
    datepicker.scrollToDate = [Utils getDataString:[NSString stringWithFormat:@"%@%@",_rModel.strDate,_rModel.startTime] formatter:@"yyyyMMddHHmm"];
    
    datepicker.isShouWeek = YES;
    
    [datepicker setDateStyle:DateStyleShowYearMonthDayHourMinute CompleteBlock:^(NSDate *startDate) {
        NSString *date = [Utils transformDate:startDate dateFormatStyle:DateFormatYearMonthDayHourMinute];//[startDate stringWithFormat:@"yyyyMMddHHmmss"];
        _rModel.strDate = date;
        _rModel.startTime = [Utils transformDate:startDate dateFormatStyle:DateFormatHourMinute01];;
        RKLog(@"时间： %@",date);
        if (_rModel.isAllday) {
            self.chooseTimeLabel.text = [Utils transformDate:startDate dateFormatStyle:DateFormatYearMonthDayWithChinese];
        }else{
            self.chooseTimeLabel.text = [Utils transformDate:startDate dateFormatStyle:DateFormatYearMonthDayHourMinute1];//[startDate stringWithFormat:@"yyyy年MM月dd日 HH:mm"];
        }
        
    }];
    [datepicker show];
}



#pragma mark - 是否全天
- (void)chooseAllDay{
    RKLog(@"选择全天");
    //放弃第一响应者,收起键盘
    [self.reminderTxetView resignFirstResponder];
    
    _rModel.isAllday = !_rModel.isAllday;
    self.chooseTimeLabel.text = [Utils transformDate:_rModel.strDate dateFormatStyle:DateFormatYearMonthDayWithChinese];
    if (_rModel.isAllday) {
        if (isAllDayRemindTime) {
            self.reminderLabel.text = [NSString stringWithFormat:@"%@(%@)",NSLocalizedString(@"punctual_remind", nil),[Utils transformDate:isAllDayRemindTime dateFormatStyle:DateFormatHourMinuteWith24HR]];
            if (!_isShow) {
                [self.showAllDayTime show];
            }
//            [Utils showMsg:[NSString stringWithFormat:@"全天默认提醒为%@,可以在设置中进行修改",isAllDayRemindTime]];
        }else{
            
            //本地没有全天提醒事件时间
            [kNSUserDefaults setObject:@"0800" forKey:allDayRemindTimeKey];
            [kNSUserDefaults synchronize];
            self.reminderLabel.text = [NSString stringWithFormat:@"%@(%@)",NSLocalizedString(@"punctual_remind", nil),[Utils transformDate:isAllDayRemindTime dateFormatStyle:DateFormatHourMinuteWith24HR]];
        }
        
    }else{
        self.chooseTimeLabel.text = [Utils transformDate:[NSString stringWithFormat:@"%@%@",_rModel.strDate,_rModel.startTime] dateFormatStyle:DateFormatYearMonthDayHourMinute1];;
        self.reminderLabel.text = _rModel.beforeTime>0?[NSString stringWithFormat:@"%@%d%@",NSLocalizedString(@"advance_remind", nil),_rModel.beforeTime,NSLocalizedString(@"minute", nil)] : NSLocalizedString(@"punctual_remind", nil);
    }
    
    
}

- (UIAlertView *)showAllDayTime{
    if (!_showAllDayTime) {
        _showAllDayTime = [[UIAlertView alloc] initWithTitle:nil message:[NSString stringWithFormat:@"%@%@,%@",NSLocalizedString(@"all_day_message01", nil),[Utils transformDate:isAllDayRemindTime dateFormatStyle:DateFormatHourMinuteWith24HR],NSLocalizedString(@"all_day_message02", nil)] delegate:self cancelButtonTitle:NSLocalizedString(@"OK", nil) otherButtonTitles:nil, nil];
    }
    _isShow = YES;
    return _showAllDayTime;
}


#pragma mark - 提醒方式
- (void)reminderTime{
    
    RKLog(@"提醒方式");
    
    //放弃第一响应者,收起键盘
    [self.reminderTxetView resignFirstResponder];
    
    ReminderWayViewController *wayVC = [[ReminderWayViewController alloc]init];
    
    wayVC.isEdit = self.isEdit;
    wayVC.reminderModel = _rModel;
    
    @KKWeak(self);
    wayVC.reminderWay = ^(ReminderModel *model){
        @KKStrong(self);
        _rModel.isRemind = model.isRemind;
        _rModel.beforeTime = model.beforeTime;
        
        
        if (_rModel.isRemind==0) {
            self.reminderLabel.text = NSLocalizedString(@"not_remind", nil);
        }else{
            self.reminderLabel.text = _rModel.beforeTime>0?[NSString stringWithFormat:@"%@%d%@",NSLocalizedString(@"advance_remind", nil),_rModel.beforeTime,NSLocalizedString(@"minute", nil)] : NSLocalizedString(@"punctual_remind", nil);
        }
        
    };
    
    [self.navigationController pushViewController:wayVC animated:YES];
}

#pragma mark - 是否重复
- (void)chooseRepeat{
    RKLog(@"是否重复");
    
    //放弃第一响应者,收起键盘
    [self.reminderTxetView resignFirstResponder];
    
    ChooseRepeatViewController *vc = [[ChooseRepeatViewController alloc]init];
    vc.reminderModel = _rModel;
    @KKWeak(self)
    vc.chooseRepeatTime = ^(NSString *repeatTime){
        @KKStrong(self)
        
        if ([repeatTime containsString:@"0"] || [repeatTime containsString:@"8"] || [repeatTime containsString:@"9"]) {
            _rModel.repeatFlag = [repeatTime intValue];
            _rModel.repeatValue = repeatTime;
            if ([repeatTime containsString:@"8"] || [repeatTime containsString:@"9"]) {
                _rModel.repeatFlag = [repeatTime containsString:@"8"]?1:2;
            }
        }else{
            
            _rModel.repeatFlag = 3;
            _rModel.repeatValue = repeatTime;
        }
        
        self.repeatBtnLabel.text = [self getRepeatString:_rModel.repeatFlag repeat_value:_rModel.repeatValue];
        
    };
    
    [self.navigationController pushViewController:vc animated:YES];
}


- (NSString *)getRepeatString:(int)flag repeat_value:(NSString *)repeat_value{
    
    NSString *repeatStr = @"";
    
    
    if (flag==0) {
        return NSLocalizedString(@"not_repeat", nil);
    }else if (flag==1){
        return NSLocalizedString(@"Repeated_legal_working_days", nil);
    }else if (flag==2){
        return NSLocalizedString(@"Repeated_legal_holidays", nil);
    }else{
        NSArray *array = [NSArray arrayWithArray:[repeat_value componentsSeparatedByString:@","]];
        
        if ([repeat_value containsString:@"1,2,3,4,5,6,7"] && array.count==7){
            return repeatStr = NSLocalizedString(@"Repeat_every_day", nil);
        }else if ([repeat_value containsString:@"1,2,3,4,5"] && array.count==5){
            return repeatStr = NSLocalizedString(@"Working_days_repeat", nil);
        }else if ([repeat_value containsString:@"6,7"] && array.count==2){
            return repeatStr = NSLocalizedString(@"Weekend_repeat", nil);
        }else{
            //            "Mon"                               = "一";
            //            "Tues"                              = "二";
            //            "Wed"                               = "三";
            //            "Thur"                              = "四";
            //            "Fri"                               = "五";
            //            "Sat"                               = "六";
            //            "Sun"                               = "日";
            NSDictionary *dict = @{@"1":NSLocalizedString(@"Mon", nil),@"2":NSLocalizedString(@"Tues", nil),@"3":NSLocalizedString(@"Wed", nil),@"4":NSLocalizedString(@"Thur", nil),@"5":NSLocalizedString(@"Fri", nil),@"6":NSLocalizedString(@"Sat", nil),@"7":NSLocalizedString(@"Sun", nil)};
            
            NSString *newTime = @"";
            
            for (int i=0; i<array.count; i++) {
                
                NSString *key = array[i];
                
                if (i==0) {
                    newTime = [NSString stringWithFormat:@"%@",[dict objectForKey:key]];
                }else{
                    newTime = [NSString stringWithFormat:@"%@、%@",newTime,[dict objectForKey:key]];
                }
                
            }
            
            return repeatStr = [NSString stringWithFormat:@"%@%@",NSLocalizedString(@"Every_week", nil),newTime];
        }
    }
    
}

#pragma mark - 保存提醒

- (void)doRightAction:(UIButton *)sender{
    //放弃第一响应者,收起键盘
    [self.reminderTxetView resignFirstResponder];
    
    [self doSaveReminder];
}

- (void)doSaveReminder{
     RKLog(@"保存提醒");
    
    if ([Utils isBlankString:_rModel.content]) {
        [Utils showMsg:NSLocalizedString(@"remind_content_cannotempty", nil)];
        return;
    }
    
    //保存提醒
    @KKWeak(self);
    [[RemindAndGtasksDBManager shareManager] doSaveRemindWithRemindModel:_rModel editType:_isEdit?2:1 success:^(BOOL ret) {
        @KKStrong(self);
        if (self.updateReminder) {
            self.updateReminder();
        }
        
        [self.navigationController popViewControllerAnimated:YES];
    }];
    
    
}


- (void)textViewDidChange:(UITextView *)textView
{
    if (textView.text.length>100) {
        
        textView.text  = [textView.text substringToIndex:100];
    }
    
    _rModel.content = textView.text;
    textView.scrollEnabled = NO;
    CGSize size = [textView sizeThatFits:CGSizeMake(CGRectGetWidth(textView.frame), MAXFLOAT)];
    CGRect textViewbgFrame = self.textViewBg.frame;
    textViewbgFrame.size.height = size.height+10;
    
    [self.textViewBg mas_updateConstraints:^(MASConstraintMaker *make) {
        make.height.mas_equalTo(textViewbgFrame.size.height);
    }];
    
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
