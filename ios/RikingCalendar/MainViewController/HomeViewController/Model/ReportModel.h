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


//扩展字段
@property (nonatomic,copy)NSString *completeId;
@property (nonatomic,copy)NSString *completeDate;
@property (nonatomic,copy)NSString *appUserId;//用户userId
@property (nonatomic,assign)int isComplete;//0未完成1完成
@end
