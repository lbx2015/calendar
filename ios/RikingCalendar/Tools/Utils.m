//
//  Utils.m
//  KakaPersonal
//
//  Created by Aaron on 2017/3/14.
//  Copyright © 2017年 kakayun. All rights reserved.
//

#import "Utils.h"
#import "SDImageCache.h"
#import "UIImageView+WebCache.h"


static Utils *instance = nil;
@implementation Utils

+ (Utils *)shareInstance
{
    static dispatch_once_t oneToken;
    dispatch_once(&oneToken,^{
        instance = [[self alloc] init];
        
    });
    return instance;
}

#pragma mark - 获取App版本号
+(NSString *)getAppVersion
{
    NSString *app_Version = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"];

    return app_Version;
}

#pragma mark - 获取当前时间
+(NSString*)getCurrentTime
{
    NSDateFormatter * formatter = [[NSDateFormatter alloc ] init];
    [formatter setDateFormat:@"yyyy.MM.dd HH:mm"];
    NSString *date = [formatter stringFromDate:[NSDate date]];
    NSString *currentdate = [NSString stringWithFormat:@"%@", date];
    return currentdate;
}

+(NSString*)getCurrentTimeWithDay
{
    NSDateFormatter * formatter = [[NSDateFormatter alloc ] init];
    [formatter setDateFormat:@"yyyy-MM-dd"];
    NSString *date = [formatter stringFromDate:[NSDate date]];
    NSString *currentdate = [NSString stringWithFormat:@"%@", date];
    return currentdate;
}

#pragma mark - 获取当前时间,传入所需的时间格式
+(NSString*)getCurrentTimeWithTimeFormat:(NSString *)format
{
    NSDateFormatter * formatter = [[NSDateFormatter alloc ] init];
    [formatter setDateFormat:format];
    NSString *date = [formatter stringFromDate:[NSDate date]];
    NSString *currentdate = [NSString stringWithFormat:@"%@", date];
    return currentdate;
}

#pragma mark - 获取当前时间的 时间戳
+(NSInteger)getNowTimestamp{
    
    NSDate *datenow = [NSDate date];//现在时间
    //时间转时间戳的方法:
    NSInteger timeSp = [[NSNumber numberWithDouble:[datenow timeIntervalSince1970]] integerValue];
    
    return timeSp;
    
}

#pragma mark - 将时间字符串转化成时间戳
+(NSInteger)timeSwitchTimestamp:(NSString *)formatTime andFormatter:(NSString *)format{
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    
    [formatter setTimeStyle:NSDateFormatterShortStyle];
    
    [formatter setDateFormat:format]; //(@"YYYY-MM-dd hh:mm:ss") ----------设置你想要的格式,hh与HH的区别:分别表示12小时制,24小时制

    NSTimeZone* timeZone = [NSTimeZone systemTimeZone];
    
    [formatter setTimeZone:timeZone];
    
    NSDate* date = [formatter dateFromString:formatTime]; //------------将字符串按formatter转成nsdate
    //时间转时间戳的方法:
    NSInteger timeSp = [[NSNumber numberWithDouble:[date timeIntervalSince1970]] integerValue];

    return timeSp;
    
}

#pragma mark - 将某个时间戳转化成时间字符串
+(NSString *)timestampSwitchTime:(NSInteger)timestamp andFormatter:(NSString *)format{

    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    
    [formatter setTimeStyle:NSDateFormatterShortStyle];
    
    [formatter setDateFormat:format]; // （@"YYYY-MM-dd hh:mm:ss"）----------设置你想要的格式,hh与HH的区别:分别表示12小时制,24小时制
    
    NSTimeZone *timeZone = [NSTimeZone systemTimeZone];
    
    [formatter setTimeZone:timeZone];
    
    NSDate *confromTimesp = [NSDate dateWithTimeIntervalSince1970:timestamp];
    
    NSString *confromTimespStr = [formatter stringFromDate:confromTimesp];
    
    return confromTimespStr;
    
}


/**
 *  判断当前时间是否处于某个时间段内
 *
 *  @param startTime        开始时间
 *  @param expireTime       结束时间
 */

