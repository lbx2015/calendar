//
//  MenuModel.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/9.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseModel.h"

@interface MenuModel : RKBaseModel

@property (nonatomic,copy)NSString *menuId;
@property (nonatomic,copy)NSString *clazz;
@property (nonatomic,copy)NSString *field;
@property (nonatomic,copy)NSString *ke;
@property (nonatomic,copy)NSString *valu;

@end



@interface CriteriaModel : RKBaseModel
@property (nonatomic,copy)NSString *ctryName;
@property (nonatomic,copy)NSString *hdayDate;
@property (nonatomic,copy)NSString *crcy;
@property (nonatomic,copy)NSString *hdayName;
@end
