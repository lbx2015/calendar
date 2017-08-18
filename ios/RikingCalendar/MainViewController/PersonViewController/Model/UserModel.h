//
//  UserModel.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/15.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseModel.h"

@interface UserModel : RKBaseModel
@property (nonatomic,copy) NSString *id;//id
@property (nonatomic,copy) NSString *name;//名字
@property (nonatomic,copy) NSString *telephone;//电话
@property (nonatomic,copy) NSString *address;//地址
@property (nonatomic,copy) NSString *birthday;//生日
@property (nonatomic,copy) NSString *deleteState;//账号状态
@property (nonatomic,copy) NSString *dept;//部门
@property (nonatomic,copy) NSString *email;//邮箱
@property (nonatomic,copy) NSString *phoneSeqNum;//设备号
@property (nonatomic,copy) NSString *phoneType;//设备类型
@property (nonatomic,copy) NSString *realName;//真实名字
@property (nonatomic,copy) NSString *remark;//备注
@property (nonatomic,assign) int sex;//性别
@property (nonatomic,copy) NSString *idCode;//证件号
@property (nonatomic,copy) NSString *idType;//证件类型
@property (nonatomic,copy) NSString *photoUrl;//照片

@end
