//
//  RemindListTableViewCell.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/10.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RemindListTableViewCell.h"

@interface RemindListTableViewCell()

@property (weak, nonatomic) IBOutlet UILabel *remindTime;

@property (weak, nonatomic) IBOutlet UILabel *remindConment;
@property (weak, nonatomic) IBOutlet UIView *lineView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lineViewLeftLayout;

@end



@implementation RemindListTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)loadDataWithReminderModel:(ReminderModel *)rModel indexPath:(NSIndexPath *)indexPath remindArray:(NSArray *)remindArray{
    
    self.remindTime.textColor = dt_app_main_color;
    self.remindTime.font = threeClassTextFont;
    self.remindTime.text = [Utils transformDate:rModel.startTime dateFormatStyle:DateFormatHourMinuteWith24HR];
    
    
    self.remindConment.textColor = dt_text_main_color;
    self.remindConment.font = threeClassTextFont;
    self.remindConment.text = rModel.content;
    
    self.lineView.backgroundColor = dt_line_color;

    if (indexPath.row==remindArray.count-1) {
        self.lineViewLeftLayout.constant=0;
    }
    
    if (rModel.isAllday) {
        self.remindTime.text = @"全天";
    }
    
    if (indexPath.section>0) {
        self.remindTime.textColor = dt_text_888888_color;
        self.remindTime.font = eightClassTextFont;
        self.remindConment.textColor = dt_text_main_color;
    }
    
    
   
}


- (void)loadHomeDataWithReminderModel:(ReminderModel *)rModel{
    
    self.remindTime.textColor = dt_app_main_color;
    self.remindTime.font = threeClassTextFont;
    self.remindTime.text = rModel.startTime;
    
    
    self.remindConment.textColor = dt_text_main_color;
    self.remindConment.font = threeClassTextFont;
    self.remindConment.text = rModel.content;
    
    
    if (rModel.isAllday) {
        self.remindTime.text = @"全天";
    }
}



- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
