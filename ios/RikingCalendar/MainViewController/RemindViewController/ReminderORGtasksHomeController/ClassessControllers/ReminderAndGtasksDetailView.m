//
//  ReminderAndGtasksDetailView.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/8.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "ReminderAndGtasksDetailView.h"
#import "ReminderModel.h"
#import "GtasksModel.h"


typedef void(^clickBtnBlock)(int type);//type:1:编辑;2:删除
@interface ReminderAndGtasksDetailView()


<UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate>

@property (weak, nonatomic) IBOutlet UITableView *detailTabView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bgViewBottomLayout;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bgViewHight;

@property (weak, nonatomic) IBOutlet UIView *bgView;

@property (strong,nonatomic) clickBtnBlock  clickBtnBlock;

@property (nonatomic,assign) int detailType;

@property (nonatomic,strong) ReminderModel *rModel;

@property (nonatomic,strong) GtasksModel *gModel;

@property (nonatomic,strong) NSMutableArray *imageArray;

@property (nonatomic,assign) CGFloat contentHeight;

@property (nonatomic,assign) BOOL isImport;
@property (nonatomic,assign) BOOL isComplete;

- (IBAction)cancelButton:(id)sender;

- (IBAction)deleteButton:(id)sender;

- (IBAction)editButton:(id)sender;


@end



@implementation ReminderAndGtasksDetailView


-(instancetype)init {
    self = [super init];
    if (self) {
        self = [[[NSBundle mainBundle] loadNibNamed:NSStringFromClass([self class]) owner:self options:nil] lastObject];
    }
    return self;
}


- (void)setDetailViewWithModel:(RKBaseModel *)model type:(int)type clickBtnType:(void(^)(int buttonType))clickBtnType{

    //默认内容的高度
    self.contentHeight = 50;
    
    self.detailType = type;
    
    CGFloat hight = 0;
    
    if (type == 1) {
        self.rModel = (ReminderModel *)model;
        self.imageArray = [NSMutableArray arrayWithObjects:@"",@"detail_time_icon",@"detail_clock_icon",@"detail_repeat_icon",nil];
        
        hight = [Utils setWidthForText:self.rModel.content fontSize:17 labelSize:kScreenWidth-30 isGetHight:YES].height;
        
        
    }else{
        self.gModel = (GtasksModel *)model;
        self.isImport = self.gModel.isImportant;
        self.isComplete = self.gModel.isComplete;
        self.imageArray = [NSMutableArray arrayWithObjects:@"",@"gtask_unfinish",@"programme_Important_normalStars" ,nil];
        
        hight = [Utils setWidthForText:self.gModel.content fontSize:17 labelSize:kScreenWidth-30 isGetHight:YES].height;
    }
    

    if (hight>50) {
        self.contentHeight = hight+10;
        self.bgViewHight.constant = 250+hight+10;
        if (self.bgViewHight.constant>kScreenHeight-64) {
            self.bgViewHight.constant = kScreenHeight-64;
        }
    }
    
    self.detailTabView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.detailTabView.delegate = self;
    self.detailTabView.dataSource = self;
    [self.detailTabView reloadData];
    
    if (clickBtnType) {
        self.clickBtnBlock = ^(int type){
            clickBtnType(type);
        };
    }
    
    self.frame=CGRectMake(0, 0, kScreenWidth, kScreenHeight);
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(dismiss)];
    tap.delegate = self;
    [self addGestureRecognizer:tap];
    self.backgroundColor = RGBA(0, 0, 0, 0);
    self.bgViewBottomLayout.constant=-self.frame.size.height;
    [self layoutIfNeeded];
    [[UIApplication sharedApplication].keyWindow bringSubviewToFront:self];
    
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return self.imageArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row==0) {
        return self.contentHeight;
    }else{
        return 40;
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"detailID"];
    if (!cell) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"detailID"];
    }
    
    //为了防止重用
    for (UIView *view in cell.contentView.subviews) {
        
        [view removeFromSuperview];
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
    return cell;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    UILabel *label = [[UILabel alloc]init];
    label.font = threeClassTextFont;
    label.textColor = dt_text_main_color;
    label.numberOfLines = 0;
    [cell.contentView addSubview:label];
    
    
    UIView *lineView = [[UIView alloc]init];
    lineView.backgroundColor = dt_line_color;
    [cell.contentView addSubview:lineView];

    if (indexPath.row==0) {
        
        if (self.detailType==1) {
            label.text = self.rModel.content;
        }else{
            label.text = self.gModel.content;
        }
        
        [label mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.edges.mas_equalTo(UIEdgeInsetsMake(5, 15, 5, 10));
            
        }];
        
        [lineView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.equalTo(cell.contentView).offset(15);
            make.bottom.right.equalTo(cell.contentView).offset(0);
            make.height.mas_equalTo(0.5);
        }];
        
    }else{
        

        //标识
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        [btn setImage:[UIImage imageNamed:self.imageArray[indexPath.row]] forState:UIControlStateNormal];
        [cell.contentView addSubview:btn];
        
        [btn mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.top.bottom.equalTo(cell.contentView).offset(0);
            make.left.equalTo(cell.contentView).offset(15);
            make.width.mas_equalTo(btn.imageView.image.size.width);
            
        }];
        
        
        //内容
        label.textColor = dt_text_light_color;
        label.font = fiveClassTextFont;
        
        [label mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.top.bottom.equalTo(cell.contentView).offset(0);
            make.left.equalTo(btn.mas_right).offset(15);
            make.right.equalTo(cell.contentView).offset(70+15);
        }];
        
    
        //分割线
        [lineView mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.left.equalTo(btn.mas_right).offset(15);
            make.bottom.right.equalTo(cell.contentView).offset(0);
            make.height.mas_equalTo(0.5);
        }];
        
        
        
        if (indexPath.row==1&&self.detailType==1) {
            UILabel *timeLabel = [[UILabel alloc]init];
            timeLabel.font = fiveClassTextFont;
            timeLabel.textColor = dt_text_light_color;
            [cell.contentView addSubview:timeLabel];
            
            [timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.bottom.equalTo(cell.contentView).offset(0);
                make.right.equalTo(cell.contentView).offset(15);
                make.width.mas_equalTo(70);
            }];
        }
        
        btn.userInteractionEnabled = NO;
        
        if (self.detailType==1) {
            
            if (indexPath.row==1) {
                
                if (self.rModel.isAllday) {
                    
                    label.text = [NSString stringWithFormat:@"%@ %@",[Utils setOldStringTime:self.rModel.strDate inputFormat:@"yyyyMMdd" outputFormat:@"yyyy年MM月dd日"],@"全天"];
                    
                }else{
                    label.text = [NSString stringWithFormat:@"%@ %@",[Utils setOldStringTime:self.rModel.strDate inputFormat:@"yyyyMMdd" outputFormat:@"yyyy年MM月dd日"],self.rModel.startTime];
                    
                }
                
                
                
            }else if (indexPath.row==2){
                if (self.rModel.isRemind) {
                    label.text = self.rModel.beforeTime>0?[NSString stringWithFormat:@"提前%d分",self.rModel.beforeTime]:@"正点提醒";
                
                }else{
                    label.text = @"不提醒";
                }
            }else{
                label.text = [self getRepeatString:self.rModel.repeatFlag repeat_value:self.rModel.repeatValue];
            }
            
        }else{
            
        
            btn.tag = indexPath.row;
            [btn addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
            if (indexPath.row==1) {
                [btn setImage:[UIImage imageNamed:@"gtaskFinsh"] forState:UIControlStateSelected];
                label.text = @"未完成";
                if (self.gModel.isComplete) {
                    btn.selected = YES;
                    label.text = @"已完成";
                }
            }else{
                [btn setImage:[UIImage imageNamed:@"programme_Important_selectStars"] forState:UIControlStateSelected];
                label.text = @"重要";
                if (self.gModel.isImportant) {
                    btn.selected = YES;
                    label.textColor = dt_text_main_color;
                }
            }
            
        }
        
    }
    
    
}

