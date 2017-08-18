//
//  UISearchBar+BG.m
//  McsPro
//
//  Created by hope on 14-9-9.
//  Copyright (c) 2014年 Lynn. All rights reserved.
//

#import "UISearchBar+Background.h"

@implementation UISearchBar (Background)

- (void)setBackgroundExten:(UIColor *)color{
    float version = [[[ UIDevice currentDevice ] systemVersion ] floatValue ];
    if ([ self respondsToSelector : @selector (barTintColor)]) {
        float  iosversion7_1 = 7.1 ;
        if (version >= iosversion7_1) {
            //iOS7.1
            [[[[self.subviews objectAtIndex:0] subviews] objectAtIndex:0] removeFromSuperview];
            [self setBackgroundColor:color];
        } else {
            //iOS7.0
            [self setBarTintColor:color];
            [self setBackgroundColor:color];
        }
    } else{
        //iOS7.0 以下
        [[self . subviews objectAtIndex:0 ] removeFromSuperview ];
        [self setBackgroundColor:color];
    }
}

@end
