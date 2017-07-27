//
//  AppDelegate.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/14.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (strong, nonatomic) NSNumber *networkReachability;//切换网络后的状态值 1:蜂窝移动 2:wifi 3:未知网络 0:无网络

@end

