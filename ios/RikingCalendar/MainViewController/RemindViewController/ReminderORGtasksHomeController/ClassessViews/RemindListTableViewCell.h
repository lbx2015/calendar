//
//  RemindListTableViewCell.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/10.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RemindListTableViewCell : UITableViewCell



- (void)loadDataWithReminderModel:(ReminderModel *)rModel indexPath:(NSIndexPath *)indexPath;

- (void)loadHomeDataWithReminderModel:(ReminderModel *)rModel;


@end
