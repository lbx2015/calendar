//
//  ReminderHistoryViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/7.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ReminderHistoryViewController.h"
#import "ReminderHistoryModel.h"
@interface ReminderHistoryViewController ()

@end

@implementation ReminderHistoryViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = @"提醒历史";
    [self initData];
    [self.view addSubview:self.dataTabView];
    
}

- (void)initData{
    
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
        NSArray *gtaskArray = [ReminderModel findByCriteria:[NSString stringWithFormat:@" where repeatFlag=0 and strDate<'%@' order by strDate desc,startTime desc",[NSString stringWithFormat:@"%@",[Utils getCurrentTimeWithTimeFormat:@"yyyyMMdd"]]]];
        //遍历时，时间相同的装在同一个数组中，先取_dataArray[0]分一组
        
        if (gtaskArray.count>0) {
            
            if (self.dataArray.count>0) {
                [self.dataArray removeAllObjects];
            }
            
            NSMutableArray *currentArr=[NSMutableArray array];
            [currentArr addObject:gtaskArray[0]];
            
            [self.dataArray addObject:currentArr];
            
            if(gtaskArray.count>1)
            {
                for (int i=1;i<gtaskArray.count;i++)
                {
                    
                    //取上一组元素并获取上一组元素的比较日期
                    
                    NSMutableArray *preArr=[self.dataArray objectAtIndex:self.dataArray.count-1];
                    
                    ReminderModel *historyModel = [preArr lastObject];
                    
                    NSString *pretime = [historyModel.strDate substringToIndex:8];
                    
                    //取当前遍历的字典中的日期
                    ReminderModel *currentModel = [gtaskArray objectAtIndex:i];
                    
                    NSString *time = [currentModel.strDate substringToIndex:8];
                    
                    //如果遍历当前字典的日期和上一组元素中日期相同则把当前字典分类到上一组元素中
                    if([time isEqualToString:pretime])
                    {
                        [currentArr addObject:currentModel];
                    }
                    //如果当前字典的日期和上一组元素日期不同，则重新开新的一组，把这组放入到分类数组中
                    else
                    {
                        currentArr=[NSMutableArray array];
                        
                        [currentArr addObject:currentModel];
                        
                        [self.dataArray addObject:currentArr];
                        
                    }
                }
            }
            
        }
        
        
        dispatch_async(dispatch_get_main_queue(), ^{
            
            RKLog(@"%@",self.dataArray);
            
            [self.dataTabView reloadData];
        });
        
    });
    
    
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return self.dataArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return [self.dataArray[section] count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 55;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 30;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    UILabel *timeLabel = [[UILabel alloc]init];
    timeLabel.textColor = dt_app_main_color;
    timeLabel.font = threeClassTextFont;
    [cell.contentView addSubview:timeLabel];
    
    ReminderModel *rModel = self.dataArray[indexPath.section][indexPath.row];
    
    NSString *title = [NSString stringWithFormat:@"%@  %@",rModel.startTime,rModel.content];
    if (rModel.isAllday) {
        title = [NSString stringWithFormat:@"全天  %@",rModel.content];
        NSMutableAttributedString* deliveryAttrString = [[NSMutableAttributedString alloc]initWithString:title];
        [deliveryAttrString addAttribute:NSForegroundColorAttributeName value:dt_text_888888_color range:NSMakeRange(0, 2)];
        [deliveryAttrString addAttribute:NSFontAttributeName value:eightClassTextFont range:NSMakeRange(0, 2)];
        [deliveryAttrString addAttribute:NSForegroundColorAttributeName value:dt_text_main_color range:NSMakeRange(4, rModel.content.length)];
        [deliveryAttrString addAttribute:NSFontAttributeName value:threeClassTextFont range:NSMakeRange(4, rModel.content.length)];
        timeLabel.attributedText = deliveryAttrString;
    }else{
        NSMutableAttributedString* deliveryAttrString = [[NSMutableAttributedString alloc]initWithString:title];
        [deliveryAttrString addAttribute:NSForegroundColorAttributeName value:dt_text_888888_color range:NSMakeRange(0, 5)];
        [deliveryAttrString addAttribute:NSFontAttributeName value:eightClassTextFont range:NSMakeRange(0, 5)];
        [deliveryAttrString addAttribute:NSForegroundColorAttributeName value:dt_text_main_color range:NSMakeRange(7, rModel.content.length)];
        [deliveryAttrString addAttribute:NSFontAttributeName value:threeClassTextFont range:NSMakeRange(7, rModel.content.length)];
        timeLabel.attributedText = deliveryAttrString;
    }
    
    [timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(UIEdgeInsetsMake(0, 15, 0, 15));
    }];
    
    
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    UIView *view = [[UIView alloc]init];
    view.backgroundColor =[UIColor whiteColor];
    UILabel *timeLabel = [[UILabel alloc]initWithFrame:CGRectMake(kScreenWidth-100, 0, 100, 32)];
    timeLabel.font = sevenClassTextFont;
    timeLabel.textColor = dt_text_818181_color;
    timeLabel.textAlignment = NSTextAlignmentLeft;
    
    
    
    NSArray *array = self.dataArray[section];
    ReminderModel *hModel = [array firstObject];
    NSString *dateStr = [Utils setOldStringTime:hModel.strDate inputFormat:@"yyyyMMdd" outputFormat:@"MM月dd日"];
    NSString *weekDay = [Utils getWeekDayWithDateStr:hModel.strDate formatter:@"yyyyMMdd"];
    timeLabel.text = [NSString stringWithFormat:@"%@ %@",dateStr,weekDay];
    [view addSubview:timeLabel];
    
    return view;
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath{
    return YES;
}


- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(nonnull NSIndexPath *)indexPath{
    
    NSMutableArray *array = self.dataArray[indexPath.section];
    ReminderModel *hModel = array[indexPath.row];
    
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        
        BOOL ret = [hModel deleteObject];
        
        if (ret) {
            
            [array removeObject:hModel];
            
            if (array.count==0) {
                [self.dataArray removeObjectAtIndex:indexPath.section];
            }
            
            [self.dataTabView reloadData];
            
        }
        
        
    }
    
}


- (NSString *)tableView:(UITableView *)tableView titleForDeleteConfirmationButtonForRowAtIndexPath:(NSIndexPath *)indexPath{
    return @"删除";
}





- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
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
