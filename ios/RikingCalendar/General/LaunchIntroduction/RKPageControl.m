//
//  RKPageControl.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/24.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKPageControl.h"

#define dotW 30
#define magrin 5
@implementation RKPageControl



- (void) setCurrentPage:(NSInteger)page {
    
    [super setCurrentPage:page];
//    //计算圆点间距
//    CGFloat marginX = dotW + magrin;
//    CGFloat marginX01 = 17 + magrin;
//    //计算整个pageControll的宽度
//    CGFloat newW = (self.subviews.count-1) * marginX01 + marginX;
//    //设置新frame
//    self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, newW, self.frame.size.height);
//    self.backgroundColor = [UIColor clearColor];
//    //设置居中
//    CGPoint center = self.center;
//    center.x = self.superview.center.x;
//    self.center = center;
//    //遍历subview,设置圆点frame
//    for (int i=0; i<[self.subviews count]; i++) {
//        UIImageView* dot = [self.subviews objectAtIndex:i];
//        if (i == self.currentPage) {
//            [dot setFrame:CGRectMake(i * marginX01, dot.frame.origin.y, 30, 6.5)];
//        }else
//        {
//            if (i > self.currentPage) {
//                [dot setFrame:CGRectMake((i-1) * marginX01 + marginX, dot.frame.origin.y, 17, 6.5)];
//            }else{
//                [dot setFrame:CGRectMake(i * marginX01, dot.frame.origin.y, 17, 6.5)];
//            }
//            
//        }
//    }

    
    
    
    
    
    
    for (NSUInteger subviewIndex = 0; subviewIndex < [self.subviews count]; subviewIndex++) {
        

        if (subviewIndex == page)
        {
            UIImageView* subview = [self.subviews objectAtIndex:subviewIndex];
            
            CGSize size;
            
            size.height = 12;
            
            size.width = 60;
            
            [subview setFrame:CGRectMake(subview.frame.origin.x, subview.frame.origin.y,
                                         
                                         size.width,size.height)];
            
//            RKLog(@"*******%f",subview.frame.origin.x);
        }else{
            
            UIImageView* subview = [self.subviews objectAtIndex:subviewIndex];
            
            CGSize size;
            
            size.height = 12;
            
            size.width = 34;
            
            [subview setFrame:CGRectMake(subview.frame.origin.x, subview.frame.origin.y,
                                         
                                         size.width,size.height)];
            
//            RKLog(@"@@@@@@%f",subview.frame.origin.x);
        }
    }
//
//    
    self.transform=CGAffineTransformScale(CGAffineTransformIdentity, 0.5, 0.5);
}

@end
