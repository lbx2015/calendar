//
//  DIYMJRefreshHaderHistory.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/31.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "MJRefreshHeader.h"

@protocol DIYMJRefreshHaderHistoryDelegate <NSObject>

- (void)showHistory;//type:1:提醒历史,2:待办历史

@end

@interface DIYMJRefreshHaderHistory : MJRefreshHeader

@property (nonatomic,copy) NSString *btnName;

@property (nonatomic,weak)id<DIYMJRefreshHaderHistoryDelegate>delegate;
@end
