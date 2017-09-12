//
//  RemindAndGtasksDBManager.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/16.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RemindAndGtasksDBManager.h"
#import "AppDelegate.h"

static RemindAndGtasksDBManager *manager = nil;
@implementation RemindAndGtasksDBManager


+ (RemindAndGtasksDBManager *)shareManager
{
    static dispatch_once_t oneToken;
    dispatch_once(&oneToken,^{
        manager = [[self alloc] init];
        
    });
    return manager;
}



#pragma mark - 保存提醒到服务器
- (void)doSaveRemindWithRemindModel:(ReminderModel *)remindModel editType:(int)editType success:(void(^)(BOOL ret))success{
    
    
    //放在子线程操作,防止主线程卡UI
        dispatch_async(dispatch_get_global_queue(0, 0), ^{

            //删除
            if (editType==3) {
                remindModel.deleteState = 1;
            }
            
            //保存
            if (editType == 1) {
                remindModel.reminderId = [Utils getCurrentTimeWithDateStyle:DateFormatYearMonthDayHourMintesecondMillisecond];
            }
            remindModel.userId = isUser?UserID:@"";
            remindModel.clientType = 1;
            
            
            //有网
            if ([self networkStatus] && isUser && (remindModel.loactionStatus != 1)) {
                
                NSMutableDictionary *param = [NSMutableDictionary dictionaryWithDictionary:[remindModel mj_keyValues]];
                [param removeObjectForKey:@"propertyNames"];
                [param removeObjectForKey:@"pk"];
                [param removeObjectForKey:@"columeTypes"];
                [param removeObjectForKey:@"columeNames"];
                
                NSMutableArray *remindArray = [NSMutableArray array];
                [remindArray addObject:param];
                
                [self syncRemindWithRemindArray:remindArray updateLoaction:nil success:^(BOOL ret) {
                    
                    int type = editType;
                    if (!ret) {
                        remindModel.syncStatus = 1;
                        if (editType==3 && remindModel.loactionStatus!=1) {
                            type = 2;
                        }
                    }
                    
                    [self saveRemindLoactionDBWithRemindModel:remindModel saveNetWorkStatus:ret editType:type success:^(BOOL ret) {
                        
                        dispatch_async(dispatch_get_main_queue(), ^{
                            
                            if (success) {
                                success(ret);
                            }
                            
                        });
                    }];
                    
                }];
                
                
            }else{
                
                remindModel.syncStatus = 1;
                //无网络的状态下,如果是删除提醒,先判断这条数据只存在本地,还是本地和服务器都有,如果只存在本地,那么直接删除即可,如果服务器也有,那么先标记状态,同步完成之后再删除
                int type = editType;
                if (editType==3 && remindModel.loactionStatus!=1) {
                    type = 2;
                }
                
                [self saveRemindLoactionDBWithRemindModel:remindModel saveNetWorkStatus:NO editType:type success:^(BOOL ret) {
                    
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        if (success) {
                            success(ret);
                        }
                    });
                }];
            }
            
            
            
            
           
            
        });
    
    
    
    
    
}

#pragma mark - 批量保存提醒到服务器
- (void)syncRemindWithRemindArray:(NSArray *)remindArray updateLoaction:(NSArray *)updateLoaction success:(void(^)(BOOL ret))success{
    
    [[AFNWorkingTool sharedManager] AFNHttpPOSTWithUrlstring:requestUrl(synchronousReminds) parameters:remindArray success:^(NSDictionary *dictData) {
        
        BOOL isSuccess = NO;
        
        if ([dictData[@"code"]isEqualToNumber:@200]) {
            
            isSuccess = YES;
            
        }else{
            isSuccess = NO;
        }
        
        if (success) {
            success(isSuccess);
        }
        
    } failure:^(NSError *error) {
    
        if (success) {
            success(NO);
        }
    }];
}

#pragma mark - 保存提醒到本地
- (void)saveRemindLoactionDBWithRemindModel:(ReminderModel *)remindMode saveNetWorkStatus:(BOOL)status editType:(int)editType success:(void(^)(BOOL ret))success{
    
    BOOL ret = NO;
    if (editType==1)
    {
        if (!status) {
            remindMode.loactionStatus = 1;
        }
        ret = [remindMode save];//保存
    }
    else if (editType==2){
        ret = [remindMode update];//更新
    }
    else
    {
        ret = [remindMode deleteObject];//删除
    }
    //添加提醒
    if (ret) {
        
        //刷新提醒列表
        postNotificationName(kRefreshRemindName);
        
        if (success) {
            success(ret);
        }
        
        //删除通知
        if (editType == 2 || editType == 3) {
            
            [[LocalNotificationManager shareManager] cancelLocalNotificationsWithKey:@"reminderId" value:remindMode.reminderId];
        }
        
        //添加通知
        if (editType == 1 || (editType == 2 && remindMode.deleteState != 1)) {
            [[LocalNotificationManager shareManager] addLocalNotifisWithRemindModel:remindMode];
        }
    
    }
}

