//
//  GlobalDefine.h
//  NewNcsPro
//
//  Created by K.E. on 14-7-21.
//  Copyright (c) 2014年 Lynn. All rights reserved.

//  常用宏定义

#ifndef yuancheng_GlobalDefine_h
#define yuancheng_GlobalDefine_h



//appdelegate
#define YMDAPPDELEGATE              (AppDelegate *)[[UIApplication sharedApplication] delegate]
#define YMDUSERDEFAULT              [NSUserDefaults standardUserDefaults]


#define UserId                      [YMDUSERDEFAULT objectForKey:@"userMessage"][@"UserId"]


//IOS版本
#define MLIOS_VERSION               [[[UIDevice currentDevice] systemVersion] floatValue]

//是否是iOS 7 及 以上版本
#define iOS_Version_7               [[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0

//是不是iOS 9.0的版本
#define iOS_Version_9               [[[UIDevice currentDevice] systemVersion] floatValue]>=9.0

//是否是 iPhone5 屏幕尺寸
#define IS_IPHONE_5                 ( fabs( ( double )[ [ UIScreen mainScreen ] bounds ].size.height - ( double )568 ) < DBL_EPSILON )

//判断手机设备
#define IsIPhone4                   [[Utils getCurrentDeviceModel] isEqualToString:@"iPhone4"]
#define IsIPhone4s                  [[Utils getCurrentDeviceModel] isEqualToString:@"iPhone4S"]
#define IsITouch4                   [[Utils getCurrentDeviceModel] isEqualToString:@"iPodTouch4G"]
#define IsIPhone5                   [[Utils getCurrentDeviceModel] isEqualToString:@"iPhone5"]
#define IsIPhone5c                  [[Utils getCurrentDeviceModel] isEqualToString:@"iPhone5c"]
#define IsIPhone5s                  [[Utils getCurrentDeviceModel] isEqualToString:@"iPhone5s"]
#define IsITouch5                   [[Utils getCurrentDeviceModel] isEqualToString:@"iPodTouch5G"]
#define IsIPhone6                   [[Utils getCurrentDeviceModel] isEqualToString:@"iPhone6"]
#define IsIPhone6p                  [[Utils getCurrentDeviceModel] isEqualToString:@"iPhone6Plus"]
#define IsIPhone6s                  [[Utils getCurrentDeviceModel] isEqualToString:@"iPhone6s"]
#define IsIPhone6sp                 [[Utils getCurrentDeviceModel] isEqualToString:@"iPhone6sPlus"]
#define IsIPhoneSimulator           [[DBManagerAndOther getCurrentDeviceModel] isEqualToString:@"iPhoneSimulator"]









#define KString(s)                          [NSString stringWithFormat:s,s]


//判断是NSString 返回 该String 否则返回 @""
#define kIsString(str) [str isKindOfClass:[NSString class]] ? str : @""


#define kIsNULL(str) ([str isEqual:[NSNull null]] || (str==nil)) ? @"" : str


#define BTNWIDTH [UIScreen mainScreen].bounds.size.width / titleArr.count

//对象 retainCount 清零
#define SAFE_RELEASE(obj) if(obj) { [obj release]; obj=nil; }

//根据RGB 获取自定义颜色
#define RGBCOLOR(r,g,b) [UIColor colorWithRed:(r)/255.0f green:(g)/255.0f blue:(b)/255.0f alpha:1]

//按钮的tag值
#define BtnTag 10000

//textFiled textView的tag值
#define textTag 20000

//tableview 的tag值
#define tableViewTag 40000




//删除小数点后多余的零
#define kDeleteZero(floatNum) [Utils deleteFloatAfterSurplusZero:floatNum]

#define KKWeak(o) autoreleasepool{} __weak typeof(o) o##Weak = o;

#define KKStrong(o) autoreleasepool{} __strong typeof(o) o = o##Weak;

//获取ROOT 屏幕Size
#define  kScreenSize   [[UIScreen mainScreen] bounds].size

#define iPhone4 ([UIScreen mainScreen].bounds.size.height == 480)
#define iPhone5 ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(640, 1136), [[UIScreen mainScreen] currentMode].size) : NO)
#define iPhone6 ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? (CGSizeEqualToSize(CGSizeMake(750, 1334), [[UIScreen mainScreen] currentMode].size)) : NO)
#define iPhone6plus ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? (CGSizeEqualToSize(CGSizeMake(1125, 2001), [[UIScreen mainScreen] currentMode].size) || CGSizeEqualToSize(CGSizeMake(1242, 2208), [[UIScreen mainScreen] currentMode].size)) : NO)


#define kScreenHeight [UIScreen mainScreen].bounds.size.height
#define kScreenWidth [UIScreen mainScreen].bounds.size.width


//看UI是针对哪个屏适配就取哪个屏
#define kWidth(R) (R)*(kScreenWidth)/375.0
//这里的320我是针对5s为标准适配的,如果需要其他标准可以修改
#define kHeight(R) (iPhone4?(R:((R)*(kScreenHeight)/667.0))
//这里的568我是针对5s为标准适配的,如果需要其他标准可以修改 代码简单我就不介绍了, 以此思想,我们可以对字体下手
#define font(R) (R)*(kScreenWidth)/375.0 //这里是6s屏幕字体

//获取ROOT 屏幕Frame
#define  kScreenFrame  [UIScreen mainScreen].applicationFrame
//获取中间内容高度（除去状态栏，UITabBar，UINavigationBar）
#define  kContentHeight [[UIScreen mainScreen] bounds].size.height-44-49-20


/**
 *  打印宏
 */
#ifdef DEBUG
#define RKLog(format, ...) do {\
fprintf(stderr, "<%s : %d> %s\n",\
[[[NSString stringWithUTF8String:__FILE__] lastPathComponent] UTF8String],  \
__LINE__, __func__);                                                        \
(NSLog)((format), ##__VA_ARGS__);                                           \
fprintf(stderr, "\n");\
} while (0)
#else
#define YMDLog(...)
#endif

#ifdef DEBUG

//A simple wrapper for `NSLog()` that is automatically removed from release builds.
#define DLOG(...)   NSLog(__VA_ARGS__)

//More detailed loogging. Logs the function name and line number after the log message.
#define DLOGEXT(fmt, ...) NSLog((@"%s [Line %d] " fmt), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__);

//Logs a methodx call's class and selector.
#define DLOGCALL DLOG(@"[%@ %@]", NSStringFromClass([self class]), NSStringFromSelector(_cmd))

// Printf method
#define DLOGMETHOD	NSLog(@"[%s] %@", class_getName([self class]), NSStringFromSelector(_cmd));

// Printf point ,include x and y
#define DLOGPOINT(p)	NSLog(@"%f,%f", p.x, p.y);

// Printf size ,include wide and height
#define DLOGSIZE(p)	NSLog(@"%f,%f", p.width, p.height);

// Printf size ,include  origin.x,origin.y,size.width and size.height
#define DLOGRECT(p)	NSLog(@"%f,%f %f,%f", p.origin.x, p.origin.y, p.size.width, p.size.height);

#else
#define DLOG(...) /* */
#define DLOGEXT(...) /* */
#define DLOGCALL /* */
#define DLOGMETHOD /* */
#define DLOGPOINT(p) /* */
#define DLOGSIZE(p) /* */
#define DLOGRECT(p) /* */
#endif


#endif
