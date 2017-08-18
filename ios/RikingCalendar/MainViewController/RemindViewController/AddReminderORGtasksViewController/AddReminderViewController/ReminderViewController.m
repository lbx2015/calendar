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
}
@property (nonatomic, strong) IQTextView *reminderTxetView;
@property (nonatomic, strong) UIView *textViewBg;
@property (nonatomic, strong) UIView *chooseTime;
@property (nonatomic, strong) UILabel *chooseTimeLabel;
@property (nonatomic, strong) UILabel *reminderLabel;//提醒方式
@property (nonatomic, strong) UILabel *repeatBtnLabel;

@end

@implementation ReminderViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = @"新建日程";
    [self setRightButtonWithTitle:@[@"完成"]];
    
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
        _rModel.strDate = [Utils getCurrentTimeWithDateStyle:DateFormatYearMonthDayHourMinute];//[Utils getCurrentTimeWithTimeFormat:@"yyyy年MM月dd日 HH:mm"];
        _rModel.startTime = [Utils getCurrentTimeWithDateStyle:DateFormatHourMinuteWith24HR];
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
    self.reminderTxetView.placeholder = @"输入提醒内容";
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
    
    self.chooseTimeLabel.text = [Utils transformDate:_rModel.strDate dateFormatStyle:DateFormatYearMonthDayWithChinese];
    if (!_rModel.isAllday) {
        self.chooseTimeLabel.text = [NSString stringWithFormat:@"%@ %@",self.chooseTimeLabel.text,_rModel.startTime];
    }else{
        
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
    UILabel *chooseAllDay = [self createLabelFrame:CGRectMake(CGRectGetMaxX(btn.frame)+15, 0, kScreenWidth-CGRectGetMaxX(btn.frame)-15-25, spaceHeight) text:@"全天" font:threeClassTextFont textColor:[UIColor hex_colorWithHex:@"#323232"] textAlignment:NSTextAlignmentLeft];
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
    UIButton *reminderBtn = [self createButtonFrame:CGRectMake(15, 0, 29, spaceHeight) normalImage:@"programme_chooseTime" selectImage:nil isBackgroundImage:NO title:nil target:self action:@selector(reminderTime)];
    [repeatView addSubview:reminderBtn];
    
    
    //提醒时间
    self.reminderLabel = [self createLabelFrame:CGRectMake(0, 0, 20, 30) text:@"" font:threeClassTextFont textColor:[UIColor hex_colorWithHex:@"#323232"] textAlignment:NSTextAlignmentLeft];
    self.reminderLabel.userInteractionEnabled = YES;
    [self.reminderLabel addGestureRecognizer: [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(reminderTime)]];
    [repeatView addSubview:self.reminderLabel];
    
    if (_rModel.isRemind==0) {
        self.reminderLabel.text = @"不提醒";
    }else{
        self.reminderLabel.text = _rModel.beforeTime>0?[NSString stringWithFormat:@"提前%d分",_rModel.beforeTime] : @"正点提醒";
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
    
    WSDatePickerView *datepicker = [[WSDatePickerView alloc] init];
    datepicker.isShouWeek = YES;
    
    NSString *chooseTime = self.chooseTimeLabel.text;
    
    datepicker.scrollToDate =  [[NSDate date:chooseTime WithFormat:@"yyyy年MM月dd日"] dateWithFormatter:@"yyyy-MM-dd"];
    
    datepicker.isShouWeek = YES;
    
    [datepicker setDateStyle:DateStyleShowYearMonthDayHourMinute CompleteBlock:^(NSDate *startDate) {
        NSString *date = [Utils transformDate:startDate dateFormatStyle:DateFormatYearMonthDayHourMinute];//[startDate stringWithFormat:@"yyyyMMddHHmmss"];
        _rModel.strDate = date;
        _rModel.startTime = [Utils transformDate:startDate dateFormatStyle:DateFormatHourMinuteWith24HR];;
        RKLog(@"时间： %@",date);
        if (_rModel.isAllday) {
            self.chooseTimeLabel.text = [Utils transformDate:startDate dateFormatStyle:DateFormatYearMonthDayWithChinese];
        }else{
            self.chooseTimeLabel.text = [startDate stringWithFormat:@"yyyy年MM月dd日 HH:mm"];
        }
        
    }];
    [datepicker show];
    
    
    
    
    
    
//    WSDatePickerView *datepicker = [[WSDatePickerView alloc] init];
//    datepicker.isShouWeek = NO;
//    
//    NSString *chooseTime = self.chooseTimeLabel.text;
//    
//    datepicker.scrollToDate =  [[NSDate date:chooseTime WithFormat:@"yyyy年MM月dd日"] dateWithFormatter:@"yyyy-MM-dd"];
//    
//    datepicker.isShouWeek = YES;
//    
//    [datepicker setDateStyle:DateStyleShowMonthOrShowYearMonthDay CompleteBlock:^(NSDate *startDate) {
//        NSString *date = [startDate stringWithFormat:@"yyyyMMdd"];
//        _rModel.strDate = date;
//        _rModel.startTime = [startDate stringWithFormat:@"HH:mm"];
//        RKLog(@"时间： %@",date);
//        
//        
//    }];
//    [datepicker show];
    
    
    
    
    
}



#pragma mark - 是否全天
- (void)chooseAllDay{
    RKLog(@"选择全天");
    _rModel.isAllday = !_rModel.isAllday;
    self.chooseTimeLabel.text = [Utils transformDate:_rModel.strDate dateFormatStyle:DateFormatYearMonthDayWithChinese];
    if (_rModel.isAllday) {
        if (isAllDayRemindTime) {
            self.reminderLabel.text = [NSString stringWithFormat:@"正点提醒(%@)",isAllDayRemindTime];
        }else{
            self.reminderLabel.text = [NSString stringWithFormat:@"正点提醒(%@)",@"08:00"];
        }
        
    }else{
        self.chooseTimeLabel.text = [NSString stringWithFormat:@"%@ %@",self.chooseTimeLabel.text,_rModel.startTime];
        self.reminderLabel.text = _rModel.beforeTime>0?[NSString stringWithFormat:@"提前%d分",_rModel.beforeTime] : @"正点提醒";
    }
    
    
}



#pragma mark - 提醒方式
- (void)reminderTime{
    
    RKLog(@"提醒方式");
    
    ReminderWayViewController *wayVC = [[ReminderWayViewController alloc]init];
    
    wayVC.isEdit = self.isEdit;
    wayVC.reminderModel = _rModel;
    
    @KKWeak(self);
    wayVC.reminderWay = ^(ReminderModel *model){
        @KKStrong(self);
        _rModel.isRemind = model.isRemind;
        _rModel.beforeTime = model.beforeTime;
        
        if (_rModel.isRemind==0) {
            self.reminderLabel.text = @"不提醒";
        }else{
            if (_rModel.beforeTime>0) {
                self.reminderLabel.text = [NSString stringWithFormat:@"提前%d分",_rModel.beforeTime];
            }else{
                self.reminderLabel.text = @"正点提醒";
            }
        }
        
    };
    
    [self.navigationController pushViewController:wayVC animated:YES];
}

#pragma mark - 是否重复
- (void)chooseRepeat{
    RKLog(@"是否重复");
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
        return @"不重复";
    }else if (flag==1){
        return @"法定工作日重复";
    }else if (flag==2){
        return @"法定节假日重复";
    }else{
        NSArray *array = [NSArray arrayWithArray:[repeat_value componentsSeparatedByString:@","]];
        
        if ([repeat_value containsString:@"1,2,3,4,5,6,7"] && array.count==7){
            return repeatStr = @"每天重复";
        }else if ([repeat_value containsString:@"1,2,3,4,5"] && array.count==5){
            return repeatStr = @"工作日重复";
        }else if ([repeat_value containsString:@"6,7"] && array.count==2){
            return repeatStr = @"周末重复";
        }else{
            
            NSDictionary *dict = @{@"1":@"一",@"2":@"二",@"3":@"三",@"4":@"四",@"5":@"五",@"6":@"六",@"7":@"日"};
            
            NSString *newTime = @"";
            
            for (int i=0; i<array.count; i++) {
                
                NSString *key = array[i];
                
                if (i==0) {
                    newTime = [NSString stringWithFormat:@"%@",[dict objectForKey:key]];
                }else{
                    newTime = [NSString stringWithFormat:@"%@、%@",newTime,[dict objectForKey:key]];
                }
                
            }
            
            return repeatStr = [NSString stringWithFormat:@"每周%@",newTime];
    }
}

}

