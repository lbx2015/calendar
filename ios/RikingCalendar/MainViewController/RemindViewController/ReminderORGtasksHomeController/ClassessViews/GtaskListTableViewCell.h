//
//  GtaskListTableViewCell.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/28.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GtaskListTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIButton *finshBtn;

@property (weak, nonatomic) IBOutlet UILabel *GtaskLabel;

@property (weak, nonatomic) IBOutlet UIButton *isImportantBtn;
@property (weak, nonatomic) IBOutlet UIView *lineView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lineViewLeftLayout;

- (void)loadDataWith:(GtasksModel *)gModel didSelectBtn:(void(^)(int buttonType,BOOL buttonStatus))didSelectBtn;

@end
