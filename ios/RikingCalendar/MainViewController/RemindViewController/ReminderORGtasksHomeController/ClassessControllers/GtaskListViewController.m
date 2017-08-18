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
#import "GtasksViewController.h"
#import "DIYMJRefreshHaderHistory.h"
#import "GtasksModel.h"
#import "GtaskHistoryViewController.h"
#import "ReminderAndGtasksDetailView.h"


@interface GtaskListViewController ()
 <UITextViewDelegate,DIYMJRefreshHaderHistoryDelegate>

{
    NSIndexPath *_selectIndexPath;
    
    BOOL _isHiddenBtn;
    BOOL _isHiddenLabel;
    BOOL _isHiddenTextView;
    CGFloat _textViewCellHight;
    NSString *_textViewText;
    
    BOOL _isShowHistory;//是否显示历史;
}

@property (nonatomic,strong)DIYMJRefreshHaderHistory *headerRefresh;

@end

@implementation GtaskListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.view addSubview:self.dataTabView];
    if ([ self.dataTabView respondsToSelector:@selector(setSeparatorInset:)]) {
        [self.dataTabView   setSeparatorInset:UIEdgeInsetsMake(0, 0, 0, 0)];
    }
    if ([self.dataTabView respondsToSelector:@selector(setLayoutMargins:)]) {
        [self.dataTabView setLayoutMargins:UIEdgeInsetsMake(0, 0, 0, 0)];
    }
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(newGtask) name:@"addGtask" object:nil];
    self.dataTabView.header = self.headerRefresh;
    _isHiddenBtn = YES;
    _isHiddenLabel = NO;
    _isHiddenTextView = YES;
    _textViewCellHight = 55;
    [self initData];
    
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];

}


- (void)newGtask{
    [self initData];
}

- (void)initData{
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
        [self.dataArray removeAllObjects];
        [self.dataArray addObjectsFromArray:[[RemindAndGtasksDBManager shareManager] getAllGtasksArray]];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            
            // 通知主线程刷新 神马的
            [self.dataTabView reloadData];
            
        });
        
        RKLog(@"%@",self.dataArray);
    });
}

- (void)userSwitch{
    [self initData];
}


- (DIYMJRefreshHaderHistory *)headerRefresh{
    if (!_headerRefresh) {
        _headerRefresh = [DIYMJRefreshHaderHistory headerWithRefreshingBlock:^{
            
        }];
    
        _headerRefresh.btnName = @"查看待办历史";
        
        _headerRefresh.delegate = self;
    }
    
    return _headerRefresh;
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.dataArray.count+1;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.row == self.dataArray.count) {
        return _textViewCellHight;
    }
    
    return 55;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 10;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 10;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.row<self.dataArray.count) {
        
        GtaskListTableViewCell *gtaskCell = [tableView dequeueReusableCellWithIdentifier:@"gtaskCell"];
        
        if (!gtaskCell) {
            
            gtaskCell  = [[[NSBundle mainBundle]loadNibNamed:@"GtaskListTableViewCell" owner:self options:nil]firstObject];
        }
        gtaskCell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        GtasksModel *gModel = self.dataArray[indexPath.row];
        
        gtaskCell.GtaskLabel.text = gModel.content;
        gtaskCell.GtaskLabel.font = threeClassTextFont;
        gtaskCell.GtaskLabel.textColor = dt_text_main_color;
        gtaskCell.finshBtn.tag = indexPath.row;
        [gtaskCell.finshBtn addTarget:self action:@selector(finshAction:) forControlEvents:UIControlEventTouchUpInside];
        [gtaskCell.finshBtn setImage:[UIImage imageNamed:@"gtask_unfinish"] forState:UIControlStateNormal];
        [gtaskCell.finshBtn setImage:[UIImage imageNamed:@"gtaskFinsh"] forState:UIControlStateSelected];
        gtaskCell.finshBtn.selected = NO;
        
        [gtaskCell.isImportantBtn setImage:[UIImage imageNamed:@"programme_Important_normalStars"] forState:UIControlStateNormal];
        [gtaskCell.isImportantBtn setImage:[UIImage imageNamed:@"programme_Important_selectStars"] forState:UIControlStateSelected];
        
        if (gModel.isImportant) {
            gtaskCell.isImportantBtn.selected = YES;
        }else{
            gtaskCell.isImportantBtn.selected = NO;
        }
        
        //是否开启提醒
        if (gModel.isOpen) {
            //gtaskCell.GtaskLabel.textColor = [UIColor orangeColor];
        }
        
        
        
        return gtaskCell;
    }
    else{
        
        SpeedCreateGtaskTableViewCell *speedCell = [tableView dequeueReusableCellWithIdentifier:@"speedCell"];
        
        if (!speedCell) {
            
            speedCell  = [[[NSBundle mainBundle]loadNibNamed:@"SpeedCreateGtaskTableViewCell" owner:self options:nil]firstObject];
        }
        
        speedCell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        speedCell.SpeedCreateBtn.layer.borderWidth = 1;
        speedCell.SpeedCreateBtn.layer.borderColor = dt_app_main_color.CGColor;
        speedCell.SpeedCreateBtn.layer.cornerRadius = 4;
        speedCell.SpeedCreateBtn.titleLabel.font = fiveClassTextFont;
        speedCell.SpeedCreateBtn.titleLabel.textColor = dt_app_main_color;
        speedCell.SpeedCreateBtn.hidden = _isHiddenBtn;
        [speedCell.SpeedCreateBtn addTarget:self action:@selector(createAction:) forControlEvents:UIControlEventTouchUpInside];
        
        speedCell.SpeedTextView.placeholder = @"快速添加";
        speedCell.SpeedTextView.font = fiveClassTextFont;
        speedCell.SpeedTextView.delegate = self;
        speedCell.SpeedTextView.hidden = _isHiddenTextView;
        speedCell.SpeedTextView.text = _textViewText;
        
        speedCell.speedCreateGtaskLabel.hidden = _isHiddenLabel;
        speedCell.speedCreateGtaskLabel.font = fourClassTextFont;
        speedCell.speedCreateGtaskLabel.themeMap = @{kThemeMapKeyColorName : lightText_color};
        
        return speedCell;
        
    }
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.row==self.dataArray.count) {
        
        _selectIndexPath = indexPath;
        
        NSIndexPath *indexPath = [tableView indexPathForSelectedRow];
        
        SpeedCreateGtaskTableViewCell *cell = [tableView cellForRowAtIndexPath: indexPath];
        _isHiddenLabel = YES;
        _isHiddenTextView = NO;
        
        cell.SpeedTextView.hidden = _isHiddenTextView;
        cell.speedCreateGtaskLabel.hidden = _isHiddenLabel;
        [cell.SpeedTextView becomeFirstResponder];
    }else{
        
        
        GtasksModel *gModel = self.dataArray[indexPath.row];
        
        ReminderAndGtasksDetailView *detailVC = [[ReminderAndGtasksDetailView alloc]init];
        
        @KKWeak(self);
        [detailVC setDetailViewWithModel:gModel type:2 clickBtnType:^(int buttonType) {
            @KKStrong(self);
            RKLog(@"%d",buttonType);
            
            if (buttonType==1) {
                
                GtasksViewController *vc = [[GtasksViewController alloc]init];
                vc.gtaskModel = gModel;
                vc.isEdit = YES;
                [self.navigationController pushViewController:vc animated:YES];
                
            }else{
                BOOL ret = [gModel deleteObject];
                if (ret) {
                    
                    [self.dataArray removeObjectAtIndex:indexPath.row];
                    
                    [self.dataTabView reloadData];
                }
            }
            
        }];
        
        [detailVC show];
        
        
    }
    
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row<self.dataArray.count) {
        
        return YES;
    }
    
    return NO;
}

