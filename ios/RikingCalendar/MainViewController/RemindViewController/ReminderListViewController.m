//
//  ReminderListViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/28.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ReminderListViewController.h"

@interface ReminderListViewController ()


{
    NSMutableArray *_detailArray;
}

@end

@implementation ReminderListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self initData];
    [self.view addSubview:self.dataTabView];

}


- (void)initData{
    
    NSArray *timeArray = @[@"16:00",@"18:00",@"19:30",@"20:00",@"21:00",@"23:00"];
    NSArray *remarks = @[@"踢足球",@"看新闻",@"直播课程开始",@"看楚乔传,跟着楚乔走",@"农药联赛开始",@"撸代码"];
    
    for (int i = 0; i<6; i++) {
        
        NSMutableArray *array = [NSMutableArray array];
        
        for (int k = 0; k<6; k++) {
            [array addObject:[NSString stringWithFormat:@"%@  %@",timeArray[arc4random() % 6],remarks[arc4random() % 6]]];
        }
        
        [self.dataArray addObject:array];
    }
    
    
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
    return 30;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    UIView *view = [[UIView alloc]init];
    view.backgroundColor =[UIColor whiteColor];
    UILabel *timeLabel = [[UILabel alloc]initWithFrame:CGRectMake(kScreenWidth-100, 0, 100, 22)];
    timeLabel.font = sevenClassTextFont;
    timeLabel.themeMap = @{kThemeMapKeyColorName : line_lightgrey_color};
    timeLabel.textAlignment = NSTextAlignmentLeft;
    timeLabel.text = @"7月28号 周五";
    [view addSubview:timeLabel];
    
    return view;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    UIView *view = [[UIView alloc]init];
    UILabel *timeLabel = [[UILabel alloc]initWithFrame:CGRectMake(20, 10, 100, 22)];
    timeLabel.font = sevenClassTextFont;
    timeLabel.themeMap = @{kThemeMapKeyColorName : line_lightgrey_color};
    timeLabel.textAlignment = NSTextAlignmentLeft;
    timeLabel.text= @"今天";
    [view addSubview:timeLabel];
    return view;
}


- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    UILabel *timeLabel = [[UILabel alloc]init];
    timeLabel.textColor = dt_app_main_color;
    timeLabel.text = self.dataArray[indexPath.section][indexPath.row];
    timeLabel.font = threeClassTextFont;
    [cell.contentView addSubview:timeLabel];
    
    if (indexPath.section>0) {
        timeLabel.font = sevenClassTextFont;
    }
    
    [timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(UIEdgeInsetsMake(0, 15, 0, 15));
    }];
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
}



- (NSString *)tableView:(UITableView *)tableView titleForDeleteConfirmationButtonForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return @"删除";
}


- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    
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
