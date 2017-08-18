//
//  HolidaysViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/12.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "HolidaysViewController.h"
#import "SearchViewController.h"
#import "DOPDropDownMenu.h"
#import "LZPickViewManager.h"
#import "DropMenuTableView.h"
#import "DropMenuTableView.h"
#import "MenuModel.h"
#import "WSDatePickerView.h"
@interface HolidaysViewController ()

@property (nonatomic, strong) NSArray *classifys;
@property (nonatomic, strong) NSArray *cates;
@property (nonatomic, strong) NSArray *movices;
@property (nonatomic, strong) NSArray *hostels;
@property (nonatomic, strong) NSArray *areas;

@property (nonatomic, strong) NSArray *sorts;
@property (nonatomic, weak) DOPDropDownMenu *menu;
@property (nonatomic, weak) DOPDropDownMenu *menuB;
@property (nonatomic, strong)UIView *btnBgView;
@property (nonatomic, strong)UIButton *selectButton;

@property (nonatomic, strong)DropMenuTableView *menuView;

@property (nonatomic, strong)NSMutableArray *countryArray;//国家
@property (nonatomic, strong)NSMutableArray *currencyArray;//币种
@property (nonatomic, strong)NSMutableArray *holidayArray;//节假日
@property (nonatomic, strong)MenuModel *selectCountryModel;
@property (nonatomic, strong)MenuModel *selecctCurrencyModel;
@property (nonatomic, strong)MenuModel *selectHolidayModel;
@property (nonatomic, strong)CriteriaModel *criteriaModel;
@property (nonatomic, assign)BOOL isMonth;
@end

@implementation HolidaysViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setRightButton:@[@"btn_search_icon"]];
    
    [self initData];
    [self getData];
    [self createMenuUI];
}


#pragma mark - 创建菜单
- (void)createMenuUI{
    
    [self.view addSubview:self.dataTabView];
    [self updateTabViewFrameWithTop:40 left:0 right:0 bottom:-49];
    
    NSMutableArray *btnArray = [NSMutableArray array];
    _btnBgView = [[UIView alloc]init];
    [self.view addSubview:_btnBgView];
    
    NSMutableArray *titleArray = [NSMutableArray arrayWithObjects:[NSString stringWithFormat:@"%@",[Utils getCurrentTimeWithTimeFormat:@"yyyy-MM"]],@"国家/地区",@"币种",@"节假日", nil];
    @KKWeak(self);
    [_btnBgView mas_makeConstraints:^(MASConstraintMaker *make) {
        @KKStrong(self);
        make.top.left.equalTo(self.view).offset(0);
        make.right.equalTo(self.view).offset(0);
        make.height.mas_equalTo(40);
    }];
    for (int i = 0; i<4; i++) {
        
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        btn.titleLabel.font = fiveClassTextFont;
        btn.tag = i;
        [btn addTarget:self action:@selector(tapAction:) forControlEvents:UIControlEventTouchUpInside];
        [_btnBgView addSubview:btn];
        [btnArray addObject:btn];
        btn.backgroundColor = [UIColor whiteColor];
        [btn setTitle:titleArray[i] forState:UIControlStateNormal];
        [btn setTitleColor:dt_text_main_color forState:UIControlStateNormal];
        [btn setImage:[UIImage imageNamed:@"down_icon"] forState:UIControlStateNormal];
        [btn setImage:[UIImage imageNamed:@"up_icon"] forState:UIControlStateSelected];
        btn.selected = NO;
        if (i==0) {
            
            [btn mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.left.bottom.equalTo(_btnBgView).offset(0);
                make.width.equalTo(_btnBgView.mas_width).multipliedBy(0.35);
                
            }];
            
        }else if (i==1){
            
            UIButton *button = _btnBgView.subviews[i-1];
            [btn mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.bottom.equalTo(_btnBgView).offset(0);
                make.left.equalTo(button.mas_right).offset(0.5);
                make.width.equalTo(_btnBgView.mas_width).multipliedBy(0.25);
                
            }];
        }else{
            
            UIButton *button = _btnBgView.subviews[i-1];
            [btn mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.bottom.equalTo(_btnBgView).offset(0);
                make.left.equalTo(button.mas_right).offset(0.5);
                make.width.equalTo(_btnBgView.mas_width).multipliedBy(0.2);
                
            }];
        }
        CGFloat imageWidth = btn.imageView.bounds.size.width;
        CGFloat labelWidth = btn.titleLabel.bounds.size.width;
        btn.imageEdgeInsets = UIEdgeInsetsMake(0, labelWidth+3, 0, -labelWidth);
        btn.titleEdgeInsets = UIEdgeInsetsMake(0, -imageWidth, 0, imageWidth+3);
        
        //创建分割线
        if (i<3) {
            
            UIView *lineView = [[UIView alloc]init];
            lineView.backgroundColor = dt_line_color;
            [self.view addSubview:lineView];
            [lineView mas_makeConstraints:^(MASConstraintMaker *make) {
                @KKStrong(self);
                make.top.equalTo(self.view).offset(10);
                make.left.equalTo(btn.mas_right).offset(0);
                make.width.mas_equalTo(0.5);
                make.height.mas_equalTo(20);
            }];
        }
    }
}

