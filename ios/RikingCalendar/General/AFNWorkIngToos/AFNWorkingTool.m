//
//  AFNWorkingTool.m
//  KakaPersonal
//
//  Created by Aaron on 2017/3/14.
//  Copyright © 2017年 kakayun. All rights reserved.
//

#import "AFNWorkingTool.h"
#import "AFNetworking.h"
#import "UIKit+AFNetworking.h"
#import "INSPECTIONSessionWrapperOperation.h"
#import "AppDelegate.h"

static BOOL isFirst = NO;
static BOOL canCHeckNetwork = NO;


static AFNWorkingTool *_manager = nil;
@implementation AFNWorkingTool
{
     AFHTTPSessionManager *_session;
}


#pragma mark - 获取唯一单例
+(AFNWorkingTool *)sharedManager
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        if (!_manager)
        {
            _manager = [[AFNWorkingTool alloc]init];
            
        }
    });
    
    return _manager;
}

#pragma mark - AFN GET请求
-(void)AFNHttpRequestGETWithUrlstring:(NSString *)url timeout:(NSInteger)timeout success:(void(^)(id responseDic))success failure:(void (^)(NSError *error))failue
{
    

    if (!_session)
    {
        _session = [AFHTTPSessionManager manager];
    }
    
    _session.responseSerializer = [AFJSONResponseSerializer serializer];
    
    _session.requestSerializer.timeoutInterval = timeout;
    
    
    [_session GET:url parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
        
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
//        NSDictionary* dict = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
        
        NSDictionary *dict = (NSDictionary*)responseObject;
        RKLog(@"请求的URL:\n********%@********",task.response.URL);
        RKLog(@"Message:\n********%@********",dict[@"Message"]);
        RKLog(@"Result------>:\n%@",dict);
        if (success)
        {
            success(dict);
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        RKLog(@"**********%@**********",error);
        if (failue)
        {
            failue(error);
        }
        
    }];
    
}



#pragma mark - AFN POST请求
-(void)AFNHttpRequestPOSTurlstring:(NSString *)url
                              parm:(NSDictionary *)parm
                           success:(void(^)(NSDictionary *dictData))success
                           failure:(void (^)(NSError *error))failue
{
    
    NSString *boundaryString = @"-----------------------------7db372eb000e2";
    
    if (!_session)
    {
        _session = [AFHTTPSessionManager manager];
    }
    
    _session.responseSerializer = [AFJSONResponseSerializer serializer];
    
    _session.requestSerializer = [AFJSONRequestSerializer serializer];
    
    _session.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"text/plain",@"text/json",@"application/json",@"text/javascript",@"text/html", @"application/javascript", @"text/js", nil];

    [_session.requestSerializer setValue:[NSString stringWithFormat:@"application/json;boundary=%@",boundaryString] forHTTPHeaderField:@"Content-Type"];

    [_session POST:url parameters:parm progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
//        NSDictionary* dict = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
        
        NSDictionary *dict = (NSDictionary *)responseObject;
        RKLog(@"请求的URL:\n********%@********",task.response.URL);
        RKLog(@"Message:\n********%@********",dict[@"Message"]);
        RKLog(@"%@",dict);
        if (success)
        {
            success(dict);
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        RKLog(@"**********%@**********",error);
        if (failue)
        {
            failue(error);
        }
        
    }];
    
}


#pragma mark - AFN POST请求
-(void)AFNHttpPOSTWithUrlstring:(NSString *)urlString
                              parameters:(id)parameters
                           success:(void(^)(NSDictionary *dictData))success
                           failure:(void (^)(NSError *error))failue
{
    
    NSString *boundaryString = @"-----------------------------7db372eb000e2";
    
    if (!_session)
    {
        _session = [AFHTTPSessionManager manager];
    }
    
    _session.responseSerializer = [AFJSONResponseSerializer serializer];
    
    _session.requestSerializer = [AFJSONRequestSerializer serializer];
    
    _session.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"text/plain",@"text/json",@"application/json",@"text/javascript",@"text/html", @"application/javascript", @"text/js", nil];
    
    [_session.requestSerializer setValue:[NSString stringWithFormat:@"application/json;boundary=%@",boundaryString] forHTTPHeaderField:@"Content-Type"];
    
    [_session POST:urlString parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        //        NSDictionary* dict = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
        
        NSDictionary *dict = (NSDictionary *)responseObject;
        RKLog(@"请求的URL:\n********%@********",task.response.URL);
        RKLog(@"Message:\n********%@********",dict[@"Message"]);
        RKLog(@"%@",dict);
        if (success)
        {
            success(dict);
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        RKLog(@"**********%@**********",error);
        if (failue)
        {
            failue(error);
        }
        
    }];
    
}


