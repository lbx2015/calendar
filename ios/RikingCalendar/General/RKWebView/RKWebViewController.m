//
//  RKWebViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/22.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKWebViewController.h"
#import "WebProgressSignManager.h"
@interface NSURLRequest (InvalidSSLCertificate)

+ (BOOL)allowsAnyHTTPSCertificateForHost:(NSString*)host;
+ (void)setAllowsAnyHTTPSCertificate:(BOOL)allow forHost:(NSString*)host;

@end

@interface RKWebViewController ()
<UIGestureRecognizerDelegate>
@property (nonatomic, strong) NSURLRequest *request;
//判断是否是HTTPS的
@property (nonatomic, assign) BOOL isAuthed;
@property (nonatomic, assign) BOOL isShowNotnetwork;
//返回按钮
@property (nonatomic, strong) UIBarButtonItem *backItem;
//关闭按钮
@property (nonatomic, strong) UIBarButtonItem *closeItem;

@property (nonatomic, assign) NSInteger statusCode;

//下面的三个属性是添加进度条的
@property (nonatomic, assign) BOOL theBool;

@property (nonatomic, strong) UIProgressView *progressView;

@property (nonatomic, strong) NSTimer *timer;
//记录上次浏览记录
@property (nonatomic, copy)   NSString *lastHtmlUrl;
@end

@implementation RKWebViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor whiteColor];
    self.webView = [[UIWebView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height - 64)];
    self.webView.scrollView.delegate = self;
    self.webView.delegate = self;
    [self.view addSubview:self.webView];
    [self addLeftButton];
    
    //添加进度条（如果没有需要，可以注释掉
    [self addProgressBar];
    
}

#pragma mark - 懒加载
- (UIView *)notWorkBg{
    if (!_notWorkBg) {
        _notWorkBg = [[UIView alloc]init];
        _notWorkBg.backgroundColor = dt_F2F2F2_color;
        [self.view addSubview:_notWorkBg];
        [_notWorkBg mas_makeConstraints:^(MASConstraintMaker *make) {
            
            //        make.centerY.equalTo(self.view);
//            make.centerX.equalTo(self.view);
//            make.width.height.mas_equalTo(kScreenWidth);
//            make.top.equalTo(self.view).offset(90);
            
            make.edges.mas_equalTo(UIEdgeInsetsMake(0, 0, 0, 0));
        }];
        
        UIImageView *imageView = [[UIImageView alloc]init];
        [imageView setImage:[UIImage imageNamed:@"not_network"]];
        [_notWorkBg addSubview:imageView];
        [imageView mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.top.equalTo(_notWorkBg).offset(90);
            make.centerX.equalTo(_notWorkBg);
            make.width.mas_equalTo(imageView.image.size.width);
            make.height.mas_equalTo(imageView.image.size.height);
        }];
        
        UILabel *label = [[UILabel alloc]init];
        label.textColor = dt_textLightgrey_color;
        label.textAlignment = NSTextAlignmentCenter;
        label.font = sixClassTextFont;
        label.text = NSLocalizedString(@"Load_failed", nil);
        [_notWorkBg addSubview:label];
        
        [label mas_makeConstraints:^(MASConstraintMaker *make) {
           
            make.top.equalTo(imageView.mas_bottom).offset(18);
            make.left.right.equalTo(_notWorkBg).offset(0);
            make.height.mas_equalTo(20);
            
        }];
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(refreshWebView)];
        tap.delegate = self;
        [self.view addGestureRecognizer:tap];
        
    }
    
    return _notWorkBg;
}

#pragma mark - UIGestureRecognizerDelegate
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if( [touch.view isDescendantOfView:self.notWorkBg]) {
        return YES;
    }
    return NO;
}

- (void)refreshWebView{
    [self.webView loadRequest:self.request];
}

//加载URL
- (void)loadHTML:(NSString *)htmlString
{
    NSURL *url = [NSURL URLWithString:htmlString];
    self.request = [NSURLRequest requestWithURL:url cachePolicy:NSURLRequestReloadIgnoringLocalCacheData timeoutInterval:5.0];
    [NSURLRequest setAllowsAnyHTTPSCertificate:YES forHost:[url host]];
    [self.webView loadRequest:self.request];
}

#pragma mark - UIWebViewDelegate

//开始加载
- (BOOL)webView:(UIWebView *)awebView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{

    NSString* scheme = [[request URL] scheme];
    self.request = request;
    //判断是不是https
    if ([scheme isEqualToString:@"https"]) {
        //如果是https:的话，那么就用NSURLConnection来重发请求。从而在请求的过程当中吧要请求的URL做信任处理。
        if (!self.isAuthed) {
            NSURLConnection* conn = [[NSURLConnection alloc] initWithRequest:request delegate:self];
            [conn start];
            [awebView stopLoading];
            return NO;
        }
    }
    
    //监测webview状态
    NSHTTPURLResponse *response = nil;
    RKLog(@"%@",self.request.URL);
    [NSURLConnection sendSynchronousRequest:self.request returningResponse:&response error:nil];
    RKLog(@"statusCode:%ld", response.statusCode);
    self.statusCode = response.statusCode;
    
    //如果加载html失败,先显示网络错误标识,后隐藏
    if (self.statusCode != 200 && self.statusCode != 0) {
        self.notWorkBg.hidden = NO;
        self.isShowNotnetwork = YES;
    }
    
    return YES;
}