- (void)initData{
    self.countryArray = [NSMutableArray array];
    self.currencyArray = [NSMutableArray array];
    self.holidayArray = [NSMutableArray array];
    self.criteriaModel = [[CriteriaModel alloc]init];
    self.isMonth = YES;
    self.criteriaModel.hdayDate = [Utils getCurrentTimeWithTimeFormat:@"yyyyMM"];
}

- (void)getData{
    
    [self getHolidayList];
    [self getMenuData];
}

#pragma mark - 获取条件
- (void)getMenuData{
    [self requestWithHTTPMethod:POST urlString:[NSString stringWithFormat:@"%@%@",ServreUrl,getParamUrl] parm:nil showWaitAlertTitile:nil isAfterDelay:NO success:^(id dictData) {
        
        NSDictionary *dataDict = (NSDictionary *)dictData;
        
        for (NSDictionary *dictCrcy in dataDict[@"ctryName"]) {
            MenuModel *model = [[MenuModel alloc]init];
            [model setValuesForKeysWithDictionary:dictCrcy];
            [self.countryArray addObject:model];
        }
        
        
        
        MenuModel *defautCity = [[MenuModel alloc]init];
        defautCity.valu = @"国家/地区";
        [self.countryArray insertObject:defautCity atIndex:0];
        if (!self.selectCountryModel) {
            self.selectCountryModel=defautCity;
        }
        
        
        for (NSDictionary *dictCrcy in dataDict[@"crcy"]) {
            MenuModel *model = [[MenuModel alloc]init];
            [model setValuesForKeysWithDictionary:dictCrcy];
            [self.currencyArray addObject:model];
        }
        
        MenuModel *defautCrcy = [[MenuModel alloc]init];
        defautCrcy.valu = @"币种";
        [self.currencyArray insertObject:defautCrcy atIndex:0];
        if (!self.selecctCurrencyModel) {
            self.selecctCurrencyModel=defautCrcy;
        }
        
        
        
        for (NSDictionary *dictCrcy in dataDict[@"hdayName"]) {
            MenuModel *model = [[MenuModel alloc]init];
            [model setValuesForKeysWithDictionary:dictCrcy];
            [self.holidayArray addObject:model];
        }
        
        MenuModel *defautHoliday = [[MenuModel alloc]init];
        defautHoliday.valu = @"节假日";
        [self.holidayArray insertObject:defautHoliday atIndex:0];
        if (!self.selectHolidayModel) {
            self.selectHolidayModel=defautHoliday;
        }
        
    } failure:^(NSString *message) {
        
        
        
    }];
}