- (NSArray<UITableViewRowAction *> *)tableView:(UITableView *)tableView editActionsForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    GtasksModel *model = self.dataArray[indexPath.row];
    
    UITableViewRowAction *editRowAction = [UITableViewRowAction  rowActionWithStyle:UITableViewRowActionStyleDefault title:@"编辑" handler:^(UITableViewRowAction * _Nonnull action, NSIndexPath * _Nonnull indexPath) {
        
        GtasksViewController *vc = [[GtasksViewController alloc]init];
        vc.gtaskModel = model;
        vc.isEdit = YES;
        [self.navigationController pushViewController:vc animated:YES];
        
        
    }];
    
    editRowAction.backgroundColor = dt_app_main_color;
    
    UITableViewRowAction *deleteRowAction = [UITableViewRowAction rowActionWithStyle:UITableViewRowActionStyleDefault title:@"删除" handler:^(UITableViewRowAction * _Nonnull action, NSIndexPath * _Nonnull indexPath) {
        
        BOOL ret = [model deleteObject];
        
        [self doSaveRemindAndGtasksWithRequestStyle:gtasksSaveUpdate model:model isHaveAlert:NO waitTitle:nil success:^(id dictData) {
            model.deleteState = 1;
        } failure:^(NSString *message) {
            
        }];
        if (ret) {
            
            [self.dataArray removeObjectAtIndex:indexPath.row];
        }
        
        [self.dataTabView reloadData];
        
    }];
    
    
    if (indexPath.row<self.dataArray.count) {
       return @[deleteRowAction,editRowAction];
    }else{
        return nil;
    }
    
}


#pragma mark - 查看待办历史
- (void)showHistory{
    
    RKLog(@"查看待办历史");
    
    GtaskHistoryViewController *historyVC = [[GtaskHistoryViewController alloc]init];
    
    @KKWeak(self);
    historyVC.editGtask = ^(BOOL isEdit){
        @KKStrong(self);
        
        if (isEdit) {
             [self initData];
        }
        
    };
    
    [self.navigationController pushViewController:historyVC animated:YES];
    
    
    
}



