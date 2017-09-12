//
//  GtasksViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/24.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "GtasksViewController.h"
#import "WSDatePickerView.h"
@interface GtasksViewController ()


<UITextViewDelegate,UIGestureRecognizerDelegate>

{
    
    GtasksModel *_gModel;
    
}

@property (weak, nonatomic) IBOutlet UIView *textViewBgView;

@property (weak, nonatomic) IBOutlet IQTextView *gtasksTextView;

@property (weak, nonatomic) IBOutlet UILabel *importantLable;

@property (weak, nonatomic) IBOutlet UIButton *importantStars;

@property (weak, nonatomic) IBOutlet UILabel *gtasksReminderLabel;

@property (weak, nonatomic) IBOutlet UISwitch *gtaskSwich;

@property (weak, nonatomic) IBOutlet UIView *importantReminderTimeBgView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *isOnImportantReminder;
@property (weak, nonatomic) IBOutlet UILabel *improtantReminderTime;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *textViewBgviewHeight;

@property (weak, nonatomic) IBOutlet UIView *line01;

@property (weak, nonatomic) IBOutlet UIView *line02;





@end

@implementation GtasksViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.title = NSLocalizedString(@"todo_title",nil);
    [self setRightButtonWithTitle:@[NSLocalizedString(@"done", nil)]];
    [self initData];
    [self createMainView];
}

- (void)initData{
    
    _gModel = [[GtasksModel alloc]init];
    
    if (_gtaskModel) {
        _gModel = _gtaskModel;
    }else{
        _gModel.isComplete = NO;
        _gModel.isImportant = NO;
        _gModel.isOpen = NO;
        _gModel.strDate = @"";
        _gModel.userId = @"";
        _gModel.completeDate = @"";
        _gModel.content = @"";
    }
    
}

- (void)createMainView{
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(dismissTextView)];
    tap.delegate = self;
    [self.view addGestureRecognizer:tap];
    
    self.textViewBgView.layer.borderWidth = 1;
    self.textViewBgView.layer.cornerRadius = 5;
    self.textViewBgView.layer.borderWidth = 1;
    self.textViewBgView.layer.borderColor = dt_line_color.CGColor;
    [self setViewShadowWithView:self.textViewBgView];
    
    self.gtasksTextView.delegate = self;
    self.gtasksTextView.placeholder = NSLocalizedString(@"todo_placeholder", nil);
    self.gtasksTextView.font = threeClassTextFont;
    self.gtasksTextView.textColor = dt_text_main_color;
    self.gtasksTextView.text = _gModel.content;
    
    //是否标记重要
    self.importantLable.textColor = dt_textLightgrey_color;
    self.importantLable.font = threeClassTextFont;
    self.importantLable.text = NSLocalizedString(@"todo_isImport", nil);
    
    
    
    self.importantStars.selected = NO;
    [self.importantStars setImage:[UIImage imageNamed:@"programme_Important_normalStars"] forState:UIControlStateNormal];
    [self.importantStars setImage:[UIImage imageNamed:@"programme_Important_selectStars"] forState:UIControlStateSelected];
    [self.importantStars addTarget:self action:@selector(importantAction:) forControlEvents:UIControlEventTouchUpInside];
    
    if (_gModel.isImportant) {
        self.importantLable.textColor = dt_text_main_color;
        self.importantStars.selected = YES;
    }
    
    //是否开启待办提醒
    [self setViewShadowWithView: self.importantReminderTimeBgView];
    
    self.gtasksReminderLabel.textColor = dt_text_main_color;
    self.gtasksReminderLabel.themeMap = @{kThemeMapKeyColorName : normalText_main_color};
    self.gtasksReminderLabel.font = threeClassTextFont;
    self.gtasksReminderLabel.text = NSLocalizedString(@"todo_remind", nil);
    
    self.improtantReminderTime.textColor = dt_text_main_color;
    self.improtantReminderTime.font = threeClassTextFont;
    self.improtantReminderTime.backgroundColor = [UIColor whiteColor];
    self.improtantReminderTime.userInteractionEnabled = YES;
    [self.improtantReminderTime addGestureRecognizer: [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tapTime)]];
    self.improtantReminderTime.text = [Utils setOldStringTime:_gModel.strDate inputFormat:@"yyyyMMddHHmm" outputFormat:@"yyyy年MM月dd日 HH:mm"];
    
    
    self.gtaskSwich.onTintColor = dt_app_main_color;
    [self.gtaskSwich addTarget:self action:@selector(openGtasksReminder:) forControlEvents:UIControlEventValueChanged];
    
    if (_gModel.isOpen) {
        self.gtaskSwich.on = YES;
        self.isOnImportantReminder.constant = 0;
    }
    
    
    
    self.line01.backgroundColor = dt_line_color;
    self.line02.backgroundColor = dt_line_color;
    
}



