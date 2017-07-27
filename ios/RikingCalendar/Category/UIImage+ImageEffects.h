//
//  UIImage+ImageEffects.h
//  HopeYunmendian
//
//  Created by Aaron on 16/8/24.
//  Copyright © 2016年 Aaron. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImage (ImageEffects)


-(UIImage *)applyLightEffect;
-(UIImage *)applyExtraLightEffect;
-(UIImage *)applyDarkEffect;
-(UIImage *)applyTintEffectWithColor:(UIColor *)tintColor;

-(UIImage *)applyBlurWithRadius:(CGFloat)blurRadius tintColor:(UIColor *)tintColor saturationDeltaFactor:(CGFloat)saturationDeltaFactor maskImage:(UIImage *)maskImage;
- (UIImage *)blurryImage:(UIImage *)image withBlurLevel:(CGFloat)blur;
@end
