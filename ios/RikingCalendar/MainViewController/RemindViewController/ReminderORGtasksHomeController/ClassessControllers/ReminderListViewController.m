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
    
    BOOL _isRefreshData;
}
@property (nonatomic,strong) DIYMJRefreshHaderHistory *historyBtn;
@property (nonatomic,strong) UIView *notDataView;//没有数据
@end

@implementation ReminderListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.

    [self initData];
    self.dataTabView.header = self.historyBtn;
    [self.view addSubview:self.dataTabView];
    self.dataTabView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(refreshReminderData) name:kRefreshRemindName object:nil];
}


- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if (_isRefreshData) {
        [self initData];
    }
}

#pragma mark - 切换用户,需要重新刷新数据
- (void)userSwitch{
    _isRefreshData = YES;
}

#pragma mark - 新增数据,修改数据,删除数据
- (void)refreshReminderData{
    _isRefreshData = YES;
}

- (void)initData{
    
    if (!_flagDateArray) {
        _flagDateArray = [NSMutableArray array];
    }
    
    [self.dataArray removeAllObjects];
    [_flagDateArray removeAllObjects];
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
            _isRefreshData = NO;
        });
        
    });
    
}



- (DIYMJRefreshHaderHistory *)historyBtn{
    if (!_historyBtn) {
        _historyBtn = [DIYMJRefreshHaderHistory headerWithRefreshingBlock:nil];
        _historyBtn.btnName = NSLocalizedString(@"showRemindHistory", nil);
        _historyBtn.delegate = self;
    }
    
    return _historyBtn;
}

/*- (UIView *)notDataView{
    
    if (!_notDataView) {
        _notDataView = [[UIView alloc]init];
        _notDataView.backgroundColor = [UIColor redColor];
        [self.view addSubview:_notDataView];
        
        UIButton *imageView = [[UIButton alloc] init];
        [imageView setImage:[UIImage imageNamed:@"remind_notdata"] forState:UIControlStateNormal];
        imageView.userInteractionEnabled = NO;
        [_notDataView addSubview:imageView];
        
        UILabel *textLabel = [self createMainLabelWithText:@"目前没有提醒,不要错过重要的事情哦"];
        textLabel.textAlignment = NSTextAlignmentCenter;
        textLabel.font = sevenClassTextFont;
        [_notDataView addSubview:textLabel];
        
        @KKWeak(self);
        [_notDataView mas_makeConstraints:^(MASConstraintMaker *make) {
            @KKStrong(self);
            make.left.top.right.equalTo(self.view).offset(0);
            make.height.mas_equalTo(240);
        }];
        
        [imageView mas_makeConstraints:^(MASConstraintMaker *make) {
           
            make.top.equalTo(_notDataView).offset(50);
            make.left.right.equalTo(_notDataView).offset(0);
            make.height.mas_equalTo(128);
            
        }];
        
        [textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
           
            make.top.equalTo(imageView.mas_bottom).offset(25);
            make.left.right.equalTo(_notDataView).offset(0);
            make.height.mas_equalTo(15);
            
        }];
        
    }
    
    return _notDataView;
}*/

