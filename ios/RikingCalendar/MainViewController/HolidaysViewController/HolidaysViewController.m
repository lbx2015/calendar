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
    [self getMenuData];
    [self getData];
    [self createMenuUI];
}


#pragma mark - 创建菜单
- (void)createMenuUI{
    
    [self.view addSubview:self.dataTabView];
    [self updateTabViewFrameWithTop:40 left:0 right:0 bottom:-49];
    
    self.dataTabView.header = self.kkRefreshHeader;
    self.dataTabView.footer = self.diyRefreshFooter;
    
    
    NSMutableArray *btnArray = [NSMutableArray array];
    _btnBgView = [[UIView alloc]init];
    _btnBgView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:_btnBgView];
    
    NSMutableArray *titleArray = [NSMutableArray arrayWithObjects:[NSString stringWithFormat:@"%@",[Utils getCurrentTimeWithTimeFormat:@"yyyy年"]],@"国家/地区",@"币种",@"节假日", nil];
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
        btn.titleLabel.font = fiveClassTextFont;
        btn.selected = NO;
        if (i==0) {
            
            [btn mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.left.equalTo(_btnBgView).offset(0);
                make.bottom.equalTo(_btnBgView).offset(-0.5);
                make.width.equalTo(_btnBgView.mas_width).multipliedBy(0.35);
                
            }];
            
        }else if (i==1){
            
            UIButton *button = _btnBgView.subviews[i-1];
            [btn mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.equalTo(_btnBgView).offset(0);
                make.bottom.equalTo(_btnBgView).offset(-0.5);
                make.left.equalTo(button.mas_right).offset(0.5);
                make.width.equalTo(_btnBgView.mas_width).multipliedBy(0.25);
                
            }];
        }else{
            
            UIButton *button = _btnBgView.subviews[i-1];
            [btn mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.equalTo(_btnBgView).offset(0);
                make.bottom.equalTo(_btnBgView).offset(-0.5);
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
    
    //添加底线
    UIView *btnBgViewLine = [[UIView alloc]init];
    btnBgViewLine.backgroundColor = dt_line_color;
    [_btnBgView addSubview:btnBgViewLine];
    [btnBgViewLine mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(_btnBgView).offset(0);
        make.top.equalTo(_btnBgView).offset(39.5);
        make.bottom.equalTo(_btnBgView).offset(0);
    }];
}

- (void)initData{
    self.countryArray = [NSMutableArray array];
    self.currencyArray = [NSMutableArray array];
    self.holidayArray = [NSMutableArray array];
    self.criteriaModel = [[CriteriaModel alloc]init];
    self.isMonth = YES;
    self.criteriaModel.hdayDate = [Utils getCurrentTimeWithTimeFormat:@"yyyy"];
    self.page = 1;
}

- (void)getData{
    
    [self getHolidayList];
}

- (void)criteriaSelectHoliday{
    self.page=1;
    [self getHolidayList];
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
    
    NSMutableDictionary *param = [NSMutableDictionary dictionaryWithDictionary:[self.criteriaModel mj_keyValues]];
    [param setValue:[NSNumber numberWithInteger:self.page] forKey:@"pindex"];
    [param setValue:@20 forKey:@"pcount"];
    [self requestWithHTTPMethod:POST urlString:url parm:param showWaitAlertTitile:@"" isAfterDelay:YES success:^(id dictData) {
        
        if (self.dataArray.count >0 && self.page == 1) {
            [self.dataArray removeAllObjects];
        }
        
        NSDictionary *dict = (NSDictionary *)dictData;
        for (NSDictionary *data in dict[@"content"]) {
            
            HolidayModel *hModel = [[HolidayModel alloc]init];
            
            [hModel setValuesForKeysWithDictionary:data];
            
            [self.dataArray addObject:hModel];
    
        }
        
        [self.kkRefreshHeader endRefreshing];
        
        if (self.page >= [dict[@"totalPages"] integerValue]) {
            [self.diyRefreshFooter endRefreshingWithNoMoreData];
        }else{
            [self.diyRefreshFooter endRefreshing];
        }
        
        [self.dataTabView reloadData];
        
        
    } failure:^(NSString *message) {
        [self.kkRefreshHeader endRefreshing];
        [self.diyRefreshFooter endRefreshing];
        
    }];
    
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];

}