#pragma 查找本地所有未同步的提醒数据
- (void)selectLoactionNotSyncRemindModelSuccess:(void(^)(BOOL ret, NSString *message))success{
    
    
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
        if (isUser) {
            
            NSArray *remindArray = [ReminderModel findByCriteria:@"where syncStatus = '1'"];
            
            NSMutableArray *tempArray = [NSMutableArray array];
            
            for (ReminderModel *rModel in remindArray) {
                
                NSMutableDictionary *remindDict = [NSMutableDictionary dictionaryWithDictionary:[rModel mj_keyValues]];
                [remindDict removeObjectForKey:@"propertyNames"];
                [remindDict removeObjectForKey:@"pk"];
                [remindDict removeObjectForKey:@"columeTypes"];
                [remindDict removeObjectForKey:@"columeNames"];
                [tempArray addObject:remindDict];
            }
            
            if (tempArray.count>0) {
                [self syncRemindWithRemindArray:tempArray updateLoaction:remindArray success:^(BOOL ret) {
                    
                    if (ret) {
                        
                        [self getUserAllRemindSuccess:^(BOOL ret, NSString *message) {
                            
                            dispatch_async(dispatch_get_main_queue(), ^{
                                
                                if (success) {
                                    success(ret,@"本地提醒上传成功");
                                }
                            });
                            
                        }];
                    }
                    
                }];
            }else{
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    if (success) {
                        success(NO,@"本地暂时没有提醒数据上传");
                    }
                });
                
            }
        }
        else{
            
            dispatch_async(dispatch_get_main_queue(), ^{
                
                if (success) {
                    success(NO,@"未登录不用上传");
                }
                
            });
            
        }
        
    });
    
}

#pragma mark - 获取用户所有的提醒
- (void)getUserAllRemindSuccess:(void(^)(BOOL ret, NSString *message))success
{
    NSDictionary *param = @{@"id":UserID};
    [[AFNWorkingTool sharedManager] AFNHttpRequestPOSTurlstring:requestUrl(getAllRemind) parm:param success:^(NSDictionary *dictData) {
        if (dictData && [dictData[@"code"]isEqualToNumber:@200]) {
            
            
            dispatch_async(dispatch_get_global_queue(0, 0), ^{
                
                //先取消本地所有的待办通知
                NSArray *remindArray = [ReminderModel findAll];
                
                for (ReminderModel * remindModel in remindArray) {
                    
                    [[LocalNotificationManager shareManager] cancelLocalNotificationsWithKey:@"reminderId" value:remindModel.reminderId];
                }
                
                //先清空本地表
                [ReminderModel clearTable];
                
                NSArray *array = dictData[@"_data"];
                //提醒
                for (NSDictionary *remind in array) {
                    
                    ReminderModel *remindModel = [[ReminderModel alloc]init];
                    [remindModel setValuesForKeysWithDictionary:remind];
                    [remindModel save];
                    
                    //添加本地提醒通知
                    [[LocalNotificationManager shareManager] addLocalNotifisWithRemindModel:remindModel];
                }
                
                postNotificationName(kRefreshRemindName);
                
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    if (success) {
                        success(YES,@"本地提醒同步成功");
                    }
                });
                
            });
            
            
        }
        
        
    } failure:^(NSError *error) {
        
        if (success) {
            success(NO,@"拉取失败");
        }
        
    }];
}




#pragma mark - 待办
#pragma mark - 添加待办到服务器
- (void)doSaveGtasksNetWorkWithGtasksModel:(GtasksModel *)gtasksModel editType:(int)editType success:(void(^)(BOOL ret))success{
    
    
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
        if (editType==1) {
            
            gtasksModel.appCreatedTime = [Utils getCurrentTime];
            gtasksModel.todoId = [Utils getCurrentTimeWithTimeFormat:@"yyyyMMddHHmmssSSS"];
            gtasksModel.isComplete = NO;
            gtasksModel.userId = isUser?UserID:@"";
            if (!gtasksModel.isOpen) {
                gtasksModel.strDate = @"";
            }
            
        }else if (editType==3){
            gtasksModel.deleteState = 1;
        }
        gtasksModel.clientType = 1;
        
        //有网
        if ([self networkStatus] && isUser) {
            
            NSMutableDictionary *param = [NSMutableDictionary dictionaryWithDictionary:[gtasksModel mj_keyValues]];
            [param removeObjectForKey:@"propertyNames"];
            [param removeObjectForKey:@"pk"];
            [param removeObjectForKey:@"columeTypes"];
            [param removeObjectForKey:@"columeNames"];
            
            NSArray *array = [NSArray arrayWithObject:param];
            
            [self syncRemindWithGtasksArray:array success:^(BOOL ret) {
                
                int type = editType;
                if (!ret) {
                    gtasksModel.syncStatus = 1;
                    if (editType==3) {
                        type = 2;
                    }
                }
                
                
                [self doSaveGtasksLocationWithGtasksModel:gtasksModel editType:type success:^(BOOL ret) {
                    
                    dispatch_async(dispatch_get_main_queue(), ^{
                        // 通知主线程刷新 神马的
                        if (success) {
                            success(ret);
                        }
                    });
                    
                }];
                
            }];
            
        }else{
            
            gtasksModel.syncStatus = 1;
            
            int type = editType;
            if (editType==3) {
                type = 2;
            }
            
            [self doSaveGtasksLocationWithGtasksModel:gtasksModel editType:type success:^(BOOL ret) {
                dispatch_async(dispatch_get_main_queue(), ^{
                    // 通知主线程刷新 神马的
                    if (success) {
                        success(ret);
                    }
                });
            }];
            
        }
        
        
    });

}

