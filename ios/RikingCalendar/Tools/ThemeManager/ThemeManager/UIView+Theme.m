//
//  UIView+Theme.m
//  WonderPlayerDemo
//
//  Created by Yanjun Zhuang on 14/6/15.
//  Copyright (c) 2015 Tencent. All rights reserved.
//

#import "UIView+Theme.h"
#import <objc/runtime.h>
#import "ThemeManager.h"
#import "NSObject+DeallocBlock.h"


//navigationBarColor
NSString *kThemeNavigationBartintColor      = @"kThemeNavigationBartintColor";
NSString *kThemeNavigationTitleTextColor    = @"kThemeNavigationTitleTextColor";
//TabBarColor
NSString *kThemeTabBarBarTintColor          = @"kThemeTabBarBarTintColor";
NSString *kThemeTabBarTintColor             = @"kThemeTabBarTintColor";

//设置阴影
NSString *kThemeViewShadowColor             = @"kThemeViewShadowColor";


NSString *kThemeColor01                     = @"kThemeColor01";
NSString *kThemeColor02                     = @"kThemeColor02";
NSString *kThemeColor03                     = @"kThemeColor03";
NSString *kThemeColor04                     = @"kThemeColor04";
NSString *kThemeColor05                     = @"kThemeColor05";



NSString *kThemeMapKeyImageName             = @"kThemeMapKeyImageName";
NSString *kThemeMapKeyHighlightedImageName  = @"kThemeMapKeyHighlightedImageName";
NSString *kThemeMapKeySelectedImageName     = @"kThemeMapKeySelectedImageName";
NSString *kThemeMapKeyDisabledImageName     = @"kThemeMapKeyDisabledImageName";

NSString *kThemeMapKeyColorName             = @"kThemeMapKeyColorName";
NSString *kThemeMapKeyHighlightedColorName  = @"kThemeMapKeyHighlightedColorName";
NSString *kThemeMapKeySelectedColorName     = @"kThemeMapKeySelectedColorName";
NSString *kThemeMapKeyDisabledColorName     = @"kThemeMapKeyDisabledColorName";

NSString *kThemeMapKeyBgColorName           = @"kThemeMapKeyBgColorName";





static void *kUIView_ThemeMap;
static void *kUIView_DeallocHelper;

@implementation UIView (Theme)
- (void)setThemeMap:(NSDictionary *)themeMap
{
    objc_setAssociatedObject(self, &kUIView_ThemeMap, themeMap, OBJC_ASSOCIATION_COPY_NONATOMIC);
    
    
    if (themeMap) {
        @autoreleasepool {
            // Need to removeObserver in dealloc
            if (objc_getAssociatedObject(self, &kUIView_DeallocHelper) == nil) {
                __unsafe_unretained typeof(self) weakSelf = self; // NOTE: need to be __unsafe_unretained because __weak var will be reset to nil in dealloc
                id deallocHelper = [self addDeallocBlock:^{
//                    NSLog(@"deallocing %@", weakSelf);
                    [[NSNotificationCenter defaultCenter] removeObserver:weakSelf];
                }];
                objc_setAssociatedObject(self, &kUIView_DeallocHelper, deallocHelper, OBJC_ASSOCIATION_ASSIGN);
            }
            
            [[NSNotificationCenter defaultCenter] removeObserver:self name:kThemeDidChangeNotification object:nil];
            [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(themeChanged) name:kThemeDidChangeNotification object:nil];
            [self themeChanged];
        }
    }
    else {
        [[NSNotificationCenter defaultCenter] removeObserver:self name:kThemeDidChangeNotification object:nil];
    }
    
}

- (NSDictionary *)themeMap
{
    return objc_getAssociatedObject(self, &kUIView_ThemeMap);
}

- (void)themeChanged
{
    // TODO: performace tuning
    if (self.hidden) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self changeTheme];
        });
    }
    else {
        [self changeTheme];
    }
}

