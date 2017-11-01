//
//  RKWebViewController.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/22.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseViewController.h"

@interface RKWebViewController : RKBaseViewController
<UIWebViewDelegate, NSURLConnectionDelegate,UIScrollViewDelegate>

//定义一个属性，方便外接调用
@property (nonatomic, strong) UIWebView *webView;
/** HTML的URL*/
@property (nonatomic, copy) NSString *htmlUrl;

@property (nonatomic, strong) UIView *notWorkBg;

//声明一个方法，外接调用时，只需要传递一个URL即可
- (void)loadHTML:(NSString *)htmlString;
@end
