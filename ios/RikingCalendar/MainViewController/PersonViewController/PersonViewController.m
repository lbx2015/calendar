//
//  PersonViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/12.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "PersonViewController.h"
#import "PersonDetailViewController.h"
#import "AppSettingViewController.h"
#import "UserLoginViewController.h"
#import "WebViewController.h"
#import "PersonMessageEditViewController.h"
@interface PersonViewController ()

{
    UIImageView *_imageView;
    UserModel *_uModel;
}
@property (nonatomic,strong)NSMutableArray *mineArray;
@property (nonatomic,strong)NSArray *imageArray;
@end


@implementation PersonViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self initData];
    [self refreshUserMessage];
    [self.view addSubview:self.dataTabView];
    if ([ self.dataTabView respondsToSelector:@selector(setSeparatorInset:)]) {
        [self.dataTabView   setSeparatorInset:UIEdgeInsetsMake(0, 59, 0, 0)];
    }
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(refreshUserMessage) name:@"userMessage" object:nil];
}


- (void)initData{
    
    NSArray *titleArray = @[@[@""],@[NSLocalizedString(@"settings", nil),NSLocalizedString(@"comment", nil)],@[NSLocalizedString(@"about", nil)]];
    self.imageArray = @[@[@""],@[@"mine_steup_icon",@"mine_goodcomment_icon"],@[@"mine_about_icon"]];
    self.mineArray = [NSMutableArray arrayWithArray:titleArray];
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
}

#pragma mark - 刷新个人信息
- (void)refreshUserMessage{
    //显示个人信息
    if (isUser) {
        if (!_uModel) {
            _uModel = [[UserModel alloc]init];
        }
        [_uModel setValuesForKeysWithDictionary:isUser];
        [self.dataTabView reloadData];
    }
}

#pragma mark - 切换用户
- (void)userSwitch{
    [self refreshUserMessage];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return self.mineArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return [self.mineArray[section] count];
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section==0) {
        return 87;
    }else{
        return 50;
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 15;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{
    
    UIView *view = cell.contentView;
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    if (indexPath.section==0) {
        _imageView = [[UIImageView alloc]init];
        _imageView.layer.cornerRadius = 6;
        _imageView.layer.borderWidth = 0.5;
        _imageView.layer.borderColor = dt_textLightgrey_color.CGColor;
        _imageView.layer.masksToBounds = YES;
        [Utils setImageView:_imageView imageUrl:@"" placeholderImage:@"default_user_image"];
        [cell.contentView addSubview:_imageView];
        
        UILabel *userName = [self createMainLabelWithText:@""];
        [cell.contentView addSubview:userName];
        
        UILabel *signature = [self createMainLabelWithText:@""];
        signature.font = sevenClassTextFont;
        [cell.contentView addSubview:signature];
        
        if (isUser) {
            [Utils setImageView:_imageView imageUrl:_uModel.photoUrl placeholderImage:@"default_user_image"];
            userName.text = _uModel.name;
            signature.text = _uModel.remark;
            
            if ([Utils isBlankString:_uModel.remark]) {
                //"Click_edit_character_signature"    = "点击编辑个性签名";
                signature.text = NSLocalizedString(@"Click_edit_character_signature", nil);
                signature.userInteractionEnabled = YES;
                [signature addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(editRemark)]];
            }
            
        }else{
            //"Click_login"                       = "点击登录";
            userName.text = NSLocalizedString(@"Click_login", nil);
            signature.text = @"";
        }
        
//        __weak typeof(self) weakSelf = self;
        [_imageView mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.top.equalTo(cell.contentView).offset(10);
            make.bottom.equalTo(cell.contentView).offset(-10);
            make.left.equalTo(cell.contentView).offset(15);
            make.width.mas_equalTo(67);
            
        }];
        
        [userName mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.top.equalTo(cell.contentView).offset(22);
            make.left.equalTo(_imageView.mas_right).offset(15);
            make.height.mas_equalTo(17);
            make.right.equalTo(cell.contentView).offset(25);
            
        }];