#pragma mark - 批量保存待办到服务器
- (void)syncRemindWithGtasksArray:(NSArray *)gtasksArray success:(void(^)(BOOL ret))success{
    
    [[AFNWorkingTool sharedManager] AFNHttpPOSTWithUrlstring:requestUrl(synchronousTodos) parameters:gtasksArray success:^(NSDictionary *dictData) {
        
        BOOL isSuccess = NO;
        
        if ([dictData[@"code"]isEqualToNumber:@200]) {
            
            isSuccess = YES;
            
        }else{
            isSuccess = NO;
        }
        
        if (success) {
            success(isSuccess);
        }
        
    } failure:^(NSError *error) {
        
        if (success) {
            success(NO);
        }
    }];
}


#pragma mark - 添加待办到本地
- (void)doSaveGtasksLocationWithGtasksModel:(GtasksModel *)gtasksModel editType:(int)editType success:(void(^)(BOOL ret))success{
    
    BOOL ret = NO;
    if (editType==1)
    {
        ret = [gtasksModel save];//保存
    }
    else if (editType==2){
        ret = [gtasksModel update];//更新
    }
    else
    {
        ret = [gtasksModel deleteObject];//删除
    }
    //添加待办
    if (ret) {
        
        //刷新首页列表
        postNotificationName(kRefreshRemindAndGtasksName);
        
        //刷新待办列表
        postNotificationName(kRefreshGtasksName);
        
        if (success) {
            success(ret);
        }
        
        //删除通知
        if (editType == 2 || editType == 3) {
            
            [[LocalNotificationManager shareManager] cancelLocalNotificationsWithKey:@"todoId" value:gtasksModel.todoId];
        }
        
        //添加通知
        if ((editType == 1 || editType == 2) && gtasksModel.isOpen) {
            if (gtasksModel.deleteState!=1) {
                [[LocalNotificationManager shareManager] addGtaskLocationNotificationWithGtasksModel:gtasksModel];
            }
            
        }
        
    }
    
}

#pragma mark - 查找本地所有未同步的待办数据
- (void)selectLoactionNotSyncGtaskslSuccess:(void(^)(BOOL ret,NSString *message))success{
    
    
    
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
        NSArray *remindArray = [GtasksModel findByCriteria:@"where syncStatus = '1'"];

        if (isUser && remindArray.count>0) {
            
            NSMutableArray *tempArray = [NSMutableArray array];
            
            for (GtasksModel *rModel in remindArray) {
                
                NSMutableDictionary *remindDict = [NSMutableDictionary dictionaryWithDictionary:[rModel mj_keyValues]];
                [remindDict removeObjectForKey:@"propertyNames"];
                [remindDict removeObjectForKey:@"pk"];
                [remindDict removeObjectForKey:@"columeTypes"];
                [remindDict removeObjectForKey:@"columeNames"];
                [tempArray addObject:remindDict];
            }
            
            [self syncRemindWithGtasksArray:tempArray success:^(BOOL ret) {
                
                if (ret) {
                    //更新本地所有待办
                    [self getUserAllGtasksSuccess:^(BOOL ret, NSString *message) {
                        
                        if (success) {
                            success(YES,message);
                        }
                    }];
                }else{
                    
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        if (success) {
                            success(NO,@"待办上传失败");
                        }
                    });
                    
                }
            }];
        }
        else{
            
            
            dispatch_async(dispatch_get_main_queue(), ^{
                
                if (success) {
                    success(NO,@"未登录或者本地没有需要上传的数据");
                }
            });
            
        }
        
    });
   
}

#pragma mark - 获取用户所有的待办
- (void)getUserAllGtasksSuccess:(void(^)(BOOL ret,NSString *message))success{
    
    NSDictionary *param = @{@"id":UserID};
    [[AFNWorkingTool sharedManager] AFNHttpRequestPOSTurlstring:requestUrl(getAllGtasks) parm:param success:^(NSDictionary *dictData) {
        if (dictData && [dictData[@"code"]isEqualToNumber:@200]) {
            
            
              dispatch_async(dispatch_get_global_queue(0, 0), ^{
                  
                  //先取消本地所有的待办通知
                  NSArray *gtasksArray = [GtasksModel findAll];
                  
                  for (GtasksModel * gModel in gtasksArray) {
                      
                      if (gModel.isOpen) {
                          [[LocalNotificationManager shareManager] cancelLocalNotificationsWithKey:@"todoId" value:gModel.todoId];
                      }
                      
                  }
                  
                  //先清空本地表
                  [GtasksModel clearTable];
                  
                  //解析数据,保存本地,重新添加通知
                  NSArray *todoArray = dictData[@"_data"];
                  
                  //待办
                  for (NSDictionary *todoDict in todoArray) {
                      
                      GtasksModel *gModel = [[GtasksModel alloc]init];
                      [gModel setValuesForKeysWithDictionary:todoDict];
                      [gModel save];
                      
                      //判断是否有提醒
                      if (gModel.isOpen) {
                          //添加本地待办通知
                          [[LocalNotificationManager shareManager] addGtaskLocationNotificationWithGtasksModel:gModel];
                      }
                  }
                  
                  postNotificationName(kRefreshGtasksName);
                  
                  
                  dispatch_async(dispatch_get_main_queue(), ^{
                      
                      if (success) {
                          success(YES,@"待办同步完成");
                      }
                  });
                  
              });
           
        }
        
        
    } failure:^(NSError *error) {
        
        if (success) {
            success(NO,@"拉取失败");
        }
        
    }];
}

