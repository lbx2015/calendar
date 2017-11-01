//
//  LaunchIntroductionView.m
//  ZYGLaunchIntroductionDemo
//
//  Created by ZhangYunguang on 16/4/7.
//  Copyright © 2016年 ZhangYunguang. All rights reserved.
//

#import "LaunchIntroductionView.h"
#import "RKPageControl.h"
static NSString *const kAppVersion = @"appVersion";


@interface LaunchIntroductionView ()<UIScrollViewDelegate>
{
    UIScrollView  *launchScrollView;
    RKPageControl *page;
}

@end

@implementation LaunchIntroductionView
NSArray *images;
BOOL isScrollOut;//在最后一页再次滑动是否隐藏引导页
BOOL isPage;
CGRect enterBtnFrame;
NSString *enterBtnImage;
static LaunchIntroductionView *launch = nil;
NSString *storyboard;

#pragma mark - 创建对象-->>不带button
+(instancetype)sharedWithImages:(NSArray *)imageNames{
    images = imageNames;
    isScrollOut = NO;
    isPage = YES;
    launch = [[LaunchIntroductionView alloc] initWithFrame:CGRectMake(0, 0, kScreen_width, kScreen_height)];
    launch.backgroundColor = [UIColor whiteColor];
    return launch;
}

#pragma mark - 创建对象-->>带button
+(instancetype)sharedWithImages:(NSArray *)imageNames buttonImage:(NSString *)buttonImageName buttonFrame:(CGRect)frame{
    images = imageNames;
    isScrollOut = NO;
    enterBtnFrame = frame;
    enterBtnImage = buttonImageName;
    launch = [[LaunchIntroductionView alloc] initWithFrame:CGRectMake(0, 0, kScreen_width, kScreen_height)];
    launch.backgroundColor = [UIColor whiteColor];
    return launch;
}
#pragma mark - 用storyboard创建的项目时调用，不带button
+ (instancetype)sharedWithStoryboardName:(NSString *)storyboardName images:(NSArray *)imageNames {
    images = imageNames;
    storyboard = storyboardName;
    isScrollOut = YES;
    launch = [[LaunchIntroductionView alloc] initWithFrame:CGRectMake(0, 0, kScreen_width, kScreen_height)];
    launch.backgroundColor = [UIColor whiteColor];
    return launch;
}
#pragma mark - 用storyboard创建的项目时调用，带button
+ (instancetype)sharedWithStoryboard:(NSString *)storyboardName images:(NSArray *)imageNames buttonImage:(NSString *)buttonImageName buttonFrame:(CGRect)frame{
    images = imageNames;
    isScrollOut = NO;
    enterBtnFrame = frame;
    storyboard = storyboardName;
    enterBtnImage = buttonImageName;
    launch = [[LaunchIntroductionView alloc] initWithFrame:CGRectMake(0, 0, kScreen_width, kScreen_height)];
    launch.backgroundColor = [UIColor whiteColor];
    return launch;
}
#pragma mark - 初始化
- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self addObserver:self forKeyPath:@"currentColor" options:NSKeyValueObservingOptionNew context:nil];
        [self addObserver:self forKeyPath:@"nomalColor" options:NSKeyValueObservingOptionNew context:nil];
        if ([self isFirstLauch]) {
            UIStoryboard *story;
            if (storyboard) {
                story = [UIStoryboard storyboardWithName:storyboard bundle:nil];
            }
            UIWindow *window = [UIApplication sharedApplication].windows.lastObject;
            if (story) {
                UIViewController * vc = story.instantiateInitialViewController;
                window.rootViewController = vc;
                [vc.view addSubview:self];
            }else {
                [window addSubview:self];
            }
            [self addImages];
        }else{
            [self removeFromSuperview];
        }
    }
    return self;
}
#pragma mark - 判断是不是首次登录或者版本更新
-(BOOL )isFirstLauch{
    //获取当前版本号
    NSDictionary *infoDic = [[NSBundle mainBundle] infoDictionary];
    NSString *currentAppVersion = infoDic[@"CFBundleShortVersionString"];
    //获取上次启动应用保存的appVersion
    NSString *version = [[NSUserDefaults standardUserDefaults] objectForKey:kAppVersion];
    //版本升级或首次登录
    if (version == nil || ![version isEqualToString:currentAppVersion]) {
//        [[NSUserDefaults standardUserDefaults] setObject:currentAppVersion forKey:kAppVersion];
//        [[NSUserDefaults standardUserDefaults] synchronize];
        return YES;
    }else{
        return NO;
    }
}
#pragma mark - 添加引导页图片
-(void)addImages{
    [self createScrollView];
}
#pragma mark - 创建滚动视图
-(void)createScrollView{
    launchScrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, kScreen_width, kScreen_height)];
    launchScrollView.showsHorizontalScrollIndicator = NO;
    launchScrollView.bounces = NO;
    launchScrollView.pagingEnabled = YES;
    launchScrollView.delegate = self;
    launchScrollView.contentSize = CGSizeMake(kScreen_width * images.count, kScreen_height);
    NSArray *titleArray = @[@"每月报表汇总,同步工作进度",@"外汇交易市场币种节假日查询",@"快速建立提醒,重要事件不忘记",@"悦历-服务金融从业人员的日历"];
    [self addSubview:launchScrollView];
    for (int i = 0; i < images.count; i ++) {
        UIImageView *imageView = [[UIImageView alloc] init];
        imageView.image = [UIImage imageNamed:images[i]];
        imageView.frame = CGRectMake(i*kScreenWidth+kScreenWidth/2-imageView.image.size.width/2, kScreenHeight-225-imageView.image.size.height, imageView.image.size.width, imageView.image.size.height);
        
        [launchScrollView addSubview:imageView];
        
        UILabel *titleLabel = [[UILabel alloc]init];
        titleLabel.text = titleArray[i];
        titleLabel.font = twoClassTextFont;
        titleLabel.textColor = dt_app_main_color;
        titleLabel.textAlignment = NSTextAlignmentCenter;
        [launchScrollView addSubview:titleLabel];
        
        
        [imageView mas_makeConstraints:^(MASConstraintMaker *make) {
           
            make.top.equalTo(launchScrollView).offset(iPhone4?64:188);
            make.size.mas_equalTo(CGSizeMake(imageView.image.size.width, imageView.image.size.height));
            make.left.equalTo(launchScrollView).offset(i*kScreenWidth+kScreenWidth/2-imageView.image.size.width/2);
        }];
        
        [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
           
            make.left.equalTo(launchScrollView).offset(i*kScreenWidth);
            make.top.equalTo(imageView.mas_bottom).offset(50);
            make.height.mas_equalTo(20);
            make.width.mas_equalTo(kScreenWidth);
        }];
        
        
        if (i == images.count - 1) {
            //判断要不要添加button
            if (!isScrollOut) {
                RKLog(@"%f",kScreenHeight);
                UIButton *enterButton = [[UIButton alloc] initWithFrame:CGRectMake((images.count-1)*kScreenWidth+kScreenWidth/2-75, kScreenHeight-(iPhone4?30+60:102+60), 160, 54)];
                [enterButton setImage:[UIImage imageNamed:@"closeGuide"] forState:UIControlStateNormal];
                enterButton.backgroundColor = [UIColor clearColor];
                [enterButton addTarget:self action:@selector(enterBtnClick) forControlEvents:UIControlEventTouchUpInside];
                [enterButton setTitle:@"立即进入" forState:UIControlStateNormal];
                [enterButton setTitleEdgeInsets:UIEdgeInsetsMake(0,-150,0,0)];
                [enterButton setImageEdgeInsets:UIEdgeInsetsMake(0,5,0,0)];
                enterButton.titleLabel.font = twoClassTextFont;
                [launchScrollView addSubview:enterButton];
                
            }
        }
    }
    
    if (isPage) {
        page = [[RKPageControl alloc] initWithFrame:CGRectMake(0, iPhone4?kScreen_height-68 : kScreen_height-108, kScreen_width, 30)];
        page.currentPage = 0;
        page.numberOfPages = images.count;
        page.backgroundColor = [UIColor clearColor];
        [page setValue:[UIImage imageNamed:@"currentPage"] forKeyPath:@"_currentPageImage"];
        [page setValue:[UIImage imageNamed:@"nomalPage"] forKeyPath:@"_pageImage"];
//        page.defersCurrentPageDisplay = YES;
        [self addSubview:page];
    }
    
}
#pragma mark - 进入按钮
-(void)enterBtnClick{
    [self hideGuidView];
}
#pragma mark - 隐藏引导页
-(void)hideGuidView{
    [UIView animateWithDuration:0.5 animations:^{
        self.alpha = 0;
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [self removeFromSuperview];
        });
        
    }];
}
#pragma mark - scrollView Delegate
-(void)scrollViewWillBeginDragging:(UIScrollView *)scrollView{
    int cuttentIndex = (int)(scrollView.contentOffset.x + kScreen_width/2)/kScreen_width;
    if (cuttentIndex == images.count - 1) {
        if ([self isScrolltoLeft:scrollView]) {
            if (!isScrollOut) {
                return ;
            }
            [self hideGuidView];
        }
    }
}
-(void)scrollViewDidScroll:(UIScrollView *)scrollView{
    if (scrollView == launchScrollView) {
        int cuttentIndex = (int)(scrollView.contentOffset.x + kScreen_width/2)/kScreen_width;
        page.currentPage = cuttentIndex;
        if (cuttentIndex==images.count-1) {
            page.hidden = YES;
        }else{
            page.hidden = NO;
        }
    }
}
#pragma mark - 判断滚动方向
-(BOOL )isScrolltoLeft:(UIScrollView *) scrollView{
    //返回YES为向左反动，NO为右滚动
    if ([scrollView.panGestureRecognizer translationInView:scrollView.superview].x < 0) {
        return YES;
    }else{
        return NO;
    }
}
#pragma mark - KVO监测值的变化
-(void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary<NSString *,id> *)change context:(void *)context{
    if ([keyPath isEqualToString:@"currentColor"]) {
        page.currentPageIndicatorTintColor = self.currentColor;
    }
    if ([keyPath isEqualToString:@"nomalColor"]) {
        page.pageIndicatorTintColor = self.nomalColor;
        
    }
}

@end
