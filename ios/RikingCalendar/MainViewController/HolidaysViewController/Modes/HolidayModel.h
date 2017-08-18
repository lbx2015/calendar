//
//  HolidayModel.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/14.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseModel.h"

@interface HolidayModel : RKBaseModel

@property (nonatomic,copy) NSString *crcy;
@property (nonatomic,copy) NSString *ctryName;
@property (nonatomic,copy) NSString *ctryNameValue;
@property (nonatomic,copy) NSString *hdayDate;
@property (nonatomic,copy) NSString *hdayName;
@property (nonatomic,copy) NSString *hdayNameValue;
@property (nonatomic,copy) NSString *iconUrl;
@property (nonatomic,copy) NSString *id;


@end
