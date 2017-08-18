//
//  DropMenuTableView.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/9.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MenuModel.h"


typedef enum{
    menuCountry = 0,
    menuCurrency,
    menuHoliday
    
}menuStyle;

@interface DropMenuTableView : UIView

@property (nonatomic, strong) void(^disMiss)();


- (void)refreshMenu:(NSArray *)dataArray selectMenuModel:(MenuModel *)menuModel menuStyle:(menuStyle)style CompleteBlock:(void(^)(MenuModel *model,menuStyle menuStyle))completeBlock;

-(void)show;

-(void)dismiss;
@end
