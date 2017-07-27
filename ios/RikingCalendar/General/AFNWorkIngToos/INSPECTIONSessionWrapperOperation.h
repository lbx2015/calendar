//
//  INSPECTIONSessionWrapperOperation.h
//  InspectionSystem
//
//  Created by Aaron on 2017/6/20.
//  Copyright © 2017年 hope. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface INSPECTIONSessionWrapperOperation : NSOperation

+ (instancetype)operationWithURLSessionTask:(NSURLSessionTask*)task;

@end
