//
//  UIColor+Hex.h
//  ShuangKeMall
//
//  Created by HelloJingQiu on 15/9/24.
//  Copyright © 2015年 JokerV. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIColor (Hex)

+(UIColor *)hex_colorWithHex:(NSString *)hexColor;
+(UIColor *)hex_colorWithHex:(NSString *)hexColor alpha:(float)opacity;

@end
