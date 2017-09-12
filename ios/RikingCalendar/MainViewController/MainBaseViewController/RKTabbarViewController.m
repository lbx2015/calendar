//
//  RKTabbarViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/13.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKTabbarViewController.h"
#import "HomeViewController.h"
#import "ThemeManager.h"
#import "UIView+Theme.h"
#import "UITabBarItem+Theme.h"
#import "myNavigationController.h"
@interface RKTabbarViewController ()

<UITabBarControllerDelegate>
@end

@implementation RKTabbarViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setupAllChildViewControllers];
}


#pragma mark - custom method
// 初始化所有的子控制器
- (void)setupAllChildViewControllers {
    
    
    NSArray *vcArray = @[@"HomeViewController",@"HolidaysViewController",@"RemindViewController",@"PersonViewController"];
    
    NSArray *titleArray = @[NSLocalizedString(@"tabbar01", nil),NSLocalizedString(@"tabbar02", nil),NSLocalizedString(@"tabbar03", nil),NSLocalizedString(@"tabbar04", nil)];
    
    self.tabBar.themeMap = @{kThemeTabBarTintColor : app_main_color};
    
    self.delegate = self;
    for (int i = 0; i<vcArray.count; i++) {
        
        UIViewController *viewController =  [[NSClassFromString(vcArray[i]) alloc]init];  // 把字符串 转换成 class
        
        [self addChildViewController:viewController  title:titleArray[i] imageName:[NSString stringWithFormat:@"tabbar_normalImage_%d",i] selectedImageName:[NSString stringWithFormat:@"tabbar_selectImage_%d",i]];
    }
    
}

/**
 *  初始化一个子控制器
 *
 *  @param childVc           需要初始化的子控制器
 *  @param title             标题
 *  @param imageName         图标
 *  @param selectedImageName 选中的图标
 */
- (void)addChildViewController:(UIViewController *)childVc title:(NSString *)title imageName:(NSString *)imageName selectedImageName:(NSString *)selectedImageName
{
    
//    childVc.tabBarItem.themeMap = @{};
    
    // 1.设置控制器的属性
    childVc.title = title;
    // 设置title字体的大小
    [childVc.tabBarItem setTitleTextAttributes:@{NSFontAttributeName:Tabbar_Title_Font} forState:UIControlStateNormal];
    
    // 设置图标
    childVc.tabBarItem.TabbarThemeMap = @{kThemeTabbarNormalImage:imageName,
                                    kThemeTabbarSelectImage:selectedImageName
                                    };
    myNavigationController *nav = [[myNavigationController alloc] initWithRootViewController:childVc];
    if ([nav respondsToSelector:@selector(interactivePopGestureRecognizer)]) {
        nav.interactivePopGestureRecognizer.delegate = nil;
    }
    nav.navigationBar.themeMap = @{kThemeMapKeyImageName : navigationBar_TintImage,kThemeNavigationTitleTextColor : navigationTitleTextColor};
    [self addChildViewController:nav];
}


-(BOOL)tabBarController:(UITabBarController *)tabBarController shouldSelectViewController:(UIViewController *)viewController
{
    return YES;
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