#pragma mark - 保存提醒

- (void)doRightAction:(UIButton *)sender{
    [self doSaveReminder];
}

- (void)doSaveReminder{
     RKLog(@"保存提醒");
    
    if ([Utils isBlankString:_rModel.content]) {
        [Utils showMsg:@"提醒内容不能为空哦"];
        return;
    }
    
    if (!_isEdit) {
        _rModel.reminderId = [Utils getCurrentTimeWithDateStyle:DateFormatYearMonthDayHourMintesecondMillisecond];
        _rModel.clientType = 1;
        if (isUser) {
            _rModel.userId = UserID;
        }
    }
    NSMutableDictionary *param = [NSMutableDictionary dictionaryWithDictionary:[_rModel mj_keyValues]];
    [param removeObjectForKey:@"propertyNames"];
    [param removeObjectForKey:@"pk"];
    [param removeObjectForKey:@"columeTypes"];
    [param removeObjectForKey:@"columeNames"];
    
    if (self.networkStatus) {
        [self kkRequestWithHTTPMethod:POST urlString:[NSString stringWithFormat:@"%@%@",ServreUrl,saveReminder] parm:param success:^(NSDictionary *dictData) {
            
        } failure:^(NSError *error) {
            
        }];
    }
    
    
    BOOL ret = NO;
    
    if (_isEdit) {
        ret = [_rModel update];
    }else{
        ret = [_rModel save];
    }
    
    //添加提醒
    if (ret) {
    
        if (_isEdit) {
            NSArray *arrLocalNotifis = [[UIApplication sharedApplication] scheduledLocalNotifications];//获取所有本地通知
            for (UILocalNotification *localNoti in arrLocalNotifis)//遍历
            {
                NSDictionary *userInfo = localNoti.userInfo;//获取通知附带的信息
                if (userInfo)
                {
                    if ([(NSString *)userInfo[@"reminderId"] isEqualToString:_rModel.reminderId])//前面保存在userInfo中的内容
                    {
                        [[UIApplication sharedApplication] cancelLocalNotification:localNoti];//这句代码会删除所有本地通知中的特定一条，并切不会再推送这一条
                    }
                }
            }
        }
        

        NSString *curronDateStr = _rModel.strDate;
        NSString *startTimeStr = [NSString stringWithFormat:@"%@%@",[Utils setOldStringTime:_rModel.startTime inputFormat:@"HH:mm" outputFormat:@"HHmm"],@"00"];
        NSDate *curronDate = [Utils getDataString:[NSString stringWithFormat:@"%@%@",curronDateStr,startTimeStr] formatter:@"yyyyMMddHHmmss"];
        NSInteger beforeTime = 0;
        
        if (_rModel.isAllday) {
            
            if (isAllDayRemindTime) {
               curronDate = [Utils getDataString:[NSString stringWithFormat:@"%@%@",[_rModel.strDate substringToIndex:8],[Utils setOldStringTime:[NSString stringWithFormat:@"%@%@",isAllDayRemindTime,@":00"] inputFormat:@"HH:mm:ss" outputFormat:@"HHmmss"]] formatter:@"yyyyMMddHHmmss"];
            }else{
                curronDate = [Utils getDataString:[NSString stringWithFormat:@"%@%@",[_rModel.strDate substringToIndex:8],[Utils setOldStringTime:[NSString stringWithFormat:@"%@%@",@"08:00",@":00"] inputFormat:@"HH:mm:ss" outputFormat:@"HHmmss"]] formatter:@"yyyyMMddHHmmss"];
            }
            
        }
        if (_rModel.beforeTime>0) {
            beforeTime = _rModel.beforeTime*60;
            NSInteger selectTimestamp = [Utils timeSwitchTimestamp:[NSString stringWithFormat:@"%@",curronDate] andFormatter:@"yyyyMMddHHmmss"] - beforeTime;
            NSDate *beforeDate = [Utils getDataString:[Utils timestampSwitchTime:selectTimestamp andFormatter:@"yyyyMMddHHmmss"] formatter:@"yyyyMMddHHmmss"];
            curronDate = beforeDate;
        }
        NSCalendar *calendar=[[NSCalendar alloc]initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
        NSDateComponents *comps = [[NSDateComponents alloc] init];
        NSInteger unitFlags = NSCalendarUnitEra | NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay | NSCalendarUnitHour | NSCalendarUnitMinute | NSCalendarUnitSecond | NSCalendarUnitWeekOfYear | NSCalendarUnitWeekday | NSCalendarUnitWeekdayOrdinal | NSCalendarUnitQuarter;
        comps = [calendar components:unitFlags fromDate:curronDate];
        
        if (_rModel.repeatFlag == 0) {
            
            if ([Utils timeSwitchTimestamp:[Utils transformDate:curronDate dateFormatStyle:DateFormatYearMonthDayHourMinute] andFormatter:@"yyyyMMddHHmmss"]>[Utils timeSwitchTimestamp:[Utils transformDate:[NSDate date] dateFormatStyle:DateFormatYearMonthDayHourMinute] andFormatter:@"yyyyMMddHHmmss"]) {
                
                [self scheduleNotificationWithAlertBody:_rModel.content userInfo:param repeatInterVal:0 fireDate:curronDate];
            }
        }else if (_rModel.repeatFlag==1){
            
            NSArray *dayData = [CalendarModel selectDataWithSql:[NSString stringWithFormat:@"select *from CalendarModel where date>='%@' and remark='1' order by date",[Utils getCurrentTimeWithDay]]];
            
            if (dayData.count>0) {
                
                for (CalendarModel *cModel in dayData) {
                    NSString *remindDateStr = [NSString stringWithFormat:@"%@%@",cModel.date,[Utils transformDateWithFormatter:@"HHmmss" date:curronDate]];
                    NSDate *remindDate = [Utils getDataString:remindDateStr formatter:@"yyyyMMddHHmmss"];
                    [self scheduleNotificationWithAlertBody:_rModel.content userInfo:param repeatInterVal:0 fireDate:remindDate];
                    
                }
                
            }
            
            
        }else if (_rModel.repeatFlag==2){
            
            
            NSArray *dayData = [CalendarModel selectDataWithSql:[NSString stringWithFormat:@"select *from CalendarModel where date>='%@' and remark='0' order by date",[Utils getCurrentTimeWithDay]]];
            
            if (dayData.count>0) {
                
                for (CalendarModel *cModel in dayData) {
                    NSString *remindDateStr = [NSString stringWithFormat:@"%@%@",cModel.date,[Utils transformDateWithFormatter:@"HHmmss" date:curronDate]];
                    NSDate *remindDate = [Utils getDataString:remindDateStr formatter:@"yyyyMMddHHmmss"];
                    [self scheduleNotificationWithAlertBody:_rModel.content userInfo:param repeatInterVal:0 fireDate:remindDate];
                    
                }
                
            }
            
            
        }else{
            for (int i=0; i<7; i++) {
                
                //i==0,当天的时间
                NSDate *myDate = [curronDate dateByAddingTimeInterval:3600 * 24 * i];
                NSString *weekDay = [Utils getWeekDayWithDate:myDate];
                
                if ([_rModel.repeatValue rangeOfString:weekDay].location!=NSNotFound) {
                    
                    [self scheduleNotificationWithAlertBody:_rModel.content userInfo:param repeatInterVal:kCFCalendarUnitWeek fireDate:myDate];
                }
            }
        }
        
        
       
        
        
        
        
        
        
    
        
        
        
        
//        for (int newWeekDay =2; newWeekDay<=6; newWeekDay++) {
//            
//            NSInteger temp = 0;
//            NSInteger days = 0;
//            
//            temp = newWeekDay - comps.weekday;
//            days = (temp >= 0 ? temp : temp + 7);
//            
//            NSDate *newFireDate = [[calendar dateFromComponents:comps] dateByAddingTimeInterval:3600 * 24 * days];
//            [self scheduleNotificationWithItem:_rModel.content fireDate:newFireDate];
//            
//            
//        }
        
        
        
        
//        NSCalendar *calendar=[[NSCalendar alloc]initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
//        NSDateComponents *comps = [[NSDateComponents alloc] init]; NSInteger unitFlags = NSCalendarUnitEra | NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay | NSCalendarUnitHour | NSCalendarUnitMinute | NSCalendarUnitSecond | NSCalendarUnitWeekOfYear | NSCalendarUnitWeekday | NSCalendarUnitWeekdayOrdinal | NSCalendarUnitQuarter;
//        comps = [calendar components:unitFlags fromDate:[NSDate date]];
//        for (int i = 0; i <10; i++) {
//            NSDate *newFireDate = [calendar dateFromComponents:comps];//[ dateByAddingTimeInterval:3*i];
//            UILocalNotification *newNotification = [[UILocalNotification alloc] init];
//            if (newNotification) {
//                
//                newNotification.fireDate = newFireDate;
//                newNotification.alertBody = @"哈哈"; newNotification.soundName = @"呵呵";
//                newNotification.applicationIconBadgeNumber=1;//应用程序图标右上角显示的消息数
//            newNotification.alertAction = @"查看闹钟"; newNotification.userInfo =@{@"id":@1,@"user":@"Kenshin Cui"};//绑定到通知上的其他附加信息;
//            [[UIApplication sharedApplication] scheduleLocalNotification:newNotification];
//        }
//        
//    }
        
        
        
        
        
        
//        // 申请通知权限
//        [[UNUserNotificationCenter currentNotificationCenter] requestAuthorizationWithOptions:(UNAuthorizationOptionAlert | UNAuthorizationOptionSound | UNAuthorizationOptionBadge) completionHandler:^(BOOL granted, NSError * _Nullable error) {
//            // A Boolean value indicating whether authorization was granted. The value of this parameter is YES when authorization for the requested options was granted. The value is NO when authorization for one or more of the options is denied.
//            if (granted) {
//                // 1、创建通知内容，注：这里得用可变类型的UNMutableNotificationContent，否则内容的属性是只读的
//                UNMutableNotificationContent *content = [[UNMutableNotificationContent alloc] init];
//                // 标题
//                content.title = @"通知到啦";
//                //次标题 content.subtitle = @"柯梵办公室通知";
//                //内容
//                content.body = _rModel.content;
//                // app显示通知数量的角标
//                content.badge = @1;
//                // 通知的提示声音，这里用的默认的声音
//                content.sound = [UNNotificationSound defaultSound];
//                
//                // 附件 可以是音频、图片、视频 这里是一张图片
//                //                NSURL *imageUrl = [[NSBundle mainBundle] URLForResource:@"jianglai" withExtension:@"jpg"];
//                //                UNNotificationAttachment *attachment = [UNNotificationAttachment attachmentWithIdentifier:@"imageIndetifier" URL:imageUrl options:nil error:nil];
//                //                content.attachments = @[attachment];
//                // 标识符
//                content.categoryIdentifier = _rModel.reminderId;
//                // 2、创建通知触发
//                /* 触发器分三种：
//                 UNTimeIntervalNotificationTrigger : 在一定时间后触发，如果设置重复的话，timeInterval不能小于60
//                 UNCalendarNotificationTrigger : 在某天某时触发，可重复
//                 UNLocationNotificationTrigger : 进入或离开某个地理区域时触发 */
//                
//                NSCalendar *calendar=[[NSCalendar alloc]initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
//                NSDateComponents *comps = [[NSDateComponents alloc] init];
//                NSInteger unitFlags = NSCalendarUnitEra | NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay | NSCalendarUnitHour | NSCalendarUnitMinute | NSCalendarUnitSecond | NSCalendarUnitWeekOfYear | NSCalendarUnitWeekday | NSCalendarUnitWeekdayOrdinal | NSCalendarUnitQuarter;
//                comps = [calendar components:unitFlags fromDate:curronDate];
//                comps.weekday = 2|3|4;
//                comps.year = curronDate.year;
//                comps.month = curronDate.month;
//                comps.hour = curronDate.hour;
//                comps.minute = curronDate.minute;
//                comps.second = 0;
//                UNCalendarNotificationTrigger *trigger2 = [UNCalendarNotificationTrigger triggerWithDateMatchingComponents:comps repeats:YES];
//                
//                // 3、创建通知请求
//                UNNotificationRequest *notificationRequest = [UNNotificationRequest requestWithIdentifier:@"KFGroupNotification" content:content trigger:trigger2];
//                // 4、将请求加入通知中心
//                [[UNUserNotificationCenter currentNotificationCenter] addNotificationRequest:notificationRequest withCompletionHandler:^(NSError * _Nullable error) { if (error == nil) { NSLog(@"已成功加推送%@",notificationRequest.identifier);
//                }
//                }];
//            }
//        }];
        
        
    }
    
    if (self.updateReminder) {
        self.updateReminder();
    }

    [self.navigationController popViewControllerAnimated:YES];
}

- (void)scheduleNotificationWithAlertBody:(NSString *)alertBody userInfo:(NSDictionary *)userInfo repeatInterVal:(NSCalendarUnit)repeatInterVal fireDate:(NSDate*)date
{
    //初始化
    UILocalNotification *locationNotification = [[UILocalNotification alloc]init];
    locationNotification.fireDate =date;
    //设置重复周期
    locationNotification.repeatInterval = repeatInterVal;
    //设置通知的音乐
    locationNotification.soundName = UILocalNotificationDefaultSoundName;
    //设置通知内容
    locationNotification.alertBody = alertBody;
    //设置推送实体
    locationNotification.userInfo = userInfo;
    //应用程序图标右上角显示的消息数
    locationNotification.applicationIconBadgeNumber=1;
    //执行本地推送
    [[UIApplication sharedApplication] scheduleLocalNotification:locationNotification];
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
