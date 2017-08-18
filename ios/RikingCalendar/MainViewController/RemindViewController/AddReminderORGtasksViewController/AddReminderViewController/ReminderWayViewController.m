//
//  ReminderWayViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/7.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ReminderWayViewController.h"
#import "ReminderDatePickerView.h"
@interface ReminderWayViewController ()

{
    NSMutableArray *_titieArray;
    ReminderModel *_rModel;
    NSMutableArray *_chooseWay;
}
@end

@implementation ReminderWayViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.

    self.title = @"提醒方式";
    [self setRightButtonWithTitle:@[@"确定"]];
    [self initData];
    [self.view addSubview:self.dataTabView];
    
}

- (void)initData{
    _titieArray = [NSMutableArray arrayWithObjects:@"不提醒",@"正点提醒",@"自定义", nil];
    _chooseWay = [NSMutableArray array];
    [_chooseWay addObject:@"1"];
    _rModel = [[ReminderModel alloc]init];
    _rModel = self.reminderModel;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return _titieArray.count;
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
    
    titleLabel.text = _titieArray[indexPath.row];
    
    if (indexPath.row==2) {
        
        if (_rModel.beforeTime>0) {
            titleLabel.text = [NSString stringWithFormat:@"提前%d分",_rModel.beforeTime];
        }
    }
    
    UIButton *sureBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [sureBtn setImage:[UIImage imageNamed:@"chooseTime_sure"] forState:UIControlStateSelected];
    [sureBtn addTarget:self action:@selector(chooseWay:) forControlEvents:UIControlEventTouchUpInside];
    [cell.contentView addSubview:sureBtn];
    
    
    [sureBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.top.bottom.equalTo(cell.contentView).offset(0);
        make.right.equalTo(cell.contentView).offset(-15);
        make.width.mas_equalTo(28);
        
    }];
    
    sureBtn.tag = indexPath.row;
    
    if (!_rModel.isRemind) {
        if (indexPath.row==0) {
            sureBtn.selected = YES;
        }
    }else{
        
        if (_rModel.beforeTime==0) {
            if (indexPath.row==1) {
                sureBtn.selected = YES;
            }
        }else{
            if (indexPath.row==2) {
                sureBtn.selected = YES;
            }
        }
        
    }
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
 
    if (_chooseWay.count==1 && [[_chooseWay lastObject] integerValue]==indexPath.row) {
        return;
    }
    
    [_chooseWay removeAllObjects];
    [_chooseWay addObject:[NSString stringWithFormat:@"%ld",indexPath.row]];
    
    if (indexPath.row==0) {
        _rModel.isRemind = 0;
    }else{
        _rModel.isRemind = 1;
        if (indexPath.row==1) {
            _rModel.beforeTime = 0;
        }else{
            [self chooseTime];
        }
    }
    
    [self.dataTabView reloadData];
}


#pragma mark - 选择提醒方式
- (void)chooseWay:(UIButton *)sender{
    
    if (_chooseWay.count==1 && [[_chooseWay lastObject] integerValue]==sender.tag) {
        return;
    }
    sender.selected = !sender.selected;
    [_chooseWay removeAllObjects];
    [_chooseWay addObject:[NSString stringWithFormat:@"%ld",sender.tag]];
    if (sender.tag==0) {
        _rModel.isRemind = 0;
    }else{
        _rModel.isRemind = 1;
        if (sender.tag==1) {
            _rModel.beforeTime = 0;
        }else{
            [self chooseTime];
        }
    }
    [self.dataTabView reloadData];
}


- (void)doRightAction:(UIButton *)sender{
    
    if (self.reminderWay) {
        self.reminderWay(_rModel);
    }
    
    [self.navigationController popViewControllerAnimated:YES];
}


- (void)chooseTime{
    ReminderDatePickerView *reminderVC = [[ReminderDatePickerView alloc]init];
    
    [reminderVC setDateStyle:ReminderStyleShowMinute CompleteBlock:^(NSString *time) {
        
        _rModel.beforeTime = [time intValue];
        [self.dataTabView reloadData];
    }];
    
    [reminderVC show];
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
