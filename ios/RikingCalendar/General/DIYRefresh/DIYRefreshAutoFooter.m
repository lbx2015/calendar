//
//  DIYRefreshAutoFooter.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/9/5.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "DIYRefreshAutoFooter.h"


@interface DIYRefreshAutoFooter()
@property (weak, nonatomic) UILabel *label;
@property (weak, nonatomic) UIActivityIndicatorView *loading;
@end

@implementation DIYRefreshAutoFooter

#pragma mark - 重写方法
#pragma mark 在这里做一些初始化配置（比如添加子控件）
- (void)prepare
{
    [super prepare];
    
    // 设置控件的高度
    self.mj_h = 50;
    
    // 添加label
    UILabel *label = [[UILabel alloc] init];
    label.textColor = [UIColor grayColor];
    label.font = [UIFont boldSystemFontOfSize:13];
    label.textAlignment = NSTextAlignmentCenter;
    label.backgroundColor = [UIColor clearColor];
    [self addSubview:label];
    self.label = label;
    
    // loading
    UIActivityIndicatorView *loading = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    [self addSubview:loading];
    self.loading = loading;
}

#pragma mark 在这里设置子控件的位置和尺寸
- (void)placeSubviews
{
    [super placeSubviews];
    
    //YMDLog(@"%@",self);
    
    self.label.frame = self.bounds;
    
    self.loading.frame = CGRectMake(kScreenWidth/2-[Utils setWidthForText:self.label.text fontSize:13 labelSize:self.mj_h isGetHight:NO].width/2-40, 15, 20, 20);
    
//    self.loading.frame = CGRectMake(kScreenWidth/2-10, 15, 20, 20);
    
}

#pragma mark 监听scrollView的contentOffset改变
- (void)scrollViewContentOffsetDidChange:(NSDictionary *)change
{
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
            self.label.hidden = YES;
            self.label.text = @"上拉加载更多";
            self.loading.frame = CGRectMake(kScreenWidth/2-[Utils setWidthForText:self.label.text fontSize:13 labelSize:self.mj_h isGetHight:NO].width/2-40, 15, 20, 20);
            [self.loading stopAnimating];
            break;
        case MJRefreshStateRefreshing:
            self.label.hidden = NO;
            self.label.text = @"加载中...";
            self.loading.frame = CGRectMake(kScreenWidth/2-[Utils setWidthForText:self.label.text fontSize:13 labelSize:self.mj_h isGetHight:NO].width/2-30, 15, 20, 20);
            [self.loading startAnimating];
            break;
        case MJRefreshStateNoMoreData:
            self.label.hidden = NO;
            self.label.text = @"已经全部加载";
            [self.loading stopAnimating];
            break;
        default:
            break;
    }
}

@end