- (void)importantAction:(UIButton *)sender{
    
    //放弃第一响应者,收起键盘
    [self.gtasksTextView resignFirstResponder];
    
    sender.selected = !sender.selected;
    _gModel.isImportant = sender.selected;
    RKLog(@"%d",sender.selected);
    if (sender.selected) {
        self.importantLable.textColor = dt_text_main_color;
    }else{
        self.importantLable.textColor = dt_textLightgrey_color;
    }
    
}


- (void)openGtasksReminder:(UISwitch *)sender{
    RKLog(@"开启提醒");
    
    //放弃第一响应者,收起键盘
    [self.gtasksTextView resignFirstResponder];
    
    if (sender.on) {
        
        _gModel.isOpen = YES;
        self.improtantReminderTime.text = [Utils getCurrentTimeWithTimeFormat:@"yyyy年MM月dd日 HH:mm"];
        if ([Utils isBlankString:_gModel.strDate]) {
            _gModel.strDate = [Utils getCurrentTime];
        }
        
        [UIView animateWithDuration:0.5 animations:^{
            self.isOnImportantReminder.constant = 0;
            [self.view layoutIfNeeded];
        } completion:nil];
        
    }
    else{
        
        _gModel.isOpen = NO;
        
        [UIView animateWithDuration:0.5 animations:^{
            self.isOnImportantReminder.constant = -50;
            [self.view layoutIfNeeded];
        }];
    }
    
}


- (void)tapTime{
    RKLog(@"请选择时间");
    
    //放弃第一响应者,收起键盘
    [self.gtasksTextView resignFirstResponder];
    WSDatePickerView *datepicker = [[WSDatePickerView alloc] init];
    datepicker.isShouWeek = YES;
    
    NSString *chooseTime = self.improtantReminderTime.text;
    
    datepicker.scrollToDate =  [[NSDate date:chooseTime WithFormat:@"yyyy年MM月dd日 HH:mm"] dateWithFormatter:@"yyyy-MM-dd HH:mm"];
    
    [datepicker setDateStyle:DateStyleShowYearMonthDayHourMinute CompleteBlock:^(NSDate *startDate) {
        NSString *date = [startDate stringWithFormat:@"yyyy年MM月dd日 HH:mm"];
        NSLog(@"时间： %@",date);
        self.improtantReminderTime.text = date;
        _gModel.strDate = [Utils transformDateWithFormatter:@"yyyyMMddHHmm" date:startDate];
    }];
    [datepicker show];
}


- (void)doRightAction:(UIButton *)sender{
    [self doSaveGtasks];
}

#pragma mark - 保存待办
- (void)doSaveGtasks{
    RKLog(@"保存待办");
    
    if ([Utils isBlankString:_gModel.content]) {
        [Utils showMsg:NSLocalizedString(@"todo_content_cannotempty", @"To Do Content Cannot Empty")];
        return;
        
    }
    
    [[RemindAndGtasksDBManager shareManager] doSaveGtasksNetWorkWithGtasksModel:_gModel editType:_isEdit?2:1 success:^(BOOL ret) {
        
        if (self.editGtask) {
            self.editGtask();
        }
        [self.navigationController popViewControllerAnimated:YES];
    }];

}


- (void)textViewDidChange:(UITextView *)textView
{
    if (textView.text.length>100) {
        
        textView.text  = [textView.text substringToIndex:100];
    }
    
    _gModel.content = textView.text;
    
    textView.scrollEnabled = NO;
    CGSize size = [textView sizeThatFits:CGSizeMake(CGRectGetWidth(textView.frame), MAXFLOAT)];
    
    self.textViewBgviewHeight.constant = size.height+10;
    
}


- (void)dismissTextView{
    
    [self.gtasksTextView resignFirstResponder];
    
}

#pragma mark - UIGestureRecognizerDelegate
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if( [touch.view isDescendantOfView:self.textViewBgView]) {
        return NO;
    }
    return YES;
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
