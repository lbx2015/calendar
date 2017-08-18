//
//  ReportModel.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/11.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseModel.h"

@class ReportlistModel;
@interface ReportModel : RKBaseModel

@property (nonatomic,copy)NSString *title;
//@property (nonatomic,strong)NSMutableArray*result;

@property (nonatomic,strong)NSMutableArray<ReportlistModel *>*result;
@end



@interface ReportlistModel : RKBaseModel

@property (nonatomic,copy)NSString *reportId;
@property (nonatomic,copy)NSString *moduleType;
@property (nonatomic,copy)NSString *reportCode;
@property (nonatomic,copy)NSString *reportName;
@end
