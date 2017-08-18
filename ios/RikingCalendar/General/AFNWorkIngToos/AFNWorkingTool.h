//
//  AFNWorkingTool.h
//  KakaPersonal
//
//  Created by Aaron on 2017/3/14.
//  Copyright © 2017年 kakayun. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSUInteger, KKHTTPMethod)
{
    POST,
    GET
};


@interface AFNWorkingTool : NSObject

+ (AFNWorkingTool *)sharedManager;

/**
 GET请求

 @param url 请求地址
 @param timeout 时间
 @param success 成功回调
 @param failue 失败回调
 */
- (void)AFNHttpRequestGETWithUrlstring:(NSString *)url timeout:(NSInteger)timeout success:(void(^)(id responseDic))success failure:(void (^)(NSError *error))failue;



/**
 POST请求
 @param url 请求地址
 @param parm 请求体
 @param success 成功回调
 @param failue 失败回调
 */
- (void)AFNHttpRequestPOSTurlstring:(NSString *)url parm:(NSDictionary *)parm success:(void(^)(NSDictionary *dictData))success failure:(void (^)(NSError *error))failue;


-(void)UpLoadImage:(NSString *)url parm:(NSMutableDictionary *)parm images:(NSMutableArray *)imageArr success:(void(^)(NSDictionary *dictData))success   failure:(void (^)(NSError *error))failue;

/**
 队列请求
 */
- (void)INSNSOperationQueueWithDataArray:(NSArray *)array urlString:(NSString *)urlString method:(NSString *)method success:(void(^)(id dictData,NSInteger row))success failure:(void (^)(NSError *error,NSString *message,NSInteger row))failue AllSuccess:(void(^)(id responseObject))allSuccess;

@end
