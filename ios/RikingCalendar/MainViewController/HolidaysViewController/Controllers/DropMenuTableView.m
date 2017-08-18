//
//  DropMenuTableView.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/9.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "DropMenuTableView.h"


typedef void(^selectBlock)(MenuModel *model,menuStyle menuStyle);

@interface DropMenuTableView ()

<UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate>


@property (weak, nonatomic) IBOutlet UITableView *dropTabview;

@property (nonatomic, strong)NSMutableArray *dataArray;

@property (nonatomic, copy) NSString *selectName;

@property (nonatomic,strong)selectBlock selectBlock;

@property (nonatomic,assign)menuStyle menuStyle;

@property (nonatomic,strong)MenuModel *selectModel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *dropTabViewHight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *dropTabViewTopLayout;

@end


@implementation DropMenuTableView

-(instancetype)init {
    self = [super init];
    if (self) {
        self = [[[NSBundle mainBundle] loadNibNamed:NSStringFromClass([self class]) owner:self options:nil] lastObject];
    }
    return self;
}



- (void)refreshMenu:(NSArray *)dataArray selectMenuModel:(MenuModel *)menuModel menuStyle:(menuStyle)style CompleteBlock:(void(^)(MenuModel *model,menuStyle menuStyle))completeBlock{
    
    
    
    self.frame=CGRectMake(0, 104, kScreenWidth, kScreenHeight);
    //点击背景是否影藏
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(dismiss)];
    tap.delegate = self;
    [self addGestureRecognizer:tap];
    self.backgroundColor = RGBA(0, 0, 0, 0);
    self.dropTabViewHight.constant=0;
    [self layoutIfNeeded];
    [[UIApplication sharedApplication].keyWindow bringSubviewToFront:self];
    
    
    self.dataArray = [NSMutableArray arrayWithArray:dataArray];
    self.dropTabview.delegate = self;
    self.dropTabview.dataSource = self;
    self.dropTabview.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.selectModel = menuModel;
    self.menuStyle = style;
    if (completeBlock) {
        
        self.selectBlock = ^(MenuModel *model,menuStyle menuStyle){
            
            completeBlock(model,menuStyle);
        };
    }
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.dataArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return 45;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cellID"];
    if (!cell) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cellID"];
    }
    
    //为了防止重用
    for (UIView *view in cell.contentView.subviews) {
        
        [view removeFromSuperview];
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
    UILabel *label = [[UILabel alloc]init];
    label.themeMap = @{kThemeMapKeyColorName : normalText_main_color};
    label.font = fiveClassTextFont;
    label.textAlignment = NSTextAlignmentLeft;
    [cell.contentView addSubview:label];
    
    UIView *lineView = [[UIView alloc]init];
    lineView.backgroundColor = dt_line_color;
    [cell.contentView addSubview:lineView];
    
    [label mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(UIEdgeInsetsMake(0, 15, 0, 15));
    }];
    
    [lineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(UIEdgeInsetsMake(44.5, 15, 0, 0));
    }];
    
    
    
    MenuModel *mModel = self.dataArray[indexPath.row];
    
    if (self.menuStyle == menuCurrency && indexPath.row>0) {
        label.text = [NSString stringWithFormat:@"%@%@",mModel.valu,mModel.ke];
    }else{
        label.text = [NSString stringWithFormat:@"%@",mModel.valu];
    }

    
    if ([self.selectModel.valu containsString:mModel.valu]) {
        
        label.textColor = dt_app_main_color;
        lineView.backgroundColor = dt_app_main_color;
        
    }
    
    if (self.menuStyle==menuCountry) {
        

    }else if (self.menuStyle==menuCurrency){
        
    }else{
        
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    MenuModel *mModel = self.dataArray[indexPath.row];
    
    if (self.selectBlock) {
        self.selectBlock(mModel,self.menuStyle);
    }
    
    [self dismiss];
}



#pragma mark - Action
-(void)show {
    
    [[UIApplication sharedApplication].keyWindow addSubview:self];
    
    [UIView animateWithDuration:.3 animations:^{

        self.backgroundColor = RGBA(0, 0, 0, 0.4);
        self.dropTabViewHight.constant=400;
        [self layoutIfNeeded];
        
    }];
}
-(void)dismiss {
    [UIView animateWithDuration:.3 animations:^{
        self.backgroundColor = RGBA(0, 0, 0, 0);
        self.dropTabViewHight.constant = 0;
        [self layoutIfNeeded];
    } completion:^(BOOL finished) {
       
        [self.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
        [self removeFromSuperview];
        
        if (self.disMiss) {
            self.disMiss();
        }
        
    }];
    
    
}


#pragma mark - UIGestureRecognizerDelegate
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if( [touch.view isDescendantOfView:self.dropTabview]) {
        return NO;
    }
    return YES;
}


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
