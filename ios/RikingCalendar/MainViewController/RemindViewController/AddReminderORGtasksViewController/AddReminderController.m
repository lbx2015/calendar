//
//  AddReminderController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/20.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "AddReminderController.h"
#import "MYSegmentView.h"
#import "ReminderViewController.h"
#import "GtasksViewController.h"
@interface AddReminderController ()
@property (nonatomic, strong) UIDatePicker *myDatePicker;

@property (nonatomic, assign)NSInteger index;

@property (nonatomic,strong)ReminderViewController *reminderVC;

@property (nonatomic,strong)GtasksViewController *gtasksVC;
@end

@implementation AddReminderController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = NSLocalizedString(@"addRemindAndGtasks_title", nil);
    self.index = self.selectIndex;
    [self setRightButtonWithTitle:@[NSLocalizedString(@"done", nil)]];
    [self createMainView];
    
    
//        self.myDatePicker = [[UIDatePicker alloc] init];
//        self.myDatePicker.center = self.view.center;
//    self.myDatePicker.datePickerMode =UIDatePickerModeDate;
//        [self.view addSubview:self.myDatePicker];
//    
//        [self.myDatePicker addTarget:self
//                              action:@selector(datePickerDateChanged:)
//                    forControlEvents:UIControlEventValueChanged];
//    
//        NSTimeInterval oneYearTime = 365 * 24 * 60 * 60;
//        NSDate *todayDate = [NSDate date];
//        NSDate *oneYearFromToday = [todayDate dateByAddingTimeInterval:oneYearTime];
//        NSDate *twoYearsFromToday = [todayDate dateByAddingTimeInterval:2 * oneYearTime];
//        self.myDatePicker.minimumDate = oneYearFromToday;
//        self.myDatePicker.maximumDate = twoYearsFromToday;
    
    
}

- (void)datePickerDateChanged:(UIDatePicker *)sender{
    
}

- (void)doRightAction:(UIButton *)sender{
    
    if (self.index == 0) {
        RKLog(@"提醒");
        [_reminderVC doSaveReminder];
        
        if (self.updateData) {
            self.updateData();
        }
        
    }else{
        RKLog(@"待办");
        
        [_gtasksVC doSaveGtasks];
    }
    
}


- (void)createMainView{
    
    _reminderVC = [[ReminderViewController alloc]init];
    _gtasksVC = [[GtasksViewController alloc]initWithNibName:@"GtasksViewController" bundle:[NSBundle mainBundle]];
    
    NSArray *controllers=@[_reminderVC,_gtasksVC];
    NSArray *titleArray =@[NSLocalizedString(@"remind_title", nil),NSLocalizedString(@"todo_title", nil)];
    
    MYSegmentView * rcs=[[MYSegmentView alloc]initWithFrame:CGRectMake(0, 0, kScreenWidth, kScreenHeight) controllers:controllers titleArray:titleArray ParentController:self lineWidth:kScreenWidth/2 lineHeight:2. selectIndex:self.selectIndex];
    
    @KKWeak(self);
    rcs.sureDown = ^(NSInteger index){
      
        @KKStrong(self);
        
        self.index = index;
        
        if (self.editSelectIndex) {
            self.editSelectIndex(index);
        }
        
        
    };
    
    [self.view addSubview:rcs];
    
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
