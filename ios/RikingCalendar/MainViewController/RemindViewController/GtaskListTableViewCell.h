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

@end
