//
//  ReportLabel.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/11.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "LKDBModel.h"

@interface ReportLabel : LKDBModel

@property (nonatomic,copy)NSString *dictData;
@property (nonatomic,copy)NSString *reportId;
@property (nonatomic,copy)NSString *saveTime;
@end
