//
//  WebViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/15.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "WebViewController.h"
#import <MessageUI/MessageUI.h>
@interface WebViewController ()
<UIWebViewDelegate,MFMessageComposeViewControllerDelegate>
{
    UIWebView *_webView;
    UIAlertView* _alert;
}
@end


@implementation WebViewController

- (void)viewDidLoad {
    
    [super viewDidLoad];
    
    [self createWebView];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    if ([self.navigationController respondsToSelector:@selector(interactivePopGestureRecognizer)]) {
        self.navigationController.interactivePopGestureRecognizer.enabled = NO;
    }
}
-(void)createWebView
{
    _webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, 0, kScreenWidth,kScreenHeight-64)];
    NSURLRequest* request = [NSURLRequest requestWithURL:[NSURL URLWithString:self.Url]];
    [_webView loadRequest:request];
    _webView.delegate = self;
    [self.view addSubview:_webView];
}

- (void)webViewDidFinishLoad:(UIWebView *)webView
{
    NSString *title = [webView stringByEvaluatingJavaScriptFromString:@"document.title"];
    
    self.title = title;
    
}

#pragma mark- 拦截URL
- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    NSString* urlString = [NSString stringWithFormat:@"%@",request.URL];
    if ([urlString rangeOfString:@"jtype=1"].location != NSNotFound)//jtype：1代表产品，2代表商家店铺 3.活动分享 4.短信提醒
    {
        if (urlString.length > 5)
        {
            NSString* productID = [self subString:urlString];//从指定位置开始截取到最后包括最后
            
            [self gotoViewControllerWithID:[NSNumber numberWithInt:[productID intValue]] andType:1];
        }
        
        
        return NO;
    }
    
    if ([urlString rangeOfString:@"jtype=2"].location != NSNotFound)
    {
        if (urlString.length > 5)
        {
            NSString* merchantID = [self subString:urlString];//从指定位置开始截取到最后包括最后
            
            if ([urlString rangeOfString:@"ShowType=2"].location != NSNotFound)
            {
                [self gotoViewControllerWithID:[NSNumber numberWithInt:[merchantID intValue]] andType:3];
            }else
            {
                
                [self gotoViewControllerWithID:[NSNumber numberWithInt:[merchantID intValue]] andType:2];
            }
        }
        
        return NO;
    }
    
    //活动分享增加BannerId
    if ([urlString rangeOfString:@"jtype=3"].location != NSNotFound)//jtype：1代表产品，2代表商家店铺 3.活动分享 4.短信提醒
    {
        if (urlString.length > 5)
        {
            //            NSString* productID = [self subString:urlString];//从指定位置开始截取到最后包括最后
            
            int bannerId = 0;//默认是0
            
            if ([urlString rangeOfString:@"bannerid"].location != NSNotFound)
            {
                
                NSString* bannerIdString = [urlString substringFromIndex:[urlString rangeOfString:@"bannerid"].location+9];
                
                bannerId = [bannerIdString intValue];
                
            }
         
        }
        
        
        return NO;
    }
    
    
    if ([urlString rangeOfString:@"jtype=4"].location != NSNotFound)//jtype：1代表产品，2代表商家店铺 3.活动分享 4.短信提醒
    {
        if (urlString.length > 5)
        {
            NSString* phoneNumberString = [self getPhoneNumber:urlString];
            NSString* contentString = [self getContentString:urlString];//从指定位置开始截取到最后包括最后
            [self sendMsg:phoneNumberString andContent:contentString];
            
        }
        return NO;
    }
    
    return YES;
}

#pragma mark - 截取
- (NSString*)subString:(NSString*)StrignID
{
    //http://h5.test.yunmendian.com/test/Jump.html?jtype=2&id=574
    
    
    NSString* subString = @"";
    
    if ([StrignID rangeOfString:@"&"].location != NSNotFound)
    {
        
        subString = [StrignID substringFromIndex:[StrignID rangeOfString:@"&"].location + 4];
        
    }
    return subString;
}

