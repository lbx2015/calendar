//
//  ChooseRepeatViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/26.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ChooseRepeatViewController.h"

@interface ChooseRepeatViewController ()

@property (nonatomic, strong)NSMutableArray *repeatArray;

@end

@implementation ChooseRepeatViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self initData];
    [self initUI];
    
    
}

#pragma mark - 加载UI
- (void)initUI{
    [self.view addSubview:self.dataTabView];
}

#pragma mark - 初始化数据源
- (void)initData{
    
    self.repeatArray = [NSMutableArray arrayWithObjects:@"不重复",@"每周一",@"每周二",@"每周三",@"每周四",@"每周五",@"每周六",@"每周日",@"法定工作日重复(智能跳过节假日)",@"法定节假日重复(智能跳过工作日)", nil];
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.repeatArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return 50;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cellID"];
    if (!cell) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cellID"];
    }
    
    //为了防止重用
    for (UIView *view in cell.contentView.subviews) {
        
        [view removeFromSuperview];
    }
    
    
    UILabel *titleLabel
    
    
    
    
    return cell;
    
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
