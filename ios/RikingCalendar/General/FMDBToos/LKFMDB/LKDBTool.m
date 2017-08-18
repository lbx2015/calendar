//
//  LKDBTool.m
//  LKFMDB_Demo
//
//  Created by lk on 16/3/21.
//  Copyright © 2016年 LK. All rights reserved.
//  github https://github.com/544523660/LKFMDB

#import "LKDBTool.h"
#import "LKDBModel.h"

@interface LKDBTool()
@property (nonatomic, retain) FMDatabaseQueue *dbQueue;
@end

static LKDBTool *_instance = nil;
@implementation LKDBTool

+ (instancetype)shareInstance{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _instance = [[super allocWithZone:NULL] init];
    });
    return _instance;
}

+ (instancetype)allocWithZone:(struct _NSZone *)zone{
    return [LKDBTool shareInstance];
}

- (instancetype)copyWithZone:(struct _NSZone *)zone
{
    return [LKDBTool shareInstance];
}

+ (NSString *)dbPath
{
    return [self dbPathWithDirectoryName:nil];
}

+ (NSString *)dbPathWithDirectoryName:(NSString *)directoryName
{
    NSString *docsdir = [NSSearchPathForDirectoriesInDomains( NSDocumentDirectory, NSUserDomainMask, YES) lastObject];
    NSFileManager *filemanage = [NSFileManager defaultManager];
    
    if (isUser) {
        if (directoryName == nil || directoryName.length == 0) {
            docsdir = [docsdir stringByAppendingPathComponent:[NSString stringWithFormat:@"%@",UserID]];
        } else {
            docsdir = [docsdir stringByAppendingPathComponent:directoryName];
        }
    }else{
        if (directoryName == nil || directoryName.length == 0) {
            docsdir = [docsdir stringByAppendingPathComponent:@"RKDB"];
        } else {
            docsdir = [docsdir stringByAppendingPathComponent:directoryName];
        }
    }

    BOOL isDir;
    BOOL exit =[filemanage fileExistsAtPath:docsdir isDirectory:&isDir];
    if (!exit || !isDir) {
        [filemanage createDirectoryAtPath:docsdir withIntermediateDirectories:YES attributes:nil error:nil];
    }
    NSString *dbpath = [docsdir stringByAppendingPathComponent:@"rkdb.db"];
    
    NSLog(@"dbpath %@",dbpath);
    return dbpath;
}


- (FMDatabaseQueue *)dbQueue
{
    if (_dbQueue == nil) {
        _dbQueue = [[FMDatabaseQueue alloc] initWithPath:[self.class dbPath]];
    }
    return _dbQueue;
}

- (BOOL)changeDBWithDirectoryName:(NSString *)directoryName
{
    if (_instance.dbQueue) {
        _instance.dbQueue = nil;
    }
    _instance.dbQueue = [[FMDatabaseQueue alloc] initWithPath:[LKDBTool dbPathWithDirectoryName:directoryName]];
    
    int numClasses;
    Class *classes = NULL;
    numClasses = objc_getClassList(NULL,0);
    
    if (numClasses > 0 )
    {
        classes = (__unsafe_unretained Class *)malloc(sizeof(Class) * numClasses);
        numClasses = objc_getClassList(classes, numClasses);
        for (int i = 0; i < numClasses; i++) {
            if (class_getSuperclass(classes[i]) == [LKDBTool class]){
                id class = classes[i];
                [class performSelector:@selector(createTable) withObject:nil];
            }
        }
        free(classes);
    }
    
    return YES;
}





@end