#pragma mark - 获取电话号码和发送内容
- (NSString*)getPhoneNumber:(NSString*)rowString
{
    NSString* subString = @"";
    
    if (rowString.length>0)
    {
        if ([rowString rangeOfString:@"&uphone="].location != NSNotFound)
        {
            
            subString = [rowString substringWithRange:NSMakeRange([rowString rangeOfString:@"'"].location+1, 12)];
        }
        
        return subString;
    }else
    {
        return @"";
    }
}

#pragma mark - 获取电话号码和发送内容
- (NSString*)getContentString:(NSString*)rowString
{
    NSString* subString = @"";
    
    if (rowString.length>0)
    {
        if ([rowString rangeOfString:@"&msg="].location != NSNotFound)
        {
            NSString* removeLastChar = [rowString substringToIndex:rowString.length-1];
            
            subString = [removeLastChar substringFromIndex:[rowString rangeOfString:@"&msg="].location+6];
            
            NSString* uncoderString = [subString stringByReplacingOccurrencesOfString:@"%5Cu" withString:@"\\u"];
            
            
            subString = [self replaceUnicode:uncoderString];
            
        }
        
        return subString;
    }else
    {
        return @"";
    }
}

- (NSString *)replaceUnicode:(NSString *)unicodeStr

{
    
    NSString *tempStr1 = [unicodeStr stringByReplacingOccurrencesOfString:@"\\u"withString:@"\\U"];
    
    NSString *tempStr2 = [tempStr1 stringByReplacingOccurrencesOfString:@"\""withString:@"\\\""];
    
    NSString *tempStr3 = [[@"\""stringByAppendingString:tempStr2] stringByAppendingString:@"\""];
    
    //%5Cu
    
    NSData *tempData = [tempStr3 dataUsingEncoding:NSUTF8StringEncoding];
    
//    NSString* returnStr = [NSPropertyListSerialization propertyListFromData:tempData
//                           
//                                                           mutabilityOption:NSPropertyListImmutable
//                           
//                                                                     format:NULL
//                           
//                                                           errorDescription:NULL];
    
    
    NSString *returnStr = [NSPropertyListSerialization propertyListWithData:tempData options:NSPropertyListImmutable format:NULL error:NULL];
    
    NSLog(@"%@",returnStr);
    
    return [returnStr stringByReplacingOccurrencesOfString:@"\\r\\n"withString:@"\n"];
    
}

#pragma mark  - 根据ID和类型跳转
- (void)gotoViewControllerWithID:(NSNumber*)ID andType:(int)type
{
    
    
}



#pragma mark 请求分享内容
- (void)shareContentRequest:(int)bannerId
{
    
}





#pragma mark - 短信提醒
- (void)sendMsg:(NSString*)phoneNumber andContent:(NSString*)contentString
{
    
    [self showMessageView:@[phoneNumber] body:contentString];
    
}

#pragma mark - 发送消息 可以群发
- (void)showMessageView:(NSArray*)phones body:(NSString*)body
{
    if ([MFMessageComposeViewController canSendText])
    {
        //        NSString* phoneNumber = [NSString stringWithFormat:@"sms://%@",phones.firstObject];
        //
        //        [[UIApplication sharedApplication]openURL:[NSURL URLWithString:phoneNumber]];
        
        MFMessageComposeViewController* mController = [[MFMessageComposeViewController alloc]init];
        mController.recipients = phones;
        mController.body = body;
        mController.messageComposeDelegate = self;
        [self presentViewController:mController animated:NO completion:nil];
    }else
    {
        
        if (!_alert)
        {
            _alert = [[UIAlertView alloc]initWithTitle:[NSString stringWithFormat:@"%@",@"提示信息"] message:@"该设备不支持短信功能" delegate:self cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        }
        
        [_alert show];
    }
}

- (void)messageComposeViewController:(MFMessageComposeViewController *)controller didFinishWithResult:(MessageComposeResult)result
{
    [self dismissViewControllerAnimated:NO completion:nil];
    
    switch (result)
    {
        case MessageComposeResultSent:
        {
            [Utils showMsg:@"消息发送成功!"];
            
        }break;
            
        case MessageComposeResultFailed:
        {
            [Utils showMsg:@"消息发送失败!"];
            
        }break;
            
        case MessageComposeResultCancelled:
        {
            [Utils showMsg:@"取消消息发送!"];
            
        }break;
            
        default:
            break;
    }
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
