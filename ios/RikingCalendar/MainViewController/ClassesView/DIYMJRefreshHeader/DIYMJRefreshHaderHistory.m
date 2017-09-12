//
//  DIYMJRefreshHaderHistory.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/31.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "DIYMJRefreshHaderHistory.h"

@interface DIYMJRefreshHaderHistory()


@property (weak, nonatomic) UIButton *historyBtn;
@end


@implementation DIYMJRefreshHaderHistory


#pragma mark - 重写方法
#pragma mark 在这里做一些初始化配置（比如添加子控件）
- (void)prepare
{
    [super prepare];
    
    // 设置控件的高度
    self.mj_h = 37;
    
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
//    btn.backgroundColor = [UIColor redColor];
    [btn setTitle:@"查看历史" forState:UIControlStateNormal];
    btn.layer.borderWidth = 1;
    btn.layer.borderColor = dt_line_color.CGColor;
    btn.layer.cornerRadius = 13.5;
    btn.titleLabel.font = eightClassTextFont;
    [btn setTitleColor:dt_text_light_color forState:UIControlStateNormal];
    [btn addTarget:self action:@selector(historyAction) forControlEvents:UIControlEventTouchUpInside];
    [self addSubview:btn];
    
    self.historyBtn = btn;
    
//    self.backgroundColor = [UIColor orangeColor];
}

#pragma mark 在这里设置子控件的位置和尺寸
- (void)placeSubviews
{
    [super placeSubviews];
    
    self.historyBtn.bounds = CGRectMake(0, 0, 114, 27);
    
    self.historyBtn.center = CGPointMake(self.mj_w * 0.5, 13.5+10);

}


- (void)setBtnName:(NSString *)btnName{
    [self.historyBtn setTitle:btnName forState:UIControlStateNormal];
}

- (void)historyAction{
    
    RKLog(@"查看历史");
    
    if (self.delegate) {
        [self.delegate showHistory];
    }
    
}

#pragma mark 监听scrollView的contentOffset改变
- (void)scrollViewContentOffsetDidChange:(NSDictionary *)change
{
//    NSLog(@"~~~~~~~~~~~~~~~~~~~%@",change);
    
    [super scrollViewContentOffsetDidChange:change];
    
    
    
}

#pragma mark 监听scrollView的contentSize改变
- (void)scrollViewContentSizeDidChange:(NSDictionary *)change
{
    [super scrollViewContentSizeDidChange:change];
    
}

#pragma mark 监听scrollView的拖拽状态改变
- (void)scrollViewPanStateDidChange:(NSDictionary *)change
{
    [super scrollViewPanStateDidChange:change];
    
}

#pragma mark 监听控件的刷新状态
- (void)setState:(MJRefreshState)state
{
    MJRefreshCheckState;
    
    switch (state) {
        case MJRefreshStateIdle:
            
            break;
        case MJRefreshStatePulling:
            
            break;
        case MJRefreshStateRefreshing:
            
            break;
        default:
            break;
    }
}

#pragma mark 监听拖拽比例（控件被拖出来的比例）
- (void)setPullingPercent:(CGFloat)pullingPercent
{
    
//    NSLog(@"**************%f",pullingPercent);
    
    [super setPullingPercent:pullingPercent];
    
}



@end
