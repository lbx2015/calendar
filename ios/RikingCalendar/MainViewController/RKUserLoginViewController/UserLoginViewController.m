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
@property (nonatomic,strong) LoginModel *loginModel;

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
    
    self.iphoneNumberTextFiled.delegate = self;
    [self.iphoneNumberTextFiled setKeyboardType:UIKeyboardTypeNumberPad];
    self.iphoneNumberTextFiled.clearButtonMode=UITextFieldViewModeWhileEditing;
    self.iphoneNumberTextFiled.borderStyle =UITextBorderStyleNone;
    
    self.codeTextFiled.borderStyle = UITextBorderStyleNone;
    [self.codeTextFiled setKeyboardType:UIKeyboardTypeNumberPad];
    self.codeTextFiled.clearButtonMode=UITextFieldViewModeWhileEditing;
    
    self.loginBtn.layer.cornerRadius = 6;
    self.loginBtn.layer.masksToBounds = YES;
    [self.loginBtn setBackgroundImage:[UIImage cf_imageWithColor:dt_app_main_color] forState:UIControlStateNormal];
    self.loginBtn.enabled = NO;
    self.loginBtn.userInteractionEnabled = NO;
    
    self.getCodeBtn.layer.cornerRadius = 6;
    self.getCodeBtn.layer.masksToBounds = YES;
    [self.getCodeBtn setBackgroundImage:[UIImage cf_imageWithColor:dt_app_main_color] forState:UIControlStateNormal];
    [self.getCodeBtn setTitle:@"获取验证码" forState:UIControlStateNormal];

    
    [self.sureRegisterBtn setImage:[UIImage imageNamed:@"gtask_unfinish"] forState:UIControlStateNormal];
    [self.sureRegisterBtn setImage:[UIImage imageNamed:@"gtaskFinsh"] forState:UIControlStateSelected];
    
    
    
    _second = 60;
}

- (LoginModel *)loginModel{
    if (!_loginModel) {
        _loginModel = [LoginModel new];
    }
    
    return _loginModel;
}

- (void)textViewDidChange:(UITextView *)textView{
    
    
}







#pragma mark - 获取验证码
- (IBAction)getCode:(id)sender {
    
    
    if ([Utils isBlankString:self.iphoneNumberTextFiled.text]) {
        
        [Utils showMsg:@"请输入手机号码"];
        return;
    }
    
    self.getCodeBtn.enabled = NO;
    [self.getCodeBtn setTitle:@"发送中..." forState:UIControlStateNormal];
    
    NSString *url = [NSString stringWithFormat:@"%@%@",ServreUrl,getValiCode];
    
    NSMutableDictionary *parm = [NSMutableDictionary dictionary];
    
    [parm setObject:self.iphoneNumberTextFiled.text forKey:@"telephone"];
    
    [self kkRequestWithHTTPMethod:POST urlString:url parm:parm success:^(NSDictionary *dictData) {
        
        _codeTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(doVaildButton) userInfo:nil repeats:YES];
        
    } failure:^(NSError *error) {
        
        [self.getCodeBtn setTitle:@"重新获取" forState:UIControlStateNormal];
        self.getCodeBtn.enabled = NO;
        
    }];
    
    
    
   
    
}


#pragma mark - 登录
- (IBAction)loginAction:(id)sender {
    
    
    if ([Utils isBlankString:self.iphoneNumberTextFiled.text] || [Utils isBlankString:self.codeTextFiled.text]) {
        
        [Utils showMsg:@"手机号码或验证码不能为空"];
        return;
    }
    
    NSString *ckickCode = [NSString stringWithFormat:@"%@%@",ServreUrl,checkValiCode];
    NSMutableDictionary *param = [NSMutableDictionary dictionary];
    [param setValue:self.codeTextFiled.text forKey:@"valiCode"];
    [param setValue:self.iphoneNumberTextFiled.text forKey:@"telephone"];
    if ([kNSUserDefaults objectForKey:@"deviceCode"]) {
        [param setValue:[kNSUserDefaults objectForKey:@"deviceCode"] forKey:@"phoneSeqNum"];
    }
    [self requestWithHTTPMethod:POST urlString:ckickCode parm:param isHaveAlert:YES waitTitle:nil success:^(id dictData) {
        
        //保存用户信息
        NSUserDefaults *userDefaut = kNSUserDefaults;
        [userDefaut setObject:dictData forKey:@"User"];
        [userDefaut synchronize];
        
        //更新数据库路径
        [[LKDBTool shareInstance] changeDBWithDirectoryName:nil];
        
        //发送通知,刷新本地数据
        postNotificationName(kUserSwitchNotificationName);
        [self.navigationController popViewControllerAnimated:YES];

        
    } failure:^(NSString *message) {
        
    }];
    
}

- (IBAction)lookRegisterProtocols:(id)sender {
    
    RKLog(@"查看注册协议");
    
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
    [self.getCodeBtn setTitle:[NSString stringWithFormat:@"已发送(%ds)",_second] forState:UIControlStateNormal];
    if (_second == 0)
    {
        [self invalidateTimer];
        
    }
    
}

- (void)invalidateTimer {
    if (_codeTimer) {
        [_codeTimer invalidate];
        [self.getCodeBtn setEnabled:YES];
        [self.getCodeBtn setTitle:@"重新获取" forState:UIControlStateNormal];
        _second = 60;
    }
    
}





- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
@end
