//
//  ReportListTableView.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/9.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ReportListTableView.h"

@interface ReportListTableView()

<UITableViewDelegate,UITableViewDataSource>


{
    
    NSMutableArray *_dataArray;
    
    NSMutableArray *_btnArray;
}

@end

@implementation ReportListTableView

- (instancetype)initWithFrame:(CGRect)frame dataArray:(NSMutableArray *)dataModel{
    
    self = [super initWithFrame:CGRectZero];
    if (self) {
        self.frame = CGRectMake(0, 0, kScreenWidth, kScreenHeight-kBottomBarHeight-64-kTopBarHeight);
        
        _btnArray = [NSMutableArray arrayWithObjects:@"0",@"0",@"0",@"0",@"0", nil];
        
        _dataArray = [NSMutableArray arrayWithArray:dataModel];
        
        
        self.reportTableView = [[UITableView alloc]initWithFrame:self.frame style:UITableViewStylePlain];
        self.reportTableView.delegate = self;
        self.reportTableView.dataSource = self;
        self.reportTableView.showsHorizontalScrollIndicator = NO;
        self.reportTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        self.reportTableView.backgroundColor = dt_F2F2F2_color;
        [self addSubview:self.reportTableView];
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(acceptMsg:) name:kGoTopNotificationName object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(acceptMsg:) name:kLeaveTopNotificationName object:nil];//其中一个TAB离开顶部的时候，如果其他几个偏移量不为0的时候，要把他们都置为0
        
    }
    return self;
    
}


-(void)acceptMsg : (NSNotification *)notification{
    //NSLog(@"%@",notification);
    NSString *notificationName = notification.name;
    if ([notificationName isEqualToString:kGoTopNotificationName]) {
        NSDictionary *userInfo = notification.userInfo;
        NSString *canScroll = userInfo[@"canScroll"];
        if ([canScroll isEqualToString:@"1"]) {
            self.canScroll = YES;
            self.reportTableView.showsVerticalScrollIndicator = YES;
        }
    }else if([notificationName isEqualToString:kLeaveTopNotificationName]){
        self.reportTableView.contentOffset = CGPointZero;
        self.canScroll = NO;
        self.reportTableView.showsVerticalScrollIndicator = NO;
    }
}

#pragma mark -UITableViewDelegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return _dataArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    ReportModel *rModel = _dataArray[section];
    ReportlistModel *listModel = rModel.result[0];
    // 如果为“1”则返回相应的行数，否则返回0
    if (rModel.result.count>1) {
        if ([listModel.reportId isEqualToString:@"1"]) {
            // 因为数组的第一位为标志位所以减1
            return ([rModel.result count] - 1);
        }
    }
    return 0;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 50;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 45;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    ReportModel *rModel = _dataArray[section];
    
    UIView *view = [[UIView alloc]init];
    view.userInteractionEnabled = YES;
    view.tag = section;
    view.backgroundColor = [UIColor whiteColor];
    UILabel *titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, 0, kScreenWidth-30, 40)];
    titleLabel.themeMap = @{kThemeMapKeyColorName : normalText_main_color};
    titleLabel.font = threeClassTextFont;
    titleLabel.textAlignment = NSTextAlignmentLeft;
    titleLabel.text=[NSString stringWithFormat:@"%@",rModel.title];
    [view addSubview:titleLabel];
    
    UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(15, 44.5, kScreenWidth, 0.5)];
    lineView.backgroundColor = dt_line_color;
    [view addSubview:lineView];
    titleLabel.tag = section;
    titleLabel.userInteractionEnabled = YES;
    [view addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapHeader:)]];
    
    
    
    UIButton *tapBtn = [[UIButton alloc]initWithFrame:CGRectMake(kScreenWidth-15-17, 0, 17, 45)];
    [tapBtn setTitleColor:dt_text_main_color forState:UIControlStateNormal];
    [tapBtn setImage:[UIImage imageNamed:@"home_more_icon"] forState:UIControlStateNormal];
    [tapBtn setImage:[UIImage imageNamed:@"home_down_icon"] forState:UIControlStateSelected];
    tapBtn.tag = section;
    if ([_btnArray[section] isEqualToString:@"1"]) {
        tapBtn.selected = YES;
    }
    else{
        tapBtn.selected = NO;
    }
    tapBtn.userInteractionEnabled = NO;
    [view addSubview:tapBtn];
    return view;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"reportID"];
    if (!cell) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"reportID"];
    }
    //为了防止重用
    for (UIView *view in cell.contentView.subviews) {
        [view removeFromSuperview];
    }
    
    ReportModel *Rmodel = _dataArray[indexPath.section];
    ReportlistModel *listModel = Rmodel.result[indexPath.row+1];
    
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(30, 0, 0, 0)];
    label.themeMap = @{kThemeMapKeyColorName : normalText_main_color};
    label.font = threeClassTextFont;
    label.textAlignment = NSTextAlignmentLeft;
    label.text = listModel.reportName;
    [cell.contentView addSubview:label];
    
    [label mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(UIEdgeInsetsMake(0, 30, 0.5, 15));
    }];
    
    UIView *lineView = [[UIView alloc]init];
    lineView.backgroundColor = dt_line_color;
    [cell.contentView addSubview:lineView];
    
    [lineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(UIEdgeInsetsMake(49.5, 30, 0, 0));
    }];
    
    return cell;
}

- (void)tapHeader:(UITapGestureRecognizer *)tap{
    
    UIView *label = (UIView *)tap.view;
    long section = label.tag;
    ReportModel *rModel = _dataArray[section];
    ReportlistModel *listModel = rModel.result[0];
    if ([listModel.reportId isEqualToString:@"0"]) {
        listModel.reportId = @"1";
        _btnArray[section] = @"1";
    }else{
        listModel.reportId = @"0";
        _btnArray[section] = @"0";
    }
    
    [self.reportTableView reloadData];
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
    
    if (self.delegate) {
        [self.delegate didSelectRowAtIndexPath:indexPath];
    }
    
}










- (void)scrollViewDidScroll:(UIScrollView *)scrollView{
    if (!self.canScroll) {
        [scrollView setContentOffset:CGPointZero];
    }
    CGFloat offsetY = scrollView.contentOffset.y;
    if (offsetY<0) {
        [[NSNotificationCenter defaultCenter] postNotificationName:kLeaveTopNotificationName object:nil userInfo:@{@"canScroll":@"1"}];
    }
}

-(void)dealloc{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

@end