+ (BOOL)validateWithStartTime:(NSString *)startTime withExpireTime:(NSString *)expireTime {
    
    NSDate *today = [self worldTimeToChinaTime:[NSDate date]];
    NSDate *start = [self getDataString:startTime];
    NSDate *expire = [self getDataString:expireTime];
    
    if ([today compare:start] == NSOrderedDescending && [today compare:expire] == NSOrderedAscending) {
        return YES;
    }
    return NO;
}

#pragma mark - 时间字符串转成日期
+ (NSDate *)getDataString:(NSString*)dataString
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    NSDate *date = [dateFormatter dateFromString:dataString];

    return [self worldTimeToChinaTime:date];
}

//将世界时间转化为中国区时间
+ (NSDate *)worldTimeToChinaTime:(NSDate *)date
{
    NSTimeZone *timeZone = [NSTimeZone systemTimeZone];
    NSInteger interval = [timeZone secondsFromGMTForDate:date];
    NSDate *localeDate = [date  dateByAddingTimeInterval:interval];
    return localeDate;
}


//#pragma mark - 获取网络当前时间
//+ (NSDate *)getInternetDate
//{
//    NSString *urlString = @"http://m.baidu.com";
//    
//    urlString = [urlString stringByAddingPercentEscapesUsingEncoding: NSUTF8StringEncoding];
//    
//    // 实例化NSMutableURLRequest，并进行参数配置
//    
//    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
//    
//    [request setURL:[NSURL URLWithString: urlString]];
//    
//    [request setCachePolicy:NSURLRequestReloadIgnoringCacheData];
//    
//    [request setTimeoutInterval: 2];
//    
//    [request setHTTPShouldHandleCookies:FALSE];
//    
//    [request setHTTPMethod:@"GET"];
//    
//    NSError *error = nil;
//    
//    NSHTTPURLResponse *response;
//    
//    [NSURLConnection sendSynchronousRequest:request
//     
//                          returningResponse:&response error:&error];
//    
//    // 处理返回的数据
//    
//    //    NSString *strReturn = [[NSString alloc] initWithData:returnData encoding:NSUTF8StringEncoding];
//    
//    if (error) {
//        return [NSDate date];
//    }
//    
//    NSLog(@"response is %@",response);
//    
//    NSString *date = [[response allHeaderFields] objectForKey:@"Date"];
//    
//    date = [date substringFromIndex:5];
//    
//    date = [date substringToIndex:[date length]-4];
//    
//    NSDateFormatter *dMatter = [[NSDateFormatter alloc] init];
//    
//    dMatter.locale = [[NSLocale alloc] initWithLocaleIdentifier:@"en_US"];
//    
//    [dMatter setDateFormat:@"dd MMM yyyy HH:mm:ss"];
//
//    NSDate *netDate = [[dMatter dateFromString:date] dateByAddingTimeInterval:60*60*8];
//    
//    return netDate;
//    
//}

//#pragma mark - 获取网络时间,并传入所需的时间格式
//+(NSString *)getInternetDateWithFormat:(NSString *)format
//{
//    NSDateFormatter * formatter = [[NSDateFormatter alloc ] init];
//    [formatter setDateFormat:format];
//    NSString *date = [formatter stringFromDate:[self getInternetDate]];
//    NSString *currentdate = [NSString stringWithFormat:@"%@", date];
//    return currentdate;
//}


/**
 利用SDImageCache进行图片异步下载

 @param imageview imageview
 @param imageurl imageurl
 */
+(void)setImageView:(UIImageView *)imageview imageUrl:(NSString *)imageurl
{
    
    [imageview sd_setImageWithURL:[NSURL URLWithString:imageurl] placeholderImage:[UIImage imageNamed:@""] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
        
        
        
    }];
    
}

