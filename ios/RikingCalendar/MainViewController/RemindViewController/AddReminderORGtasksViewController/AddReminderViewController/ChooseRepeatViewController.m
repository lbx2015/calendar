//
//  ChooseRepeatViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/26.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ChooseRepeatViewController.h"

@interface ChooseRepeatViewController ()

{
    NSMutableArray *_repeatStrArray;
}

@property (nonatomic, strong)NSMutableArray *repeatArray;

@property (nonatomic,strong)UIButton *selectBtn;

@property (nonatomic,copy)NSMutableString *repeatTime;


@end

@implementation ChooseRepeatViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = NSLocalizedString(@"repeat_time", nil);
    [self setRightButtonWithTitle:@[NSLocalizedString(@"sure", nil)]];
    [self initData];
    [self initUI];
    
    
}

- (NSMutableString *)repeatTime{
    
    if (!_repeatTime) {
        _repeatTime = [[NSMutableString alloc]init];
    }
    
    return _repeatTime;
}

#pragma mark - 加载UI
- (void)initUI{
    [self.view addSubview:self.dataTabView];
}

#pragma mark - 初始化数据源
- (void)initData{
    
    _repeatStrArray = [NSMutableArray  array];
    if (self.reminderModel.repeatFlag ==3 && self.reminderModel.repeatValue.length>1) {
        [_repeatStrArray addObjectsFromArray:[self.reminderModel.repeatValue componentsSeparatedByString:@"," ]];
    }else{
        [_repeatStrArray addObject:self.reminderModel.repeatValue];
    }
//    "not_repeat"                        = "不重复";
//    "Monday"                            = "每周一";
//    "Tuesday"                           = "每周二";
//    "Wednesday"                         = "每周三";
//    "Thursday"                          = "每周四";
//    "Friday"                            = "每周五";
//    "Saturday"                          = "每周六";
//    "Sunday"                            = "每周日";
//    "Worksday"                          = "法定工作日重复(智能跳过节假日)";
//    "Legalholiday"                      = "法定节假日重复(智能跳过工作日)";
    
    self.repeatArray = [NSMutableArray arrayWithObjects:NSLocalizedString(@"not_repeat", nil),NSLocalizedString(@"Monday", nil),NSLocalizedString(@"Tuesday", nil),NSLocalizedString(@"Wednesday", nil),NSLocalizedString(@"Thursday", nil),NSLocalizedString(@"Friday", nil),NSLocalizedString(@"Saturday", nil),NSLocalizedString(@"Sunday", nil),NSLocalizedString(@"Worksday", nil),NSLocalizedString(@"Legalholiday", nil), nil];
    
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
    
    
    
    
    for (NSString *str in _repeatStrArray) {
        
        if ([str intValue] == indexPath.row) {
            sureBtn.selected = YES;
            break;
        }
        
    }
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (_repeatStrArray.count==1 && [[_repeatStrArray lastObject] intValue] == indexPath.row) {
        return;
    }
    
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
                    
                    if (button.selected) {
                        [_repeatStrArray addObject:[NSString stringWithFormat:@"%ld",button.tag]];
                    }else{
                        [_repeatStrArray removeObject:[NSString stringWithFormat:@"%ld",button.tag]];
                    }
                }
               
                if (indexPath.row == 0 || indexPath.row  == 8 || indexPath.row == 9) {
                    if (button.tag != indexPath.row ) {
                        button.selected = NO;
                        [_repeatStrArray removeObject:[NSString stringWithFormat:@"%ld",button.tag]];
                    }
                }
                else
                {
                    if (button.tag == 0 || button.tag == 8 || button.tag == 9) {
                        button.selected = NO;
                        [_repeatStrArray removeObject:[NSString stringWithFormat:@"%ld",button.tag]];
                    }
                }
                
            }
        }
    }
    
    RKLog(@"%@",_repeatStrArray);
}


- (void)chooseTime:(UIButton *)sender{
    
    if (_repeatStrArray.count==1 && [[_repeatStrArray lastObject] intValue] == sender.tag) {
        return;
    }
    
    sender.selected = !sender.selected;
    if (sender.selected) {
        [_repeatStrArray addObject:[NSString stringWithFormat:@"%ld",sender.tag]];
    }else{
        [_repeatStrArray removeObject:[NSString stringWithFormat:@"%ld",sender.tag]];
    }
    
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
                        [_repeatStrArray removeObject:[NSString stringWithFormat:@"%ld",button.tag]];
                    }
                }
                else
                {
                    if (button.tag == 0 || button.tag == 8 || button.tag == 9) {
                        button.selected = NO;
                        [_repeatStrArray removeObject:[NSString stringWithFormat:@"%ld",button.tag]];
                    }
                }
                
               
            }
        }
    }
    
    RKLog(@"%@",_repeatStrArray);
}


- (void)doRightAction:(UIButton *)sender{
    
    NSArray *result = [_repeatStrArray sortedArrayUsingComparator:^NSComparisonResult(id  _Nonnull obj1, id  _Nonnull obj2) {
        
//        RKLog(@"%@~%@",obj1,obj2);
        
        //降序
//        return [obj2 compare:obj1];
        
        //升序
         return [obj1 compare:obj2];
        
        
    }];
    
    RKLog(@"result=%@",result);
    
    NSString *str = [result componentsJoinedByString:@","];
    
    if (self.chooseRepeatTime) {
        self.chooseRepeatTime(str);
    }
    
    [self.navigationController popViewControllerAnimated:YES];
    
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
