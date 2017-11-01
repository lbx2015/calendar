//
//  GtaskHistoryViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/3.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "GtaskHistoryViewController.h"
#import "GtasksModel.h"
#import "GtaskListTableViewCell.h"
#import "GtasksViewController.h"
@interface GtaskHistoryViewController ()

@end

@implementation GtaskHistoryViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.title = @"待办历史";
    [self initData];
    [self.view addSubview:self.dataTabView];
    self.dataTabView.separatorStyle = UITableViewCellSeparatorStyleNone;
}


- (void)initData{
    
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
        if (self.dataArray.count>0) {
            [self.dataArray removeAllObjects];
        }
        
        [self.dataArray addObjectsFromArray:[[RemindAndGtasksDBManager shareManager] getAllGtasksHistory]];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            //RKLog(@"%@",self.dataArray);
            [self.dataTabView reloadData];
        });
        
    });
    
    
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return self.dataArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    NSMutableArray *array = self.dataArray[section];
    
    return array.count+1;

}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 10;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    NSMutableArray *array = self.dataArray[indexPath.section];
    
    if (indexPath.row<array.count) {
        return 55;
    }else{
        return 30;
    }
    
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (self.dataArray.count>0) {
        NSMutableArray *array = self.dataArray[indexPath.section];
        
        if (indexPath.row<array.count) {
            
            GtaskListTableViewCell *gtaskCell = [tableView dequeueReusableCellWithIdentifier:@"gtaskCell"];
            
            if (!gtaskCell) {
                
                gtaskCell  = [[[NSBundle mainBundle]loadNibNamed:@"GtaskListTableViewCell" owner:self options:nil]lastObject];
            }
            gtaskCell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            GtasksModel *gModel = array[indexPath.row];
            RKLog(@"-----------------%@",gModel.content);
            
            if (indexPath.row == array.count-1) {
                gtaskCell.lineViewLeftLayout.constant=0;
            }
            
            [gtaskCell loadDataWith:gModel didSelectBtn:^(int buttonType,BOOL buttonStatus) {
                
                //完成
                if (buttonType==1) {
                    [array removeObjectAtIndex:indexPath.row];
                    if (array.count==0) {
                        [self.dataArray removeObject:array];
                    }
                    
                    [tableView reloadData];
                    
                    if (self.editGtask) {
                        self.editGtask(YES);
                    }
                }
            }];
            
            return gtaskCell;
            
            
            
//            gtaskCell.GtaskLabel.text = gModel.content;
//            gtaskCell.GtaskLabel.font = threeClassTextFont;
//            gtaskCell.GtaskLabel.textColor = dt_text_main_color;
//            gtaskCell.lineView.backgroundColor = dt_line_color;
//            gtaskCell.finshBtn.tag = indexPath.row;
//            [gtaskCell.finshBtn addTarget:self action:@selector(finshAction:) forControlEvents:UIControlEventTouchUpInside];
//            [gtaskCell.finshBtn setImage:[UIImage imageNamed:@"gtaskFinsh"] forState:UIControlStateNormal];
//            [gtaskCell.finshBtn setImage:[UIImage imageNamed:@"gtask_unfinish"] forState:UIControlStateSelected];
//            gtaskCell.finshBtn.selected = NO;
//            
//            [gtaskCell.isImportantBtn setImage:[UIImage imageNamed:@"programme_Important_normalStars"] forState:UIControlStateNormal];
//            [gtaskCell.isImportantBtn setImage:[UIImage imageNamed:@"programme_Important_selectStars"] forState:UIControlStateSelected];
//            
//            if (gModel.isImportant) {
//                gtaskCell.isImportantBtn.selected = YES;
//            }else{
//                gtaskCell.isImportantBtn.selected = NO;
//            }
//            
//           
//        
//            return gtaskCell;
        }else{
            
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cellID"];
            if (!cell) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cellID"];
            }
            //为了防止重用
            for (UIView *view in cell.contentView.subviews) {
                
                [view removeFromSuperview];
            }
            
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            
            GtasksModel *gModel = [self.dataArray[indexPath.section] lastObject];
            
            UILabel *timeLabel = [self createMainLabelWithText:[NSString stringWithFormat:@"%@ %@",NSLocalizedString(@"Completed", nil),[Utils distanceWithBeforeTime:gModel.completeDate]]];
    
            timeLabel.font = sevenClassTextFont;
            
            timeLabel.textColor = dt_textLightgrey_color;
            
            timeLabel.textAlignment = NSTextAlignmentRight;
            
            [cell.contentView addSubview:timeLabel];
        
            [timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
                
                make.edges.mas_equalTo(UIEdgeInsetsMake(0, 20, 0, 15));
                
            }];
            
            
            return cell;
            
        }
    }
    else{
        return nil;
    }
}


- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath{
    
    NSMutableArray *array = self.dataArray[indexPath.section];
    
    if (indexPath.row<array.count) {
        
        return YES;
    }
    
    return NO;
}

- (NSArray<UITableViewRowAction *> *)tableView:(UITableView *)tableView editActionsForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    NSMutableArray *array = self.dataArray[indexPath.section];
    GtasksModel *gModel = array[indexPath.row];
//    UITableViewRowAction *editRowAction = [UITableViewRowAction  rowActionWithStyle:UITableViewRowActionStyleDefault title:@"编辑" handler:^(UITableViewRowAction * _Nonnull action, NSIndexPath * _Nonnull indexPath) {
//        
//        [self.dataTabView reloadData];
//        
//        GtasksViewController *vc = [[GtasksViewController alloc]init];
//        vc.gtaskModel = gModel;
//        vc.isEdit = YES;
//        vc.editGtask = ^(){
//            
//            [self initData];
//            
//        };
//        [self.navigationController pushViewController:vc animated:YES];
//        
//        
//        
//    }];
//    
//    editRowAction.backgroundColor = dt_app_main_color;
    
    UITableViewRowAction *deleteRowAction = [UITableViewRowAction rowActionWithStyle:UITableViewRowActionStyleDefault title:@"删除" handler:^(UITableViewRowAction * _Nonnull action, NSIndexPath * _Nonnull indexPath) {
        
        @KKWeak(self);
        [[RemindAndGtasksDBManager shareManager] doSaveGtasksNetWorkWithGtasksModel:gModel editType:3 success:^(BOOL ret) {
            @KKStrong(self);
            
            if (array.count>0) {
                [array removeObjectAtIndex:indexPath.row];
            }
            
            if (array.count==0) {
                [self.dataArray removeObjectAtIndex:indexPath.section];
            }
            [tableView reloadData];
        }];
        
    }];
    
    
    if (indexPath.row<array.count) {
        return @[deleteRowAction];
    }else{
        return nil;
    }
    
}



#pragma mark - 重新编辑
- (void)finshAction:(UIButton *)sender{
    
    sender.selected = !sender.selected;
    
    UITableViewCell *cell = (UITableViewCell *)[sender superview];
    NSIndexPath *indexPath = [self.dataTabView indexPathForCell:cell];
    GtasksModel *gModel = self.dataArray[indexPath.row];
    gModel.isComplete = YES;
    gModel.completeDate = [Utils getCurrentTime];
    
    @KKWeak(self);
    [[RemindAndGtasksDBManager shareManager] doSaveGtasksNetWorkWithGtasksModel:gModel editType:2 success:^(BOOL ret) {
        @KKStrong(self);
        
        [self.dataArray removeObjectAtIndex:indexPath.row];
        [self.dataTabView reloadData];
        
        if (self.editGtask) {
            self.editGtask(YES);
        }
    }];
    
    
    
//    UITableViewCell *cell = (UITableViewCell *)[sender superview];
//    
//    NSIndexPath *indexPath = [self.dataTabView indexPathForCell:cell];
//    
//    dispatch_async(dispatch_get_global_queue(0,0), ^{
//        GtasksModel *gModel = self.dataArray[indexPath.section][indexPath.row];
//        gModel.isComplete = NO;
//        gModel.completeDate = [Utils getCurrentTime];
//        
//        BOOL ret =  [gModel update];
//        
//        dispatch_async(dispatch_get_main_queue(), ^{
//            
//            if (ret) {
//               
//                NSLog(@"indexPath is = %ld",indexPath.row);
//                
//                NSMutableArray *array = self.dataArray[indexPath.section];
//                [array removeObjectAtIndex:indexPath.row];
//                
//                if (array.count==0) {
//                    [self.dataArray removeObjectAtIndex:indexPath.section];
//                }
//    
//                [self.dataTabView reloadData];
//                
//                if (self.editGtask) {
//                    self.editGtask(YES);
//                }
//                
//            }
//            
//        });
//        
//    });
    
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