#pragma mark - 获取当前网路状态
- (int)networkStatus
{
    int status = 0;
    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    status = [app.networkReachability intValue];
    return status;
}


//实现断点下载
- (NSURLSessionDownloadTask *)downFileFromServerWithStringUrl:(NSString *)url
                                                  finishBlock:(void(^)(id Json))finishBlock
                                                    failBlock:(void(^)(NSError *error))failBlock
                                                pregrossBlock:(void(^)(double precent))pregrossBlock{
    
    //下载地址
    NSURL *URL = [NSURL URLWithString:url];
    //默认配置
    NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    //AFN3.0+基于封住URLSession的句柄
    AFURLSessionManager *manager = [[AFURLSessionManager alloc] initWithSessionConfiguration:configuration];
    //请求
    NSURLRequest *request = [NSURLRequest requestWithURL:URL];
    //下载Task操作
    NSURLSessionDownloadTask *downloadTask = [manager downloadTaskWithRequest:request progress:^(NSProgress * _Nonnull downloadProgress) {
        // @property int64_t totalUnitCount;  需要下载文件的总大小
        // @property int64_t completedUnitCount; 当前已经下载的大小
        // 给Progress添加监听 KVO
        
        // 回到主队列刷新UI
        dispatch_async(dispatch_get_main_queue(), ^{
            // 设置进度条的百分比
            
        });
        
        
        
    } destination:^NSURL * _Nonnull(NSURL * _Nonnull targetPath, NSURLResponse * _Nonnull response) {
        //- block的返回值, 要求返回一个URL, 返回的这个URL就是文件的位置的路径(设置下载文件路径)
        NSString *cachesPath = [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) lastObject];
        
        NSString *path = [cachesPath stringByAppendingPathComponent:response.suggestedFilename];
        
        return [NSURL fileURLWithPath:path];
        
    } completionHandler:^(NSURLResponse * _Nonnull response, NSURL * _Nullable filePath, NSError * _Nullable error) {
        
        if (error) {
            if (failBlock) {
                failBlock(error);
            }
        }else{
            if (finishBlock) {
                finishBlock(response);
            }
        }
        
        //设置下载完成操作
        // filePath就是你下载文件的位置，你可以解压，也可以直接拿来使用
        
        
    }];
    
    return downloadTask;
    
    //[downloadTask resume];//开始下载
    //[downloadTask suspend];//暂停下载
}



//上传1
- (void)upLoad1{
    
    //1。创建管理者对象
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    //2.上传文件
    NSDictionary *dict = @{@"username":@"1234"};
    
    NSString *urlString = @"22222";
    [manager POST:urlString parameters:dict constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
        //上传文件参数
        UIImage *iamge = [UIImage imageNamed:@"123.png"];
        NSData *data = UIImagePNGRepresentation(iamge);
        //这个就是参数
        [formData appendPartWithFileData:data name:@"file" fileName:@"123.png" mimeType:@"image/png"];
        
    } progress:^(NSProgress * _Nonnull uploadProgress) {
        
        //打印下上传进度
        RKLog(@"%lf",1.0 *uploadProgress.completedUnitCount / uploadProgress.totalUnitCount);
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        //请求成功
        RKLog(@"请求成功：%@",responseObject);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        //请求失败
        RKLog(@"请求失败：%@",error);
    }];
    
}