#pragma mark - 上传本地提醒和待办(放在队列里面操作)
- (void)syncLoactionRemindAndGtasksSuccess:(void(^)(BOOL ret))success{
    
    //提醒
    NSArray *remindArray = [ReminderModel findByCriteria:@"where syncStatus = '1'"];
    
    NSMutableArray *tempRemindArray = [NSMutableArray array];
    
    for (ReminderModel *rModel in remindArray) {
        
        NSMutableDictionary *remindDict = [NSMutableDictionary dictionaryWithDictionary:[rModel mj_keyValues]];
        [remindDict removeObjectForKey:@"propertyNames"];
        [remindDict removeObjectForKey:@"pk"];
        [remindDict removeObjectForKey:@"columeTypes"];
        [remindDict removeObjectForKey:@"columeNames"];
        [tempRemindArray addObject:remindDict];
    }
    
    //待办
    NSArray *gtasksdArray = [GtasksModel findByCriteria:@"where syncStatus = '1'"];
    
    NSMutableArray *tempGtasksArray = [NSMutableArray array];
    
    for (GtasksModel *gModel in gtasksdArray) {
        
        NSMutableDictionary *gtasksDict = [NSMutableDictionary dictionaryWithDictionary:[gModel mj_keyValues]];
        [gtasksDict removeObjectForKey:@"propertyNames"];
        [gtasksDict removeObjectForKey:@"pk"];
        [gtasksDict removeObjectForKey:@"columeTypes"];
        [gtasksDict removeObjectForKey:@"columeNames"];
        [tempGtasksArray addObject:gtasksDict];
    }
    
    NSMutableArray *dataArray = [NSMutableArray array];
    NSMutableArray *urlArray = [NSMutableArray array];
    NSMutableArray *methodArray = [NSMutableArray array];
    if (tempRemindArray.count>0) {
        [dataArray addObject:tempRemindArray];
        [urlArray addObject:requestUrl(synchronousReminds)];
        [methodArray addObject:@"POST"];
    }
    
    if (tempGtasksArray.count  >0) {
        [dataArray addObject:tempGtasksArray];
        [urlArray addObject:requestUrl(synchronousTodos)];
        [methodArray addObject:@"POST"];
    }
    
    if (dataArray.count>0) {
        
        [[AFNWorkingTool sharedManager] INSNSOperationQueueWithDataArray:dataArray urlStringArray:urlArray methodArray:methodArray success:^(id dictData, NSInteger row) {
            
        } failure:^(NSError *error, NSString *message, NSInteger row) {
            
        } AllSuccess:^(id responseObject) {
            
            if (success) {
                success(YES);
            }
            
        }];
    }
    else{
        if (success) {
            success(YES);
        }
    }

}


#pragma mark - 查询本地未同步的待办
- (NSArray *)selectNotSyncGtasks{
    
    return [GtasksModel findByCriteria:@"where syncStatus = '1'"];
}

#pragma mark - 查询本地未同步的提醒
- (NSArray *)selectNotSyncRemind{
    
    return [ReminderModel findByCriteria:@"where syncStatus = '1'"];
}

#pragma mark - 获取今天的提醒
- (NSMutableArray *)getRemindArrayWithDate:(NSDate *)date{

    NSString *todayStr = [Utils transformDate:date dateFormatStyle:DateFormatYearMonthDayHourMinute];
    NSString *todayWeekDay = [self weekdayStringFromDate:date];
    NSString *sql = [NSString stringWithFormat:@"select * from ReminderModel a where repeatFlag=0 and strDate='%@' and deleteState = '0' union select * from ReminderModel a where (repeatFlag=3 and repeatValue like '%%%@%%' and strDate<='%@' and deleteState = '0')  order by startTime",todayStr,todayWeekDay,todayStr];
    
    NSMutableArray *todyReminderArray = [NSMutableArray arrayWithArray:[ReminderModel selectDataWithSql:sql]];
    
    
    //判断今天是工作日还是非工作日
    NSMutableArray *isWorkDayArray = [NSMutableArray arrayWithArray:[CalendarModel selectDataWithSql:[NSString stringWithFormat:@"select *from CalendarModel where date = '%@'",todayStr]]];
    CalendarModel *cModel = [isWorkDayArray lastObject];
    
    NSString *isNotWorkSql = @"";
    if ([cModel.remark isEqualToString:@"1"]) {
        isNotWorkSql = [NSString stringWithFormat:@"select *from ReminderModel where repeatFlag = 1 and deleteState = '0' and strDate <= '%@' order by startTime",todayStr];
    }else{
        isNotWorkSql = [NSString stringWithFormat:@"select *from ReminderModel where repeatFlag = 2 and deleteState = '0' and strDate <= '%@' order by startTime",todayStr];
    }
    
    NSMutableArray *isWorkRemindArray = [NSMutableArray arrayWithArray:[ReminderModel selectDataWithSql:isNotWorkSql]];
    
    NSMutableArray *allDayReminderArray = [NSMutableArray array];
    if (todyReminderArray.count>0 || isWorkRemindArray.count>0) {
        NSMutableArray *newTodyReminderArray = [NSMutableArray array];
        for (int i = 0; i<todyReminderArray.count; i++) {
            
            ReminderModel *model = todyReminderArray[i];
            if (model.isAllday) {
                [allDayReminderArray addObject:model];
            }else{
                [newTodyReminderArray addObject:model];
            }
            
        }
        
        //把非工作日和工作日的加进来
        for (int k = 0; k<isWorkRemindArray.count; k++) {
            
            [newTodyReminderArray addObject:isWorkRemindArray[k]];
        }
        
        NSArray *newArray = [self arraySortDescOrAscWithType:0 sortArray:newTodyReminderArray];
        
        for (int j = 0; j<newArray.count; j++) {
            
            [allDayReminderArray addObject:newArray[j]];
        }
        
        
    }
    
    return allDayReminderArray;
    
}

