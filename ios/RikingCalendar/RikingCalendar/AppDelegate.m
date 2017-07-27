//
//  AppDelegate.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/14.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "AppDelegate.h"
#import "RKTabbarViewController.h"
#import "AFNetworking.h"
@interface AppDelegate ()
@property (strong, nonatomic) NSNumber *originalNetworkReachability;//原始的网络状态
@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    
    [self initializesBaseConfiguration];
    
    [self goToMainViewController];
   
    return YES;
}

#pragma mark - 初始化基础配置
- (void)initializesBaseConfiguration{
    //默认主题
    [[ThemeManager sharedInstance] switchToStyleByID:THEME_STYLE_CLASSIC];
    
    //键盘
    [[IQKeyboardManager sharedManager] setEnable:YES];
    
    //开启网络监测
    [self checkNetwork];
    
}

#pragma mark - 进入主界面
- (void)goToMainViewController{

//    [[UINavigationBar appearance] setBackgroundImage:[UIImage imageNamed:@"navgationBar_bgImage"] forBarMetrics:UIBarMetricsDefault];
//    [[UINavigationBar appearance] setTitleTextAttributes:@{NSFontAttributeName:NavigationBar_Text_Font,NSForegroundColorAttributeName:[UIColor whiteColor]}];
////    [[UINavigationBar appearance] setBarTintColor:[UIColor hex_colorWithHex:@"#1B82D2"]];
    [[UIApplication sharedApplication]setStatusBarStyle:UIStatusBarStyleLightContent];
    
    
    RKTabbarViewController *mainController = [[RKTabbarViewController alloc]init];
    self.window.rootViewController = mainController;
}


#pragma mark - 监测网络状态
-(void)checkNetwork
{
    
    AFNetworkReachabilityManager *manager = [AFNetworkReachabilityManager sharedManager];
    
    // 连接状态回调处理
    /* AFNetworking的Block内使用self须改为weakSelf, 避免循环强引用, 无法释放 */
    __weak typeof(self) weakSelf = self;
    [manager setReachabilityStatusChangeBlock:^(AFNetworkReachabilityStatus status)
     {
         switch (status)
         {
             case AFNetworkReachabilityStatusUnknown:
                 // 回调处理
                 weakSelf.networkReachability = [NSNumber numberWithInt:3];
                 
                 [weakSelf networkReachabilityMessage:@"当前网络为未知网络,请谨慎!!!"];
                 break;
             case AFNetworkReachabilityStatusNotReachable:
                 // 回调处理
                 weakSelf.networkReachability = [NSNumber numberWithInt:0];
                 
                 [weakSelf networkReachabilityMessage:@"您网络已断开,请检查你的网络"];
                 
                 break;
             case AFNetworkReachabilityStatusReachableViaWWAN:
                 // 回调处理
                 weakSelf.networkReachability = [NSNumber numberWithInt:1];
                 
                 [weakSelf networkReachabilityMessage:@"您已切换到蜂窝移动网络"];
                 
                 break;
             case AFNetworkReachabilityStatusReachableViaWiFi:
                 // 回调处理
                 weakSelf.networkReachability = [NSNumber numberWithInt:2];
                 
                 [weakSelf networkReachabilityMessage:@"您已切换到WiFi"];
                 
                 break;
             default:
                 break;
         }
     }];
    
    
    [manager startMonitoring];
}

-(void)networkReachabilityMessage:(NSString *)message
{
    if (_originalNetworkReachability)
    {
        if (![_originalNetworkReachability isEqualToNumber:_networkReachability])
        {
//            [MBManager showBriefAlert:message inView:self.window];
            
            if ([self.networkReachability integerValue]>0) {
                [[NSNotificationCenter defaultCenter] postNotificationName:@"HAVE_NETWORK" object:nil];
            }
            
        }
    }
    
    _originalNetworkReachability = _networkReachability;
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}


- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}


@end
