//
//  Utils.h
//  KakaPersonal
//
//  Created by Aaron on 2017/3/14.
//  Copyright © 2017年 kakayun. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef NS_ENUM(NSUInteger, DateFormatStyle) {
    DateFormatYearMonthDayWithChinese  = 0,
    DateFormatYearMonthWithChinese,
    DateFormatMonthDayWithChinese,
    DateFormatHourMinuteWith24HR,
    DateFormatYearMonthDay,
    DateFormatYearMonthDayHourMintesecond,
    DateFormatYearMonthDayHourMintesecondMillisecond,
    DateFormatYearMonthDayWithChineseAndHourMinute24HR,
    DateFormatYearMonthDayHourMinute,
    DateFormatYearMonthDayHourMinute1,
    DateFormatHourMinute01,
    DateFormatYearMonthDay01
};


typedef NS_ENUM(NSUInteger, SaveDateFormatStyle) {
    SaveDateFormatYearMonthDayHourMintesecondMillisecond = 0,
    SaveDateFormatYearMonthDayHourMintesecond,
    SaveDateFormatYearMonthDay,
    SaveDateFormatYearMonth,
    SaveDateFormatMonthDay,
    SaveDateFormatHourMinuteSecond
};

@interface Utils : NSObject

//获取Utils唯一对象
+ (Utils *)shareInstance;


/**
 *  获取App当前版本号
 *
 *  @return app版本号
 */
+(NSString *)getAppVersion;

#pragma mark - 获取当前时间
+(NSString*)getCurrentTime;

+(NSString*)getCurrentTimeWithTimeFormat:(NSString *)format;

+(NSString*)getCurrentTimeWithDay;

#pragma mark - 时间字符串转成日期
/**
 时间字符串转成日期
 */
+ (NSDate *)getDataString:(NSString*)dataString formatter:(NSString *)formatter;


/**
 将某个时间转化成时间戳
 */
+(NSInteger)timeSwitchTimestamp:(NSString *)formatTime andFormatter:(NSString *)format;

#pragma mark - 将某个时间戳转化成时间字符串

/**
 将某个时间戳转化成时间字符串

 @param timestamp 时间戳
 @param format 时间格式
 @return 时间字符串
 */
+(NSString *)timestampSwitchTime:(NSInteger)timestamp andFormatter:(NSString *)format;

/**
 获取时间戳
 */
+(NSInteger)getNowTimestamp;

/**
 *  判断当前时间是否处于某个时间段内
 *
 *  @param startTime        开始时间
 *  @param expireTime       结束时间
 */
+ (BOOL)validateWithStartTime:(NSString *)startTime withExpireTime:(NSString *)expireTime;

#pragma mark - 把时间转换成时间字符串
+ (NSString *)transformDateWithFormatter:(NSString *)formatter date:(NSDate *)date;


/**
 将时间或者时间字符串转换成相应的格式
 */
+ (NSString *)transformDate:(id)date dateFormatStyle:(DateFormatStyle)dateStyle;


/**
 将本地当前时间转换成相应的格式

 */
+ (NSString *)getCurrentTimeWithDateStyle:(DateFormatStyle)dateStyle;

#pragma mark - 获取网络时间,并传入所需的时间格式
//+(NSString *)getInternetDateWithFormat:(NSString *)format;

/**
 利用SDImageCache进行图片异步下载
 @param imageview ImageView
 @param imageurl url
 */
+(void)setImageView:(UIImageView *)imageview imageUrl:(NSString *)imageurl placeholderImage:(NSString *)placeholder;

//判断输入框输入两位小数
+ (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string;

#pragma mark - 动态计算label的高度或者宽度
+ (CGSize)setWidthForText:(NSString*)text fontSize:(CGFloat)fontSize labelSize:(CGFloat)labelSize isGetHight:(BOOL)isHight;

+ (NSDictionary *)openServiceMessageWithTitleOpenType:(NSString *)openType;

+ (NSString *)getMessageError:(NSError *)error;

+ (NSString *)setOldStringTime:(NSString *)strTime inputFormat:(NSString *)inputFormat outputFormat:(NSString *)outputFormat;


/**
 快速创建Button
 */
+ (UIButton *)createButtonFrame:(CGRect)frame normalImage:(NSString *)normalImage selectImage:(NSString *)selectImage isBackgroundImage:(BOOL)backgroundImage title:(NSString *)title target:(id)target action:(SEL)action;

#pragma mark - 自适应label
+ (CGSize)sizeWithString:(NSString *)string font:(UIFont *)font;

#pragma mark - 快速创建Label

/**
 快速创建Label

 @param frame frame description
 @param text text description
 @param font font description
 @param color color description
 @param textAlignment textAlignment description
 @return label
 */
+ (UILabel *)createLabelFrame:(CGRect)frame text:(NSString *)text font:(CGFloat)font textColor:(UIColor*)color textAlignment:(NSTextAlignment)textAlignment;


/**
 获取图片的主色调

 @param image image description
 @return return value description
 */
+ (UIColor*)mostColor:(UIImage*)image;


/**
 显示简短提示语

 */
+ (void)showMsg:(NSString *)msg;


/**
 判断字符串不为空
 */
+ (BOOL)isBlankString:(NSString *)string;


+ (NSString *)distanceWithBeforeTime:(NSString *)date;



#pragma mark - 根据时间字符串获取周几
/**
 根据时间字符串获取周几(周一,周二....)
 @param dateStr dateStr description
 @param formatter formatter description
 @return return value description
 */
+ (NSString *)getWeekDayWithDateStr:(NSString *)dateStr formatter:(NSString *)formatter;

#pragma mark - 获取周几(1,2,3,4,5,6,7)

/**
 获取周几(1,2,3,4,5,6,7)
 */
+ (NSString*)weekdayStringFromDate:(NSString*)inputDateStr formatter:(NSString *)formatter;


/**
 根据Date获取

 @param date  获取周几(1,2,3,4,5,6,7)
 */
+(NSString *)getWeekDayWithDate:(NSDate *)date;


+ (NSString *)convertToJsonData:(NSDictionary *)dict;


/**
 解析json
 */
+ (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString;


/**
 判断手机号码是否有效
 */
+ (BOOL)isNumText:(NSString *)str;
@end