- (void)changeTheme
{
    NSDictionary *map = self.themeMap;
    
    if ([self isKindOfClass:[UILabel class]]) {
        UILabel *obj = (UILabel *)self;
        if (map[kThemeMapKeyColorName]) {
            obj.textColor = TColor(map[kThemeMapKeyColorName]);
        }
        if (map[kThemeMapKeyHighlightedColorName]) {
            obj.highlightedTextColor = TColor(map[kThemeMapKeyHighlightedColorName]);
        }
        if (map[kThemeMapKeyBgColorName]) {
            obj.backgroundColor = TColor(map[kThemeMapKeyBgColorName]);
        }
    }
    else if ([self isKindOfClass:[UIButton class]]) {
        UIButton *obj = (UIButton *)self;
        if (map[kThemeMapKeyColorName]) {
            [obj setTitleColor:TColor(map[kThemeMapKeyColorName]) forState:UIControlStateNormal];
        }
        if (map[kThemeMapKeyHighlightedColorName]) {
            [obj setTitleColor:TColor(map[kThemeMapKeyHighlightedColorName]) forState:UIControlStateHighlighted];
        }
        if (map[kThemeMapKeySelectedColorName]) {
            [obj setTitleColor:TColor(map[kThemeMapKeySelectedColorName]) forState:UIControlStateSelected];
        }
        if (map[kThemeMapKeyDisabledColorName]) {
            [obj setTitleColor:TColor(map[kThemeMapKeyDisabledColorName]) forState:UIControlStateDisabled];
        }
        if (map[kThemeMapKeyImageName]) {
            [obj setImage:TImage(map[kThemeMapKeyImageName]) forState:UIControlStateNormal];
        }
        if (map[kThemeMapKeyHighlightedImageName]) {
            [obj setImage:TImage(map[kThemeMapKeyHighlightedImageName]) forState:UIControlStateHighlighted];
        }
        if (map[kThemeMapKeySelectedImageName]) {
            [obj setImage:TImage(map[kThemeMapKeySelectedImageName]) forState:UIControlStateSelected];
        }
        if (map[kThemeMapKeyDisabledImageName]) {
            [obj setImage:TImage(map[kThemeMapKeyDisabledImageName]) forState:UIControlStateDisabled];
        }
        if (map[kThemeMapKeyBgColorName]) {
            obj.backgroundColor = TColor(map[kThemeMapKeyBgColorName]);
        }
    }else if ([self isKindOfClass:[UITabBar class]]){
        UITabBar *obj = (UITabBar *)self;
        if (map[kThemeMapKeyColorName]) {
            obj.tintColor = TColor(map[kThemeMapKeyColorName]);
        }
        
        if (map[kThemeTabBarBarTintColor]) {
            obj.barTintColor = TColor(keyName(kThemeTabBarBarTintColor));
        }
        
    }else if ([self isKindOfClass:[UINavigationBar class]]){
        
        if (map[kThemeNavigationTitleTextColor]) {
            [[UINavigationBar appearance] setTitleTextAttributes:@{NSFontAttributeName:NavigationBar_Text_Font,NSForegroundColorAttributeName:TColor(keyName(kThemeNavigationTitleTextColor))}];
        }
        
        if (map[kThemeNavigationBartintColor]) {
            [[UINavigationBar appearance] setBarTintColor:TColor(keyName(kThemeNavigationBartintColor))];
        }
        
        if (map[kThemeMapKeyImageName]) {
            [[UINavigationBar appearance] setBackgroundImage:TImage(map[kThemeMapKeyImageName]) forBarMetrics:UIBarMetricsDefault];
        }
        
    }
    else if ([self isKindOfClass:[UIImageView class]]) {
        UIImageView *obj = (UIImageView *)self;
        
        [self getImageWithimageName:map[kThemeMapKeyImageName]];
        
        if (map[kThemeMapKeyImageName]) {
            obj.image = TImage(map[kThemeMapKeyImageName]);

        }
        if (map[kThemeMapKeyHighlightedImageName]) {
            obj.highlightedImage = TImage(map[kThemeMapKeyHighlightedImageName]);

        }
        if (map[kThemeMapKeyColorName]) {
            obj.backgroundColor = TColor(map[kThemeMapKeyColorName]);
        }
    }
    else if ([self isKindOfClass:[UITextField class]]) {
        UITextField *obj = (UITextField *)self;
        if (map[kThemeMapKeyColorName]) {
            obj.textColor = TColor(map[kThemeMapKeyColorName]);
        }
    }
    else if ([self isKindOfClass:[UITextView class]]) {
        UITextView *obj = (UITextView *)self;
        if (map[kThemeMapKeyColorName]) {
            obj.textColor = TColor(map[kThemeMapKeyColorName]);
        }
    }
    else {
        if (map[kThemeMapKeyColorName]) {
            self.backgroundColor = TColor(map[kThemeMapKeyColorName]);
        }
        
        if (map[kThemeViewShadowColor]) {
            
            self.layer.shadowColor = TColor(map[kThemeViewShadowColor]).CGColor;
        }
        
    }
}



/**
 根据路径获取图片
 */
- (UIImage *)getImageWithimageName:(NSString *)imageName{
    
    NSString *newPath = [[ThemeManager sharedInstance].skinInstance fullResourcePathForName:imageName];
    
    NSString * themeImagePath = [newPath stringByAppendingPathComponent:imageName];
    
    UIImage * themeImage = [UIImage imageNamed:themeImagePath];//[UIImage imageWithContentsOfFile:themeImagePath];
    
    return themeImage;
}

@end
