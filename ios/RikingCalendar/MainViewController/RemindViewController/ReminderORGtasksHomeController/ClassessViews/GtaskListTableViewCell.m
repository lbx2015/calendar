//
//  GtaskListTableViewCell.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/28.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "GtaskListTableViewCell.h"

typedef void(^didSelectBtn)(int buttonType,BOOL buttonStatus);//buttonType:1:是否完成按钮,2:是否重要按妞

@interface GtaskListTableViewCell()

@property (nonatomic,strong)didSelectBtn didSelectBtn;

@property (nonatomic,strong)GtasksModel *gModel;

@end


@implementation GtaskListTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}



- (void)loadDataWith:(GtasksModel *)gModel didSelectBtn:(void(^)(int buttonType,BOOL buttonStatus))didSelectBtn{

    _gModel = gModel;
    
    self.GtaskLabel.text = gModel.content;
    self.GtaskLabel.font = threeClassTextFont;
    self.GtaskLabel.textColor = dt_text_main_color;
    [self.finshBtn addTarget:self action:@selector(finshAction:) forControlEvents:UIControlEventTouchUpInside];
    [self.finshBtn setImage:[UIImage imageNamed:@"gtask_unfinish"] forState:UIControlStateNormal];
    [self.finshBtn setImage:[UIImage imageNamed:@"gtaskFinsh"] forState:UIControlStateSelected];
    [self.isImportantBtn setImage:[UIImage imageNamed:@"programme_Important_normalStars"] forState:UIControlStateNormal];
    [self.isImportantBtn setImage:[UIImage imageNamed:@"programme_Important_selectStars"] forState:UIControlStateSelected];
    
    [self.isImportantBtn addTarget:self action:@selector(isImportant:) forControlEvents:UIControlEventTouchUpInside];
    
    if (gModel.isImportant) {
        self.isImportantBtn.selected = YES;
    }else{
        self.isImportantBtn.selected = NO;
    }
    
    if (gModel.isComplete) {
        self.finshBtn.selected = YES;
    }else{
        self.finshBtn.selected = NO;
    }
    
    self.lineView.backgroundColor = dt_line_color;
    
    if (didSelectBtn) {
        self.didSelectBtn = ^(int buttonType,BOOL buttonStatus){
            didSelectBtn(buttonType,buttonStatus);
        };
    }
    
}
- (void)finshAction:(UIButton *)sender{
    
    sender.selected = !sender.selected;
    
    _gModel.isComplete = sender.selected;
    _gModel.completeDate = [Utils getCurrentTime];
    
    @KKWeak(self);
    [[RemindAndGtasksDBManager shareManager] doSaveGtasksNetWorkWithGtasksModel:_gModel editType:2 success:^(BOOL ret) {
        @KKStrong(self);
        
        if (self.didSelectBtn) {
            self.didSelectBtn(1,sender.selected);
        }
    }];
    
    
    
}

- (void)isImportant:(UIButton *)sender;{
    
    sender.selected = !sender.selected;
    _gModel.isImportant = sender.selected;
    
    @KKWeak(self);
    [[RemindAndGtasksDBManager shareManager] doSaveGtasksNetWorkWithGtasksModel:_gModel editType:2 success:^(BOOL ret) {
        @KKStrong(self);
        
        if (self.didSelectBtn) {
            self.didSelectBtn(2,sender.selected);
        }
    }];
    
    
    
    
}


@end
