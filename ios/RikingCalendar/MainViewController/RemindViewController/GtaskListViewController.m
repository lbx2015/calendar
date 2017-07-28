//
//  GtaskListViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/28.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "GtaskListViewController.h"
#import "GtaskListTableViewCell.h"
#import "SpeedCreateGtaskTableViewCell.h"
@interface GtaskListViewController ()

@end

@implementation GtaskListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self.view addSubview:self.dataTabView];
    if ([ self.dataTabView respondsToSelector:@selector(setSeparatorInset:)]) {
        [self.dataTabView   setSeparatorInset:UIEdgeInsetsMake(0, 50, 0, 0)];
    }
    if ([self.dataTabView respondsToSelector:@selector(setLayoutMargins:)]) {
        [self.dataTabView setLayoutMargins:UIEdgeInsetsMake(0, 50, 0, 0)];
    }
    
    
    NSArray *remarks = @[@"踢足球",@"看新闻",@"直播课程开始",@"看楚乔传,跟着楚乔走",@"农药联赛开始",@"撸代码"];
    
    for (int i= 0; i<20; i++) {
        
        [self.dataArray addObject:  remarks[ arc4random()%6]];
    }
    
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.dataArray.count+1;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 55;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.row<self.dataArray.count) {
        
        GtaskListTableViewCell *gtaskCell = [tableView dequeueReusableCellWithIdentifier:@"gtaskCell"];
        
        if (!gtaskCell) {
            
            gtaskCell  = [[[NSBundle mainBundle]loadNibNamed:@"GtaskListTableViewCell" owner:self options:nil]firstObject];
        }
        
        gtaskCell.GtaskLabel.text = self.dataArray[indexPath.row];
        gtaskCell.GtaskLabel.font = threeClassTextFont;
        gtaskCell.GtaskLabel.textColor = dt_text_main_color;
        
        return gtaskCell;
    }
    else{
        
        SpeedCreateGtaskTableViewCell *speedCell = [tableView dequeueReusableCellWithIdentifier:@"speedCell"];
        
        if (!speedCell) {
            
            speedCell  = [[[NSBundle mainBundle]loadNibNamed:@"SpeedCreateGtaskTableViewCell" owner:self options:nil]firstObject];
        }
        
        
//
        if (speedCell.SpeedTextView.text.length>0) {
            speedCell.SpeedCreateBtn.layer.borderWidth = 1;
            speedCell.SpeedCreateBtn.layer.borderColor = dt_app_main_color.CGColor;
            speedCell.SpeedCreateBtn.layer.cornerRadius = 4;
            speedCell.SpeedCreateBtn.titleLabel.font = fiveClassTextFont;
            speedCell.SpeedCreateBtn.titleLabel.textColor = dt_app_main_color;
            speedCell.SpeedCreateBtn.hidden = NO;
        }else{
            speedCell.SpeedCreateBtn.hidden = YES;
        }
        
        speedCell.SpeedTextView.placeholder = @"快速添加";
        speedCell.SpeedTextView.font = threeClassTextFont;
        
        return speedCell;
        
    }
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.row==self.dataArray.count) {
        
        NSIndexPath *indexPath = [tableView indexPathForSelectedRow];
        
        SpeedCreateGtaskTableViewCell *cell = [tableView cellForRowAtIndexPath: indexPath];
        
        cell.speedCreateGtaskLabel.hidden = YES;
        [tableView reloadData];
        
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
