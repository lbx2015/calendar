//
//  ReminderListViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/28.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ReminderListViewController.h"
#import "DIYMJRefreshHaderHistory.h"
#import "ReminderModel.h"
#import "ReminderViewController.h"
#import "ReminderHistoryViewController.h"
#import "ReminderHistoryModel.h"
#import "ReminderAndGtasksDetailView.h"
#import "RemindListTableViewCell.h"
#import "NSDate+Extension.h"
#import "RemindAndGtasksDBManager.h"
@interface ReminderListViewController ()

<DIYMJRefreshHaderHistoryDelegate>
{
    NSMutableArray *_detailArray;
    
    NSMutableArray *_flagDateArray;
}
@property (nonatomic,strong) DIYMJRefreshHaderHistory *historyBtn;
@end

@implementation ReminderListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.

    [self initData];
    self.dataTabView.header = self.historyBtn;
    [self.view addSubview:self.dataTabView];
}


- (void)refreshReminderData{
    [self initData];
}

- (void)initData{
    
    if (!_flagDateArray) {
        _flagDateArray = [NSMutableArray array];
    }
    
    [self.dataArray removeAllObjects];
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
        //今天
        NSMutableArray *todayRemind = [[RemindAndGtasksDBManager shareManager] getRemindArrayWithDate:[NSDate date]];
        if (todayRemind.count>0) {
            [_flagDateArray addObject:@"1"];
            [self.dataArray addObject:todayRemind];
        }
        
        //明天
        NSMutableArray *tomorrowRemind = [[RemindAndGtasksDBManager shareManager] getTomorrowRemindArray];
        if (tomorrowRemind.count>0) {
            [_flagDateArray addObject:@"2"];
            [self.dataArray addObject:tomorrowRemind];
        }
        
        
        //以后
        NSMutableArray *futureRemind = [[RemindAndGtasksDBManager shareManager] getFutureRemindArray];
        if (futureRemind.count>0) {
            [_flagDateArray addObject:@"3"];
            [self.dataArray addObject:futureRemind];
        }
        
        dispatch_async(dispatch_get_main_queue(), ^{
            // 通知主线程刷新
            [self.dataTabView reloadData];
            
        });
        
    });
    
}

- (void)userSwitch{
    [self initData];
}

- (DIYMJRefreshHaderHistory *)historyBtn{
    if (!_historyBtn) {
        _historyBtn = [DIYMJRefreshHaderHistory headerWithRefreshingBlock:nil];
        _historyBtn.btnName = @"查看提醒历史";
        _historyBtn.delegate = self;
    }
    
    return _historyBtn;
}


#pragma mark - 查看提醒历史
- (void)showHistory{
    RKLog(@"查看提醒历史");
    
    ReminderHistoryViewController *historyVC = [[ReminderHistoryViewController alloc]init];
    
    [self.navigationController pushViewController:historyVC animated:YES];
    
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return self.dataArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    NSArray *array = self.dataArray[section];
    return [array count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 55;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 32;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    
    if (section>1) {
        return 0.0001;
    }
    return 30;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    
    if (section>1) {
        return nil;
    }
    UIView *view = [[UIView alloc]init];
    view.backgroundColor =[UIColor whiteColor];
    UILabel *timeLabel = [[UILabel alloc]initWithFrame:CGRectMake(kScreenWidth-100, 0, 100, 32)];
    timeLabel.font = sevenClassTextFont;
    timeLabel.textColor = dt_text_818181_color;
    timeLabel.textAlignment = NSTextAlignmentLeft;
    
    
    
    NSString *flag = _flagDateArray[section];
    NSString *dateStr = [Utils getCurrentTimeWithDateStyle:DateFormatMonthDayWithChinese];
    NSString *weekDay = [Utils getWeekDayWithDateStr:[Utils getCurrentTimeWithTimeFormat:@"yyyyMMdd"] formatter:@"yyyyMMdd"];
    
    if ([flag isEqualToString:@"2"]) {
        dateStr = [[RemindAndGtasksDBManager shareManager] GetTomorrowDay:[NSDate date]];
        weekDay = [Utils getWeekDayWithDateStr:dateStr formatter:@"yyyyMMdd"];
        timeLabel.text = [NSString stringWithFormat:@"%@ %@",[Utils setOldStringTime:dateStr inputFormat:@"yyyyMMdd" outputFormat:@"MM月dd日"],weekDay];
    }
    else{
        timeLabel.text = [NSString stringWithFormat:@"%@ %@",dateStr,weekDay];
    }
    
    [view addSubview:timeLabel];
    
    return view;
}


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    UIView *view = [[UIView alloc]init];
    UILabel *timeLabel = [[UILabel alloc]initWithFrame:CGRectMake(20, 10, 100, 22)];
    timeLabel.font = sevenClassTextFont;
    timeLabel.themeMap = @{kThemeMapKeyColorName : line_lightgrey_color};
    timeLabel.textAlignment = NSTextAlignmentLeft;
    
    NSString *flag = _flagDateArray[section];
    if ([flag isEqualToString:@"1"]) {
        timeLabel.text= @"今天";
    }else if ([flag isEqualToString:@"2"]){
        timeLabel.text= @"明天";
    }else{
        timeLabel.text= @"以后";
    }
    
    [view addSubview:timeLabel];
    return view;
}




- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    RemindListTableViewCell *remindCell = [tableView dequeueReusableCellWithIdentifier:@"remindCell"];
    
    if (!remindCell) {
        
        remindCell  = [[[NSBundle mainBundle]loadNibNamed:@"RemindListTableViewCell" owner:self options:nil]firstObject];
    }
    remindCell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    ReminderModel *rModel = self.dataArray[indexPath.section][indexPath.row];
    
    [remindCell loadDataWithReminderModel:rModel indexPath:indexPath];
    

    return remindCell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    NSArray *array = self.dataArray[indexPath.section];
    ReminderModel *rModel = array[indexPath.row];
    
    ReminderAndGtasksDetailView *detailVC = [[ReminderAndGtasksDetailView alloc]init];
    
    @KKWeak(self);
    [detailVC setDetailViewWithModel:rModel type:1 clickBtnType:^(int buttonType) {
        @KKStrong(self);
        RKLog(@"%d",buttonType);
        
        if (buttonType==1) {
            
            ReminderViewController *vc = [[ReminderViewController alloc]init];
            vc.reminderMode = rModel;
            vc.isEdit = YES;
            vc.updateReminder = ^(){
                
                [self initData];
            };
            [self.navigationController pushViewController:vc animated:YES];
            
        }else{
            BOOL ret = [rModel deleteObject];
            if (ret) {
                
                [[LocalNotificationManager shareManager] cancelLocalNotificationsWithKey:@"reminderId" value:rModel.reminderId];
                
                NSMutableArray *deleteArray = self.dataArray[indexPath.section];
                [deleteArray removeObjectAtIndex:indexPath.row];
                
                if (deleteArray.count==0) {
                    [self.dataArray removeObjectAtIndex:indexPath.section];
                }
                [self.dataTabView reloadData];
            }
        }
    
    }];
    
    [detailVC show];
}




- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath{
    return YES;
}


- (NSArray<UITableViewRowAction *> *)tableView:(UITableView *)tableView editActionsForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    ReminderModel *model = self.dataArray[indexPath.section][indexPath.row];
    
    UITableViewRowAction *editRowAction = [UITableViewRowAction  rowActionWithStyle:UITableViewRowActionStyleDefault title:@"编辑" handler:^(UITableViewRowAction * _Nonnull action, NSIndexPath * _Nonnull indexPath) {
        
        ReminderViewController *vc = [[ReminderViewController alloc]init];
        vc.reminderMode = model;
        vc.isEdit = YES;
        vc.updateReminder = ^(){
            
            [self initData];
        };
        [self.navigationController pushViewController:vc animated:YES];
        
        [self.dataTabView reloadData];
    }];
    
    editRowAction.backgroundColor = dt_app_main_color;
    
    UITableViewRowAction *deleteRowAction = [UITableViewRowAction rowActionWithStyle:UITableViewRowActionStyleDefault title:@"删除" handler:^(UITableViewRowAction * _Nonnull action, NSIndexPath * _Nonnull indexPath) {
        
        BOOL ret = [model deleteObject];
        if (ret) {
            
            [[LocalNotificationManager shareManager] cancelLocalNotificationsWithKey:@"reminderId" value:model.reminderId];
            NSMutableArray *deleteArray = self.dataArray[indexPath.section];
            [deleteArray removeObjectAtIndex:indexPath.row];
            
            if (deleteArray.count==0) {
                [self.dataArray removeObjectAtIndex:indexPath.section];
            }
            [self.dataTabView reloadData];
        }
        
        
    }];
    

   return @[deleteRowAction,editRowAction];
    
}


- (void)scrollViewDidScroll:(UIScrollView *)scrollView{
    
    if (scrollView.contentOffset.y>30) {
        
        if ([self.dataTabView.header isRefreshing]) {
            [self.dataTabView.header endRefreshing];
        }
    }

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