//上传2
//第二种是通过URL来获取路径，进入沙盒或者系统相册等等
- (void)upLoda2{
    //1.创建管理者对象
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    //2.上传文件
    NSDictionary *dict = @{@"username":@"1234"};
    
    NSString *urlString = @"22222";
    [manager POST:urlString parameters:dict constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
        
        [formData appendPartWithFileURL:[NSURL fileURLWithPath:@"文件地址"] name:@"file" fileName:@"1234.png" mimeType:@"application/octet-stream" error:nil];
    } progress:^(NSProgress * _Nonnull uploadProgress) {
        
        //打印下上传进度
        RKLog(@"%lf",1.0 *uploadProgress.completedUnitCount / uploadProgress.totalUnitCount);
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        //请求成功
        RKLog(@"请求成功：%@",responseObject);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        //请求失败
        RKLog(@"请求失败：%@",error);
    }];
}



#pragma mark - 批量上传图片
-(void)UpLoadImage:(NSString *)url parm:(NSMutableDictionary *)parm images:(NSMutableArray *)imageArr success:(void(^)(NSDictionary *dictData))success   failure:(void (^)(NSError *error))failue
{
    
    if (!_session)
    {
        _session = [AFHTTPSessionManager manager];
    }
    
    //AFHTTPSessionManager *session = [AFHTTPSessionManager manager];
    /*
     *    出现如下请求error:Request failed: unacceptable content-type: text/plain
     *    使用下面一行代码解决
     */
    _session.responseSerializer = [AFJSONResponseSerializer serializer];
    
    _session.requestSerializer = [AFJSONRequestSerializer serializer];
    
    _session.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"text/plain",@"text/json",@"application/json",@"text/javascript",@"text/html", @"application/javascript", @"text/js", nil];
    
    [_session POST:url parameters:parm constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
        
        for(NSInteger i = 0; i < imageArr.count; i++)
        {
            NSData * imageData = [imageArr objectAtIndex: i];
            // 上传的参数名
            NSString * Name = [NSString stringWithFormat:@"%@", @"mFile"];
            // 上传filename
            NSString * fileName = [NSString stringWithFormat:@"%@.jpg", Name];
            
            [formData appendPartWithFileData:imageData name:Name fileName:fileName mimeType:@"image/jpeg/png"];
        }
        
    } progress:^(NSProgress * _Nonnull uploadProgress) {
        
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        //        NSDictionary *json = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
        
        NSDictionary *json = (NSDictionary *)responseObject;
        
        if (success) {
            success(json);
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        
        if (error) {
            failue(error);
        }
        
    }];
    
}
- (void)INSNSOperationQueueWithDataArray:(NSArray *)array urlStringArray:(NSArray *)urlStringArray methodArray:(NSArray *)methodArray success:(void(^)(id dictData,NSInteger row))success failure:(void (^)(NSError *error,NSString *message,NSInteger row))failue AllSuccess:(void(^)(id responseObject))allSuccess{
    
    NSMutableArray* result = [NSMutableArray array];
    
    for (NSInteger i = 0; i < array.count; i++){
        [result addObject:[NSNull null]];
    }
    
    NSOperationQueue *queue = [[NSOperationQueue alloc] init];
    queue.maxConcurrentOperationCount = 3;
    
    NSBlockOperation *completionOperation = [NSBlockOperation blockOperationWithBlock:^{
        [[NSOperationQueue mainQueue] addOperationWithBlock:^{ // 回到主线程执行，方便更新 UI 等
            RKLog(@"全部上传完成!");
            if (allSuccess) {
                allSuccess(@"完成");
            }
            //            for (id response in result) {
            //                NSLog(@"%@", response);
            //            }
        }];
    }];
    
    for (NSInteger i = 0; i < array.count; i++) {
        
        NSURLSessionUploadTask* uploadTask = [self upLoadTaskWithUrl:urlStringArray[i] parameters:array[i] method:methodArray[i] completion:^(NSURLResponse *response, id responseObject, NSError *error) {
            
            NSInteger row = (int)i;
            
            if (error) {
                RKLog(@"第 %d 个上传失败: %@", (int)i + 1, error);
                if (failue) {
                    failue(error,[Utils getMessageError:error],row);
                }
            } else {
                
                NSDictionary *dictData = (NSDictionary *)responseObject;
                RKLog(@"%@",dictData);
                if ([dictData[@"code"]isEqualToNumber:@200]) {
                    RKLog(@"第 %d 个上传成功: %@,Message:%@", (int)i + 1, responseObject,dictData[@"codeDesc"]);
                    if (success) {
                        success(responseObject,row);
                    }
                }else{
                    RKLog(@"%@",dictData[@"Message"]);
                    if (failue) {
                        failue(error,dictData[@"Message"],row);
                    }
                }
                
                @synchronized (result) { // NSMutableArray 是线程不安全的，所以加个同步锁
                    result[i] = responseObject;
                }
                
            }
            
        }];
        
        INSPECTIONSessionWrapperOperation *uploadOperation = [INSPECTIONSessionWrapperOperation operationWithURLSessionTask:uploadTask];
        
        [completionOperation addDependency:uploadOperation];
        [queue addOperation:uploadOperation];
    }
    
    [queue addOperation:completionOperation];
}


