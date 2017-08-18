//
//  UIView+Theme.h
//  WonderPlayerDemo
//
//  Created by Yanjun Zhuang on 14/6/15.
//  Copyright (c) 2015 Tencent. All rights reserved.
//

#import <UIKit/UIKit.h>



//navigationBarColor
extern NSString *kThemeNavigationBartintColor;
extern NSString *kThemeNavigationTitleTextColor;

extern NSString *kThemeTabBarBarTintColor;
extern NSString *kThemeTabBarTintColor;


//阴影
extern NSString *kThemeViewShadowColor;

extern NSString *kThemeColor01;
extern NSString *kThemeColor02;
extern NSString *kThemeColor03;
extern NSString *kThemeColor04;
extern NSString *kThemeColor05;


extern NSString *kThemeMapKeyImageName;
extern NSString *kThemeMapKeyHighlightedImageName;
extern NSString *kThemeMapKeySelectedImageName;
extern NSString *kThemeMapKeyDisabledImageName;

extern NSString *kThemeMapKeyColorName;
extern NSString *kThemeMapKeyHighlightedColorName;
extern NSString *kThemeMapKeySelectedColorName;
extern NSString *kThemeMapKeyDisabledColorName;

extern NSString *kThemeMapKeyBgColorName;


@interface UIView (Theme)
@property (nonatomic, copy) NSDictionary *themeMap;

- (void)themeChanged;
@end
