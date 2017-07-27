//
//  MYNavigationControllerShouldPopProtocol.h
//  KakaPersonal
//
//  Created by Aaron on 2017/3/16.
//  Copyright © 2017年 kakayun. All rights reserved.
//

#import <Foundation/Foundation.h>

@class myNavigationController;

@protocol MYNavigationControllerShouldPopProtocol <NSObject>


-(BOOL)my_navigationControllerShouldPopWhenSystemBackBtnSelected:(myNavigationController *)navigationController;

-(BOOL)my_navigationControllerShouldPopWhenGestureRecognizer:(myNavigationController *)navigationController;

@end