- (void)INSNSOperationQueueWithDataArray:(NSArray *)array urlString:(NSString *)urlString method:(NSString *)method success:(void(^)(id dictData,NSInteger row))success failure:(void (^)(NSError *error,NSString *message,NSInteger row))failue AllSuccess:(void(^)(id responseObject))allSuccess
{
    NSMutableArray* result = [NSMutableArray array];
    
    for (NSInteger i = 0; i < array.count; i++){
         [result addObject:[NSNull null]];
    }
    
    NSOperationQueue *queue = [[NSOperationQueue alloc] init];
    queue.maxConcurrentOperationCount = 3;
    
    NSBlockOperation *completionOperation = [NSBlockOperation blockOperationWithBlock:^{
        [[NSOperationQueue mainQueue] addOperationWithBlock:^{ // 回到主线程执行，方便更新 UI 等
            RKLog(@"全部上传完成!");
            if (allSuccess) {
                allSuccess(@"完成");
            }
        }];
    }];
    
    for (NSInteger i = 0; i < array.count; i++) {
        
        NSURLSessionUploadTask* uploadTask = [self upLoadTaskWithUrl:urlString parameters:array[i] method:method completion:^(NSURLResponse *response, id responseObject, NSError *error) {
            
            NSInteger row = (int)i;
            
            if (error) {
                RKLog(@"第 %d 个上传失败: %@", (int)i + 1, error);
                if (failue) {
                    failue(error,[Utils getMessageError:error],row);
                }
            } else {
        
                NSDictionary *dictData = (NSDictionary *)responseObject;
                if ([dictData[@"Code"]isEqualToNumber:@200]) {
                    RKLog(@"第 %d 个上传成功: %@,Message:%@", (int)i + 1, responseObject,dictData[@"Message"]);
                    if (success) {
                        success(responseObject,row);
                    }
                }else{
                    RKLog(@"%@",dictData[@"Message"]);
                    if (failue) {
                        failue(error,dictData[@"Message"],row);
                    }
                }
                
                @synchronized (result) { // NSMutableArray 是线程不安全的，所以加个同步锁
                    result[i] = responseObject;
                }
                
            }
            
        }];
        
        INSPECTIONSessionWrapperOperation *uploadOperation = [INSPECTIONSessionWrapperOperation operationWithURLSessionTask:uploadTask];
        
        [completionOperation addDependency:uploadOperation];
        [queue addOperation:uploadOperation];
    }
    
    [queue addOperation:completionOperation];
}