+ (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    if ([string isEqualToString:@"\n"]) {
        return NO;
    }
    
    
    if ([string isEqualToString:@""]) {
        return YES;
    }
    
    
    NSMutableString * futureString = [NSMutableString stringWithString:textField.text];
    
    [futureString  insertString:string atIndex:range.location];
    
    
    if (textField.tag == BtnTag) {
        return YES;
    }
    
    
    //第一次点击不能出现小数点
    if (textField.text.length == 0) {
        if ([string isEqualToString:@"."]) {
            return NO;
        }
    }
    
    //点击小数点，判断是否有小数点
    NSRange rangeSearchPoint = [textField.text rangeOfString:@"."];
    if (rangeSearchPoint.location != NSNotFound) {
        if ([string isEqualToString:@"."]) {
            return NO;
        }
    }
    
    //点击0后面必须跟小数点
    if ([textField.text isEqualToString:@"0"]) {
        if ([string isEqualToString:@"."]) {
            return YES;
        } else {
            return NO;
            
        }
    }
    
    
    NSInteger flag=0;
    
    const NSInteger limited = 2;//小数点后需要限制的个数
    int nlen=(int)[futureString length]-1;
    
    for (int i =nlen; i>=0; i--) {
        
        
        if ([futureString characterAtIndex:i] =='.') {
            
            if (flag > limited) {
                
                return NO;
            }
            break;
        }
        
        flag++;
    }
    
    
    return YES;

    
}







#pragma mark - 动态计算label的高度或者宽度
+ (CGSize)setWidthForText:(NSString*)text fontSize:(CGFloat)fontSize labelSize:(CGFloat)labelSize isGetHight:(BOOL)isHight
{
    NSDictionary *parameterDict = @{NSFontAttributeName:[UIFont systemFontOfSize:fontSize]};
    
    CGSize titleSize  = CGSizeMake(0, 0);
    
    if (isHight)
    {
        titleSize = [text boundingRectWithSize:CGSizeMake(labelSize,CGFLOAT_MAX) options:NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingTruncatesLastVisibleLine|NSStringDrawingUsesFontLeading attributes:parameterDict context:nil].size;
    }
    else
    {
        titleSize = [text boundingRectWithSize:CGSizeMake(CGFLOAT_MAX,labelSize) options:NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingTruncatesLastVisibleLine|NSStringDrawingUsesFontLeading attributes:parameterDict context:nil].size;
    }
    
    return titleSize;
}

+(NSString *)getMessageError:(NSError *)error
{
    NSString *errorMessage = @"你的网络不给力,请稍后再试";
    
    if (error.userInfo[@"NSLocalizedDescription"])
    {
        errorMessage = error.userInfo[@"NSLocalizedDescription"];
        
        if ( error.code == -1001)
        {
            errorMessage = @"请求超时";
        }
        
        if (error.code == -1011 || error.code == -1003)
        {
            errorMessage = @"你的网络不给力,请稍后再试";
        }
        
    }
    
    return errorMessage;
}





//创建Button的方法
+ (UIButton *)createButtonFrame:(CGRect)frame normalImage:(NSString *)normalImage selectImage:(NSString *)selectImage isBackgroundImage:(BOOL)backgroundImage title:(NSString *)title target:(id)target action:(SEL)action
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
        
        [btn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    }
    
    if (target && action)
    {
        [btn addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
    }
    
    return btn;
}

