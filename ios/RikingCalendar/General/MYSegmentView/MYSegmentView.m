//
//  MYSegmentView.m
//  Kitchen
//
//  Created by su on 16/8/8.
//  Copyright © 2016年 susu. All rights reserved.
//

#import "MYSegmentView.h"

@implementation MYSegmentView

- (instancetype)initWithFrame:(CGRect)frame controllers:(NSArray *)controllers titleArray:(NSArray *)titleArray ParentController:(UIViewController *)parentC  lineWidth:(float)lineW lineHeight:(float)lineH
{
    if ( self=[super initWithFrame:frame])
    {
        float avgWidth = (frame.size.width/controllers.count);
   
        self.controllers=controllers;
        self.nameArray=titleArray;
        
        self.segmentView=[[UIView alloc]initWithFrame:CGRectMake(0, 0, frame.size.width, 45)];
        self.segmentView.tag=50;
        [self addSubview:self.segmentView];
        self.segmentScrollV=[[UIScrollView alloc]initWithFrame:CGRectMake(0, 45, frame.size.width, frame.size.height -45)];
        self.segmentScrollV.contentSize=CGSizeMake(frame.size.width*self.controllers.count, 0);
        self.segmentScrollV.delegate=self;
        self.segmentScrollV.showsHorizontalScrollIndicator=NO;
        self.segmentScrollV.pagingEnabled=YES;
        self.segmentScrollV.scrollEnabled = NO;
        self.segmentScrollV.bounces=NO;
        [self addSubview:self.segmentScrollV];
        
        for (int i=0;i<self.controllers.count;i++)
        {
            UIViewController * contr=self.controllers[i];
            [self.segmentScrollV addSubview:contr.view];
            contr.view.frame=CGRectMake(i*frame.size.width, 0, frame.size.width,frame.size.height);
            [parentC addChildViewController:contr];
            [contr didMoveToParentViewController:parentC];
        }
        for (int i=0;i<self.controllers.count;i++)
        {
            UIButton * btn=[ UIButton buttonWithType:UIButtonTypeCustom];
            btn.frame=CGRectMake(i*(frame.size.width/self.controllers.count), 0, (frame.size.width-(self.controllers.count-1))/self.controllers.count, 41);
            btn.tag=i;
            [btn setTitle:self.nameArray[i] forState:(UIControlStateNormal)];
            [btn setTitleColor:[UIColor hex_colorWithHex:@"#323232"] forState:(UIControlStateNormal)];
            [btn setTitleColor:[UIColor hex_colorWithHex:@"29A1F7"] forState:(UIControlStateSelected)];
            [btn addTarget:self action:@selector(Click:) forControlEvents:(UIControlEventTouchUpInside)];
            btn.titleLabel.font=[UIFont systemFontOfSize:17.];
            
            
            if (i>0 && i<self.controllers.count) {
                UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(i*(frame.size.width/self.controllers.count), 12, 1, 21)];
                lineView.backgroundColor = [UIColor hex_colorWithHex:@"#D9D9D9"];
                [self.segmentView addSubview:lineView];
            }
            
            if (i==0)
            {btn.selected=YES ;self.seleBtn=btn;
                btn.titleLabel.font=[UIFont systemFontOfSize:17.0];
            } else { btn.selected=NO; }
            
            [self.segmentView addSubview:btn];
        }
        
        self.down=[[UILabel alloc]initWithFrame:CGRectMake(0, 40, frame.size.width, 0.5)];
        self.down.backgroundColor = [UIColor lightGrayColor];
        [self.segmentView addSubview:self.down];
        
        self.line=[[UILabel alloc]initWithFrame:CGRectMake((avgWidth-lineW)/2,41-lineH, lineW, lineH)];
        self.line.backgroundColor = [UIColor hex_colorWithHex:@"29A1F7"];
        self.line.tag=100;
        [self.segmentView addSubview:self.line];
    }
    
    
    return self;
}

- (void)Click:(UIButton*)sender
{
    self.seleBtn.titleLabel.font= [UIFont systemFontOfSize:17.];;
    self.seleBtn.selected=NO;
    self.seleBtn=sender;
    self.seleBtn.selected=YES;
    self.seleBtn.titleLabel.font= [UIFont systemFontOfSize:17.];;
    [UIView animateWithDuration:0.2 animations:^{
        CGPoint  frame=self.line.center;
        frame.x=self.frame.size.width/(self.controllers.count*2) +(self.frame.size.width/self.controllers.count)* (sender.tag);
        self.line.center=frame;
    }];
    [self.segmentScrollV setContentOffset:CGPointMake((sender.tag)*self.frame.size.width, 0) animated:NO];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    
    [[NSNotificationCenter defaultCenter] postNotificationName:@"merchantViewScroll" object:scrollView userInfo:nil];
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
{
    [UIView animateWithDuration:0.2 animations:^{
        CGPoint  frame=self.line.center;
        frame.x=self.frame.size.width/(self.controllers.count*2) +(self.frame.size.width/self.controllers.count)*(self.segmentScrollV.contentOffset.x/self.frame.size.width);
        self.line.center=frame;
    }];
    UIButton * btn=(UIButton*)[self.segmentView viewWithTag:(self.segmentScrollV.contentOffset.x/self.frame.size.width)];
    self.seleBtn.selected=NO;
    self.seleBtn=btn;
    self.seleBtn.selected=YES;
    
}

@end
