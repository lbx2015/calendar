//
//  RKBaseViewController.h
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/12.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YPTabBarController.h"
#import "MJRefresh.h"
#import "RKBaseScrollview.h"
@interface RKBaseViewController : YPTabBarController

@property (nonatomic,strong) MJRefreshNormalHeader *kkRefreshHeader;

@property (nonatomic,strong) MJRefreshBackNormalFooter *kkRefreshFooter;

@property (nonatomic,assign) NSInteger page;

@property (nonatomic,assign) BOOL loadingAll;//加载全部了
/**
 设置导航透明
 */
-(void)settingNavTransparent;


/**
 创建导航左边按钮
 */
- (void)setLeftButton:(NSString *)buttonName;

/**
 创建
 
 @param buttonImageNames <#buttonImageNames description#>
 */
- (void)setRightButton:(NSArray *)buttonImageNames;


#pragma mark - 获取当前网路状态
/**
 获取当前网路状态
 */
- (int)networkStatus;


#pragma mark - 网络发生变化是否需要刷新数据
/**
 网络发生变化是否需要刷新数据
 */
-(void)isShouldRefreshData;

/**
 创建导航右边按钮
 
 @param titleNames 按钮名称
 */
- (void)setRightButtonWithTitle:(NSArray *)titleNames;

- (void)doRightAction:(UIButton *)sender;

- (void)doLeftAction:(UIButton *)sender;




#pragma mark - 创建导航栏按钮
/**
 *  创建导航栏按钮
 *
 *  @param frame       位置
 *  @param bgImageName 背景
 *  @param title       名称
 *  @param target      target
 *  @param action      action
 *  @param isLeft      左边/右边
 */
-(void)addNavBtnFrame:(CGRect)frame bgImage:(NSString *)bgImageName isSetImage:(BOOL)setImage title:(NSString *)title target:(id)target action:(SEL)action isLeftL:(BOOL)isLeft;


/**
 快速创建Button
 */
- (UIButton *)createButtonFrame:(CGRect)frame normalImage:(NSString *)normalImage selectImage:(NSString *)selectImage isBackgroundImage:(BOOL)backgroundImage title:(NSString *)title target:(id)target action:(SEL)action;


/**
 快速创建Laber
 */
- (UILabel *)createLabelFrame:(CGRect)frame text:(NSString *)text font:(UIFont *)font textColor:(UIColor*)color textAlignment:(NSTextAlignment)textAlignment;

/**
 网络请求数据
 
 @param method 请求方式
 @param urlString 地址
 @param parm 参数
 @param success 成功回调
 @param failue 失败回调
 */
-(void)kkRequestWithHTTPMethod:(KKHTTPMethod)method urlString:(NSString *)urlString parm:(NSDictionary *)parm success:(void(^)(NSDictionary *dictData))success failure:(void (^)(NSError *error))failue;


/**
 网络请求数据(数据已经处理过,返回Code=200的值)
 */
-(void)requestWithHTTPMethod:(KKHTTPMethod)method urlString:(NSString *)urlString parm:(NSDictionary *)parm showWaitAlertTitile:(NSString *)title isAfterDelay:(BOOL)afterDelay success:(void(^)(id dictData))success failure:(void (^)(NSString *message))failue;


/**
 网络请求数据(数据已经处理过,返回Code=200的值),自动添加网络请求等待
 */
-(void)requestWithHTTPMethod:(KKHTTPMethod)method urlString:(NSString *)urlString parm:(NSDictionary *)parm isHaveAlert:(BOOL)alert waitTitle:(NSString *)waitTitle success:(void(^)(id dictData))success failure:(void (^)(NSString *message))failue;

/**
 请求数据
 */
-(void)getData;

/**
 停止刷新
 */
-(void)endRefreshWithShowMessage:(NSString *)message isNoMoreData:(BOOL)noMoreData;

@end
