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
#import "LaunchIntroductionView.h"



#ifdef NSFoundationVersionNumber_iOS_9_x_Max
#import <UserNotifications/UserNotifications.h>
#endif
@interface AppDelegate ()

<JPUSHRegisterDelegate,UIAlertViewDelegate>

{
    BOOL _didReceiveNotification;
    UIAlertView *_alertAppVersion;
    NSDictionary *_appVersionDict;
    NSTimer *_AddRemindTimer;
    NSInteger _count;
    dispatch_source_t _timer;
}

@property (strong, nonatomic) NSNumber *originalNetworkReachability;//原始的网络状态
@property (nonatomic,assign) NSInteger badgeValueNum;//角标
@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    
    [self initializesBaseConfiguration];//初始化基础配置
    
    [self registerPushlaunchOptions:launchOptions];//初始化推送
    
    [self goToMainViewController];//进入主界面
    
    [self checkAppVersion];

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
-(void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification{
        
    if (notification) {
        
        [application scheduleLocalNotification:notification];
        
    }
    
}

#ifdef NSFoundationVersionNumber_iOS_9_x_Max
#pragma mark- JPUSHRegisterDelegate
- (void)jpushNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(NSInteger))completionHandler {
    
    //前台接受通知
    NSDictionary * userInfo = notification.request.content.userInfo;
    UNNotificationRequest *request = notification.request; // 收到推送的请求
    UNNotificationContent *content = request.content; // 收到推送的消息内容
    
    NSNumber *badge = content.badge;  // 推送消息的角标
    NSString *body = content.body;    // 推送消息体
    UNNotificationSound *sound = content.sound;  // 推送消息的声音
    NSString *subtitle = content.subtitle;  // 推送消息的副标题
    NSString *title = content.title;  // 推送消息的标题
    
    RKLog(@"%ld",(long)self.badgeValueNum);
    
    
    
    if([notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
        [JPUSHService handleRemoteNotification:userInfo];
        self.badgeValueNum -- ;
        [JPUSHService setBadge:self.badgeValueNum];
        [[UIApplication sharedApplication] setApplicationIconBadgeNumber:self.badgeValueNum];
    }
    else {
        // 判断为本地通知
        self.badgeValueNum = [UIApplication sharedApplication].applicationIconBadgeNumber;
        self.badgeValueNum ++;
        [[UIApplication sharedApplication] setApplicationIconBadgeNumber:self.badgeValueNum];
        NSLog(@"iOS10 前台收到本地通知:{\nbody:%@，\ntitle:%@,\nsubtitle:%@,\nbadge：%@，\nsound：%@，\nuserInfo：%@\n}",body,title,subtitle,badge,sound,userInfo);
        
    }
//    completionHandler(UNNotificationPresentationOptionBadge|UNNotificationPresentationOptionSound|UNNotificationPresentationOptionAlert); // 需要执行这个方法，选择是否提醒用户，有Badge、Sound、Alert三种类型可以设置
    
    completionHandler(UNNotificationPresentationOptionSound|UNNotificationPresentationOptionAlert);
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
    
    //是否点击消息进来
    _didReceiveNotification = YES;
    self.badgeValueNum = [UIApplication sharedApplication].applicationIconBadgeNumber;
    self.badgeValueNum --;
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:self.badgeValueNum];
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
    
    //监测版本信息
    
}

