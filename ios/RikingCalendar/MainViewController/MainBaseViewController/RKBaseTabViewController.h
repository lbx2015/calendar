//
//  RKBaseTabViewController.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/26.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseViewController.h"

@interface RKBaseTabViewController : RKBaseViewController
<UITableViewDelegate,UITableViewDataSource>
@property (nonatomic,strong) UITableView *dataTabView;

@property (nonatomic,strong) NSMutableArray *dataArray;

@end