- (void)getHolidayList{
    
    NSString *url = [NSString stringWithFormat:@"%@%@",ServreUrl,getHolidays];
    
    NSDictionary *param = [self.criteriaModel mj_keyValues];
    
    [self requestWithHTTPMethod:POST urlString:url parm:param showWaitAlertTitile:@"" isAfterDelay:YES success:^(id dictData) {
        
        if (self.dataArray.count >0) {
            [self.dataArray removeAllObjects];
        }
        
        NSDictionary *dict = (NSDictionary *)dictData;
        for (NSDictionary *data in dict[@"content"]) {
            
            HolidayModel *hModel = [[HolidayModel alloc]init];
            
            [hModel setValuesForKeysWithDictionary:data];
            
            [self.dataArray addObject:hModel];
    
        }
        
        [self.dataTabView reloadData];
        
        
    } failure:^(NSString *message) {
        
    }];
    
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];

}



- (void)tapAction:(UIButton *)button{
    
    self.selectButton.selected = NO;
    [self.menuView dismiss];
    button.selected = !button.selected;
    self.selectButton = button;
    //时间
    if (button.tag==0) {
        
        WSDatePickerView *datepicker = [[WSDatePickerView alloc] init];
        datepicker.isShouWeek = NO;
        datepicker.scrollToDate =  [[NSDate date:self.criteriaModel.hdayDate WithFormat:@"yyyy年MM月dd日"] dateWithFormatter:@"yyyy-MM-dd"];
        @KKWeak(self);
        [datepicker setDateStyle:DateStyleShowMonthOrShowYearMonthDay isMonth:self.isMonth completeBlock:^(NSDate *date, BOOL isMonth) {
            @KKStrong(self);
            self.isMonth = isMonth;
            NSString *dateStr = @"";
            if (isMonth) {
                dateStr = [Utils transformDateWithFormatter:@"yyyy-MM" date:date];
                self.criteriaModel.hdayDate = [Utils transformDateWithFormatter:@"yyyyMM" date:date];
            }else{
                dateStr = [Utils transformDateWithFormatter:@"yyyy-MM-dd" date:date];
                self.criteriaModel.hdayDate = [Utils transformDateWithFormatter:@"yyyyMMdd" date:date];
            }
            
            [button setTitle:dateStr forState:UIControlStateNormal];
            button.selected = NO;
            CGFloat imageWidth = button.imageView.bounds.size.width;
            CGFloat labelWidth = button.titleLabel.bounds.size.width;
            button.imageEdgeInsets = UIEdgeInsetsMake(0, labelWidth+3, 0, -labelWidth);
            button.titleEdgeInsets = UIEdgeInsetsMake(0, -imageWidth, 0, imageWidth+3);
        }];
        
        datepicker.disMiss= ^(){
            
            button.selected = NO;
            
        };
        
        [datepicker show];
        
    }else{

        menuStyle style = menuCountry;
        NSMutableArray *array = self.countryArray;
        MenuModel *menuModel = self.selectCountryModel;
        if (button.tag==1) {
            style = menuCountry;
            array = self.countryArray;
            menuModel = self.selectCountryModel;
        
        }else if (button.tag==2){
            style = menuCurrency;
            array = self.currencyArray;
            menuModel = self.selecctCurrencyModel;
            
        }else{
            style = menuHoliday;
            array = self.holidayArray;
            menuModel = self.selectHolidayModel;
        }
        
        
        @KKWeak(self);
        self.menuView = [[DropMenuTableView alloc]init];
        [self.menuView refreshMenu:array selectMenuModel:menuModel menuStyle:style CompleteBlock:^(MenuModel *model,menuStyle style) {
            @KKStrong(self);
            
            switch (style) {
                case menuCountry:
                    
                    self.selectCountryModel = model;
                    self.criteriaModel.ctryName = model.ke;
                    if ([model.valu isEqualToString:@"国家/地区"]) {
                        self.criteriaModel.hdayName = @"";
                    }
                    
                    break;
                case menuCurrency:
                    
                    self.selecctCurrencyModel = model;
                    self.criteriaModel.crcy = model.ke;
                    if ([model.valu isEqualToString:@"币种"]) {
                        self.criteriaModel.hdayName = @"";
                    }
                    
                    break;
                case menuHoliday:
                    
                    self.selectHolidayModel = model;
                    self.criteriaModel.hdayName = model.ke;
                    if ([model.valu isEqualToString:@"节假日"]) {
                        self.criteriaModel.hdayName = @"";
                    }
                    
                    break;
                default:
                    break;
            }
            
            button.selected = NO;
            [button setTitle:model.valu forState:UIControlStateNormal];
            CGFloat imageWidth = button.imageView.bounds.size.width;
            CGFloat labelWidth = button.titleLabel.bounds.size.width;
            button.imageEdgeInsets = UIEdgeInsetsMake(0, labelWidth+3, 0, -labelWidth);
            button.titleEdgeInsets = UIEdgeInsetsMake(0, -imageWidth, 0, imageWidth+3);
            [self getHolidayList];
            
            
        }];
        
        self.menuView.disMiss = ^(){
            button.selected = NO;
        };
        
        [self.menuView show];
    }
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.dataArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 45;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 0.001;
}


- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    HolidayModel *hModel = self.dataArray[indexPath.row];
    
    //日期
    UILabel *timeLable = [self createMainLabelWithText:hModel.hdayDate];
    [cell.contentView addSubview:timeLable];
    
    //国家
    UIView *countryView = [[UIView alloc]init];
    [cell.contentView addSubview:countryView];
    
    UIImageView *countryImageView = [[UIImageView  alloc]init];
    [Utils setImageView:countryImageView imageUrl:hModel.iconUrl placeholderImage:@""];
    [countryView addSubview:countryImageView];
    
    UILabel *countyrLbale = [self createMainLabelWithText:hModel.ctryNameValue];
    [countryView addSubview:countyrLbale];
    
    
    //币种
    UILabel *currencyLabel = [self createMainLabelWithText:hModel.crcy];
    [cell.contentView addSubview:currencyLabel];
    
    
    //节假日
    UILabel *holidayLabel = [self createMainLabelWithText:hModel.hdayNameValue];
    [cell.contentView addSubview:holidayLabel];
    
   
    
    //日期
    [timeLable mas_makeConstraints:^(MASConstraintMaker *make) {
       
        make.top.bottom.equalTo(cell.contentView).offset(0);
        make.left.equalTo(cell.contentView).offset(10);
        make.width.mas_equalTo(cell.contentView.mas_width).multipliedBy(0.35);
        
    }];
    
    //国家
    [countryView mas_makeConstraints:^(MASConstraintMaker *make) {
       make.top.bottom.equalTo(cell.contentView).offset(0);
        make.left.equalTo(timeLable.mas_right).offset(0);
        make.width.mas_equalTo(cell.contentView.mas_width).multipliedBy(0.25);
    }];
    
    [countryImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(countryView).offset(16);
        make.bottom.equalTo(countryView).offset(-16);
        make.left.equalTo(countryView).offset(8);
        make.width.mas_equalTo(20);
        
    }];
    
    [countyrLbale mas_makeConstraints:^(MASConstraintMaker *make) {
       
        make.top.equalTo(countryView).offset(0);
        make.bottom.equalTo(countryView).offset(0);
        make.left.equalTo(countryImageView.mas_right).offset(3);
        make.right.equalTo(countryView).offset(-8);

    }];
    
    
    [currencyLabel mas_makeConstraints:^(MASConstraintMaker *make) {
       
        make.top.bottom.equalTo(cell.contentView).offset(0);
        make.left.equalTo(countryView.mas_right).offset(0);
        make.width.mas_equalTo(cell.contentView.mas_width).multipliedBy(0.2);
    }];
    
    [holidayLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.top.bottom.equalTo(cell.contentView).offset(0);
        make.left.equalTo(currencyLabel.mas_right).offset(0);
        make.width.mas_equalTo(cell.contentView.mas_width).multipliedBy(0.2);
    }];
    
    
}









- (void)doRightAction:(UIButton *)sender{
    SearchViewController *searchVC = [[SearchViewController alloc]init];
    searchVC.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:searchVC animated:YES];
    
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
