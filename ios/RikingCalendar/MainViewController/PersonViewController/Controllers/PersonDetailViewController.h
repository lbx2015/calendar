//
//  PersonDetailViewController.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/2.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseTabViewController.h"
#import "UserModel.h"
@interface PersonDetailViewController : RKBaseTabViewController

@property (nonatomic,assign) int type;//1:个人信息,2:个人更多信息

@property (nonatomic,copy)void(^userImage)(UIImage *userImage);

@property (nonatomic,strong)UserModel *moreUserModel;

@property (nonatomic,copy) void(^editMoreUserMessage)(UserModel *uModel);
@end
