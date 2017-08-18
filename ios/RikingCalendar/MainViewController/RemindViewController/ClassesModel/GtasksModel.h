//
//  GtasksModel.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/2.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "LKDBModel.h"

@interface GtasksModel : LKDBModel

@property (nonatomic,copy)      NSString    *todoId;//待办ID

@property (nonatomic,copy)      NSString    *userId;

@property (nonatomic,copy)      NSString    *content;//内容

@property (nonatomic,assign)    int         isImportant;//是否重要

@property (nonatomic,assign)    int         isComplete;//是否完成

@property (nonatomic,copy)      NSString    *completeDate;//完成时间

@property (nonatomic,copy)      NSString    *appCreatedTime;//创建时间

@property (nonatomic,assign)    int         isOpen;//是否提醒

@property (nonatomic,copy)      NSString    *strDate;//提醒时间

@property (nonatomic, assign)   int         clientType;//客户端类型 1:iOS 2:android 3 web

@property (nonatomic, assign)   int         deleteState;
@end
