//
//  PersonMessageEditViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/15.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "PersonMessageEditViewController.h"
#import "JPullEmailTF.h"
typedef void(^completeEdit)(int type,NSString *userMessage);

@interface PersonMessageEditViewController ()

<UITextViewDelegate,UITextFieldDelegate>
{
    
    UIView *_textViewBg;
    
    UITextField *_editTxetView;
    
    JPullEmailTF *_textField;
}

@property (nonatomic,copy)completeEdit completeEdit;

@end

@implementation PersonMessageEditViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setRightButtonWithTitle:@[@"完成"]];
    
    _textViewBg = [[UIView alloc]init];
    
    [self setViewShadowWithView:_textViewBg];
    [self.view addSubview:_textViewBg];
    
    @KKWeak(self);
    [_textViewBg mas_makeConstraints:^(MASConstraintMaker *make) {
        @KKStrong(self);
        
        make.top.mas_equalTo(self.view).offset(20);
        make.left.mas_equalTo(self.view).offset(0);
        make.right.mas_equalTo(self.view).offset(0);
        make.height.mas_equalTo(50);
        
    }];

    if (self.editTpye==2) {
        self.title = @"邮箱";
        _textField = [[JPullEmailTF alloc] initWithFrame:CGRectMake(10, 20, kScreenWidth-20, 50) InView:self.view];
        _textField.placeholder = @"请输入邮箱";
        _textField.backgroundColor = [UIColor whiteColor];
        _textField.clearButtonMode = UITextFieldViewModeWhileEditing;
        if (![Utils isBlankString:self.editMessage]) {
            _textField.text = self.editMessage;
        }
        //自定义
        _textField.mailCellHeight = 40;
        _textField.mailListHeight = 40*4;
        _textField.mailFont = [UIFont systemFontOfSize:16];
        _textField.MailFontColor = dt_text_main_color;
        _textField.mailCellColor = [UIColor whiteColor];
        _textField.mailBgColor =[UIColor whiteColor];
        _textField.mLeftMargin = 10;
        _textField.separatorInsets = @[@1,@0,@0,@0];
        [self.view addSubview:_textField];
        
        
        [_textField mas_makeConstraints:^(MASConstraintMaker *make) {
            @KKStrong(self);
            make.top.equalTo(self.view).offset(20);
            make.left.equalTo(self.view).offset(10);
            make.right.equalTo(self.view).offset(-10);
            make.height.mas_equalTo(50);
            
        }];
    }else{
        
        _editTxetView = [[UITextField alloc]init];
        _editTxetView.clearButtonMode=UITextFieldViewModeWhileEditing;
        [_editTxetView addTarget:self action:@selector(textViewDidChange:) forControlEvents:UIControlEventEditingChanged];
        _editTxetView.placeholder = @"请输入昵称";
        _editTxetView.font = threeClassTextFont;
        _editTxetView.delegate = self;
        [_textViewBg addSubview:_editTxetView];
        
        [_editTxetView mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.edges.mas_equalTo(UIEdgeInsetsMake(5, 5, 5, 5));
            
        }];
        
        if (![Utils isBlankString:self.editMessage]) {
            _editTxetView.text = self.editMessage;
        }
        
        if (self.editTpye == 1) {
            _editTxetView.placeholder = @"请输入昵称";
            self.title = @"昵称";
        }
        
        if (self.editTpye==3) {
            _editTxetView.placeholder = @"请输入个性签名";
            self.title = @"个性签名";
        }
        
        if (self.editTpye==4) {
            _editTxetView.placeholder = @"请输入部门名称";
            self.title = @"部门";
        }
        
        if (self.editTpye ==5) {
            _editTxetView.placeholder = @"请输入地区";
            self.title = @"地区";
        }
    }
    
}



- (void)completeAction{
    
    
    
}

- (void)doRightAction:(UIButton *)sender{
    
    if (self.editUserMessage) {
        
        if (self.editTpye==2) {
            self.editUserMessage(self.editTpye, _textField.text);
        }else{
           self.editUserMessage(self.editTpye, _editTxetView.text);
        }
    }
    
    
    [self.navigationController popViewControllerAnimated:YES];
}


- (void)textViewDidChange:(UITextField *)textView
{
    if (textView.text.length>20) {
        
        textView.text  = [textView.text substringToIndex:20];
    }
//    CGSize size = [textView sizeThatFits:CGSizeMake(CGRectGetWidth(textView.frame), MAXFLOAT)];
//    CGRect textViewbgFrame = _textViewBg.frame;
//    textViewbgFrame.size.height = size.height+10;
//    
//    [_textViewBg mas_updateConstraints:^(MASConstraintMaker *make) {
//        make.height.mas_equalTo(textViewbgFrame.size.height);
//    }];
    
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