#pragma mark - 获取明天的提醒
- (NSMutableArray *)getTomorrowRemindArray{
    
    //明天
    NSString *nextDay = [self GetTomorrowDay:[NSDate date]];
    NSArray *nextDayArray = [ReminderModel selectDataWithSql:[NSString stringWithFormat:@"select * from ReminderModel a where repeatFlag=0 and strDate='%@' and deleteState = '0' union select * from ReminderModel a where (repeatFlag=3 and repeatValue like '%%%@%%'and strDate<='%@' and deleteState = '0') or repeatFlag=2 order by startTime",nextDay,[NSString stringWithFormat:@"%d",[[self weekdayStringFromDate:[NSDate date]] intValue]+1],nextDay]];
    
    
    
    //明天非工作日
    NSMutableArray *isWorkDayArray = [NSMutableArray arrayWithArray:[CalendarModel selectDataWithSql:[NSString stringWithFormat:@"select *from CalendarModel where date = '%@' ",nextDay]]];
    CalendarModel *cModel = [isWorkDayArray lastObject];
    NSString *isNotWorkSql = @"";
    if ([cModel.remark isEqualToString:@"1"]) {
        isNotWorkSql = @"select *from ReminderModel where repeatFlag = 1 and deleteState = '0' order by startTime";//查询工作日的提醒
    }else{
        isNotWorkSql = @"select *from ReminderModel where repeatFlag = 2 and deleteState = '0' order by startTime";//查询节假日的提醒
    }
    
    NSMutableArray *isWorkRemindArray = [NSMutableArray arrayWithArray:[ReminderModel selectDataWithSql:isNotWorkSql]];
    
    NSMutableArray *allDayReminderArray = [NSMutableArray array];
    if (nextDayArray.count>0 || isWorkDayArray.count>0) {
        
        NSMutableArray *newTodyReminderArray = [NSMutableArray array];
        for (int i = 0; i<nextDayArray.count; i++) {
            
            ReminderModel *model = nextDayArray[i];
            if (model.isAllday) {
                [allDayReminderArray addObject:model];
            }else{
                [newTodyReminderArray addObject:model];
            }
            
        }
        
        //把非工作日和工作日的加进来
        for (int k = 0; k<isWorkRemindArray.count; k++) {
            
            [newTodyReminderArray addObject:isWorkRemindArray[k]];
        }
        
        NSArray *newArray = [self arraySortDescOrAscWithType:0 sortArray:newTodyReminderArray];
        
        for (int j = 0; j<newArray.count; j++) {
            
            [allDayReminderArray addObject:newArray[j]];
        }
        
    }
    
    return allDayReminderArray;
    
}

#pragma mark - 获取未来的提醒
- (NSMutableArray *)getFutureRemindArray{
    
    //以后
    NSArray *afterArray = [ReminderModel selectDataWithSql:[NSString stringWithFormat:@"select * from ReminderModel a where (strDate>'%@' or repeatFlag <>0) and deleteState = '0' ",[self GetTomorrowDay:[NSDate date]]]];
    NSMutableArray *allDayReminderArray = [NSMutableArray array];
    if (afterArray.count>0) {
        
        NSMutableArray *newTodyReminderArray = [NSMutableArray array];
        for (int i = 0; i<afterArray.count; i++) {
            
            ReminderModel *model = afterArray[i];
            if (model.isAllday) {
                [allDayReminderArray addObject:model];
            }else{
                [newTodyReminderArray addObject:model];
            }
            
        }
        
        if (newTodyReminderArray.count>0) {
            for (int k = 0; k<newTodyReminderArray.count; k++) {
                [allDayReminderArray addObject:newTodyReminderArray[k]];
            }
        }
    }
    return allDayReminderArray;
}

#pragma mark - 获取所有待办
- (NSMutableArray *)getAllGtasksArray{
    NSArray *remindArray = [GtasksModel findByCriteria:[NSString stringWithFormat:@" WHERE isOpen = '1' and isComplete = '0' and deleteState = '0' order by strDate"]];
    
    NSArray *gtaskArray = [GtasksModel findByCriteria:[NSString stringWithFormat:@" WHERE isOpen = '0' and isComplete = '0' and deleteState = '0' order by appCreatedTime"]];
    
    
    NSMutableArray *todayGtasksArray = [NSMutableArray array];
    
    if (remindArray.count>0 || gtaskArray.count>0) {
        for (int i=0; i<remindArray.count; i++) {
            [todayGtasksArray addObject:remindArray[i]];
            
//            GtasksModel *gModel = remindArray[i];
//            RKLog(@"%@",gModel.content);
        }
        for (int k=0; k<gtaskArray.count; k++) {
            [todayGtasksArray addObject:gtaskArray[k]];
        }
        
    }

    return todayGtasksArray;
}