#pragma mark - 进入主界面
- (void)goToMainViewController{

//    [[UINavigationBar appearance] setBackgroundImage:[UIImage imageNamed:@"navgationBar_bgImage"] forBarMetrics:UIBarMetricsDefault];
//    [[UINavigationBar appearance] setTitleTextAttributes:@{NSFontAttributeName:NavigationBar_Text_Font,NSForegroundColorAttributeName:[UIColor whiteColor]}];
//    [[UINavigationBar appearance] setBarTintColor:[UIColor hex_colorWithHex:@"#1B82D2"]];
    [[UIApplication sharedApplication]setStatusBarStyle:UIStatusBarStyleLightContent];
    
    
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    
    RKTabbarViewController *mainController = [[RKTabbarViewController alloc]init];
    
    self.window.rootViewController = mainController;
    
    [self.window makeKeyAndVisible];
    
    [LaunchIntroductionView sharedWithImages:@[@"guide01.jpg",@"guide02.jpg",@"guide03.jpg",@"guide04.jpg"]];
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

#pragma mark - 监测app版本信息
- (void)checkAppVersion{
    
    //{"versionNumber":"string"}
    NSDictionary *param = @{@"versionNumber":[Utils getAppVersion],@"type":@"1"};
    
    [[AFNWorkingTool sharedManager] AFNHttpRequestPOSTurlstring:[NSString stringWithFormat:@"%@",requestUrl(getappVersion)] parm:param success:^(NSDictionary *dictData) {
        
        
        if ([dictData[@"code"]isEqualToNumber:@200]) {
            
            NSDictionary *appVersionDict = [NSDictionary dictionaryWithDictionary:dictData[@"_data"]];
            _appVersionDict = appVersionDict;
        
            RKLog(@"%@",appVersionDict[@"type"]);
            if ([appVersionDict[@"type"] isEqualToString:@"2"])//强制更新
            {
                RKLog(@"强制更新%@",appVersionDict[@"msg"]);
                if (!_alertAppVersion)
                {
                    _alertAppVersion = [[UIAlertView alloc] initWithTitle:nil message:appVersionDict[@"msg"] delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
                }
                _alertAppVersion.tag = 2;
                
                [_alertAppVersion show];
            }
            else if ([appVersionDict[@"type"] isEqualToString:@"1"])
            {
                RKLog(@"更新%@",appVersionDict[@"msg"]);
                if ([self isHaveHistoryVersion:appVersionDict[@"versionNumber"]]) {
                    
                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil message:appVersionDict[@"msg"] delegate:self cancelButtonTitle:@"忽略" otherButtonTitles:@"确定", nil];
                    alert.tag = 1;
                    [alert show];
                    
                }
                
            }
            
        }
        
        
    } failure:^(NSError *error) {
        
    }];
    
}


- (BOOL)isHaveHistoryVersion:(NSString *)netWorkVersion
{
    NSDictionary *histVer = [kNSUserDefaults objectForKey:kAppVersionMessage];
    
    if (histVer)
    {
        NSString *version = [NSString stringWithFormat:@"%@",histVer[@"versionNumber"]];
        
        BOOL ret = [version isEqualToString:netWorkVersion];
        
        if (ret)
        {
            return NO;
        }
        else
        {
            return YES;
        }
    }
    
    return YES;
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    if (alertView.tag==1) {
        
        if (buttonIndex == 1)
        {
            [kNSUserDefaults removeObjectForKey:kAppVersionMessage];
            [kNSUserDefaults synchronize];
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"https://itunes.apple.com/us/app/id1279115732?l=zh&ls=1&mt=8"]];
            
        }
        else
        {
            
            [kNSUserDefaults setObject:_appVersionDict forKey:kAppVersionMessage];
            [kNSUserDefaults synchronize];
        }
        
        
    }else if (alertView.tag==2){
        
        [kNSUserDefaults removeObjectForKey:kAppVersionMessage];
        [kNSUserDefaults synchronize];
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"https://itunes.apple.com/us/app/id1279115732?l=zh&ls=1&mt=8"]];
        
        _alertAppVersion = nil;
        
        if ([_appVersionDict[@"type"] isEqualToString:@"2"])
        {
            if (!_alertAppVersion)
            {
                _alertAppVersion = [[UIAlertView alloc] initWithTitle:nil message:_appVersionDict[@"msg"] delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            }
            
            _alertAppVersion.tag = 2;
            
            [_alertAppVersion show];
        }
        
    }
    
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
    NSString *str = [NSPropertyListSerialization propertyListWithData:tempData
                                                              options:NSPropertyListImmutable
                                                               format:NULL
                                                                error:NULL];
    
    return str;
}

