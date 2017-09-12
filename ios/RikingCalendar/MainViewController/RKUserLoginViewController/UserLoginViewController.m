//
//  UserLoginViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/4.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "UserLoginViewController.h"
#import "LoginModel.h"
#import "MJExtension.h"
#import "WebViewController.h"
@interface UserLoginViewController ()

<UITextViewDelegate,UITextFieldDelegate>

{
    NSTimer *_codeTimer;

    int _second;
}
@property (weak, nonatomic) IBOutlet UIButton *sureRegisterBtn;
@property (weak, nonatomic) IBOutlet UIButton *getCodeBtn;
@property (weak, nonatomic) IBOutlet UIButton *loginBtn;
@property (weak, nonatomic) IBOutlet UITextField *iphoneNumberTextFiled;
@property (weak, nonatomic) IBOutlet UITextField *codeTextFiled;

@property (weak, nonatomic) IBOutlet UIButton *loginLogo;





@property (nonatomic,strong) LoginModel *loginModel;





- (IBAction)coloseLogin:(id)sender;

- (IBAction)loginAction:(id)sender;
- (IBAction)lookRegisterProtocols:(id)sender;
- (IBAction)isSureRegister:(id)sender;
- (IBAction)getCode:(id)sender;
@end

@implementation UserLoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.title = @"登录";
    self.view.backgroundColor = [UIColor whiteColor];
    self.iphoneNumberTextFiled.delegate = self;
    [self.iphoneNumberTextFiled setKeyboardType:UIKeyboardTypeNumberPad];
    self.iphoneNumberTextFiled.clearButtonMode=UITextFieldViewModeWhileEditing;
    self.iphoneNumberTextFiled.borderStyle =UITextBorderStyleNone;
    [self.iphoneNumberTextFiled addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    
    
    self.codeTextFiled.borderStyle = UITextBorderStyleNone;
    [self.codeTextFiled setKeyboardType:UIKeyboardTypeNumberPad];
    self.codeTextFiled.clearButtonMode=UITextFieldViewModeWhileEditing;
    self.codeTextFiled.delegate = self;
    [self.codeTextFiled addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
    
    
    self.loginBtn.layer.cornerRadius = 3;
    self.loginBtn.layer.masksToBounds = YES;
    [self.loginBtn setBackgroundImage:[UIImage cf_imageWithColor:dt_line_color] forState:UIControlStateNormal];
    self.loginBtn.userInteractionEnabled = NO;
    
    self.getCodeBtn.layer.cornerRadius = 3;
    self.getCodeBtn.layer.masksToBounds = YES;
    [self.getCodeBtn setBackgroundImage:[UIImage cf_imageWithColor:dt_line_color] forState:UIControlStateNormal];
    [self.getCodeBtn setTitle:@"发送验证码" forState:UIControlStateNormal];

    
    [self.sureRegisterBtn setImage:[UIImage imageNamed:@"gtask_unfinish"] forState:UIControlStateNormal];
    [self.sureRegisterBtn setImage:[UIImage imageNamed:@"gtaskFinsh"] forState:UIControlStateSelected];
    
    self.loginLogo.layer.borderWidth = 0.5;
    self.loginLogo.layer.borderColor = dt_line_color.CGColor;
    self.loginLogo.layer.cornerRadius = 13;
    self.loginLogo.layer.masksToBounds = YES;
    
    
    [self.getCodeBtn setBackgroundImage:[UIImage cf_imageWithColor:dt_app_main_color] forState:UIControlStateNormal];
    _second = 60;
}

- (LoginModel *)loginModel{
    if (!_loginModel) {
        _loginModel = [LoginModel new];
    }
    
    return _loginModel;
}

- (void)textFieldDidChange:(UITextField *)textFiled{
    
    if (![Utils isBlankString:self.iphoneNumberTextFiled.text] && ![Utils isBlankString:self.codeTextFiled.text]) {
    
        self.loginBtn.userInteractionEnabled = YES;
        [self.loginBtn setBackgroundImage:[UIImage cf_imageWithColor:dt_app_main_color] forState:UIControlStateNormal];
    }else{
        self.loginBtn.userInteractionEnabled = NO;
        [self.loginBtn setBackgroundImage:[UIImage cf_imageWithColor:dt_line_color] forState:UIControlStateNormal];
    }
    
    if (self.iphoneNumberTextFiled.text.length>11) {
        self.iphoneNumberTextFiled.text = [self.iphoneNumberTextFiled.text substringToIndex:11];
    }
    
    if (self.codeTextFiled.text.length>6) {
        self.codeTextFiled.text = [self.codeTextFiled.text substringToIndex:6];
    }
}








#pragma mark - 获取验证码
- (IBAction)getCode:(id)sender {
    
    
    if ([Utils isBlankString:self.iphoneNumberTextFiled.text]) {
        [Utils showMsg:@"请输入手机号码"];
        return;
    }
    
    if (![Utils isNumText:self.iphoneNumberTextFiled.text]) {
        [Utils showMsg:@"请输入正确的手机号"];
        return;
    }
    
    self.getCodeBtn.userInteractionEnabled = NO;
    [self.getCodeBtn setTitle:@"发送中..." forState:UIControlStateNormal];
    
    NSString *url = [NSString stringWithFormat:@"%@%@",ServreUrl,getValiCode];
    
    NSMutableDictionary *parm = [NSMutableDictionary dictionary];
    
    [parm setObject:self.iphoneNumberTextFiled.text forKey:@"telephone"];
    
    [self kkRequestWithHTTPMethod:POST urlString:url parm:parm success:^(NSDictionary *dictData) {
        
        _codeTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(doVaildButton) userInfo:nil repeats:YES];
        [self.getCodeBtn setBackgroundImage:[UIImage cf_imageWithColor:dt_line_color] forState:UIControlStateNormal];
        
    } failure:^(NSError *error) {
        
        [self.getCodeBtn setTitle:@"重新获取" forState:UIControlStateNormal];
        [self.getCodeBtn setBackgroundImage:[UIImage cf_imageWithColor:dt_app_main_color] forState:UIControlStateNormal];
        self.getCodeBtn.userInteractionEnabled = YES;
        
    }];
    
    
    
   
    
}

