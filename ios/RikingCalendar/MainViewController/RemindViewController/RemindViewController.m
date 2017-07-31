//
//  RemindViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/12.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RemindViewController.h"
#import "AddReminderController.h"
#import "ReminderListViewController.h"
#import "GtaskListViewController.h"
#import "MYSegmentView.h"
@interface RemindViewController ()

@end

@implementation RemindViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setRightButton:@[@"navigationBar_itemIcon_add"]];
    
    [self createMainView];
}

- (void)doRightAction:(UIButton *)sender{
    
    AddReminderController *addReminder = [AddReminderController new];
    [addReminder setHidesBottomBarWhenPushed:YES];
    [self.navigationController pushViewController:addReminder animated:YES];
    
}

- (void)createMainView{
    
    
    ReminderListViewController *reminderVC = [[ReminderListViewController alloc]init];
    GtaskListViewController *gtasksVC = [[GtaskListViewController alloc]init];
    
    NSArray *controllers=@[reminderVC,gtasksVC];
    NSArray *titleArray =@[@"提醒",@"待办"];
    
    MYSegmentView * rcs=[[MYSegmentView alloc]initWithFrame:CGRectMake(0, 0, kScreenWidth, kScreenHeight-64-49) controllers:controllers titleArray:titleArray ParentController:self lineWidth:kScreenWidth/2 lineHeight:2.];
    
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
