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
#import "JPUSHService.h"
#import <AdSupport/AdSupport.h>
#ifdef NSFoundationVersionNumber_iOS_9_x_Max
#import <UserNotifications/UserNotifications.h>
#endif
@interface AppDelegate ()

<JPUSHRegisterDelegate>
@property (strong, nonatomic) NSNumber *originalNetworkReachability;//原始的网络状态
@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    
    [self initializesBaseConfiguration];//初始化基础配置
    
    [self registerPushlaunchOptions:launchOptions];//初始化推送
    
    [self goToMainViewController];//进入主界面
    return YES;
}


- (void)registerPushlaunchOptions:(NSDictionary *)launchOptions{
    
    
    
    //注册本地通知
//    UIUserNotificationType types = UIUserNotificationTypeBadge | UIUserNotificationTypeSound | UIUserNotificationTypeAlert;
//    
//    UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:types categories:nil];
//    
//    [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
    
    
    
    //注册远程通知
//    NSString *advertisingId = [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
    // 3.0.0及以后版本注册可以这样写，也可以继续用旧的注册方式
    JPUSHRegisterEntity * entity = [[JPUSHRegisterEntity alloc] init];
    entity.types = JPAuthorizationOptionAlert|JPAuthorizationOptionBadge|JPAuthorizationOptionSound;
    if ([[UIDevice currentDevice].systemVersion floatValue] >= 8.0) {
        //可以添加自定义categories
        //    if ([[UIDevice currentDevice].systemVersion floatValue] >= 10.0) {
        //      NSSet<UNNotificationCategory *> *categories;
        //      entity.categories = categories;
        //    }
        //    else {
        //      NSSet<UIUserNotificationCategory *> *categories;
        //      entity.categories = categories;
        //    }
    }
    [JPUSHService registerForRemoteNotificationConfig:entity delegate:self];
    
    //如不需要使用IDFA，advertisingIdentifier 可为nil
    [JPUSHService setupWithOption:launchOptions appKey:appKey
                          channel:channel
                 apsForProduction:isProduction
            advertisingIdentifier:nil];
    
    //2.1.9版本新增获取registration id block接口。
    [JPUSHService registrationIDCompletionHandler:^(int resCode, NSString *registrationID) {
        if(resCode == 0){
            NSLog(@"registrationID获取成功：%@",registrationID);
            
            NSUserDefaults *defaults = kNSUserDefaults;
            [defaults setObject:registrationID forKey:@"deviceCode"];
            [defaults synchronize];
            
        }
        else{
            NSLog(@"registrationID获取失败，code：%d",resCode);
        }
    }];
    
    
    
}


- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken{
    
    [JPUSHService registerDeviceToken:deviceToken];
    
    RKLog(@"registrationId:%@",[JPUSHService registrationID]);
    
    if ([JPUSHService registrationID])
    {
        NSUserDefaults *defaults = kNSUserDefaults;
        [defaults setObject:[JPUSHService registrationID] forKey:@"DeviceCodeJPush"];
        [defaults synchronize];
    }
}


- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo{
    
    [JPUSHService handleRemoteNotification:userInfo];
}


#ifdef NSFoundationVersionNumber_iOS_9_x_Max
#pragma mark- JPUSHRegisterDelegate
- (void)jpushNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(NSInteger))completionHandler {
    NSDictionary * userInfo = notification.request.content.userInfo;
    
    UNNotificationRequest *request = notification.request; // 收到推送的请求
    UNNotificationContent *content = request.content; // 收到推送的消息内容
    
    NSNumber *badge = content.badge;  // 推送消息的角标
    NSString *body = content.body;    // 推送消息体
    UNNotificationSound *sound = content.sound;  // 推送消息的声音
    NSString *subtitle = content.subtitle;  // 推送消息的副标题
    NSString *title = content.title;  // 推送消息的标题
    
    if([notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
        [JPUSHService handleRemoteNotification:userInfo];
        
    }
    else {
        // 判断为本地通知
        NSLog(@"iOS10 前台收到本地通知:{\nbody:%@，\ntitle:%@,\nsubtitle:%@,\nbadge：%@，\nsound：%@，\nuserInfo：%@\n}",body,title,subtitle,badge,sound,userInfo);
    }
    completionHandler(UNNotificationPresentationOptionBadge|UNNotificationPresentationOptionSound|UNNotificationPresentationOptionAlert); // 需要执行这个方法，选择是否提醒用户，有Badge、Sound、Alert三种类型可以设置
}

- (void)jpushNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)())completionHandler {
    
    NSDictionary * userInfo = response.notification.request.content.userInfo;
    UNNotificationRequest *request = response.notification.request; // 收到推送的请求
    UNNotificationContent *content = request.content; // 收到推送的消息内容
    
    NSNumber *badge = content.badge;  // 推送消息的角标
    NSString *body = content.body;    // 推送消息体
    UNNotificationSound *sound = content.sound;  // 推送消息的声音
    NSString *subtitle = content.subtitle;  // 推送消息的副标题
    NSString *title = content.title;  // 推送消息的标题
    
    if([response.notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
        [JPUSHService handleRemoteNotification:userInfo];
        NSLog(@"iOS10 收到远程通知:%@", [self logDic:userInfo]);
    }
    else {
        // 判断为本地通知
        NSLog(@"iOS10 收到本地通知:{\nbody:%@，\ntitle:%@,\nsubtitle:%@,\nbadge：%@，\nsound：%@，\nuserInfo：%@\n}",body,title,subtitle,badge,sound,userInfo);
    }
    
    completionHandler();  // 系统要求执行这个方法
}
#endif



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
//    [[UINavigationBar appearance] setBarTintColor:[UIColor hex_colorWithHex:@"#1B82D2"]];
    [[UIApplication sharedApplication]setStatusBarStyle:UIStatusBarStyleLightContent];
    
    
    
    
    
//    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    
    RKTabbarViewController *mainController = [[RKTabbarViewController alloc]init];
    
    self.window.rootViewController = mainController;
    
//    [self.window makeKeyAndVisible];
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


- (NSString *)logDic:(NSDictionary *)dic {
    if (![dic count]) {
        return nil;
    }
    NSString *tempStr1 =
    [[dic description] stringByReplacingOccurrencesOfString:@"\\u"
                                                 withString:@"\\U"];
    NSString *tempStr2 =
    [tempStr1 stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""];
    NSString *tempStr3 =
    [[@"\"" stringByAppendingString:tempStr2] stringByAppendingString:@"\""];
    NSData *tempData = [tempStr3 dataUsingEncoding:NSUTF8StringEncoding];
    NSString *str =
    [NSPropertyListSerialization propertyListFromData:tempData
                                     mutabilityOption:NSPropertyListImmutable
                                               format:NULL
                                     errorDescription:NULL];
    return str;
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