- (IBAction)coloseLogin:(id)sender {
    
    [self.iphoneNumberTextFiled resignFirstResponder];
    [self.codeTextFiled resignFirstResponder];
    
    [self dismissViewControllerAnimated:YES completion:^{
        
        
    }];
    
}


#pragma mark - 登录


- (IBAction)loginAction:(id)sender {
    
    if ([Utils isBlankString:self.iphoneNumberTextFiled.text] || [Utils isBlankString:self.codeTextFiled.text]) {
        
        [Utils showMsg:@"手机号码或验证码不能为空"];
        return;
    }
    
    
    
    [self.iphoneNumberTextFiled resignFirstResponder];
    [self.codeTextFiled resignFirstResponder];
    
    NSString *ckickCode = [NSString stringWithFormat:@"%@%@",ServreUrl,checkValiCode];
    NSMutableDictionary *param = [NSMutableDictionary dictionary]; 
    [param setValue:self.codeTextFiled.text forKey:@"valiCode"];
    [param setValue:self.iphoneNumberTextFiled.text forKey:@"telephone"];
    [param setValue:@"1" forKey:@"PhoneType"];
    if ([kNSUserDefaults objectForKey:@"deviceCode"]) {
        [param setValue:[kNSUserDefaults objectForKey:@"deviceCode"] forKey:@"phoneSeqNum"];
    }
    [self requestWithHTTPMethod:POST urlString:ckickCode parm:param isHaveAlert:YES waitTitle:@"正在登录..." success:^(id dictData) {
        
        [MBManager showWaitingWithTitle:@"正在登录..."];
        //撤销所有本地(未登录产生的所有通知)
        [[LocalNotificationManager shareManager] cancelAllLocalNotifications];
        //保存用户信息
        NSUserDefaults *userDefaut = kNSUserDefaults;
        [userDefaut setObject:dictData forKey:@"User"];
        [userDefaut synchronize];
        
        //更新本地数据库,主要是提醒和待办
        @KKWeak(self);
        [[RemindAndGtasksDBManager shareManager] instantiationLoactionDBWithSynchronousAllSuccess:^(BOOL ret) {
            @KKStrong(self);
            
            [MBManager hideAlert];
            //发送通知,刷新本地数据
            postNotificationName(kUserSwitchNotificationName);
            [self dismissViewControllerAnimated:YES completion:^{
                
                
            }];
            
        }];
        
    
    } failure:^(NSString *message) {
        
    }];
    
}

- (IBAction)lookRegisterProtocols:(id)sender {
    
    RKLog(@"查看注册协议");
    
    [self requestWithHTTPMethod:POST urlString:requestUrl(userRegisterAgree) parm:nil showWaitAlertTitile:nil isAfterDelay:NO success:^(id dictData) {
        
        WebViewController *webVC = [[WebViewController alloc]init];
        
        webVC.htmlUrl = (NSString *)dictData;
        
        [self.navigationController pushViewController:webVC animated:YES];
        
        
    } failure:^(NSString *message) {
        
    }];
    
    
    
}

- (IBAction)isSureRegister:(id)sender {
    UIButton *btn = (UIButton *)sender;
    btn.selected = !btn.selected;
    self.loginBtn.enabled = btn.selected;
    self.loginBtn.userInteractionEnabled = btn.selected;
}


#pragma mark - 验证码倒计时
- (void)doVaildButton{
    
    _second --;
    [self.getCodeBtn setTitle:[NSString stringWithFormat:@"%ds后重新发送",_second] forState:UIControlStateNormal];
    if (_second == 0)
    {
        [self invalidateTimer];
        
    }
    
}

- (void)invalidateTimer {
    if (_codeTimer) {
        [_codeTimer invalidate];
        self.getCodeBtn.userInteractionEnabled = YES;
        [self.getCodeBtn setTitle:@"重新获取" forState:UIControlStateNormal];
        [self.getCodeBtn setBackgroundImage:[UIImage cf_imageWithColor:dt_app_main_color] forState:UIControlStateNormal];
        _second = 60;
    }
    
}

- (BOOL)fd_prefersNavigationBarHidden{
    return YES;
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
@end