#pragma mark - 获取所有待办历史
- (NSMutableArray *)getAllGtasksHistory{
    
    NSArray *array = [GtasksModel findByCriteria:[NSString stringWithFormat:@" WHERE isComplete = '1' and deleteState = '0' order by completeDate desc"]];
    //遍历时，时间相同的装在同一个数组中，先取_dataArray[0]分一组
    NSMutableArray *gtasksArray = [NSMutableArray array];
    if (array.count>0) {
        
        NSMutableArray *currentArr=[NSMutableArray array];
        [currentArr addObject:array[0]];
        
        [gtasksArray addObject:currentArr];
        
        if(array.count>1)
        {
            for (int i=1;i<array.count;i++)
            {
                //取上一组元素并获取上一组元素的比较日期
                NSMutableArray *preArr=[gtasksArray objectAtIndex:gtasksArray.count-1];
                
                GtasksModel *gModel = [preArr objectAtIndex:0];
                
                NSString *pretime = [gModel.completeDate substringToIndex:8];
                
                //取当前遍历的字典中的日期
                GtasksModel *currentModel = [array objectAtIndex:i];
                
                NSString *time = [currentModel.completeDate substringToIndex:8];
                
                //如果遍历当前字典的日期和上一组元素中日期相同则把当前字典分类到上一组元素中
                if([time isEqualToString:pretime])
                {
                    [currentArr addObject:currentModel];
                }
                //如果当前字典的日期和上一组元素日期不同，则重新开新的一组，把这组放入到分类数组中
                else
                {
                    currentArr= nil;
                    
                    currentArr=[NSMutableArray array];
                    
                    [currentArr addObject:currentModel];
                    
                    [gtasksArray addObject:currentArr];
                    
                }
            }
        }
        
    }
    
    return gtasksArray;
    
}

- (NSArray *)arraySortDescOrAscWithType:(int)type sortArray:(NSArray *)sortArray{
    
    //type:1降序,0升序
    NSArray *result = [sortArray sortedArrayUsingComparator:^NSComparisonResult(id  _Nonnull obj1, id  _Nonnull obj2) {
        
//        NSLog(@"%@~%@",obj1,obj2); //3~4 2~1 3~1 3~2
        
        if ([obj1 isKindOfClass:[ReminderModel class]]) {
            
            ReminderModel *mode1 = (ReminderModel *)obj1;
            ReminderModel *mode2 = (ReminderModel *)obj2;
            NSLog(@"%@~%@",mode1.startTime,mode2.startTime); //3~4 2~1 3~1 3~2
            if (type==0) {
                return [mode1.startTime compare:mode2.startTime];
            }else{
                return [mode2.startTime compare:mode1.startTime];
            }
        }else{
            
            if (type==0) {
                return [obj1 compare:obj2];
            }else{
                return [obj2 compare:obj1];
            }
            
        }
    }];
    
    return result;
    
}

- (NSArray *)selectAllRemindAndGtasks{

    //记录所要提醒的日期
    NSMutableArray *allDay = [NSMutableArray array];
    
    
    //不重复的提醒
    NSArray *notRepeatRemind = [ReminderModel findByCriteria:[NSString stringWithFormat:@" WHERE repeatFlag=0 and deleteState = '0' order by strDate"]];
    for (ReminderModel *model in notRepeatRemind) {
        [allDay addObject:model.strDate];
    }
    
    //法定工作日提醒
    NSArray *legalWorkDayRemind = [ReminderModel findByCriteria:[NSString stringWithFormat:@" WHERE repeatFlag=1 and deleteState = '0' order by strDate"]];

    if (legalWorkDayRemind.count>0) {
        ReminderModel *legalModel = [legalWorkDayRemind firstObject];
        //查询时间
        NSArray *legalWorkDayremindTimeArray = [CalendarModel findByCriteria:[NSString stringWithFormat:@" where date >= '%@' and remark = '1'",legalModel.strDate]];
        for (CalendarModel *model in legalWorkDayremindTimeArray) {
            [allDay addObject:model.date];
        }
    }
    
    
    
    //法定节假日提醒
    NSArray *legalHolidaysRemind = [ReminderModel findByCriteria:[NSString stringWithFormat:@" WHERE repeatFlag=2 and deleteState = '0' order by strDate"]];
    if (legalHolidaysRemind.count>0) {
        ReminderModel *legalHolidaysModel = [legalHolidaysRemind firstObject];
        //查询时间
        NSArray *legalHolidaysRemindTimeArray = [CalendarModel findByCriteria:[NSString stringWithFormat:@" where date >= '%@' and remark = '0'",legalHolidaysModel.strDate]];
        for (CalendarModel *model in legalHolidaysRemindTimeArray) {
            [allDay addObject:model.date];
        }
    }
    
    
    
    //星期提醒
    NSArray *weekDayRemind = [ReminderModel findByCriteria:[NSString stringWithFormat:@" WHERE repeatFlag=3 and (repeatValue like '%%1%%' or repeatValue like '%%2%%' or repeatValue like '%%3%%' or repeatValue like '%%4%%' or repeatValue like '%%5%%' or repeatValue like '%%6%%' or repeatValue like '%%7%%') and deleteState = '0'  order by strDate"]];
    
    
    //记录所要提醒的星期
    NSMutableArray *repeatValueArray = [NSMutableArray array];
    
    NSString *repeatValueStr = @"";
    
    for (ReminderModel *rModel in weekDayRemind) {
        
        //如果1~7,每天都有了,就不在循环
        if (repeatValueArray.count==7) {
            break;
        }
        
        //把星期字符串转化为数组
        NSArray *repeatWeekDayArray = [NSArray arrayWithArray:[rModel.repeatValue componentsSeparatedByString:@"," ]];
        
        for (NSString *weekDay in repeatWeekDayArray) {
            
            if (repeatValueArray.count==7) {
                break;
            }
            
            if (repeatValueArray.count>0) {
                
                //先匹配这个星期有没有查询过时间,如果没有查询这个提醒这个星期最早的时间
                if (![repeatValueStr containsString:weekDay]) {
                    [repeatValueArray addObject:weekDay];
                    repeatValueStr = [repeatValueArray componentsJoinedByString:@","];
                    
                    //查询时间
                    NSArray *remindTimeArray = [CalendarModel findByCriteria:[NSString stringWithFormat:@" where weekday = '%@' and date >= '%@'",weekDay,rModel.strDate]];
                    
                    for (CalendarModel *cModel in remindTimeArray) {
                        [allDay addObject:cModel.date];
                    }
                    
                }
                
                
            }else{
                
                [repeatValueArray addObject:weekDay];
                repeatValueStr = [repeatValueArray componentsJoinedByString:@","];
                //查询时间
                NSArray *remindTimeArray = [CalendarModel findByCriteria:[NSString stringWithFormat:@" where weekday = '%@' and date >= '%@'",weekDay,rModel.strDate]];
                
                for (CalendarModel *cModel in remindTimeArray) {
                    [allDay addObject:cModel.date];
                }

            }
            
        }
        
    }
    
    
    //去除重复时间
    NSSet *set = [NSSet setWithArray:allDay];
//    RKLog(@"%@",[set allObjects]);
    
    NSArray *finalResultArray = [self arraySortDescOrAscWithType:0 sortArray:[set allObjects]];
    
    NSMutableArray *array = [NSMutableArray array];
    for (NSString *dateStr in finalResultArray) {
        [array addObject:[Utils setOldStringTime:dateStr inputFormat:@"yyyyMMdd" outputFormat:@"yyyy-MM-dd"]];
    }

    return array;
    
}