//设置webview的title为导航栏的title
- (void)webViewDidFinishLoad:(UIWebView *)webView
{
    self.theBool = true; //加载完毕后，进度条完成
    
    if (self.statusCode == 200 || self.statusCode == 0) {
    
        if (self.isShowNotnetwork) {
            self.notWorkBg.hidden = YES;
        }
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        self.title = [webView stringByEvaluatingJavaScriptFromString:@"document.title"];
        self.lastHtmlUrl = webView.request.URL.absoluteString;
        
        //取上次的浏览的记录
        if ([[WebProgressSignManager shareManager].htmlUrlDict objectForKey:self.lastHtmlUrl]) {
            
            CGFloat offsetY = [[[WebProgressSignManager shareManager].htmlUrlDict objectForKey:self.lastHtmlUrl] floatValue];
            
            if (offsetY) {
                
                [webView.scrollView setContentOffset:CGPointMake(0, offsetY) animated:NO];
                
            }
            
        }
    }
    
    if ([self.webView canGoBack]) {
        //同时设置返回按钮和关闭按钮为导航栏左边的按钮
        self.navigationItem.leftBarButtonItems = @[self.backItem, self.closeItem];
    }
    
    
}

#pragma mark ================= NSURLConnectionDataDelegate <NSURLConnectionDelegate>

- (BOOL)connection:(NSURLConnection *)connection canAuthenticateAgainstProtectionSpace:(NSURLProtectionSpace *)protectionSpace
{
    return [protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodServerTrust];
}

- (void)connection:(NSURLConnection *)connection willSendRequestForAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge
{
    if ([challenge previousFailureCount] == 0) {
        self.isAuthed = YES;
        //NSURLCredential 这个类是表示身份验证凭据不可变对象。凭证的实际类型声明的类的构造函数来确定。
        NSURLCredential *cre = [NSURLCredential credentialForTrust:challenge.protectionSpace.serverTrust];
        [challenge.sender useCredential:cre forAuthenticationChallenge:challenge];
    }
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
    RKLog(@"网络不给力");
    self.notWorkBg.hidden = NO;
    self.isShowNotnetwork = YES;
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    self.isAuthed = YES;
    //webview 重新加载请求。
    [self.webView loadRequest:self.request];
    [connection cancel];
}

#pragma mark - 添加关闭按钮

- (void)addLeftButton
{
    self.navigationItem.leftBarButtonItem = self.backItem;
}

//点击返回的方法
- (void)backNative
{
    //判断是否有上一层H5页面
    if ([self.webView canGoBack]) {
        //如果有则返回
        [self.webView goBack];
        
    } else {
        [self closeNative];
    }
}

//关闭H5页面，直接回到原生页面
- (void)closeNative
{
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - init

- (UIBarButtonItem *)backItem
{
    if (!_backItem) {
        _backItem = [[UIBarButtonItem alloc] init];
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        btn.themeMap = @{kThemeMapKeyImageName : @"navigationBar_itemIcon_back"};
        [btn setTitle:NSLocalizedString(@"back", nil) forState:UIControlStateNormal];
        [btn addTarget:self action:@selector(backNative) forControlEvents:UIControlEventTouchUpInside];
        [btn.titleLabel setFont:[UIFont systemFontOfSize:17]];
        [btn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        //字体的多少为btn的大小
        [btn sizeToFit];
        //左对齐
        btn.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
        //让返回按钮内容继续向左边偏移15，如果不设置的话，就会发现返回按钮离屏幕的左边的距离有点儿大，不美观
        btn.contentEdgeInsets = UIEdgeInsetsMake(0, -10, 0, 0);
        btn.frame = CGRectMake(0, 0, 40, 40);
        _backItem.customView = btn;
    }
    return _backItem;
}

- (UIBarButtonItem *)closeItem
{
    if (!_closeItem) {
        _closeItem = [[UIBarButtonItem alloc] initWithTitle:@"关闭" style:UIBarButtonItemStylePlain target:self action:@selector(closeNative)];
        _closeItem.tintColor = [UIColor whiteColor];
    }
    return _closeItem;
}

#pragma mark - 下面所有的方法是添加进度条

- (void)addProgressBar
{
    // 仿微信进度条
    CGFloat progressBarHeight = 1.0f;
    CGRect navigationBarBounds = self.navigationController.navigationBar.bounds;
    CGRect barFrame = CGRectMake(0, navigationBarBounds.size.height - progressBarHeight, navigationBarBounds.size.width, progressBarHeight);
    self.progressView = [[UIProgressView alloc] initWithFrame:barFrame];
    self.progressView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleTopMargin;
    self.progressView.trackTintColor = [UIColor grayColor]; //背景色
    self.progressView.progressTintColor = [UIColor greenColor]; //进度色
    [self.navigationController.navigationBar addSubview:self.progressView];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    //移除progressView  because UINavigationBar is shared with other ViewControllers
    [self.progressView removeFromSuperview];
}

- (void)webViewDidStartLoad:(UIWebView *)webView
{
    self.progressView.progress = 0;
    self.theBool = false;
    //0.01667 is roughly 1/60, so it will update at 60 FPS
    self.timer = [NSTimer scheduledTimerWithTimeInterval:0.01667 target:self selector:@selector(timerCallback) userInfo:nil repeats:YES];
}

- (void)timerCallback
{
    if (self.theBool) {
        if (self.progressView.progress >= 1) {
            self.progressView.hidden = true;
            [self.timer invalidate];
        } else {
            self.progressView.progress += 0.1;
        }
    } else {
        self.progressView.progress += 0.1;
        if (self.progressView.progress >= 0.9) {
            self.progressView.progress = 0.9;
        }
    }
}


- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
    
    CGFloat offsetY =  self.webView.scrollView.contentOffset.y;
    
    //存记录
    [[WebProgressSignManager shareManager].htmlUrlDict setObject:[NSString stringWithFormat:@"%f",offsetY] forKey:[NSString stringWithFormat:@"%@",self.webView.request.URL.absoluteString]];
    RKLog(@"%@",[WebProgressSignManager shareManager].htmlUrlDict);
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