#pragma mark - 选择条件
- (void)tapAction:(UIButton *)button{
    
    if (self.selectButton == button) {
        button.selected = !button.selected;
        if (!self.selectButton.selected) {
            [self.menuView dismiss];
            return;
        }
        
    }else{
        
        if (self.selectButton.selected) {
            [self.menuView dismiss];
        }
        self.selectButton.selected = NO;
        button.selected = !button.selected;
        self.selectButton = button;
    }
    
    //时间
    if (button.tag==0) {
        
        WSDatePickerView *datepicker = [[WSDatePickerView alloc] init];
        datepicker.isShouWeek = NO;
        datepicker.scrollToDate =  [[NSDate date:self.criteriaModel.hdayDate WithFormat:@"yyyy"] dateWithFormatter:@"yyyy-MM-dd"];
        @KKWeak(self);
        [datepicker setDateStyle:DateStyleShowYear isMonth:self.isMonth completeBlock:^(NSDate *date, BOOL isMonth) {
            @KKStrong(self);
            self.isMonth = isMonth;
            NSString *dateStr = @"";
            if (isMonth) {
                dateStr = [Utils transformDateWithFormatter:@"yyyy年" date:date];
                self.criteriaModel.hdayDate = [Utils transformDateWithFormatter:@"yyyy" date:date];
            }else{
                dateStr = [Utils transformDateWithFormatter:@"yyyy年MM月" date:date];
                self.criteriaModel.hdayDate = [Utils transformDateWithFormatter:@"yyyyMM" date:date];
            }
            
            [button setTitle:dateStr forState:UIControlStateNormal];
            button.selected = NO;
            CGFloat imageWidth = button.imageView.bounds.size.width;
            CGFloat labelWidth = button.titleLabel.bounds.size.width;
            button.imageEdgeInsets = UIEdgeInsetsMake(0, labelWidth+3, 0, -labelWidth);
            button.titleEdgeInsets = UIEdgeInsetsMake(0, -imageWidth, 0, imageWidth+3);
            
            //查询节假日
            [self criteriaSelectHoliday];
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
                        self.criteriaModel.ctryName = @"";
                    }
                    
                    break;
                case menuCurrency:
                    
                    self.selecctCurrencyModel = model;
                    self.criteriaModel.crcy = model.ke;
                    if ([model.valu isEqualToString:@"币种"]) {
                        self.criteriaModel.crcy = @"";
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
            [self criteriaSelectHoliday];
            
            
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
    UILabel *timeLable = [self createMainLabelWithText:[Utils setOldStringTime:hModel.hdayDate inputFormat:@"yyyyMMdd" outputFormat:@"yyyy年MM月dd日"]];
    timeLable.textAlignment = NSTextAlignmentCenter;
    timeLable.font = sixClassTextFont;
//    timeLable.backgroundColor = [UIColor redColor];
    [cell.contentView addSubview:timeLable];
    
    //国家
    UIView *countryView = [[UIView alloc]init];
//    countryView.backgroundColor = [UIColor orangeColor];
    [cell.contentView addSubview:countryView];
    
    
    
//    //先计算国家名和图片总的宽度
//    CGFloat countyrLbaleWith = [Utils setWidthForText:hModel.ctryNameValue fontSize:14 labelSize:45 isGetHight:NO].width;
//    CGFloat countyrwith = countyrLbaleWith+20+3;//国家名+图片+间隔
    
    UIView *bgView = [[UIView alloc]init];
//    bgView.backgroundColor = [UIColor purpleColor];
    [countryView addSubview:bgView];
    
    UIImageView *countryImageView = [[UIImageView  alloc]init];
    [Utils setImageView:countryImageView imageUrl:hModel.flagUrl placeholderImage:@""];
    [bgView addSubview:countryImageView];
    
    UILabel *countyrLbale = [self createMainLabelWithText:hModel.ctryNameValue];
    countyrLbale.font = sixClassTextFont;
    countyrLbale.numberOfLines = 0;
    [bgView addSubview:countyrLbale];
    
    
    //币种
    UILabel *currencyLabel = [self createMainLabelWithText:hModel.crcy];
//    currencyLabel.backgroundColor = [UIColor purpleColor];
    currencyLabel.textAlignment = NSTextAlignmentCenter;
    currencyLabel.font = sixClassTextFont;
    [cell.contentView addSubview:currencyLabel];
    
    
    //节假日
    UILabel *holidayLabel = [self createMainLabelWithText:hModel.hdayNameValue];
//    holidayLabel.backgroundColor = [UIColor blueColor];
    holidayLabel.textAlignment = NSTextAlignmentCenter;
    holidayLabel.numberOfLines = 0;
    holidayLabel.font = sixClassTextFont;
    [cell.contentView addSubview:holidayLabel];
    
   
    
    //日期
    [timeLable mas_makeConstraints:^(MASConstraintMaker *make) {
       
        make.top.bottom.equalTo(cell.contentView).offset(0);
        make.left.equalTo(cell.contentView).offset(0);
        make.width.mas_equalTo(cell.contentView.mas_width).multipliedBy(0.35);
        
    }];
    
    //国家
    [countryView mas_makeConstraints:^(MASConstraintMaker *make) {
       make.top.bottom.equalTo(cell.contentView).offset(0);
        make.left.equalTo(timeLable.mas_right).offset(0);
        make.width.mas_equalTo(cell.contentView.mas_width).multipliedBy(0.25);
    }];
    
    //图片和国家名背景
    [bgView mas_makeConstraints:^(MASConstraintMaker *make) {
       
        make.centerX.equalTo(countryView);
        make.top.bottom.equalTo(countryView).offset(0);
        make.left.equalTo(countryView).offset(5);
        make.right.equalTo(countryView).offset(-5);
    }];
    
    
    [countryImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(bgView).offset(16);
        make.bottom.equalTo(bgView).offset(-16);
        make.left.equalTo(bgView).offset(0);
        make.width.mas_equalTo(20);
        
    }];
    
    [countyrLbale mas_makeConstraints:^(MASConstraintMaker *make) {
       
        make.top.equalTo(bgView).offset(0);
        make.bottom.equalTo(bgView).offset(0);
        make.left.equalTo(countryImageView.mas_right).offset(3);
        make.right.equalTo(bgView).offset(0);

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
    
    self.selectButton.selected = NO;
    [self.menuView dismiss];
    
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