- (NSURLSessionUploadTask *)upLoadTaskWithUrl:(NSString *)url parameters:(id)parameters method:(NSString *)method completion:(void (^)(NSURLResponse *response, id responseObject, NSError *error))completionBlock
{

    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
    
    NSString *boundaryString = @"-----------------------------7db372eb000e2";
    
    [request addValue:[NSString stringWithFormat:@"application/json;boundary=%@",boundaryString] forHTTPHeaderField:@"Content-Type"];
    
    [request setHTTPMethod:method];
    
    [request setHTTPBody:[NSJSONSerialization dataWithJSONObject:parameters options:NSJSONWritingPrettyPrinted error:nil]];
    
    
    AFURLSessionManager *manager = [[AFURLSessionManager alloc] initWithSessionConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration]];
    
    
    NSURLSessionUploadTask *uploadTask = [manager uploadTaskWithStreamedRequest:request progress:^(NSProgress * _Nonnull uploadProgress) {
    } completionHandler:completionBlock];
    
    return uploadTask;
}





/**
 上传图片
 */
- (NSURLSessionUploadTask*)uploadTaskWithImage:(UIImage*)image url:(NSString *)url completion:(void (^)(NSURLResponse *response, id responseObject, NSError *error))completionBlock {
    // 构造 NSURLRequest
    NSError* error = NULL;
    NSMutableURLRequest *request = [[AFHTTPRequestSerializer serializer] multipartFormRequestWithMethod:@"POST" URLString:url parameters:nil constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
        NSData* imageData = UIImageJPEGRepresentation(image, 1.0);
        [formData appendPartWithFileData:imageData name:@"file" fileName:@"someFileName" mimeType:@"multipart/form-data"];
    } error:&error];
    
    AFURLSessionManager *manager = [[AFURLSessionManager alloc] initWithSessionConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration]];
    NSURLSessionUploadTask *uploadTask = [manager uploadTaskWithStreamedRequest:request progress:^(NSProgress * _Nonnull uploadProgress) {
    } completionHandler:completionBlock];
    
    return uploadTask;
}


/**
 批量上传图片
 */
- (void)batchUploadImageWithImageArray:(NSArray *)imageArray upLoadUrl:(NSString *)uploadurl success:(void(^)(id responseDic))success allSuccess:(void(^)(id responseDic))allsuccess failure:(void (^)(NSError *error))failue
{
    
    // 准备保存结果的数组，元素个数与上传的图片个数相同，先用 NSNull 占位
    NSMutableArray* result = [NSMutableArray array];
    for (NSInteger i = 0; i < imageArray.count; i++){
        [result addObject:[NSNull null]];
    }
    
    dispatch_group_t group = dispatch_group_create();
    
    for (NSInteger i = 0; i < imageArray.count; i++) {
        
        dispatch_group_enter(group);
        
        NSURLSessionUploadTask* uploadTask = [self uploadTaskWithImage:imageArray[i] url:uploadurl  completion:^(NSURLResponse *response, NSDictionary* responseObject, NSError *error) {
            if (error) {
                NSLog(@"第 %d 张图片上传失败: %@", (int)i + 1, error);
                
                if (failue) {
                    failue(error);
                }
                
                dispatch_group_leave(group);
            } else {
                NSLog(@"第 %d 张图片上传成功: %@", (int)i + 1, responseObject);
                if (success) {
                    success(responseObject);
                }
                
                @synchronized (result) { // NSMutableArray 是线程不安全的，所以加个同步锁
                    result[i] = responseObject;
                }
                dispatch_group_leave(group);
            }
        }];
        [uploadTask resume];
    }
    
    dispatch_group_notify(group, dispatch_get_main_queue(), ^{
        NSLog(@"上传完成!");
        
        if (allsuccess) {
            allsuccess(result);
        }
        
        for (id response in result) {
            NSLog(@"%@", response);
        }
    });
}



+(BOOL) checkNetWork
{
    if (isFirst == NO) {
        //网络只有在startMonitoring完成后才可以使用检查网络状态
        [[AFNetworkReachabilityManager sharedManager] startMonitoring];
        [[AFNetworkReachabilityManager sharedManager]setReachabilityStatusChangeBlock:^(AFNetworkReachabilityStatus status) {
            canCHeckNetwork = YES;
        }];
        isFirst = YES;
    }
    
    return canCHeckNetwork;
    
}




@end