- (NSArray *)selectnearRemindAndGtasks{
    
    NSMutableArray *allDay = [NSMutableArray array];
    
    NSMutableArray *RGModelArray = [NSMutableArray array];
    
    NSInteger advanceDay = 4;//查询几天的量通知
    
    //不重复的提醒(3天内的)
    NSArray *notRepeatRemind = [ReminderModel findByCriteria:[NSString stringWithFormat:@" WHERE repeatFlag=0 and strDate >='%@' and deleteState = '0' and strDate <='%@'  order by strDate",[Utils getCurrentTimeWithDay],[self GetDay:[NSDate date] days:advanceDay]]];
    [RGModelArray addObjectsFromArray:notRepeatRemind];
    
    for (ReminderModel *rModel in notRepeatRemind) {
        [allDay addObject:rModel.strDate];
    }
    
    
    //工作日重复
    //查询时间
    NSArray *legalWorkDayremindTimeArray = [CalendarModel findByCriteria:[NSString stringWithFormat:@" where date >= '%@' and remark = '1' and date <= '%@'",[Utils getCurrentTimeWithDay],[self GetDay:[NSDate date] days:advanceDay]]];
    
    if (legalWorkDayremindTimeArray.count>0) {
        NSArray *legalWorkDayRemind = [ReminderModel findByCriteria:[NSString stringWithFormat:@" WHERE repeatFlag=1 and deleteState = '0' order by strDate"]];
        [RGModelArray addObjectsFromArray:legalWorkDayRemind];
    }
    
    
    
    //法定节假日提醒
    //查询时间
    NSArray *legalHolidaysRemindTimeArray = [CalendarModel findByCriteria:[NSString stringWithFormat:@" where date >= '%@' and remark = '0' and date <= '%@'",[Utils getCurrentTimeWithDay],[self GetDay:[NSDate date] days:advanceDay]]];
    
    if (legalHolidaysRemindTimeArray.count>0) {
        
        NSArray *legalHolidaysRemind = [ReminderModel findByCriteria:[NSString stringWithFormat:@" WHERE repeatFlag=2 and deleteState = '0' order by strDate"]];
        
        [RGModelArray addObjectsFromArray:legalHolidaysRemind];
    }
    
    
   
    //查询星期
    NSString *today = [Utils getCurrentTimeWithDay];
    
    NSMutableArray *dayArray = [NSMutableArray array];
    
    for (int j = 0; j<=advanceDay; j++) {
        
        if (j==0) {
            [dayArray addObject:today];
        }else{
            [dayArray addObject:[self GetDay:[NSDate date] days:j]];
        }
        
    }
    
    NSMutableArray *weekRemindArray = [NSMutableArray array];
    
    for (NSString *dayStr in dayArray) {
        
        NSString *weekDay = [Utils getWeekDayWithDate:[Utils getDataString:dayStr formatter:@"yyyyMMdd"]];
        //星期提醒
        NSArray *weekDayRemind = [ReminderModel findByCriteria:[NSString stringWithFormat:@" WHERE repeatFlag=3 and (repeatValue like '%%%@%%') and deleteState = '0'  order by strDate",weekDay]];
        
        if (weekRemindArray.count>0) {
            
            NSMutableArray *tempArray = [NSMutableArray array];
            
            for (ReminderModel *rrModel in weekDayRemind) {
                
                for (ReminderModel *rModel in weekRemindArray) {
                    
                    if (![rrModel.reminderId isEqualToString:rModel.reminderId]) {
                        
                        [tempArray addObject:rrModel];
                    }
                }
                
            }
            
            [weekRemindArray addObjectsFromArray:tempArray];
            
        }else{
            [weekRemindArray addObjectsFromArray:weekDayRemind];
        }
        
    }
    
    [RGModelArray addObjectsFromArray:weekRemindArray];
    

    [[LocalNotificationManager shareManager] cancelAllLocalNotifications];
    
    for (ReminderModel *model in RGModelArray) {
        
        [[LocalNotificationManager shareManager] addLocalNotifisWithRemindModel:model];
    }
    
    NSArray *gtasksArray = [GtasksModel findByCriteria:[NSString stringWithFormat:@" WHERE isOpen = '1' and isComplete = '0' and strDate >='%@' and deleteState = '0' and strDate <='%@'  order by strDate",[Utils getCurrentTimeWithDay],[self GetDay:[NSDate date] days:advanceDay]]];
    
    
    if (gtasksArray.count>0) {
        
        for (GtasksModel *model in gtasksArray) {
            
            [[LocalNotificationManager shareManager] addGtaskLocationNotificationWithGtasksModel:model];
        }
        
    }

    return allDay;
}


