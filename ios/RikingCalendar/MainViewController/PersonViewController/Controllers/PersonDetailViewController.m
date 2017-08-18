//
//  PersonDetailViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/2.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "PersonDetailViewController.h"
#import "PerPickImageView.h"
#import "LZPickViewManager.h"
#import "UserModel.h"
#import "PersonMessageEditViewController.h"
#import "AFNWorkingTool.h"
#import "PersonMessageEditViewController.h"
#import "JPullEmailTF.h"

@interface PersonDetailViewController ()

<PerPickViewDelegate>

{
    NSMutableArray *_leftTitleArray;
    
    UIImageView *_userImageView;
    
    UserModel *_userModel;
    
    BOOL _isEdit;//是否编辑了个人信息
}
@end

@implementation PersonDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"个人信息";
    [self initData];
    [self.view addSubview:self.dataTabView];
}

- (void)initData{
    
    
    if (self.type==1) {
       _leftTitleArray = [NSMutableArray arrayWithObjects:@"头像",@"名字",@"公司邮箱",@"部门",@"更多", nil];
        _userModel = [[UserModel alloc]init];
        [_userModel setValuesForKeysWithDictionary:isUser];
    }else{
        _leftTitleArray = [NSMutableArray arrayWithObjects:@"性别",@"地区",@"出生日期",@"个性签名", nil];
        _userModel = self.moreUserModel;
    }
    
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return _leftTitleArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (self.type==1) {
        if (indexPath.row==0) {
            return 80;
        }else{
            return 50;
        }
    }else{
        return 50;
    }
   
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{
    
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    
    UILabel *title = [self createMainLabelWithText:_leftTitleArray[indexPath.row]];
    [cell.contentView addSubview:title];
    [title mas_makeConstraints:^(MASConstraintMaker *make) {
       
        make.top.left.mas_equalTo(cell.contentView).offset(15);
        make.bottom.mas_equalTo(cell.contentView).offset(-15);
        make.width.mas_equalTo(80);
        
    }];
    
    
    
    if (self.type==1) {
        
        if (0<indexPath.row && indexPath.row<_leftTitleArray.count-1) {
            
            UILabel *content = [self createMainLabelWithText:nil];
            content.font = fiveClassTextFont;
            content.themeMap = @{kThemeMapKeyColorName : lightText_color};
            content.textAlignment = NSTextAlignmentRight;
            [cell.contentView addSubview:content];
            
            [content mas_makeConstraints:^(MASConstraintMaker *make) {
                
                make.edges.mas_equalTo(UIEdgeInsetsMake(10, 100, 10, 0));
                
            }];
            
            
            if (indexPath.row==1) {
                content.text = _userModel.name;
            }
            
            if (indexPath.row==2) {
                content.text = _userModel.email;
            }
            
            if (indexPath.row == 3) {
                
                content.text = _userModel.dept;
            }
            
        }
        
        if (indexPath.row == 0) {
            
            _userImageView = [[UIImageView alloc]init];
            _userImageView.layer.cornerRadius = 6;
            _userImageView.layer.borderWidth = 0.5;
            _userImageView.layer.borderColor = dt_textLightgrey_color.CGColor;
            _userImageView.layer.masksToBounds = YES;
            [Utils setImageView:_userImageView imageUrl:_userModel.photoUrl placeholderImage:@""];
            [cell.contentView addSubview:_userImageView];
            
            [_userImageView mas_makeConstraints:^(MASConstraintMaker *make) {
                
                make.top.equalTo(cell.contentView).offset(8);
                make.right.equalTo(cell.contentView).offset(0);
                make.bottom.equalTo(cell.contentView).offset(-8);
                make.width.mas_equalTo(cell.contentView.mj_h-16);
                
            }];
        }
        
        
    }else{
        
        UILabel *content = [self createMainLabelWithText:nil];
        content.font = fiveClassTextFont;
        content.themeMap = @{kThemeMapKeyColorName : lightText_color};
        content.textAlignment = NSTextAlignmentRight;
        [cell.contentView addSubview:content];
        [content mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.edges.mas_equalTo(UIEdgeInsetsMake(10, 100, 10, 0));
            
        }];
        
        if (indexPath.row==0) {
            content.text = _userModel.sex==0?@"女":@"男";
        }else if (indexPath.row==1){
            content.text = _userModel.address;
        }else if (indexPath.row == 2){
            content.text = [Utils transformDate:_userModel.birthday dateFormatStyle:DateFormatYearMonthDayWithChinese];
        }else{
            content.text = _userModel.remark;
        }
        
        
    }
    
    
    
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (self.type==1) {
        
        //头像
        if (indexPath.row==0) {
            int pickID = 2;
            if (self.type==1) {
                pickID = 1;
            }
            //选择头像
            PerPickImageView *pickImage = [[PerPickImageView alloc]initWithId:pickID];
            pickImage.delegate = self;
            [pickImage showInView];
        }
        //昵称/邮箱
        if (indexPath.row==1 || indexPath.row==2 || indexPath.row==3) {
            
            if (indexPath.row==1) {
                [self goToEditViewControllerWithType:1 editMessage:_userModel.name];
                
            }else if(indexPath.row==2){
                [self goToEditViewControllerWithType:2 editMessage:_userModel.email];
                
            }else{
                [self goToEditViewControllerWithType:4 editMessage:_userModel.dept];
                
            }
            
        }
        
        
        //更多
        if (indexPath.row==4) {
            
            PersonDetailViewController *personMore = [PersonDetailViewController new];
            personMore.type = 2;
            personMore.moreUserModel = _userModel;
            personMore.editMoreUserMessage = ^(UserModel *uModel){
              
                _userModel = uModel;
                _isEdit = YES;
            };
            
            [self.navigationController pushViewController:personMore animated:YES];
        }
        
        
    }else{
        
        
        if (indexPath.row==0) {
            
            //选择性别
            PerPickImageView *pickImage = [[PerPickImageView alloc]initWithId:2];
            pickImage.delegate = self;
            [pickImage showInView];
            
        }
        
        if (indexPath.row==1) {
            [self goToEditViewControllerWithType:5 editMessage:_userModel.address];
        }
        
        
        if (indexPath.row==2) {
            
            LZPickViewManager * shareManager = [LZPickViewManager initLZPickerViewManager];
            NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
            [dateFormatter setDateFormat:@"yyyy-MM-dd"];
            
            NSDate *maxDate = [Utils getDataString:@"2099-12-31" formatter:@"yyyy-MM-dd"];
            NSDate *minDate = [Utils getDataString:@"1901-12-31" formatter:@"yyyy-MM-dd"];
            
            //设置最大、最小、当前
            [shareManager setDatePickerWithMaxDate:maxDate withMinDate:minDate withFixedValueDate:nil];
            
            [shareManager setPickViewTopWithLeftButtonColor:nil withRightButtonColor:nil withTitleColor:nil withTopBgColor:dt_text_F8F8F8_color];
            [shareManager setPickViewTopWithLeftButtonString:@"" withRightButtonString:@"" withTitleString:@""];
            [shareManager setPickViewTopWithLeftButtonImage:[UIImage imageNamed:@"chooseTime_cancel"] rightButtonImage:[UIImage imageNamed:@"chooseTime_sure"]];
            
            [shareManager showWithDatePickerMode:UIDatePickerModeDate compltedBlock:^(NSDate *choseDate) {
                
                _userModel.birthday = [Utils transformDate:choseDate dateFormatStyle:DateFormatYearMonthDayHourMinute];
                _isEdit = YES;
                [tableView reloadData];
                
            } cancelBlock:^{
                
            }];
        }
        
        if (indexPath.row==3) {
        
            [self goToEditViewControllerWithType:3 editMessage:_userModel.remark];
            
        }
        
    }
    
}


