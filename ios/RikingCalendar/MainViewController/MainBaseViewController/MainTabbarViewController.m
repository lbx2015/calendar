//
//  MainTabbarViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/12.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "MainTabbarViewController.h"
#import "HomeViewController.h"
#import "RemindViewController.h"
#import "HolidaysViewController.h"
#import "PersonViewController.h"


@interface MainTabbarViewController ()

@end

@implementation MainTabbarViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self initViewControllers];
    self.tabBar.itemTitleSelectedColor = [UIColor redColor];
    self.tabBar.itemTitleColor = [UIColor lightGrayColor];
//    self.tabBar.backgroundColor = [UIColor orangeColor];
//    // 设置数字样式的badge的位置和大小
//    [self.tabBar setNumberBadgeMarginTop:2
//                       centerMarginRight:25
//                     titleHorizonalSpace:8
//                      titleVerticalSpace:2];
//    // 设置小圆点样式的badge的位置和大小
//    [self.tabBar setDotBadgeMarginTop:5
//                    centerMarginRight:30
//                           sideLength:10];
//    
    
//    UIViewController *controller1 = self.viewControllers[0];
//    UIViewController *controller2 = self.viewControllers[1];
//    UIViewController *controller3 = self.viewControllers[2];
//    UIViewController *controller4 = self.viewControllers[3];
//    controller1.yp_tabItem.badge = 8;
//    controller2.yp_tabItem.badge = 0;
//    controller3.yp_tabItem.badge = 120;
//    controller4.yp_tabItem.badgeStyle = YPTabItemBadgeStyleDot;
//    controller4.yp_tabItem.badge = 10000;
    
}


- (void)initViewControllers {
    
    NSArray *vcArray = @[@"HomeViewController",@"HolidaysViewController",@"RemindViewController",@"PersonViewController"];
    
    NSArray *titleArray = @[@"首页",@"工作",@"提醒",@"个人"];
    
    NSMutableArray *viewControllers = [NSMutableArray array];
    
    for (int i = 0; i<vcArray.count; i++) {
        
        RKBaseViewController *viewController =  [[NSClassFromString(vcArray[i]) alloc]init];  // 把字符串 转换成 class
        
//        UINavigationController *navVC = [[UINavigationController alloc]initWithRootViewController:viewController];
        viewController.yp_tabItemTitle = titleArray[i];
        viewController.yp_tabItemImage = [UIImage imageNamed:@"tab_discover_normal"];
        viewController.yp_tabItemSelectedImage = [UIImage imageNamed:@"tab_discover_selected"];
        
        [viewControllers addObject:viewController];
        
    }
    
    
    self.viewControllers = [NSArray arrayWithArray:viewControllers];
    
    
    //是否支持左右滑动
//    [self setContentScrollEnabledAndTapSwitchAnimated:YES];
    
    // 生成一个居中显示的YPTabItem对象，即“+”号按钮
    //    YPTabItem *item = [YPTabItem buttonWithType:UIButtonTypeCustom];
    //    item.title = @"+";
    //    item.titleColor = [UIColor yellowColor];
    //    item.backgroundColor = [UIColor darkGrayColor];
    //    item.titleFont = [UIFont boldSystemFontOfSize:40];
    //
    //    // 设置其size，如果不设置，则默认为与其他item一样
    //    item.size = CGSizeMake(80, 60);
    //    // 高度大于tabBar，所以需要将此属性设置为NO
    //    self.tabBar.clipsToBounds = NO;
    //
    //    [self.tabBar setSpecialItem:item
    //             afterItemWithIndex:1
    //                     tapHandler:^(YPTabItem *item) {
    //                         NSLog(@"item--->%ld", (long)item.index);
    //                     }];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
