//
//  WebViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/15.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "WebViewController.h"
#import <MessageUI/MessageUI.h>
#import "SeeProfilesViewController.h"
@interface WebViewController ()
@end


@implementation WebViewController

- (void)viewDidLoad {
    
    [super viewDidLoad];
    self.webView.delegate = self;
    [self loadHTML:self.htmlUrl];
    self.webView.scalesPageToFit = YES;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