- (void)applicationWillResignActive:(UIApplication *)application {
    
    RKLog(@"即将进入后台");
    
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    
    RKLog(@"进入后台");
    
//    _AddRemindTimer = [NSTimer scheduledTimerWithTimeInterval:1.0f target:self selector:@selector(addRemind) userInfo:nil repeats:YES];
//    NSDate *date = [NSDate date];
//    NSLog(@"date:%@",date);
//    NSDate *fireDate = [NSDate dateWithTimeInterval:10.0f sinceDate:date];
//    [timer setFireDate:fireDate];
    
    
//    NSTimeInterval period = 1.0; //设置时间间隔
//    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
//    _timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
//    dispatch_source_set_timer(_timer, dispatch_walltime(NULL, 0), period * NSEC_PER_SEC, 0); //每秒执行
//    // 事件回调
//    dispatch_source_set_event_handler(_timer, ^{
//        
//        NSLog(@"Count");
//        
////        dispatch_async(dispatch_get_main_queue(), ^{
////            
////        });
//    });
//    
//    // 开启定时器
//    dispatch_resume(_timer);
    
    
//    UIApplication *app = [UIApplication sharedApplication];
//    __block  UIBackgroundTaskIdentifier bgTask;
//    bgTask = [app beginBackgroundTaskWithExpirationHandler:^{
//        
//        
//        dispatch_async(dispatch_get_main_queue(), ^{
//            
//            if (bgTask != UIBackgroundTaskInvalid) {
//                bgTask = UIBackgroundTaskInvalid;
//            }
//            
//        });
//        
//    }];
//    
//    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
//        
//        if (bgTask != UIBackgroundTaskInvalid) {
//            bgTask = UIBackgroundTaskInvalid;
//        }
//        
//    });
    
    
//    [[NSNotificationCenter defaultCenter] postNotificationName:@"gotoBackgrount" object:nil];
    
//    [_AddRemindTimer setFireDate:[NSDate distantPast]];
//    _count =0;
    
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

-(void)startListen:(NSTimer *)timer
{
    
    RKLog(@"--------后台开始运行%ld",(long)_count);
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    _didReceiveNotification = NO;
    
    RKLog(@"即将进入前台");
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    RKLog(@"进入前台");
    
    if (!_didReceiveNotification) {
        //清除角标
        [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
    }
    
    if (![kNSUserDefaults objectForKey:@"resetNotificationDate"] || ![[kNSUserDefaults objectForKey:@"resetNotificationDate"] isEqualToString:[Utils getCurrentTimeWithDay]]) {
        
        //重置本地通知
        dispatch_async(dispatch_get_global_queue(0, 0), ^{
            
            NSArray * notificationArray01 = [[LocalNotificationManager shareManager] queryAllSystemNotifications];
            RKLog(@"本地推送数量:%ld",notificationArray01.count);
            
            [[RemindAndGtasksDBManager shareManager] selectnearRemindAndGtasks];
            
            NSArray * notificationArray = [[LocalNotificationManager shareManager] queryAllSystemNotifications];
            RKLog(@"本地推送数量:%ld",notificationArray.count);
            
            
            dispatch_async(dispatch_get_main_queue(), ^{
                // 通知主线程刷新 神马的
                //更新时间
                [kNSUserDefaults setObject:[Utils getCurrentTimeWithDay] forKey:@"resetNotificationDate"];
                [kNSUserDefaults synchronize];
                
            });
            
//            for (UILocalNotification * notification in notificationArray) {
//                [[LocalNotificationManager shareManager] compareFiretime:notification needRemove:^(UILocalNotification *item) {
//                    
//                    if (item.userInfo[@"repeatFlag"]==0) {
//                        [[UIApplication sharedApplication] cancelLocalNotification:item];
//                    }
//                    
//                }];
//            }
            
            
            
        });
        
    }

}


- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}


@end
