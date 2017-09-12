//
//  RemindAndGtasksDBManager.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/16.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface RemindAndGtasksDBManager : NSObject

+ (RemindAndGtasksDBManager *)shareManager;

#pragma mark - 获取提醒根据时间

/**
 获取提醒根据时间

 @param date date description
 @return return value description
 */
- (NSMutableArray *)getRemindArrayWithDate:(NSDate *)date;

#pragma mark - 获取明天的提醒

/**
 获取明天的提醒

 @return return value description
 */
- (NSMutableArray *)getTomorrowRemindArray;

#pragma mark - 获取未来的提醒

/**
 获取未来的提醒

 @return return value description
 */
- (NSMutableArray *)getFutureRemindArray;

#pragma mark - 获取所有待办

/**
 获取所有待办

 @return return value description
 */
- (NSMutableArray *)getAllGtasksArray;


#pragma mark - 获取所有待办历史

/**
 获取所有待办历史

 @return return value description
 */
- (NSMutableArray *)getAllGtasksHistory;

#pragma mark - 明天的时间

/**
 明天的时间

 @param aDate aDate description
 @return return value description
 */
- (NSString *)GetTomorrowDay:(NSDate *)aDate;



#pragma mark - 保存提醒
/**
 保存提醒

 @param remindModel remindModel description
 @param editType editType description
 @param success success description
 */
- (void)doSaveRemindWithRemindModel:(ReminderModel *)remindModel
                           editType:(int)editType
                            success:(void(^)(BOOL ret))success;




#pragma mark - 添加待办到服务器

/**
 添加待办到服务器

 @param gtasksModel gtasksModel description
 @param editType editType description
 @param success success description
 */
- (void)doSaveGtasksNetWorkWithGtasksModel:(GtasksModel *)gtasksModel
                                  editType:(int)editType
                                   success:(void(^)(BOOL ret))success;


#pragma mark - 查找本地所有未同步的提醒并上传服务器
/**
 查找本地所有未同步的提醒并上传服务器
 */
- (void)selectLoactionNotSyncRemindModelSuccess:(void(^)(BOOL ret, NSString *message))success;

#pragma mark - 查找本地所有未同步的待办数据

/**
 查找本地所有未同步的待办数据

 */
- (void)selectLoactionNotSyncGtaskslSuccess:(void(^)(BOOL ret,NSString *message))success;

#pragma mark - 登录同步所有信息
/**
 登录同步所有信息
 */
- (void)instantiationLoactionDBWithSynchronousAllSuccess:(void(^)(BOOL ret))success;

- (void)syncLoactionRemindAndGtasksSuccess:(void(^)(BOOL ret))success;

#pragma mark - 查询本地未同步的待办

/**
 查询本地未同步的待办

 @return return value description
 */
- (NSArray *)selectNotSyncGtasks;

#pragma mark - 查询本地未同步的提醒

/**
 查询本地未同步的提醒

 @return return value description
 */
- (NSArray *)selectNotSyncRemind;

#pragma mark -  获取所有提醒和待办的时间
/**
 获取所有提醒和待办的时间
 */
- (NSArray *)selectAllRemindAndGtasks;


- (NSArray *)selectnearRemindAndGtasks;
@end
