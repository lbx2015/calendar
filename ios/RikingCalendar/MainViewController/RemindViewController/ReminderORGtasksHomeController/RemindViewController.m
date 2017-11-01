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


{
    ReminderListViewController *reminderVC;
}

@property (nonatomic,assign)NSInteger selectIndex;
@property (nonatomic,strong)MYSegmentView * rcs;


@end

@implementation RemindViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self setRightButton:@[@"navigationBar_itemIcon_add"]];
    self.selectIndex = 0;
    [self createMainView];
}

- (void)doRightAction:(UIButton *)sender{
    
    AddReminderController *addReminder = [AddReminderController new];
    addReminder.selectIndex = self.selectIndex;
    
    @KKWeak(self);
    addReminder.editSelectIndex = ^(NSInteger index){
        @KKStrong(self);
        [self.rcs didiSelectIndex:index];
        self.selectIndex = index;
    };
    
    addReminder.updateData = ^(){
      @KKStrong(self);
        if (self.selectIndex == 0) {
//            [reminderVC refreshReminderData];
        }
        
    };
    
    [addReminder setHidesBottomBarWhenPushed:YES];
    [self.navigationController pushViewController:addReminder animated:YES];
    
}

- (void)createMainView{
    
    
    reminderVC = [[ReminderListViewController alloc]init];
    
    GtaskListViewController *gtasksVC = [[GtaskListViewController alloc]init];
    
    NSArray *controllers=@[reminderVC,gtasksVC];
    NSArray *titleArray =@[NSLocalizedString(@"remind_title", nil),NSLocalizedString(@"todo_title", nil)];
    
    self.rcs=[[MYSegmentView alloc]initWithFrame:CGRectMake(0, 0, kScreenWidth, kScreenHeight-64-49) controllers:controllers titleArray:titleArray ParentController:self lineWidth:kScreenWidth/2 lineHeight:2. selectIndex:self.selectIndex];
    
    @KKWeak(self);
    self.rcs.sureDown = ^(NSInteger index){
        @KKStrong(self);
        self.selectIndex = index;
        
    };
    
    [self.view addSubview:self.rcs];
    
    
    
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
