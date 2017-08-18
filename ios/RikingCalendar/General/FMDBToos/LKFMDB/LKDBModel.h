//
//  LKDBModel.h
//  LKFMDB_Demo
//
//  Created by lk on 16/3/21.
//  Copyright © 2016年 LK. All rights reserved.
//  github https://github.com/544523660/LKFMDB

#import <Foundation/Foundation.h>
#import "RKBaseModel.h"
/** SQLite五种数据类型 */
#define SQLTEXT     @"TEXT"
#define SQLINTEGER  @"INTEGER"
#define SQLREAL     @"REAL"
#define SQLBLOB     @"BLOB"
#define SQLNULL     @"NULL"
#define PrimaryKey  @"primary key"
@interface LKDBModel : RKBaseModel
/** rowid */
@property (nonatomic, assign)   int        pk;
/** 属性名 */
@property (retain, readonly, nonatomic) NSMutableArray         *propertyNames;
/** 列类型 */
@property (retain, readonly, nonatomic) NSMutableArray         *columeTypes;
/** 列名 */
@property (retain, readonly, nonatomic) NSMutableArray         *columeNames;

#pragma mark 常用方法
/** 保存或更新
 * 如果不存在主键，保存，
 * 有主键，则更新
 */
- (BOOL)saveOrUpdate;
/** 保存或更新
 * 如果根据特定的列数据可以获取记录，则更新，
 * 没有记录，则保存
 */
- (BOOL)saveOrUpdateByColumnName:(NSString*)columnName AndColumnValue:(NSString*)columnValue;
/** 保存单个数据 */
- (BOOL)save;
/** 批量保存数据 */
+ (BOOL)saveObjects:(NSArray *)array;

+(BOOL)saveOrUpdateObjects:(NSArray *)array;
/** 更新单个数据 */
- (BOOL)update;
/** 批量更新数据*/
+ (BOOL)updateObjects:(NSArray *)array;
/** 删除单个数据 */
- (BOOL)deleteObject;
/** 批量删除数据 */
+ (BOOL)deleteObjects:(NSArray *)array;
/** 通过条件删除数据 */
+ (BOOL)deleteObjectsByCriteria:(NSString *)criteria;
/** 通过条件删除 (多参数）--2 */
+ (BOOL)deleteObjectsWithFormat:(NSString *)format, ...;
/** 清空表 */
+ (BOOL)clearTable;

/** 查询全部数据 */
+ (NSArray *)findAll;

/** 通过主键查询 */
+ (instancetype)findByPK:(id)inPk;

+ (instancetype)findFirstWithFormat:(NSString *)format, ...;

/** 查找某条数据 */
+ (instancetype)findFirstByCriteria:(NSString *)criteria;

+ (NSArray *)findWithFormat:(NSString *)format, ...;

/** 通过条件查找数据
 * 这样可以进行分页查询 @" WHERE pk > 5 limit 10"
 */
+ (NSArray *)findByCriteria:(NSString *)criteria;
/**
 * 创建表
 * 如果已经创建，返回YES
 */
+ (BOOL)createTable;



#pragma mark 必须要重写的方法
/** 如果子类中有一些property不需要创建数据库字段,或者对字段加修饰属性   具体请参考LKDBColumnDes类*/
+ (NSDictionary *)describeColumnDict;

#pragma mark 不重要的方法
/**
 *  获取该类的所有属性
 */
+ (NSDictionary *)getPropertys;

/** 获取所有属性，包括主键 */
+ (NSDictionary *)getAllProperties;

/** 数据库中是否存在表 */
+ (BOOL)isExistInTable;

/** 表中的字段*/
+ (NSArray *)getColumns;

+ (NSArray *)selectDataWithSql:(NSString *)sql;

@end
