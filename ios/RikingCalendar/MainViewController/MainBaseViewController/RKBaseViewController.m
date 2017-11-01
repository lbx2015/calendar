//
//  RKBaseViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/12.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "RKBaseViewController.h"
#import "AppDelegate.h"
@interface RKBaseViewController ()


{
    void *(^SuccessBlock)(NSDictionary *dictData);
    
    void *(^FailureBlock)(NSError *error);
    
    BOOL _isShowAlert;
}

@property (nonatomic,strong)NSNotificationCenter *notificationCenter;

@end

@implementation RKBaseViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.view.themeMap = @{kThemeMapKeyColorName : view_backgroundColor};
    
    int controllerIndex = (int)[self.navigationController.viewControllers indexOfObject:self];
    
    if (controllerIndex > 0)
    {
        [self setDefaultButton:@""];
    }else{
        
        //发送一个通知,用于切换用户,更新本地数据
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(userSwitch) name:kUserSwitchNotificationName object:nil];

        
    }
    
}




- (void)setDefaultButton:(NSString *)title
{
    UIButton *backBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [backBtn setFrame:CGRectMake(0, 0, 44, 44)];
    backBtn.themeMap = @{kThemeMapKeyImageName : @"navigationBar_itemIcon_back"};
//    [backBtn setImage:[UIImage imageNamed:@"icon-back.png"] forState:UIControlStateNormal];
    [backBtn setImageEdgeInsets:UIEdgeInsetsMake(10, 0, 10, 44 - 13)];
    [backBtn addTarget:self action:@selector(doLeftAction:) forControlEvents:UIControlEventTouchUpInside];
    UILabel *lbl = [[UILabel alloc] initWithFrame:CGRectMake(15, 0, 17*[title length], 44)];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextColor:[UIColor whiteColor]];
    [lbl setText:title];
    [backBtn addSubview:lbl];
    UIBarButtonItem *leftItem = [[UIBarButtonItem alloc] initWithCustomView:backBtn];
    self.navigationItem.leftBarButtonItem = leftItem;
    
}

/**
 网络请求数据
 
 @param method 请求方式
 @param urlString 地址
 @param parm 参数
 @param success 成功回调
 @param failue 失败回调
 */
-(void)kkRequestWithHTTPMethod:(KKHTTPMethod)method urlString:(NSString *)urlString parm:(NSDictionary *)parm success:(void(^)(NSDictionary *dictData))success failure:(void (^)(NSError *error))failue
{
    if (method == POST) {
        [[AFNWorkingTool sharedManager] AFNHttpRequestPOSTurlstring:urlString parm:parm success:success failure:failue];
    }else{
        [[AFNWorkingTool sharedManager] AFNHttpRequestGETWithUrlstring:urlString timeout:30 success:success failure:failue];
    }
}

-(void)requestWithHTTPMethod:(KKHTTPMethod)method urlString:(NSString *)urlString parm:(NSDictionary *)parm showWaitAlertTitile:(NSString *)title isAfterDelay:(BOOL)afterDelay success:(void(^)(id dictData))success failure:(void (^)(NSString *message))failue
{
    if (title) {
        _isShowAlert = YES;
        if (afterDelay) {
            [self performSelector:@selector(showAlert:) withObject:title afterDelay:1.0 inModes:@[NSRunLoopCommonModes]];
        }else{
            [self showAlert:title];
        }
    }
    
    NSMutableDictionary *dictParm = [NSMutableDictionary dictionaryWithDictionary:parm];
    
    if ([dictParm objectForKey:@"columeNames"]) {
        [dictParm removeObjectForKey:@"columeNames"];
        [dictParm removeObjectForKey:@"columeTypes"];
        [dictParm removeObjectForKey:@"propertyNames"];
        [dictParm removeObjectForKey:@"pk"];
    }
    
    [self kkRequestWithHTTPMethod:method urlString:urlString parm:dictParm success:^(NSDictionary *dictData) {
        [self hideMBManager];
        
        
        if (dictData && [dictData[@"code"]isEqualToNumber:@200]) {
            if (success) {
                
                if ([dictData objectForKey:@"_data"]) {
                    success(dictData[@"_data"]);
                }

            }
        }else{
            
            NSString *message = dictData?dictData[@"codeDesc"]:@"网络链接错误";
            [MBManager showBriefAlert:message inView:self.view];
            if (failue) {
                failue(message);
            }
        }
        
        
    } failure:^(NSError *error) {
        [self hideMBManager];
        [MBManager showBriefAlert:[Utils getMessageError:error] inView:self.view];
        [self.diyRefreshFooter endRefreshing];
        [self.kkRefreshHeader endRefreshing];
        [self.kkRefreshFooter endRefreshing];
    }];
}


