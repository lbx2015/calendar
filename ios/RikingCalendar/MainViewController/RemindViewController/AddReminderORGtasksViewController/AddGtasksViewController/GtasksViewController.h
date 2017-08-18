//
//  GtasksViewController.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/24.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseViewController.h"
#import "GtasksModel.h"
@interface GtasksViewController : RKBaseViewController

@property(nonatomic,strong)GtasksModel *gtaskModel;

@property (nonatomic,assign)BOOL isEdit;

@property (nonatomic,copy)void (^editGtask)();

/**
 保存待办
 */
- (void)doSaveGtasks;
@end
