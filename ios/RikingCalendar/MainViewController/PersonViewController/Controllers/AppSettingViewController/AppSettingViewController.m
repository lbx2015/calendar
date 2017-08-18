//
//  AppSettingViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/4.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "AppSettingViewController.h"
#import "SDImageCache.h"
#import "LZPickViewManager.h"
#import "PerPickImageView.h"


@interface AppSettingViewController ()


<UIActionSheetDelegate,PerPickViewDelegate>
{
    NSArray *_titleArray;
    
    UIActionSheet *_actionSheet;
    
    
    NSString *_allDayRemindTime;//全天提醒时间
    
    NSString *_weekDay;//周首日;
}

@end

@implementation AppSettingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"设置";
    
    _titleArray = @[@[@"周首日",@"全天事件提醒时间"],@[@"手机号"],@[@"清理缓存"],@[@"退出登录"]];
    [self.view addSubview:self.dataTabView];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    
    if (isUser) {
        return _titleArray.count;
    }
    else{
        return _titleArray.count-1;
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return [_titleArray[section] count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 15;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 50;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    
    if (indexPath.section<_titleArray.count-1) {
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        UILabel *label = [self createMainLabelWithText:_titleArray[indexPath.section][indexPath.row]];
        [cell.contentView addSubview:label];
        
        [label mas_makeConstraints:^(MASConstraintMaker *make) {
           
            make.top.bottom.equalTo(cell.contentView).offset(0);
            make.left.equalTo(cell.contentView).offset(15);
            make.width.mas_equalTo(140);
            
        }];
        
        UILabel *connetLabel =[self createMainLabelWithText:@""];
        connetLabel.font = fiveClassTextFont;
        connetLabel.themeMap = @{kThemeMapKeyColorName : lightText_color};
        connetLabel.textAlignment = NSTextAlignmentRight;
        [cell.contentView addSubview:connetLabel];
        
        [connetLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            
            make.edges.mas_equalTo(UIEdgeInsetsMake(10, 100, 10, 0));
            
        }];
        
        
        if (indexPath.section==0) {
            
            if ([kNSUserDefaults objectForKey:@"weekDay"]) {
                NSString *weekDay = [kNSUserDefaults objectForKey:@"weekDay"];
                
                if ([weekDay isEqualToString:@"1"]) {
                    connetLabel.text = @"周日";
                }else{
                    connetLabel.text = @"周一";
                }
                
            }else{
                connetLabel.text = @"周日";
            }
            
            if (indexPath.row==1) {
                
                if (isAllDayRemindTime) {
                    _allDayRemindTime = [kNSUserDefaults objectForKey:allDayRemindTimeKey];
                    connetLabel.text = _allDayRemindTime;
                    
                }else{
                    connetLabel.text = @"08:00";
                    [kNSUserDefaults setObject:@"08:00" forKey:allDayRemindTimeKey];
                    [kNSUserDefaults synchronize];
                }
            }
            
        }
        
        if (indexPath.section==1 && indexPath.row==0) {
            
            if (isUser) {
                connetLabel.text = isUser[@"telephone"];
            }else{
                connetLabel.text = @"未绑定";
            }
        }
        
        if (indexPath.section==2) {
            
            
            CGFloat cachSize = [self getCachSize];
            
            if (cachSize>0) {
                connetLabel.text = [NSString stringWithFormat:@"%.2fM",[self getCachSize]];
            }else{
                connetLabel.text = @"0M";
            }
            
        }
        
        
        
    }else{
        
        UIButton *outLoginBtn = [self createButtonFrame:CGRectMake(0, 0, 0, 0) normalImage:nil selectImage:nil isBackgroundImage:NO title:nil target:self action:@selector(outLogin)];
        [outLoginBtn setTitle:_titleArray[indexPath.section][indexPath.row] forState:UIControlStateNormal];
        outLoginBtn.titleLabel.font = threeClassTextFont;
        [outLoginBtn setTitleColor:dt_text_main_color forState:UIControlStateNormal];
        outLoginBtn.backgroundColor = [UIColor whiteColor];
        [cell.contentView addSubview:outLoginBtn];
        
        [outLoginBtn mas_makeConstraints:^(MASConstraintMaker *make) {
           
            make.edges.mas_equalTo(UIEdgeInsetsMake(0, 0, 0, 0));
            
        }];
    }
    
    
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    if (indexPath.section==0) {
        
        if (indexPath.row==0) {
        
            //选择性别
            PerPickImageView *pickImage = [[PerPickImageView alloc]initWithId:3];
            pickImage.delegate = self;
            [pickImage showInView];
        
            
        }else{
            
            LZPickViewManager * manager =  [LZPickViewManager initLZPickerViewManager];
            
            [manager setPickViewTopWithLeftButtonColor:nil withRightButtonColor:nil withTitleColor:nil withTopBgColor:dt_text_F8F8F8_color];
            [manager setPickViewTopWithLeftButtonString:@"" withRightButtonString:@"" withTitleString:@""];
            [manager setPickViewTopWithLeftButtonImage:[UIImage imageNamed:@"chooseTime_cancel"] rightButtonImage:[UIImage imageNamed:@"chooseTime_sure"]];
            
            
            [manager showWithDatePickerMode:UIDatePickerModeTime compltedBlock:^(NSDate *choseDate) {
                
                
                RKLog(@"%@",choseDate);
                
                _allDayRemindTime = [Utils transformDate:choseDate dateFormatStyle:DateFormatHourMinuteWith24HR];
                
                NSUserDefaults *userDefault = kNSUserDefaults;
                [userDefault setValue:_allDayRemindTime forKey:allDayRemindTimeKey];
                [userDefault synchronize];
                
                [self.dataTabView reloadData];
                
            } cancelBlock:^{
                
            }];
        }
    }
    
    
    
    
    
    if (indexPath.section==2) {
        
        if (!_actionSheet)
        {
            _actionSheet = [[UIActionSheet alloc] initWithTitle:@"确认清空所有缓存数据" delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:@"清空缓存数据" otherButtonTitles:nil];
        }
        
        UIWindow* window = [[UIApplication sharedApplication] keyWindow];
        
        if ([window.subviews containsObject:self.view])
        {
            [_actionSheet showInView:self.view];
        }
        else
        {
            [_actionSheet showInView:window];
        }
        
    }
    
}

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 1)
    {
        return;
    }
    else
    {
        [self clearTmpPics];
    }
}

