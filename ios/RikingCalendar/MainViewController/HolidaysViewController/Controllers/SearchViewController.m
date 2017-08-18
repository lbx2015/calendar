//
//  SearchViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/8.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "SearchViewController.h"
#import "UINavigationController+FDFullscreenPopGesture.h"
#import "UISearchBar+Background.h"
@interface SearchViewController ()

<UISearchBarDelegate>
{
    NSString *_searchText;
    
    UISearchBar *_search;
}

@end

@implementation SearchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
      self.view.backgroundColor = dt_FAFAFA_color;
    

    
    [self.view addSubview:self.dataTabView];
    [self updateTabViewFrameWithTop:64 left:0 right:0 bottom:0];
    
    UIView *searchBgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenWidth, 64)];
    searchBgView.backgroundColor = RGBA(239, 239, 244, 1);
    [self.view addSubview:searchBgView];
    [searchBgView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.right.equalTo(self.view).offset(0);
        make.height.mas_equalTo(64);
    }];
    
    UIButton *searchBtn = [UIButton buttonWithType: UIButtonTypeCustom];
    [searchBtn setTitle:@"搜索" forState:UIControlStateNormal];
    [searchBtn setTitleColor:dt_app_main_color forState:UIControlStateNormal];
    searchBtn.titleLabel.font = threeClassTextFont;
    [searchBtn addTarget:self action:@selector(searchAction) forControlEvents:UIControlEventTouchUpInside];
    [searchBgView addSubview:searchBtn];
    
    [searchBtn mas_makeConstraints:^(MASConstraintMaker *make) {
       
        make.bottom.equalTo(searchBgView).offset(0);
        make.right.equalTo(searchBgView).offset(-15);
        make.top.equalTo(searchBgView).offset(20);
        make.width.mas_equalTo(50);
    
    }];
    
   _search = [[UISearchBar alloc]initWithFrame:CGRectMake(10, 27, kScreenWidth-15-50-10, 28)];
    [_search becomeFirstResponder];
    _search.backgroundColor = [UIColor orangeColor];
    _search.placeholder = @"国家/币种/节假日";
    _search.searchBarStyle = UIBarStyleDefault;
    _search.returnKeyType = UIReturnKeySearch;
    [_search setBackgroundExten:[UIColor clearColor]];
    _search.delegate = self;
    [searchBgView addSubview:_search];
    
    [_search mas_makeConstraints:^(MASConstraintMaker *make) {
        
        make.bottom.equalTo(searchBgView).offset(-8);
        make.right.equalTo(searchBtn.mas_left).offset(-10);
        make.top.equalTo(searchBgView).offset(28);
        make.left.equalTo(searchBgView).offset(10);
        
    }];
    
    
}


- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText{
    
    RKLog(@"%@",searchText);
    _searchText = searchText;
}


#pragma mark - 搜索
- (void)searchAction
{
    if ([Utils isBlankString:_searchText]) {
        
        [Utils showMsg:@"搜索内容不能为空"];
        return;
    }
    
    [_search resignFirstResponder];
    _search.showsCancelButton = NO;
    [self searchHoliday];
    
}





- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{
    [searchBar resignFirstResponder];
    searchBar.showsCancelButton = NO;
    [self searchHoliday];
}

-(void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar{
    searchBar.showsCancelButton = YES;
    UIButton *btn=[searchBar valueForKey:@"_cancelButton"];
    [btn setTitle:@"取消" forState:UIControlStateNormal];
    [btn setTitleColor:[UIColor hex_colorWithHex:@"0488dd"] forState:UIControlStateNormal];
}


- (void)searchHoliday{
    NSString *url = [NSString stringWithFormat:@"%@%@",ServreUrl,vagueQuery];
    
    NSDictionary *param = [NSDictionary dictionaryWithObject:_searchText forKey:@"queryParam"];
    
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
    [self.navigationController popViewControllerAnimated:YES];
}



- (BOOL)fd_prefersNavigationBarHidden{
    return YES;
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