#pragma mark - 完成待办
- (void)finshAction:(UIButton *)sender{
    
    sender.selected = !sender.selected;
    
    dispatch_async(dispatch_get_global_queue(0,0), ^{
        UITableViewCell *cell = (UITableViewCell *)[sender superview];
        NSIndexPath *indexPath = [self.dataTabView indexPathForCell:cell];
        GtasksModel *gModel = self.dataArray[indexPath.row];
        gModel.isComplete = YES;
        gModel.completeDate = [Utils getCurrentTime];
        
        BOOL ret =  [gModel update];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            
            if (ret) {
               
                NSLog(@"indexPath is = %ld",indexPath.row);
                [self.dataArray removeObjectAtIndex:indexPath.row];
//                [self.dataTabView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
                
                [self.dataTabView reloadData];
                
            }
            
        });
        
    });
    
    
    
    
}



- (void)scrollViewDidScroll:(UIScrollView *)scrollView{
    
    RKLog(@"%f",scrollView.contentOffset.y);
    
    
    if (scrollView.contentOffset.y>30) {
        
        if ([self.dataTabView.header isRefreshing]) {
            [self.dataTabView.header endRefreshing];
        }
        
    }
    
    
}


- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
    
    RKLog(@"加速停止");
    
}

- (void)scrollViewDidScrollToTop:(UIScrollView *)scrollView{
    
    
    RKLog(@"回到顶部");
}




- (void)textViewDidChange:(UITextView *)textView
{
    NSIndexPath *indexPath = [self.dataTabView indexPathForSelectedRow];
    
    SpeedCreateGtaskTableViewCell *cell = [self.dataTabView cellForRowAtIndexPath: indexPath];
    
    CGFloat maxHeight =_textViewCellHight-10;
    CGRect frame = textView.frame;
    CGSize constraintSize = CGSizeMake(frame.size.width, MAXFLOAT);
    
    if (textView.text.length>0) {
        
        _isHiddenBtn = NO;
        
    }else{
        _isHiddenBtn = YES;
    }
    
    cell.SpeedCreateBtn.hidden = _isHiddenBtn;
    
    if (textView.text.length>100) {
        
        textView.text  = [textView.text substringToIndex:100];
    }
    
    _textViewText = textView.text;
    
    CGSize size = [textView sizeThatFits:constraintSize];
    if (size.height<=frame.size.height) {
        size.height=frame.size.height;
    }else{
        if (size.height >= maxHeight)
        {
            size.height = maxHeight;
            textView.scrollEnabled = YES;   // 允许滚动
        }
        else
        {
            textView.scrollEnabled = NO;    // 不允许滚动
        }
    }
    textView.frame = CGRectMake(frame.origin.x, frame.origin.y, frame.size.width, size.height);
    
    
    
//    [self.dataTabView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:[NSIndexPath indexPathForRow:_selectIndexPath.row inSection:_selectIndexPath.section], nil] withRowAnimation:UITableViewRowAnimationNone];
    
    
    //刷新某一行;
//    [self.dataTabView reloadRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:_selectIndexPath.row inSection:0]] withRowAnimation:UITableViewRowAnimationNone];
//    
//    [cell.SpeedTextView canBecomeFirstResponder];
}


//完成
- (void)textViewDidEndEditing:(UITextView *)textView{
    
    NSIndexPath *indexPath = [self.dataTabView indexPathForSelectedRow];
    
    SpeedCreateGtaskTableViewCell *cell = [self.dataTabView cellForRowAtIndexPath: indexPath];
    
    _isHiddenBtn = YES;
    _isHiddenTextView = YES;
    _isHiddenLabel = NO;
    _textViewText = @"";
    cell.SpeedCreateBtn.hidden = _isHiddenBtn;
    cell.SpeedTextView.hidden = _isHiddenTextView;
    cell.speedCreateGtaskLabel.hidden = _isHiddenLabel;
    cell.SpeedTextView.text = _textViewText;
    
    
}

- (BOOL)textViewShouldEndEditing:(UITextView *)textView{
    
    NSLog(@"%@",textView.text);
    
//    [self doSaveGtasks];
    
    return YES;
}

#pragma mark - 快速创建待办
- (void)createAction:(UIButton *)sender{

    [self doSaveGtasks];
}

#pragma mark - 保存待办
- (void)doSaveGtasks{
    GtasksModel *model = [[GtasksModel alloc]init];
    model.appCreatedTime = [Utils getCurrentTime];
    model.todoId = [Utils getCurrentTimeWithTimeFormat:@"yyyyMMddHHmmssSSS"];
    model.clientType = 1;
    model.content = _textViewText;
    BOOL ret = [model save];
    [self doSaveRemindAndGtasksWithRequestStyle:gtasksSaveUpdate model:model isHaveAlert:NO waitTitle:nil success:^(id dictData) {
        
    } failure:^(NSString *message) {
        
    }];
    if (ret) {
        [self.dataArray addObject:model];
        [self.dataTabView reloadData];
    }
}


- (void)dealloc{
    
    [[NSNotificationCenter defaultCenter] removeObserver:@"addGtask"];
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
