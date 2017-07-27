//
//  UIImage+ColorFix.m
//  ShuangKeMall
//
//  Created by HelloJingQiu on 15/9/24.
//  Copyright © 2015年 JokerV. All rights reserved.
//

#import "UIImage+ColorFix.h"

@implementation UIImage (ColorFix)

+(UIImage *)cf_imageWithColor:(UIColor *)color{
    CGRect rect=CGRectMake(0.0f, 0.0f, 1.0f, 1.0f);
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    UIImage*theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return theImage;
}

@end