#pragma mark - PerPickViewDelegate
- (void)selectImage:(id)icon{
    
    NSString *weekDay = (NSString *)icon;
    if ([weekDay isEqualToString:@"周一"]) {
        [kNSUserDefaults setObject:@"2" forKey:@"weekDay"];
    }else{
        [kNSUserDefaults setObject:@"1" forKey:@"weekDay"];
    }
    [kNSUserDefaults synchronize];
    
    [self.dataTabView reloadData];
    [[NSNotificationCenter defaultCenter] postNotificationName:@"changeWeekDay" object:nil];
}

- (void)jumpOtherView:(UIImagePickerController *)object{}



- (void)clearTmpPics
{
    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    [[SDImageCache sharedImageCache] clearDiskOnCompletion:^{
        
        [[NSURLCache sharedURLCache] removeAllCachedResponses] ;
        
        [MBProgressHUD hideHUDForView:self.view animated:YES];
        
        [Utils showMsg:@"清除完成"];
        
        [self.dataTabView reloadData];
        
    }];
    
}

//获取缓存大小
-(float)getCachSize
{
    float folderSize = 0;
    
    NSString *fileDirectory = [[NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) lastObject] stringByAppendingFormat:@"/%@",@"DCWebImageCash"];
    
    folderSize += [self folderSizeAtPath: fileDirectory];
    
    folderSize += [[SDImageCache sharedImageCache]getSize]/(1024.0*1024.0);
    
    return folderSize;
}

- (float ) folderSizeAtPath:(NSString*) folderPath
{
    NSFileManager* manager = [NSFileManager defaultManager];
    if (![manager fileExistsAtPath:folderPath]) return 0;
    NSEnumerator *childFilesEnumerator = [[manager subpathsAtPath:folderPath] objectEnumerator];
    NSString* fileName;
    long long folderSize = 0;
    while ((fileName = [childFilesEnumerator nextObject]) != nil)
    {
        NSString* fileAbsolutePath = [folderPath stringByAppendingPathComponent:fileName];
        folderSize += [self fileSizeAtPath:fileAbsolutePath];
    }
    
    return folderSize/(1024.0*1024.0);
}

- (long long) fileSizeAtPath:(NSString*) filePath
{
    NSFileManager* manager = [NSFileManager defaultManager];
    if ([manager fileExistsAtPath:filePath]){
        return [[manager attributesOfItemAtPath:filePath error:nil] fileSize];
    }
    return 0;
}

#pragma mark - 注销登录
- (void)outLogin{
    RKLog(@"退出登录");
    //清除本地账号信息
    NSUserDefaults *userDefault = kNSUserDefaults;
    [userDefault removeObjectForKey:@"User"];
    [userDefault synchronize];
    
    //撤销所有本地通知
    [[LocalNotificationManager shareManager] cancelAllLocalNotifications];
    
    //更改数据库路径
    [[LKDBTool shareInstance] changeDBWithDirectoryName:nil];
    
    //发送通知,刷新本地数据
    postNotificationName(kUserSwitchNotificationName);
    
    [self.navigationController popViewControllerAnimated:YES];
    
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
