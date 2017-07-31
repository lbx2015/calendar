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

#define MAX_LIMIT_NUMS      10000000

#define spaceHeight         50

@interface ReminderViewController ()
<UITextViewDelegate>

@property (nonatomic, strong) IQTextView *reminderTxetView;
@property (nonatomic, strong) UIView *textViewBg;
@property (nonatomic, strong) UIView *chooseTime;
@property (nonatomic, strong) UILabel *chooseTimeBtn;
@end

@implementation ReminderViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self createMainUI];
    
    
}

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
    
    self.chooseTimeBtn = [self createLabelFrame:CGRectMake(CGRectGetMaxX(btn.frame)+15, 0, kScreenWidth-CGRectGetMaxX(btn.frame)-15-25, spaceHeight) text:[Utils getCurrentTimeWithTimeFormat:@"yyyy年MM月dd日 HH:mm"] font:threeClassTextFont textColor:[UIColor hex_colorWithHex:@"#323232"] textAlignment:NSTextAlignmentLeft];
    self.chooseTimeBtn.userInteractionEnabled = YES;
    [self.chooseTimeBtn addGestureRecognizer: [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(chooseTimeAction)]];
    [self.chooseTime addSubview:self.chooseTimeBtn];
    
    UIButton *btn01 = [self createButtonFrame:CGRectMake(CGRectGetMaxX(self.chooseTimeBtn.frame)+5, 0, kScreenWidth-15-9, spaceHeight) normalImage:@"btn_normalImage_next" selectImage:nil isBackgroundImage:NO title:nil target:self action:@selector(chooseTimeAction)];
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
    
    [self.chooseTimeBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        
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
    switchView.on = NO;
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
    UILabel *reminderLabel = [self createLabelFrame:CGRectMake(0, 0, 20, 30) text:@"提醒方式" font:threeClassTextFont textColor:[UIColor hex_colorWithHex:@"#323232"] textAlignment:NSTextAlignmentLeft];
    reminderLabel.userInteractionEnabled = YES;
    [reminderLabel addGestureRecognizer: [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(reminderTime)]];
    [repeatView addSubview:reminderLabel];
    
    //向右箭头
    UIButton *reminderNextBtn = [self createButtonFrame:CGRectMake(0, 0, 30, 30) normalImage:@"btn_normalImage_next" selectImage:nil isBackgroundImage:NO title:nil target:self action:nil];
    [repeatView addSubview:reminderNextBtn];
    
    
    //分割线
    UIView *reminderLineView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 30, 1)];
    reminderLineView.themeMap = @{kThemeMapKeyColorName : line_lightgrey_color};
    [repeatView addSubview:reminderLineView];
    
    
    UIButton *repeatBtn = [self createButtonFrame:CGRectMake(0, 0, 20, 30) normalImage:@"programme_ repeat" selectImage:nil isBackgroundImage:NO title:nil target:self action:@selector(chooseRepeat)];
    [repeatView addSubview:repeatBtn];
    
    
    UILabel *repeatBtnLabel = [self createLabelFrame:CGRectMake(0, 0, 0, 0) text:@"不重复" font:threeClassTextFont textColor:[UIColor hex_colorWithHex:@"#323232"] textAlignment:NSTextAlignmentLeft];
    repeatBtnLabel.userInteractionEnabled = YES;
    [repeatBtnLabel addGestureRecognizer: [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(chooseRepeat)]];
    [repeatView addSubview:repeatBtnLabel];
    
    UIButton *btn02 = [self createButtonFrame:CGRectMake(CGRectGetMaxX(self.chooseTimeBtn.frame)+5, 0, kScreenWidth-15-9, spaceHeight) normalImage:@"btn_normalImage_next" selectImage:nil isBackgroundImage:NO title:nil target:self action:@selector(chooseRepeat)];
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
    
    [reminderLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        
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
    
    [repeatBtnLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        
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


- (void)chooseTimeAction{
    RKLog(@"请选择时间");
    WSDatePickerView *datepicker = [[WSDatePickerView alloc] init];
    datepicker.isShouWeek = YES;
    
    NSString *chooseTime = self.chooseTimeBtn.text;
    
    datepicker.scrollToDate =  [NSDate date:chooseTime WithFormat:@"yyyy-MM-dd HH:mm"];
    
    [datepicker setDateStyle:DateStyleShowYearMonthDayHourMinute CompleteBlock:^(NSDate *startDate) {
        NSString *date = [startDate stringWithFormat:@"yyyy-MM-dd HH:mm"];
        NSLog(@"时间： %@",date);
        self.chooseTimeBtn.text = date;
    }];
    [datepicker show];
}


- (void)chooseAllDay{
    RKLog(@"选择全天");
}


- (void)reminderTime{
    
    RKLog(@"提醒方式");
    
    ReminderDatePickerView *reminderVC = [[ReminderDatePickerView alloc]init];
    
    [reminderVC setDateStyle:ReminderStyleShowMinute CompleteBlock:^(NSString *time) {
       
        
    }];
    
    [reminderVC show];
    
    
}

- (void)chooseRepeat{
    RKLog(@"是否重复");
    ChooseRepeatViewController *vc = [[ChooseRepeatViewController alloc]init];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)textViewDidChange:(UITextView *)textView
{
    if (textView.text.length>100) {
        
        textView.text  = [textView.text substringToIndex:100];
    }
    
    textView.scrollEnabled = NO;
    CGSize size = [textView sizeThatFits:CGSizeMake(CGRectGetWidth(textView.frame), MAXFLOAT)];
    
    CGRect textViewbgFrame = self.textViewBg.frame;
    textViewbgFrame.size.height = size.height+10;
//    self.textViewBg.frame =textViewbgFrame;
    
    CGRect chooseTimeFrame = self.chooseTime.frame;
    chooseTimeFrame.origin.y = CGRectGetMaxY(self.textViewBg.frame)+15;
//    self.chooseTime.frame = chooseTimeFrame;
    
    CGRect frame = textView.frame;
    frame.size.height = size.height;
//    textView.frame = frame;
    
    [self.textViewBg mas_updateConstraints:^(MASConstraintMaker *make) {
        
        make.height.mas_equalTo(textViewbgFrame.size.height);
    }];
    
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