//
        [signature mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.left.equalTo(_imageView.mas_right).offset(15);
            make.bottom.equalTo(cell.contentView).offset(-22);
            make.right.equalTo(cell.contentView).offset(25);
            make.height.mas_equalTo(17);
            
        }];
    }
    else{
        
        NSString *title = self.mineArray[indexPath.section][indexPath.row];
        
        NSString *imageName = self.imageArray[indexPath.section][indexPath.row];
        
        UIButton *btn = [self createButtonFrame:CGRectMake(0, 0, 20, 30) normalImage:imageName selectImage:nil isBackgroundImage:NO title:nil target:nil action:nil];
        
        [cell.contentView addSubview:btn];
        
        UILabel *label = [self createMainLabelWithText:title];
        [cell.contentView addSubview:label];
        
        
        [btn mas_makeConstraints:^(MASConstraintMaker *make) {
           
            make.top.bottom.equalTo(view).offset(0);
            make.width.mas_equalTo(29);
            make.left.equalTo(view).offset(15);
        }];
        
        
        [label mas_makeConstraints:^(MASConstraintMaker *make) {
           
            make.edges.equalTo(view).mas_offset(UIEdgeInsetsMake(0, 59, 0, 25));
            
        }];
        
    }
    
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    //用户信息
    if (indexPath.section==0) {
        
        if (isUser) {
            PersonDetailViewController *detailVC = [PersonDetailViewController new];
            detailVC.type=1;
            detailVC.userImage = ^(UIImage *userImage){
                
                _imageView.image = userImage;
            };
            detailVC.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:detailVC animated:YES];
        }else{
            UserLoginViewController *login = [[UserLoginViewController alloc]initWithNibName:@"UserLoginViewController" bundle:[NSBundle mainBundle]];
            UINavigationController *nav = [[UINavigationController alloc]initWithRootViewController:login];
            
            [self presentViewController:nav animated:YES completion:^{
                
            }];
        }
        
    
    }else if (indexPath.section==1){
        
        //设置
        if (indexPath.row == 0) {
            
            AppSettingViewController *setVC = [AppSettingViewController new];
            setVC.userOutLogin = ^(){
                [tableView reloadData];
            };
            setVC.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:setVC animated:YES];
        }
    }else{
        

        //关于
        [self requestWithHTTPMethod:POST urlString:[NSString stringWithFormat:@"%@?versionNumber=%@",requestUrl(aboutUrl),[Utils getAppVersion]] parm:nil isHaveAlert:NO waitTitle:nil success:^(id dictData) {
            
            WebViewController *webVC = [[WebViewController alloc]init];
            webVC.htmlUrl = (NSString *)dictData;
            webVC.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:webVC animated:YES];
            
        } failure:^(NSString *message) {
            
        }];
        
        
        
    }
}

#pragma mark - 编辑个性签名
- (void)editRemark{
    
    PersonMessageEditViewController *editVC = [[PersonMessageEditViewController alloc]init];
    editVC.editTpye = 3;
    @KKWeak(self);
    editVC.editUserMessage = ^(int type, NSString *message){
        @KKStrong(self);
        
        if (![_uModel.remark isEqualToString:message]) {
            
            _uModel.remark = message;
            NSDictionary *param = [_uModel mj_keyValues];
            [kNSUserDefaults setObject:param forKey:UserKey];
            [kNSUserDefaults synchronize];
            [self requestWithHTTPMethod:POST urlString:[NSString stringWithFormat:@"%@%@",ServreUrl,addOrUpdate] parm:param isHaveAlert:NO waitTitle:@"" success:^(id dictData) {
                
            } failure:^(NSString *message) {
                
            }];
            [self.dataTabView reloadData];
        }
        
    };
    editVC.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:editVC animated:YES];
    
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