#pragma mark - 初始化本地数据库,并同步所有信息
- (void)instantiationLoactionDBWithSynchronousAllSuccess:(void(^)(BOOL ret))success{
    
    //更新数据库路径
    [[LKDBTool shareInstance] changeDBWithDirectoryName:nil];
    
    //初始化本地数据库,主要判断有无数据库,如果没有,创建
    [ReminderModel createTable];
    [GtasksModel createTable];
    [CalendarModel createTable];
    [ReportLabel createTable];
    
    [[AFNWorkingTool sharedManager] AFNHttpRequestPOSTurlstring:requestUrl(synchronousAll) parm:@{@"id":UserID} success:^(NSDictionary *dictData) {
        
        RKLog(@"%@",dictData);
        
        if (dictData && [dictData[@"code"]isEqualToNumber:@200]) {
            
            //先清空本地表
            [ReminderModel clearTable];
            [GtasksModel clearTable];
            
            NSDictionary *userDict = dictData[@"_data"];
            //提醒
            for (NSDictionary *remind in userDict[@"remind"]) {
                
                ReminderModel *remindModel = [[ReminderModel alloc]init];
                [remindModel setValuesForKeysWithDictionary:remind];
                [remindModel save];
                
                //添加本地提醒通知
                [[LocalNotificationManager shareManager] addLocalNotifisWithRemindModel:remindModel];
            }
            
            //待办
            for (NSDictionary *todoDict in userDict[@"todo"]) {
                
                GtasksModel *gModel = [[GtasksModel alloc]init];
                [gModel setValuesForKeysWithDictionary:todoDict];
                [gModel save];
                
                //判断是否有提醒
                if (gModel.isOpen) {
                    //添加本地待办通知
                    [[LocalNotificationManager shareManager] addGtaskLocationNotificationWithGtasksModel:gModel];
                }
            }
            
            if (success) {
                success(YES);
            }
        }else{
            if (success) {
                success(NO);
            }
        }
        
    } failure:^(NSError *error) {
        
        RKLog(@"用户同步所有信息失败");
        
        if (success) {
            success(NO);
        }
    }];
    
}

#pragma mark - 明天
- (NSString *)GetTomorrowDay:(NSDate *)aDate {
    NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    NSDateComponents *components = [gregorian components:NSCalendarUnitWeekday | NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay fromDate:aDate];
    [components setDay:([components day]+1)];
    NSDate *beginningOfWeek = [gregorian dateFromComponents:components];
    NSDateFormatter *dateday = [[NSDateFormatter alloc] init];
    [dateday setDateFormat:@"yyyyMMdd"];
    return [dateday stringFromDate:beginningOfWeek];
}

#pragma mark - 距离某个时间几天后的时间
- (NSString *)GetDay:(NSDate *)aDate days:(NSInteger)days {
    NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    NSDateComponents *components = [gregorian components:NSCalendarUnitWeekday | NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay fromDate:aDate];
    [components setDay:([components day]+days)];
    NSDate *beginningOfWeek = [gregorian dateFromComponents:components];
    NSDateFormatter *dateday = [[NSDateFormatter alloc] init];
    [dateday setDateFormat:@"yyyyMMdd"];
    return [dateday stringFromDate:beginningOfWeek];
}

#pragma mark - 根据时间获取周
- (NSString*)weekdayStringFromDate:(NSDate*)inputDate {
    
    NSArray *weekdays = [NSArray arrayWithObjects: [NSNull null], @"7", @"1", @"2", @"3", @"4", @"5", @"6", nil];
    
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    
    NSTimeZone *timeZone = [[NSTimeZone alloc] initWithName:@"Asia/Shanghai"];
    
    [calendar setTimeZone: timeZone];
    
    NSCalendarUnit calendarUnit = NSCalendarUnitWeekday;
    
    NSDateComponents *theComponents = [calendar components:calendarUnit fromDate:inputDate];
    
    return [weekdays objectAtIndex:theComponents.weekday];
    
}



#pragma mark - 获取当前网路状态
- (int)networkStatus
{
    int status = 0;
    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    status = [app.networkReachability intValue];
    return status;
}



@end



