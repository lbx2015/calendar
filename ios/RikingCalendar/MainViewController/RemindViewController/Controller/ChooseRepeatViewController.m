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

@property (nonatomic,strong)UIButton *selectBtn;

@end

@implementation ChooseRepeatViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = @"重复时间";
    [self setRightButtonWithTitle:@[@"确定"]];
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
    
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    UILabel *titleLabel = [[UILabel alloc]init];
    titleLabel.textColor = dt_text_main_color;
    titleLabel.font = threeClassTextFont;
    [cell.contentView addSubview:titleLabel];
    [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.edges.mas_equalTo(UIEdgeInsetsMake(0, 15, 0, 58));
        
    }];
    
    titleLabel.text = self.repeatArray[indexPath.row];
    
    
    
    
    UIButton *sureBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [sureBtn setImage:[UIImage imageNamed:@"chooseTime_sure"] forState:UIControlStateSelected];
    [sureBtn addTarget:self action:@selector(chooseTime:) forControlEvents:UIControlEventTouchUpInside];
    [cell.contentView addSubview:sureBtn];
    
    [sureBtn mas_makeConstraints:^(MASConstraintMaker *make) {
       
        make.top.bottom.equalTo(cell.contentView).offset(0);
        make.right.equalTo(cell.contentView).offset(-15);
        make.width.mas_equalTo(28);
        
    }];
    
    sureBtn.tag = indexPath.row;
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    NSArray *arr = [self.dataTabView indexPathsForVisibleRows];
    for (NSIndexPath *indexPath01 in arr)
    {
        //根据索引，获取cell 然后就可以做你想做的事情啦
        UITableViewCell *cell = [self.dataTabView cellForRowAtIndexPath:indexPath01];
        //我这里要隐藏cell 的图片
        for (UIView *view in cell.contentView.subviews) {
            
            if ([view isKindOfClass:[UIButton class]]) {
                
                UIButton *button = (UIButton *)view;
                
                if (indexPath.row == button.tag) {
                     button.selected = !button.selected;
                }
               
                if (indexPath.row == 0 || indexPath.row  == 8 || indexPath.row == 9) {
                    if (button.tag != indexPath.row ) {
                        button.selected = NO;
                    }
                }
                else
                {
                    if (button.tag == 0 || button.tag == 8 || button.tag == 9) {
                        button.selected = NO;
                    }
                }
                
            }
        }
    }
    
    
}


- (void)chooseTime:(UIButton *)sender{
    
    sender.selected = !sender.selected;
    NSArray *arr = [self.dataTabView indexPathsForVisibleRows];
    for (NSIndexPath *indexPath in arr)
    {
        //根据索引，获取cell 然后就可以做你想做的事情啦
        UITableViewCell *cell = [self.dataTabView cellForRowAtIndexPath:indexPath];
        //我这里要隐藏cell 的图片
        for (UIView *view in cell.contentView.subviews) {
            
            if ([view isKindOfClass:[UIButton class]]) {
                
                UIButton *button = (UIButton *)view;
                
                if (sender.tag == 0 || sender.tag == 8 || sender.tag == 9) {
                    if (button.tag != sender.tag) {
                        button.selected = NO;
                    }
                }
                else
                {
                    if (button.tag == 0 || button.tag == 8 || button.tag == 9) {
                        button.selected = NO;
                    }
                }
                
               
            }
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
