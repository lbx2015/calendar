//
//  RKBaseTabViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/26.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseTabViewController.h"

@interface RKBaseTabViewController ()



@end

@implementation RKBaseTabViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}



-(UITableView *)dataTabView
{
    if (!_dataTabView) {
        _dataTabView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, kScreenWidth, kScreenHeight-64) style:UITableViewStyleGrouped];

        _dataTabView.delegate = self;
        _dataTabView.dataSource = self;
        _dataTabView.showsVerticalScrollIndicator = NO;
        _dataTabView.showsHorizontalScrollIndicator = NO;
        _dataTabView.backgroundColor = [UIColor clearColor];
        if ([ _dataTabView respondsToSelector:@selector(setSeparatorInset:)]) {
            [_dataTabView   setSeparatorInset:UIEdgeInsetsMake(0, 0, 0, 0)];
        }
        if ([_dataTabView respondsToSelector:@selector(setLayoutMargins:)]) {
            [_dataTabView setLayoutMargins:UIEdgeInsetsMake(0, 0, 0, 0)];
        }
        
        if([_dataTabView respondsToSelector:@selector(setPreservesSuperviewLayoutMargins:)]){
            [_dataTabView setPreservesSuperviewLayoutMargins:NO];
        }
        
        [_dataTabView setTableFooterView:[[UIView alloc] initWithFrame:CGRectZero]];
        
        [self.view addSubview:_dataTabView];
        
        [_dataTabView mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.edges.mas_equalTo(UIEdgeInsetsMake(0, 0, 0, 0));
            
        }];
        
    }
    return _dataTabView;
}

- (NSMutableArray *)dataArray{
    
    if (!_dataArray) {
        _dataArray = [NSMutableArray array];
    }
    return _dataArray;
}


- (void)updateTabViewFrameWithTop:(CGFloat)top left:(CGFloat)left right:(CGFloat)right bottom:(CGFloat)bottom {

    @KKWeak(self);
    [self.dataTabView mas_updateConstraints:^(MASConstraintMaker *make) {
        @KKStrong(self);
        
        make.top.mas_equalTo(self.view).offset(top);
        make.left.mas_equalTo(self.view).offset(left);
        make.right.mas_equalTo(self.view).offset(right);
        make.bottom.mas_equalTo(self.view).offset(bottom);
        
    }];
    
}


#pragma mark -UITableViewDelegate
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 10;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 49;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 0.0001;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    
    return 0.0001;
}


-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cellID"];
    if (!cell) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cellID"];
    }
    
    //为了防止重用
    for (UIView *view in cell.contentView.subviews) {
        
        [view removeFromSuperview];
    }
    
    
    
    
    
    return cell;
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
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
