//
//  SeeProfilesViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/29.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "SeeProfilesViewController.h"

@interface SeeProfilesViewController ()

{
    NSString *_urlString;
}

@end

@implementation SeeProfilesViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.webView.delegate = self;
    [self loadHTML:self.htmlUrl];
    
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
