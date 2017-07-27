//
//  UITabBarItem+Theme.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/17.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "UITabBarItem+Theme.h"
#import <objc/runtime.h>
#import "NSObject+DeallocBlock.h"
//tabbarImage
NSString *kThemeTabbarNormalImage           = @"kThemeTabbarNormalImage";
NSString *kThemeTabbarSelectImage           = @"kThemeTabbarSelectImage";

static void *kUITabBarItem_ThemeMap;
static void *kUITabBarItem_DeallocHelper;
@implementation UITabBarItem (Theme)

-(void)setTabbarThemeMap:(NSDictionary *)TabbarThemeMap
{
    objc_setAssociatedObject(self, &kUITabBarItem_ThemeMap, TabbarThemeMap, OBJC_ASSOCIATION_COPY_NONATOMIC);
    
    if (TabbarThemeMap) {
        @autoreleasepool {
            if (objc_getAssociatedObject(self, &kUITabBarItem_DeallocHelper) == nil) {
                __unsafe_unretained typeof(self) weakSelf = self; // NOTE: need to be __unsafe_unretained because __weak var will be reset to nil in dealloc
                id deallocHelper = [self addDeallocBlock:^{
                    NSLog(@"deallocing %@", weakSelf);
                    [[NSNotificationCenter defaultCenter] removeObserver:weakSelf];
                }];
                objc_setAssociatedObject(self, &kUITabBarItem_DeallocHelper, deallocHelper, OBJC_ASSOCIATION_ASSIGN);
            }
            
            [[NSNotificationCenter defaultCenter] removeObserver:self name:kThemeDidTabbarChangeNotification object:nil];
            [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(tabbarThemeChanged) name:kThemeDidTabbarChangeNotification object:nil];
            [self tabbarThemeChanged];
        }
    }
    else {
        [[NSNotificationCenter defaultCenter] removeObserver:self name:kThemeDidTabbarChangeNotification object:nil];
    }
    
}

- (NSDictionary *)TabbarThemeMap
{
    return objc_getAssociatedObject(self, &kUITabBarItem_ThemeMap);
}

- (void)tabbarThemeChanged
{
    [self changeTheme];
}

- (void)changeTheme
{
    NSDictionary *map = self.TabbarThemeMap;
    if ([self isKindOfClass:[UITabBarItem class]]){
        UITabBarItem *obj = (UITabBarItem *)self;
        if (map[kThemeTabbarNormalImage]) {
            obj.image = [[self getTabbarImageWithImageName:map[kThemeTabbarNormalImage]] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        }
        
        if (map[kThemeTabbarSelectImage]) {
            obj.selectedImage = [[self getTabbarImageWithImageName:map[kThemeTabbarSelectImage]] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        }
    }
    
    
}

- (UIImage *)getTabbarImageWithImageName:(NSString *)imageName
{
    return [UIImage imageNamed:[[ThemeManager sharedInstance].skinInstance fullResourcePathForName:imageName]];
}



@end