- (void)btnAction:(UIButton *)sender{
    sender.selected = !sender.selected;
    
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
}



- (IBAction)cancelButton:(id)sender {
    
    [self dismiss];
}

- (IBAction)deleteButton:(id)sender {
    
    if (self.clickBtnBlock) {
        self.clickBtnBlock(2);
    }
    
    [self dismiss];
}

- (IBAction)editButton:(id)sender {
    if (self.clickBtnBlock) {
        self.clickBtnBlock(1);
    }
    
    [self dismiss];
}


-(void)show {
    
    
    self.bgViewBottomLayout.constant=-self.frame.size.height;
    [self layoutIfNeeded];
    [[UIApplication sharedApplication].keyWindow bringSubviewToFront:self];
    
    [[UIApplication sharedApplication].keyWindow addSubview:self];
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(UIEdgeInsetsMake(0, 0, 0, 0));
    }];
    [UIView animateWithDuration:.3 animations:^{
        self.backgroundColor = RGBA(0, 0, 0, 0.4);
        self.bgViewBottomLayout.constant=0;
        [self layoutIfNeeded];
        
    }];
}
-(void)dismiss {
    [UIView animateWithDuration:.3 animations:^{
        self.backgroundColor = RGBA(0, 0, 0, 0);
        self.bgViewBottomLayout.constant = -self.frame.size.height;
        [self layoutIfNeeded];
    } completion:^(BOOL finished) {
        [self.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
        [self removeFromSuperview];
    }];
}



- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if( [touch.view isDescendantOfView:self.bgView]) {
        return NO;
    }
    return YES;
}


- (NSString *)getRepeatString:(int)flag repeat_value:(NSString *)repeat_value{
    
    NSString *repeatStr = @"";
    
    
    if (flag==0) {
        return @"不重复";
    }else if (flag==1){
        return @"法定工作日重复";
    }else if (flag==2){
        return @"法定节假日重复";
    }else{
        NSArray *array = [NSArray arrayWithArray:[repeat_value componentsSeparatedByString:@","]];
        
        if ([repeat_value containsString:@"1,2,3,4,5,6,7"] && array.count==7){
            return repeatStr = @"每天重复";
        }else if ([repeat_value containsString:@"1,2,3,4,5"] && array.count==5){
            return repeatStr = @"工作日重复";
        }else if ([repeat_value containsString:@"6,7"] && array.count==2){
            return repeatStr = @"周末重复";
        }else{
            
            NSDictionary *dict = @{@"1":@"一",@"2":@"二",@"3":@"三",@"4":@"四",@"5":@"五",@"6":@"六",@"7":@"日"};
            
            NSString *newTime = @"";
            
            for (int i=0; i<array.count; i++) {
                
                NSString *key = array[i];
                
                if (i==0) {
                    newTime = [NSString stringWithFormat:@"%@",[dict objectForKey:key]];
                }else{
                    newTime = [NSString stringWithFormat:@"%@、%@",newTime,[dict objectForKey:key]];
                }
                
            }
            
            return repeatStr = [NSString stringWithFormat:@"每周%@",newTime];
        }
    }
    
}

@end
