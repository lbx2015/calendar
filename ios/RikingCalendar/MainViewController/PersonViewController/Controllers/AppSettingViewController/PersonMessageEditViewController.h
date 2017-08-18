//
//  PersonMessageEditViewController.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/15.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseViewController.h"

@interface PersonMessageEditViewController : RKBaseViewController
@property (nonatomic,assign)int editTpye;
@property (nonatomic,copy) NSString *editMessage;
@property (nonatomic,copy)void(^editUserMessage)(int type,NSString *message);
@end
