//
//  ReportListTableView.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/9.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol ReportListTableViewDelegate <NSObject>

- (void)didSelectRowAtIndexPath:(NSIndexPath *)indexPath;

@end


@interface ReportListTableView : UIView
@property (nonatomic, strong) UITableView *reportTableView;
@property (nonatomic, assign) BOOL canScroll;
@property (nonatomic, weak)id<ReportListTableViewDelegate>delegate;

- (instancetype)initWithFrame:(CGRect)frame dataArray:(NSMutableArray *)dataModel;
@end