- (void)goToEditViewControllerWithType:(int)type editMessage:(NSString *)editMessage{
    
    PersonMessageEditViewController *editVC = [[PersonMessageEditViewController alloc]init];
    editVC.editMessage = editMessage;
    editVC.editTpye = type;
    @KKWeak(self);
    editVC.editUserMessage = ^(int type, NSString *message){
        @KKStrong(self);
        if (type==3) {
            _userModel.remark = message;
        }
        
        if (type==5) {
            _userModel.address = message;
        }
        
        if (type==1) {
            _userModel.name = message;
        }
        
        if (type==2) {
            _userModel.email = message;
        }
        
        if (type==4) {
            _userModel.dept = message;
        }
        
        _isEdit = YES;
    
        [self.dataTabView reloadData];
    };
    
    [self.navigationController pushViewController:editVC animated:YES];
    
}


- (void)jumpOtherView:(UIImagePickerController *)object{
    object.allowsEditing = YES;
    // 点击按钮跳转到能够利用的图片来源
    [self presentViewController:object animated:YES completion:nil];
}



#pragma mark - 上传图片
- (void)selectImage:(id)icon{
    if ([icon isKindOfClass:[UIImage class]]) {
        //压缩
        NSData *imageData = UIImageJPEGRepresentation(icon, 0.7);
        _userImageView.image = icon;
        if (self.userImage) {
            self.userImage(icon);
        }
        
        
        NSMutableDictionary *param = [NSMutableDictionary dictionary];
        [param setObject:_userModel.id forKey:@"id"];
        
        NSString *uploadUrl = [NSString stringWithFormat:@"%@%@",ServreUrl,uploadUserImage];
        [[AFNWorkingTool sharedManager] UpLoadImage:uploadUrl parm:param images:[NSMutableArray arrayWithObject:imageData] success:^(NSDictionary *dictData) {
            _isEdit = YES;
            _userModel.photoUrl = dictData[@"_data"];
            [self.dataTabView reloadData];
        } failure:^(NSError *error) {
            RKLog(@"%@",error);
        }];
        
    }else{
        
        NSString *sex = (NSString *)icon;
        if ([sex isEqualToString:@"男"]) {
            _userModel.sex = 1;
        }else{
            _userModel.sex = 0;
        }
        _isEdit = YES;
        [self.dataTabView reloadData];
        
    }
}


- (void)viewWillDisappear:(BOOL)animated {
    NSArray *viewControllers = self.navigationController.viewControllers;//获取当前的视图控制其
    if (viewControllers.count > 1 && [viewControllers objectAtIndex:viewControllers.count-2] == self) {
        //当前视图控制器在栈中，故为push操作
        RKLog(@"push");
    } else if ([viewControllers indexOfObject:self] == NSNotFound) {
        //当前视图控制器不在栈中，故为pop操作
        RKLog(@"pop");
        
        if (self.type==2) {
            
            if (_isEdit) {
                if (self.editMoreUserMessage) {
                    self.editMoreUserMessage(_userModel);
                }
            }
            
        }
        
        if (self.type==1) {
            
            if (_isEdit) {
                NSDictionary *param = [_userModel mj_keyValues];
                [self requestWithHTTPMethod:POST urlString:[NSString stringWithFormat:@"%@%@",ServreUrl,addOrUpdate] parm:param isHaveAlert:NO waitTitle:@"" success:^(id dictData) {
                    //服务器更新成功,再更新本地个人信息
                    [kNSUserDefaults setObject:param forKey:UserKey];
                    [kNSUserDefaults synchronize];
                    
                } failure:^(NSString *message) {
                    
                }];
            }
            
        }
    }
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