-(void)requestWithHTTPMethod:(KKHTTPMethod)method urlString:(NSString *)urlString parm:(NSDictionary *)parm isHaveAlert:(BOOL)alert waitTitle:(NSString *)waitTitle success:(void(^)(id dictData))success failure:(void (^)(NSString *message))failue
{
    if (alert) {
        _isShowAlert = YES;
        
        /**
         注：此方法是一种非阻塞的执行方式，未找到取消执行的方法
         可传任意类型参数
         */
        //        [self performSelector:@selector(doSomething:) withObject:nil afterDelay:1.0 inModes:@[NSRunLoopCommonModes]];
        
        
        /**
         注：此方法是一种非阻塞的执行方式，
         取消执行方法：- (void)invalidate;即可
         */
        NSTimer *timer = [NSTimer timerWithTimeInterval:1.0 target:self selector:@selector(doSomething:) userInfo:nil repeats:NO];
        [[NSRunLoop currentRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
        
    }
    [self kkRequestWithHTTPMethod:method urlString:urlString parm:parm success:^(NSDictionary *dictData) {
        [self hideMBProgressHUD];
        if (dictData && [dictData[@"code"]isEqualToNumber:@200]) {
            if ([dictData objectForKey:@"_data"]) {
                success(dictData[@"_data"]);
            }
        }else{
            
            NSString *message = dictData?dictData[@"codeDesc"]:@"网络链接错误";
            [MBManager showBriefAlert:message inView:self.view];
            if (failue) {
                failue(message);
            }
        }
        
    } failure:^(NSError *error) {
        [self hideMBProgressHUD];
        [MBManager showBriefAlert:[Utils getMessageError:error] inView:self.view];
        if (failue) {
            failue([Utils getMessageError:error]);
        }
    }];
}


- (void)doSaveRemindAndGtasksWithRequestStyle:(RGRequestStyle)requestStyle model:(RKBaseModel *)model isHaveAlert:(BOOL)alert waitTitle:(NSString *)waitTitle success:(void(^)(id dictData))success failure:(void (^)(NSString *message))failue{
    
    
    if (!(isUser&&self.networkStatus)) {
        
        if (failue) {
            failue(@"无网络或者未登录");
        }
        return;
    }
    
    NSString *requestUrl = @"";
    NSMutableDictionary *parm = [NSMutableDictionary dictionary];
    
    if (requestStyle == remindSaveUpdate) {
        requestUrl = requestUrl(saveReminder);
        
    }else if (requestStyle == remindDelete){
        
        
    }else if (requestStyle == gtasksSaveUpdate){
        requestUrl = requestUrl(saveTodo);
        
    }else{
        
    }
    
    [parm setDictionary:[model mj_keyValues]];
    
    if ([parm objectForKey:@"columeNames"]) {
        [parm removeObjectForKey:@"columeNames"];
        [parm removeObjectForKey:@"columeTypes"];
        [parm removeObjectForKey:@"propertyNames"];
        [parm removeObjectForKey:@"pk"];
    }
    
    [parm setObject:UserID forKey:@"userId"];
    
    if (alert) {
        _isShowAlert = YES;
        
        /**
         注：此方法是一种非阻塞的执行方式，未找到取消执行的方法
         可传任意类型参数
         */
        //        [self performSelector:@selector(doSomething:) withObject:nil afterDelay:1.0 inModes:@[NSRunLoopCommonModes]];
        
        
        /**
         注：此方法是一种非阻塞的执行方式，
         取消执行方法：- (void)invalidate;即可
         */
        NSTimer *timer = [NSTimer timerWithTimeInterval:1.0 target:self selector:@selector(doSomething:) userInfo:nil repeats:NO];
        [[NSRunLoop currentRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
        
    }
    [self kkRequestWithHTTPMethod:POST urlString:requestUrl parm:parm success:^(NSDictionary *dictData) {
        [self hideMBProgressHUD];
        if (dictData && [dictData[@"code"]isEqualToNumber:@200]) {
            
            if ([dictData objectForKey:@"_data"]) {
                success(dictData[@"_data"]);
            }
        }else{
            
            NSString *message = dictData?dictData[@"codeDesc"]:@"网络链接错误";
            if (failue) {
                failue(message);
            }
        }
        
    } failure:^(NSError *error) {
        
        if (failue) {
            failue([Utils getMessageError:error]);
        }
        
    }];
    
    
}




-(void)doSomething:(NSTimer *)timer
{
    [timer invalidate];
    if (_isShowAlert) {
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    }
}

-(void)hideMBProgressHUD
{
    _isShowAlert = NO;
    [MBProgressHUD hideHUDForView:self.view animated:YES];
}



-(void)showAlert:(NSString *)title
{
    if (_isShowAlert) {
        [MBManager showWaitingWithTitle:title inView:self.view];
    }
}

-(void)hideMBManager
{
    _isShowAlert = NO;
    [MBManager hideAlert];
}


/**
 懒加载刷新控件
 
 @return 头部刷新
 */
-(MJRefreshNormalHeader *)kkRefreshHeader
{
    if (!_kkRefreshHeader) {
        
        _kkRefreshHeader = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(addNewData)];
        _kkRefreshHeader.lastUpdatedTimeLabel.hidden = YES;
    }
    
    return _kkRefreshHeader;
}


/**
 懒加载刷新控件
 
 @return 尾部刷新
 */
-(MJRefreshBackNormalFooter *)kkRefreshFooter
{
    if (!_kkRefreshFooter) {
        _kkRefreshFooter = [MJRefreshBackNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(addMoreData)];
    }
    
    return _kkRefreshFooter;
}

- (DIYRefreshAutoFooter *)diyRefreshFooter{
    
    if (!_diyRefreshFooter) {
        _diyRefreshFooter = [DIYRefreshAutoFooter footerWithRefreshingTarget:self refreshingAction:@selector(addMoreData)];
    }
    
    return _diyRefreshFooter;
}

-(void)addNewData
{
    if ([self.kkRefreshFooter isRefreshing]  || [self.diyRefreshFooter isRefreshing])
    {
        [self.kkRefreshHeader endRefreshing];
        [self.diyRefreshFooter endRefreshing];
        return;
    }
    
    self.page = 1;
    
    self.loadingAll = NO;
    
    [self.kkRefreshFooter resetNoMoreData];
    [self.kkRefreshFooter resetNoMoreData];
    [self getData];
}

-(void)addMoreData
{
    if ([self.kkRefreshHeader isRefreshing] || self.loadingAll)
    {
        [self.kkRefreshFooter endRefreshing];
        [self.diyRefreshFooter endRefreshing];
        if (self.loadingAll) {
//            [MBManager showWaitingWithTitle:@"已经到底了" inView:self.view];
            [self.kkRefreshFooter endRefreshingWithNoMoreData];
            [self.diyRefreshFooter endRefreshingWithNoMoreData];
        }
        
        return;
    }
    self.page ++;
    
    [self getData];
}



/**
 网络发生变化是否需要刷新数据
 */
-(void)isShouldRefreshData
{
    
}


-(void)getData
{
    
}


/**
 停止刷新
 */
-(void)endRefreshWithShowMessage:(NSString *)message isNoMoreData:(BOOL)noMoreData
{
    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
    [self.kkRefreshHeader endRefreshing];
    if (noMoreData) {
        [self.kkRefreshFooter endRefreshingWithNoMoreData];
    }else{
        [self.kkRefreshFooter endRefreshing];
    }
    if (message) {
        [MBManager showWaitingWithTitle:message inView:self.view];
    }
    
}
#pragma mark - 用户切换刷新数据
- (void)userSwitch{
    
}

#pragma mark - 获取当前网路状态
- (int)networkStatus
{
    int status = 0;
    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    status = [app.networkReachability intValue];
    return status;
}

#pragma mark - 设置导航透明
-(void)settingNavTransparent
{
    [self.navigationController.navigationBar setTintColor:[UIColor whiteColor]];
    [self.navigationController.navigationBar setTranslucent:true];
    [self.navigationController.navigationBar setBackgroundImage:[[UIImage alloc] init] forBarMetrics:UIBarMetricsDefault];
    [self.navigationController.navigationBar setShadowImage:[[UIImage alloc] init]];
}


#pragma mark - 设置左边的按钮
- (void)setLeftButton:(NSString *)buttonName
{
    UIButton *backBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    
    [backBtn setFrame:CGRectMake(0, 0, 30, 30)];
    
    [backBtn setImageEdgeInsets:UIEdgeInsetsMake(0,0,0, 30 - 13)];
    
    if (buttonName)
    {
        if (![buttonName isEqualToString:@""])
        {
            [backBtn setImage:nil forState:UIControlStateNormal];
            [backBtn setTitle:buttonName forState:UIControlStateNormal];
            [backBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            [backBtn.titleLabel setFont:[UIFont systemFontOfSize:16]];
            
            int a = [buttonName characterAtIndex:1];
            if( a > 0x4e00 && a < 0x9fff)//判断输入的是否是中文
            {
                [backBtn setFrame:CGRectMake(0, 0, 17*[buttonName length], 30)];
            }
            else{
                [backBtn setFrame:CGRectMake(0, 0, 9*[buttonName length], 30)];
            }
            
        }
        
    }
    backBtn.backgroundColor = [UIColor clearColor];
    [backBtn addTarget:self action:@selector(doLeftAction:) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *leftItem = [[UIBarButtonItem alloc] initWithCustomView:backBtn];
    self.navigationItem.leftBarButtonItem = leftItem;
}


#pragma mark - 批量设置右边的按钮(根据图片)
- (void)setRightButton:(NSArray *)buttonImageNames
{
    NSMutableArray *rightItems = [NSMutableArray array];
    for (int i = 0; i < [buttonImageNames count]; i ++) {
        NSString *buttonName = [NSString stringWithFormat:@"%@",[buttonImageNames objectAtIndex:i]];
        UIImage *img = [UIImage imageNamed:buttonName];
        UIButton *rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [rightBtn setFrame:CGRectMake(0, 0, img.size.width+10, 44)];
        [rightBtn setImage:img forState:UIControlStateNormal];
        //rightBtn.themeMap = @{kThemeMapKeyImageName : buttonName};
        [rightBtn addTarget:self action:@selector(doRightAction:) forControlEvents:UIControlEventTouchUpInside];
        [rightBtn setTag:i];
        UIBarButtonItem *rightItem = [[UIBarButtonItem alloc] initWithCustomView:rightBtn];
        [rightItems addObject:rightItem];
    }
    self.navigationItem.rightBarButtonItems = rightItems;
}
#pragma mark - 批量设置右边的按钮(根据名称)
- (void)setRightButtonWithTitle:(NSArray *)titleNames
{
    NSMutableArray *rightItems = [NSMutableArray array];
    for (int i = 0; i < [titleNames count]; i ++)
    {
        NSString *titleName = [NSString stringWithFormat:@"%@",[titleNames objectAtIndex:i]];
        
        UIButton *rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [rightBtn setTag:i];
        [rightBtn setTitle:titleName forState:UIControlStateNormal];
        [rightBtn.titleLabel setFont:[UIFont systemFontOfSize:17]];
        [rightBtn setFrame:CGRectMake(0, 0, 18*[titleName length], 44)];
        [rightBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [rightBtn setBackgroundColor:[UIColor clearColor]];
        [rightBtn addTarget:self action:@selector(doRightAction:) forControlEvents:UIControlEventTouchUpInside];
        
        UIBarButtonItem *rightItem = [[UIBarButtonItem alloc] initWithCustomView:rightBtn];
        [rightItems addObject:rightItem];
    }
    self.navigationItem.rightBarButtonItems = rightItems;
}

#pragma mark - 创建导航栏按钮
-(void)addNavBtnFrame:(CGRect)frame bgImage:(NSString *)bgImageName isSetImage:(BOOL)setImage title:(NSString *)title target:(id)target action:(SEL)action isLeftL:(BOOL)isLeft
{
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    btn.frame = frame;
    
    if (bgImageName)
    {
        if (setImage)
        {
            [btn setImage:[UIImage imageNamed:bgImageName] forState:UIControlStateNormal];
            [btn setImage:[UIImage imageNamed:bgImageName] forState:UIControlStateHighlighted];
        }
        else
        {
            [btn setBackgroundImage:[UIImage imageNamed:bgImageName] forState:UIControlStateNormal];
            [btn setBackgroundImage:[UIImage imageNamed:bgImageName] forState:UIControlStateHighlighted];
        }
        
    }
    
    
    
    if (title)
    {
        [btn setTitle:title forState:UIControlStateNormal];
        
        [btn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    }
    
    if (target && action)
    {
        [btn addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
    }
    
    
    UIBarButtonItem *item = [[UIBarButtonItem alloc]initWithCustomView:btn];
    
    if (isLeft)
    {
        self.navigationItem.leftBarButtonItem = item;
    }
    else
    {
        self.navigationItem.rightBarButtonItem = item;
    }
}

-(void)doLeftAction:(UIButton *)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)doRightAction:(UIButton *)sender
{
    
}



//是否可以旋转
- (BOOL)shouldAutorotate
{
    return YES;
}
//支持的方向
-(UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}



//创建Button的方法
- (UIButton *)createButtonFrame:(CGRect)frame normalImage:(NSString *)normalImage selectImage:(NSString *)selectImage isBackgroundImage:(BOOL)backgroundImage title:(NSString *)title target:(id)target action:(SEL)action
{
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    
    btn.frame = frame;
    
    if (backgroundImage)
    {
        if (normalImage) {
            [btn setBackgroundImage:[UIImage imageNamed:normalImage] forState:UIControlStateNormal];
        }
        
        if (selectImage) {
            [btn setBackgroundImage:[UIImage imageNamed:selectImage] forState:UIControlStateSelected];
        }
        
    }else{
        if (normalImage) {
            [btn setImage:[UIImage imageNamed:normalImage] forState:UIControlStateNormal];
        }
        
        if (selectImage) {
            [btn setImage:[UIImage imageNamed:selectImage] forState:UIControlStateSelected];
        }
    }
    
    if (title)
    {
        [btn setTitle:title forState:UIControlStateNormal];
    }
    
    if (target && action)
    {
        [btn addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
    }
    
    return btn;
}



- (UILabel *)createLabelFrame:(CGRect)frame text:(NSString *)text font:(UIFont *)font textColor:(UIColor*)color textAlignment:(NSTextAlignment)textAlignment
{
    UILabel *label = [[UILabel alloc]initWithFrame:frame];
    label.text = text;
    label.textColor = color;
    label.font = font;
    label.textAlignment = textAlignment;
    return label;
}

- (UILabel *)createMainLabelWithText:(NSString *)text{
    
    UILabel *label = [[UILabel alloc]init];
    if (text) {
        label.text = text;
    }
    label.themeMap = @{kThemeMapKeyColorName : normalText_main_color};
    label.font = threeClassTextFont;
    label.textAlignment = NSTextAlignmentLeft;
    return label;
}


- (void)setViewShadowWithView:(UIView *)view shadowOffset:(CGSize)offset shadowOpacity:(CGFloat)shadowOpacity shadowRadius:(CGFloat)shadowRadius shadowColor:(NSString *)colorName{
    view.layer.shadowOffset = offset;
    view.layer.shadowOpacity = shadowOpacity;
    view.layer.shadowRadius = shadowRadius;
    view.themeMap = @{kThemeViewShadowColor : colorName};
}

- (void)setViewShadowWithView:(UIView *)view{
    
    view.layer.shadowOffset =CGSizeMake(0, 1);
    view.layer.shadowOpacity = 0.8;
    view.layer.shadowRadius = 4;
    view.themeMap = @{kThemeMapKeyColorName : setwhiteColor, kThemeViewShadowColor : line_lightgrey_color};
    
    
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
