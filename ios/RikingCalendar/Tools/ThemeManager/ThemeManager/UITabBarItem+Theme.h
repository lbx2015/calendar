//
//  UITabBarItem+Theme.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/17.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <UIKit/UIKit.h>

extern NSString *kThemeTabbarNormalImage;
extern NSString *kThemeTabbarSelectImage;

@interface UITabBarItem (Theme)

@property (nonatomic, copy) NSDictionary *TabbarThemeMap;

- (void)tabbarThemeChanged;
@end