+(UIButton *)createButtonFrame:(CGRect)frame imageWithColor:(UIColor *)color  title:(NSString *)title target:(id)target action:(SEL)action
{
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    
    btn.frame = frame;
    
    [btn setBackgroundImage:[UIImage cf_imageWithColor:color] forState:UIControlStateNormal];
    
    if (title)
    {
        [btn setTitle:title forState:UIControlStateNormal];
        
        [btn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    }
    
    if (target && action)
    {
        [btn addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
    }
    return btn;
}

#pragma mark - 快速创建Label
+(UILabel *)createLabelFrame:(CGRect)frame text:(NSString *)text font:(UIFont *)font textColor:(UIColor*)color textAlignment:(NSTextAlignment)textAlignment
{
    UILabel *label = [[UILabel alloc]initWithFrame:frame];
    label.text = text;
    label.textColor = color;
    label.font = font;
    label.textAlignment = textAlignment;
    return label;
}


//根据图片获取图片的主色调
+(UIColor*)mostColor:(UIImage*)image{
    
#if __IPHONE_OS_VERSION_MAX_ALLOWED > __IPHONE_6_1
    int bitmapInfo = kCGBitmapByteOrderDefault | kCGImageAlphaPremultipliedLast;
#else
    int bitmapInfo = kCGImageAlphaPremultipliedLast;
#endif
    //第一步 先把图片缩小 加快计算速度. 但越小结果误差可能越大
    CGSize thumbSize=CGSizeMake(image.size.width, image.size.height);
    
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    CGContextRef context = CGBitmapContextCreate(NULL,
                                                 thumbSize.width,
                                                 thumbSize.height,
                                                 8,//bits per component
                                                 thumbSize.width*4,
                                                 colorSpace,
                                                 bitmapInfo);
    
    CGRect drawRect = CGRectMake(0, 0, thumbSize.width, thumbSize.height);
    CGContextDrawImage(context, drawRect, image.CGImage);
    CGColorSpaceRelease(colorSpace);
    
    //第二步 取每个点的像素值
    unsigned char* data = CGBitmapContextGetData (context);
    if (data == NULL) return nil;
    NSCountedSet *cls=[NSCountedSet setWithCapacity:thumbSize.width*thumbSize.height];
    
    for (int x=0; x<thumbSize.width; x++) {
        for (int y=0; y<thumbSize.height; y++) {
            int offset = 4*(x*y);
            int red = data[offset];
            int green = data[offset+1];
            int blue = data[offset+2];
            int alpha =  data[offset+3];
            if (alpha>0) {//去除透明
                if (red==255&&green==255&&blue==255) {//去除白色
                }else{
                    NSArray *clr=@[@(red),@(green),@(blue),@(alpha)];
                    [cls addObject:clr];
                }
                
            }
        }
    }
    CGContextRelease(context);
    //第三步 找到出现次数最多的那个颜色
    NSEnumerator *enumerator = [cls objectEnumerator];
    NSArray *curColor = nil;
    NSArray *MaxColor=nil;
    NSUInteger MaxCount=0;
    while ( (curColor = [enumerator nextObject]) != nil )
    {
        NSUInteger tmpCount = [cls countForObject:curColor];
        if ( tmpCount < MaxCount ) continue;
        MaxCount=tmpCount;
        MaxColor=curColor;
        
    }

    return [UIColor colorWithRed:([MaxColor[0] intValue]/255.0f) green:([MaxColor[1] intValue]/255.0f) blue:([MaxColor[2] intValue]/255.0f) alpha:([MaxColor[3] intValue]/255.0f)];
}



#pragma mark - 自适应label
+ (CGSize)sizeWithString:(NSString *)string font:(UIFont *)font
{
    CGRect rect = [string boundingRectWithSize:CGSizeMake(kScreenWidth, 8000)//限制最大的宽度和高度
                                       options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading  |NSStringDrawingUsesLineFragmentOrigin//采用换行模式
                                    attributes:@{NSFontAttributeName: font}//传人的字体字典
                                       context:nil];
    
    return rect.size;
}


#pragma mark - 开启本地服务提示信息
+(NSDictionary *)openServiceMessageWithTitleOpenType:(NSString *)openType
{
    NSDictionary *dict = nil;
    
    if ([openType isEqualToString:@"Loaction"])
    {
        dict = [NSDictionary dictionaryWithObjectsAndKeys:@"请进入系统 设置 > 隐私 > 定位服务,允许＂App＂获取你的位置",@"message",@"获取位置失败",@"title", nil];
    }
    else if ([openType isEqualToString:@"Camera"])
    {
        dict = [NSDictionary dictionaryWithObjectsAndKeys:@"请进入系统 设置 > 隐私 > 相机,允许＂App＂访问你的相机",@"message",@"访问相机失败",@"title", nil];
    }
    else if ([openType isEqualToString:@"Photo"])
    {
        dict = [NSDictionary dictionaryWithObjectsAndKeys:@"请进入系统 设置 > 隐私 > 相机,允许＂App＂访问你的照片",@"message",@"访问本地相册失败",@"title", nil];
    }
    else if ([openType isEqualToString:@"Notice"])
    {
        dict = [NSDictionary dictionaryWithObjectsAndKeys:@"请进入系统 设置 > 通知 >＂App＂,允许通知",@"message",@"通知关闭会收不到付款信息",@"title", nil];
    } else if ([openType isEqualToString:@"AddressBook"])
    {
        dict = [NSDictionary dictionaryWithObjectsAndKeys:@"请进入系统 设置 > 隐私 > 通讯录,允许＂App＂访问你的通讯录",@"message",@"访问通讯录失败",@"title", nil];
    }
    
    return dict;
}


@end