#pragma mark - 查看提醒历史
- (void)showHistory{
    RKLog(@"查看提醒历史");
    
    ReminderHistoryViewController *historyVC = [[ReminderHistoryViewController alloc]init];
    historyVC.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:historyVC animated:YES];
    
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    
    if (self.dataArray.count==0) {
        return 1;
    }
    return self.dataArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.dataArray.count==0) {
        return 1;
    }
    NSArray *array = self.dataArray[section];
    return [array count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (self.dataArray.count==0) {
        return 240;
    }
    return 55;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (self.dataArray.count==0) {
        return 0.0001;
    }
    return 32;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    
    if (self.dataArray.count==0) {
        return 0.0001;
    }
    NSString *flag = _flagDateArray[section];
    if ([flag isEqualToString:@"1"] || [flag isEqualToString:@"2"]) {
        return 30;
    }else{
        return 0.0001;
    }
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    
    if (self.dataArray.count==0) {
        return nil;
    }
   
    NSString *flag = _flagDateArray[section];
    if ([flag isEqualToString:@"1"] || [flag isEqualToString:@"2"]) {
        
        UIView *view = [[UIView alloc]init];
        view.backgroundColor =[UIColor whiteColor];
        UILabel *timeLabel = [[UILabel alloc]init];
        timeLabel.font = sevenClassTextFont;
        timeLabel.textColor = dt_text_818181_color;
        timeLabel.textAlignment = NSTextAlignmentRight;
        
        
        NSString *flag = _flagDateArray[section];
        NSString *dateStr = [Utils getCurrentTimeWithDateStyle:DateFormatMonthDayWithChinese];
        NSString *weekDay = [Utils getWeekDayWithDateStr:[Utils getCurrentTimeWithTimeFormat:@"yyyyMMdd"] formatter:@"yyyyMMdd"];
        
        if ([flag isEqualToString:@"2"]) {
            dateStr = [[RemindAndGtasksDBManager shareManager] GetTomorrowDay:[NSDate date]];
            weekDay = [Utils getWeekDayWithDateStr:dateStr formatter:@"yyyyMMdd"];
            timeLabel.text = [NSString stringWithFormat:@"%@ %@",[Utils transformDate:dateStr dateFormatStyle:DateFormatMonthDayWithChinese],weekDay];
        }
        else{
            timeLabel.text = [NSString stringWithFormat:@"%@ %@",dateStr,weekDay];
        }
        
        [view addSubview:timeLabel];
        
        [timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.top.bottom.equalTo(view).offset(0);
            make.right.equalTo(view).offset(-10);
        }];
        
        
        return view;
        
    }else{
        return nil;
    }

}


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    
    if (self.dataArray.count==0) {
        return nil;
    }
    
    UIView *view = [[UIView alloc]init];
    UILabel *timeLabel = [[UILabel alloc]initWithFrame:CGRectMake(20, 10, 100, 22)];
    timeLabel.font = sevenClassTextFont;
    timeLabel.themeMap = @{kThemeMapKeyColorName : line_lightgrey_color};
    timeLabel.textAlignment = NSTextAlignmentLeft;
//    "today"                             = "Today";
//    "tomorrow"                          = "Tomorrow";
//    "after"                             = "After";
    NSString *flag = _flagDateArray[section];
    if ([flag isEqualToString:@"1"]) {
        timeLabel.text= NSLocalizedString(@"today", nil);
    }else if ([flag isEqualToString:@"2"]){
        timeLabel.text= NSLocalizedString(@"tomorrow", nil);
    }else{
        timeLabel.text= NSLocalizedString(@"after", nil);
    }
    
    [view addSubview:timeLabel];
    return view;
}




- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (self.dataArray.count>0) {
        RemindListTableViewCell *remindCell = [tableView dequeueReusableCellWithIdentifier:@"remindCell"];
        
        if (!remindCell) {
            
            remindCell  = [[[NSBundle mainBundle]loadNibNamed:@"RemindListTableViewCell" owner:self options:nil]firstObject];
        }
        remindCell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        ReminderModel *rModel = self.dataArray[indexPath.section][indexPath.row];
        
        [remindCell loadDataWithReminderModel:rModel indexPath:indexPath remindArray:self.dataArray[indexPath.section]];
        
        
        return remindCell;
    }else{
        
        tableView.backgroundColor = [UIColor clearColor];
        
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cellId"];
        
        if (!cell) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cellId"];
        }
        
        //为了防止重用
        for (UIView *view in cell.contentView.subviews) {
            
            [view removeFromSuperview];
        }
        
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.backgroundColor = [UIColor clearColor];
        
        _notDataView = [[UIView alloc]init];
        _notDataView.backgroundColor = [UIColor clearColor];
        [cell.contentView addSubview:_notDataView];
        
        UIButton *imageView = [[UIButton alloc] init];
        [imageView setImage:[UIImage imageNamed:@"remind_notdata"] forState:UIControlStateNormal];
        imageView.userInteractionEnabled = NO;
        [_notDataView addSubview:imageView];
        
        UILabel *textLabel = [self createMainLabelWithText:NSLocalizedString(@"notRemindMessage", nil)];
        textLabel.textAlignment = NSTextAlignmentCenter;
        textLabel.font = sevenClassTextFont;
        [_notDataView addSubview:textLabel];
        

        [_notDataView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.top.right.equalTo(cell.contentView).offset(0);
            make.height.mas_equalTo(240);
        }];
        
        [imageView mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.top.equalTo(_notDataView).offset(50);
            make.left.right.equalTo(_notDataView).offset(0);
            make.height.mas_equalTo(128);
            
        }];
        
        [textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.top.equalTo(imageView.mas_bottom).offset(25);
            make.left.right.equalTo(_notDataView).offset(0);
            make.height.mas_equalTo(15);
            
        }];
        
        
        return cell;
    }
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (self.dataArray.count>0) {
        NSArray *array = self.dataArray[indexPath.section];
        ReminderModel *rModel = array[indexPath.row];
        
        ReminderAndGtasksDetailView *detailVC = [[ReminderAndGtasksDetailView alloc]init];
        
        @KKWeak(self);
        [detailVC setDetailViewWithModel:rModel type:1 clickBtnType:^(int buttonType) {
            @KKStrong(self);
            RKLog(@"%d",buttonType);
            
            if (buttonType==2) {
                
                ReminderViewController *vc = [[ReminderViewController alloc]init];
                vc.reminderMode = rModel;
                vc.isEdit = YES;
                vc.updateReminder = ^(){
                    
                    [self initData];
                };
                vc.hidesBottomBarWhenPushed = YES;
                [self.navigationController pushViewController:vc animated:YES];
                
            }else{
                
                @KKWeak(self);
                [[RemindAndGtasksDBManager shareManager] doSaveRemindWithRemindModel:rModel editType:3 success:^(BOOL ret) {
                    @KKStrong(self);
                    
                    NSMutableArray *deleteArray = self.dataArray[indexPath.section];
                    [deleteArray removeObjectAtIndex:indexPath.row];
                    
                    if (deleteArray.count==0) {
                        [self.dataArray removeObjectAtIndex:indexPath.section];
                    }
                    
                    [tableView reloadData];
                    
                }];
            }
            
        }];
        
        [detailVC show];
    }
   
}




- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (self.dataArray.count>0) {
        return YES;
    }
    return NO;
}


- (NSArray<UITableViewRowAction *> *)tableView:(UITableView *)tableView editActionsForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    if (self.dataArray.count>0) {
        ReminderModel *model = self.dataArray[indexPath.section][indexPath.row];
        
        UITableViewRowAction *editRowAction = [UITableViewRowAction  rowActionWithStyle:UITableViewRowActionStyleDefault title:NSLocalizedString(@"edit", nil) handler:^(UITableViewRowAction * _Nonnull action, NSIndexPath * _Nonnull indexPath) {
            
            ReminderViewController *vc = [[ReminderViewController alloc]init];
            vc.reminderMode = model;
            vc.isEdit = YES;
            
            @KKWeak(self);
            vc.updateReminder = ^(){
                @KKStrong(self);
                [self initData];
            };
            
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];
            
            [self.dataTabView reloadData];
        }];
        
        editRowAction.backgroundColor = dt_app_main_color;
        
        UITableViewRowAction *deleteRowAction = [UITableViewRowAction rowActionWithStyle:UITableViewRowActionStyleDefault title:NSLocalizedString(@"delete", nil) handler:^(UITableViewRowAction * _Nonnull action, NSIndexPath * _Nonnull indexPath) {
            
            
            @KKWeak(self);
            [[RemindAndGtasksDBManager shareManager] doSaveRemindWithRemindModel:model editType:3 success:^(BOOL ret) {
                @KKStrong(self);
                
                [self initData];
                
            }];
            
        }];
        
        
        return @[deleteRowAction,editRowAction];
    }
    
    
    return nil;
}


- (void)scrollViewDidScroll:(UIScrollView *)scrollView{
    
    if (scrollView.contentOffset.y>-10) {
        
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
