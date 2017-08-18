//
//  HomeViewController.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/7/12.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "HomeViewController.h"
#import "ViewController.h"
#import "FSCalendar.h"
#import "Masonry.h"
#import "NSCalendar+SFHelper.h"
#import <EventKit/EventKit.h>
#import "LZPickViewManager.h"
#import "AddReminderController.h"
#import "RKTabvIewView.h"
#import "ReportListTableView.h"
#import "RemindListTableViewCell.h"
#import "GtaskListTableViewCell.h"
#import "MJExtension.h"
#import "WebViewController.h"

#define NMWCalendarTop 0.0
#define NMWCalendarBottom 0.0
#define NMWCalendarViewWeekHeight 35.0

@interface HomeViewController ()
<
UITableViewDelegate,
UITableViewDataSource,
FSCalendarDelegate,
FSCalendarDataSource,
FSCalendarDelegateAppearance,
ReportListTableViewDelegate
>

@property (strong, nonatomic) NSMutableArray *items;
@property (strong, nonatomic) RKTableView *tableView;
@property (nonatomic, assign) BOOL isTopIsCanNotMoveTabView;
@property (nonatomic, assign) BOOL isTopIsCanNotMoveTabViewPre;
@property (nonatomic, assign) BOOL canScroll;
@property (nonatomic, assign) CGFloat calendarHight;

@property (nonatomic,strong) FSCalendar *calendar;
@property (strong, nonatomic) NSCalendar *cldar;
@property (strong, nonatomic) UIView *calendarView;
@property (strong, nonatomic) MASConstraint *calendarViewHeight;
@property (assign, nonatomic) CGFloat calendarHeight;
@property (strong, nonatomic) NSDate *selectedDate;
@property (strong, nonatomic) NSDateFormatter *dateFormatter;
@property (strong, nonatomic) NSDateFormatter *dateFormatter2;
@property (strong, nonatomic) NSDate *minimumDate;
@property (strong, nonatomic) NSDate *maximumDate;
@property (strong, nonatomic) NSCalendar *lunarCalendar;
@property (strong, nonatomic) NSArray<NSString *> *lunarChars;
@property (strong, nonatomic) NSArray *datesWithEvent;
@property (strong, nonatomic) NSCache *cache;


@property (strong, nonatomic) UIView *titleView;
@property (strong, nonatomic) UILabel *titleLabel;
@property (copy,   nonatomic) NSString *yearMouth;
@property (strong, nonatomic) UIButton *downArrow;
@property (strong, nonatomic) NSDate *selectYearMouthDay;

@property (strong, nonatomic)NSMutableArray *remindArray;//提醒
@property (strong, nonatomic)NSMutableArray *gatasksArray;//待办
@property (strong, nonatomic)NSMutableArray *reportArray;//报表
@end

@implementation HomeViewController

- (instancetype)init
{
    self = [super init];
    if (self) {
        _lunarCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierChinese];
        _lunarCalendar.locale = [NSLocale localeWithLocaleIdentifier:@"zh-CN"];
        _lunarChars = @[@"初一",@"初二",@"初三",@"初四",@"初五",@"初六",@"初七",@"初八",@"初九",@"初十",@"十一",@"十二",@"十三",@"十四",@"十五",@"十六",@"十七",@"十八",@"十九",@"二十",@"二一",@"二二",@"二三",@"二四",@"二五",@"二六",@"二七",@"二八",@"二九",@"三十"];
        
        self.datesWithEvent = @[@"2017-07-19",
                                @"2017-07-21",
                                @"2017-07-22",
                                @"2017-07-25"];
    }
    return self;
}


- (void)viewDidLoad {

    [super viewDidLoad];
    
    [self createMainView];
    [self getCalendarData];
    
    //0.5s后请求报表数据,为什么的0.5s后呢,因为初始化网络配置需要时间,如果直接请求此时判断网络状态是无效的,因为网络状态还没有
    NSTimer *timer = [NSTimer timerWithTimeInterval:.5 target:self selector:@selector(getReport:) userInfo:nil repeats:NO];
    [[NSRunLoop currentRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
    
    
    //发送通知
    //周首日改变
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(isChangeWeekDay) name:@"changeWeekDay" object:nil];
    //tabview联动
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(acceptMsg:) name:kLeaveTopNotificationName object:nil];
    
}
#pragma mark - 用户切换刷新本地数据库
- (void)userSwitch{
    [self getCalendarData];
}

#pragma mark - 创建主UI
- (void)createMainView{
    
    [self setRightButton:@[@"navigationBar_itemIcon_add"]];
    self.view.themeMap = @{kThemeMapKeyColorName:@"view_backgroundColor"};
    [self setLeftButton:@"今天"];
    
    //设置日历
    [self refreshTitleViewFrame:[Utils getCurrentTimeWithTimeFormat:@"yyyy年MM月"]];
    self.selectedDate = [NSDate date];
    self.dateFormatter = [[NSDateFormatter alloc] init];
    self.dateFormatter.dateFormat = @"yyyy-MM-dd";
    [self.view addSubview:self.calendarView];
    [self.calendarView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.view).offset(0);
        make.left.right.equalTo(self.view);
    }];
    
    
    //表格
    [self.view insertSubview:self.tableView belowSubview:self.calendarView];
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.calendarView.mas_bottom);
        make.left.right.bottom.equalTo(self.view);
    }];
}

#pragma mark - 添加提醒,待办
- (void)doRightAction:(UIButton *)sender{
    
    AddReminderController *addReminder = [AddReminderController new];
    addReminder.selectIndex = 0;
    
    addReminder.updateData = ^(){
        
    };
    
    [addReminder setHidesBottomBarWhenPushed:YES];
    [self.navigationController pushViewController:addReminder animated:YES];
    
}

#pragma mark - 回到今天的时间
- (void)doLeftAction:(UIButton *)sender{
    [self.calendar setCurrentPage:[NSDate date] animated:NO];
    [_calendar selectDate:[NSDate date]];
    [self getTodayReminderDataWithDate:[NSDate date]];
}


#pragma mark - 获取日历表信息
- (void)getCalendarData{
    
    //先判断本地有无数据库,并且查看更新时间,每年的12月31号会从服务器更新日历表
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
        
        if ([CalendarModel isExistInTable]) {
            
            NSArray *oneData = [CalendarModel selectDataWithSql:@"SELECT * FROM CalendarModel LIMIT 1"];
            RKLog(@"%@",oneData);
            
            if (oneData.count>0) {
                CalendarModel *model = [oneData lastObject];
                
                if ([[Utils getCurrentTimeWithDay] intValue]>[[model.dateId substringToIndex:8] intValue]) {
                    //更新日历表
                    [self UpdateCalendar];
                }
                
            }else{
                //更新日历表
                [self UpdateCalendar];
            }
        }else{
            //更新日历表
            [self UpdateCalendar];
        }
        
        
        //查询提醒待办
        [self getTodayReminderDataWithDate:[NSDate date]];
        
    });

}

#pragma mark - 更新日历表
- (void)UpdateCalendar{
    [self requestWithHTTPMethod:POST urlString:requestUrl(synchronousDate) parm:nil isHaveAlert:NO waitTitle:nil success:^(id dictData) {
        
        NSArray *array  = (NSArray *)dictData;
        dispatch_async(dispatch_get_global_queue(0, 0), ^{
            //先删除
            [CalendarModel deleteObjectsByCriteria:@""];
            //后添加
            NSMutableArray *arrayModel = [NSMutableArray array];
            //日历表的id,前缀定为每次更新后第二年的12月16号
            NSString *dateId = [NSString stringWithFormat:@"%d%@",[[[Utils getCurrentTimeWithDay] substringToIndex:4] intValue]+1,@"1216"];
            for (int i = 0; i<array.count; i++) {
                CalendarModel *cModel = [[CalendarModel alloc]init];
                [cModel setValuesForKeysWithDictionary:array[i]];
                cModel.dateId = [NSString stringWithFormat:@"%@%d",dateId,1+i];
                [arrayModel addObject:cModel];
            }
            [CalendarModel saveObjects:arrayModel];
        });
        
    } failure:^(NSString *message) {
        
    }];
}

#pragma mark - 请求报表数据
- (void)getAllreport{
    
    if (self.networkStatus) {
        [self requestWithHTTPMethod:POST urlString:[NSString stringWithFormat:@"%@%@",ServreUrl,getAllReport] parm:nil isHaveAlert:YES waitTitle:nil success:^(id dictData) {
            
            NSArray *array = (NSArray *)dictData;
            [self parseReportData:array];
            
            //本地保存
            dispatch_async(dispatch_get_global_queue(0, 0), ^{
                
                NSArray *reportArray = [ReportLabel findAll];
                if (reportArray.count>0) {
                    
                    for (ReportLabel *rLabel in reportArray) {
                        
                        [rLabel deleteObject];
                    }
                }
                
                ReportLabel *newLabel = [[ReportLabel alloc]init];
                newLabel.reportId = @"1";
                newLabel.saveTime = [Utils getCurrentTime];
                newLabel.dictData = [Utils convertToJsonData:dictData];
                [newLabel save];
                
                
            });
            
        } failure:^(NSString *message) {
            
        }];
    }else{
        
        
        dispatch_async(dispatch_get_global_queue(0, 0), ^{
            
            NSArray *reportArray = [ReportLabel findByCriteria:@"order by saveTime"];
            ReportLabel*rLabel = [reportArray lastObject];
            NSDictionary *dict = [Utils dictionaryWithJsonString:rLabel.dictData];
            
            NSArray *array = (NSArray *)dict;
            dispatch_async(dispatch_get_main_queue(), ^{
                [self parseReportData:array];
            });
            
        });
    
    }

}

- (void)getReport:(NSTimer *)timer
{
    [timer invalidate];
    
    [self getAllreport];
    
//    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(haveNetWor) name:@"HAVE_NETWORK" object:nil];
}

- (void)haveNetWor{
    
    if (self.reportArray.count==0) {
        [self getAllreport];
    }
    
}

#pragma mark - 解析报表数据
- (void)parseReportData:(NSArray *)data{
    
    [self.reportArray removeAllObjects];
    for (NSDictionary *resultDict in data) {
        
        ReportModel *Rmodel = [[ReportModel alloc]init];
        Rmodel.title = resultDict[@"title"];
        Rmodel.result  = [NSMutableArray array];
        
        NSArray *resultArray = resultDict[@"result"];
        for (int i = 0; i<= resultArray.count; i++) {
            
            ReportlistModel *listModel = [[ReportlistModel alloc]init];
            if (i==0) {
                listModel.reportId = @"0";
            }else{
                
                [listModel setValuesForKeysWithDictionary:resultArray[i-1]];
            }
            
//            RKLog(@"%@",listModel.reportId);
//            RKLog(@"%@",listModel.moduleType);
            [Rmodel.result addObject:listModel];
        }
        
        [self.reportArray addObject:Rmodel];
    }
     [self.tableView reloadData];
}



- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[RKTableView alloc] initWithFrame:CGRectMake(0, 0, CGRectGetWidth(self.view.frame), CGRectGetHeight(self.view.frame)-kBottomBarHeight) style:UITableViewStylePlain];
        _tableView.backgroundColor = dt_F2F2F2_color;
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.showsVerticalScrollIndicator = NO;
    }
    return _tableView;
}

- (FSCalendar *)calendar {
    if (!_calendar) {
        _calendar = [[FSCalendar alloc] init];
        _calendar.frame = CGRectMake(15, 2, kScreenWidth-30, 500);
        _calendar.dataSource = self;
        _calendar.delegate = self;
        _calendar.appearance.weekdayTextColor = [UIColor hex_colorWithHex:@"#b6b6b6"];
        _calendar.appearance.weekdayFont = sixClassTextFont;
        _calendar.appearance.titleFont = threeClassTextFont;
        _calendar.appearance.subtitleFont = nineClassTextFont;
        _calendar.backgroundColor = [UIColor orangeColor];
        _calendar.placeholderType = FSCalendarPlaceholderTypeNone;
        _calendar.appearance.caseOptions = FSCalendarCaseOptionsHeaderUsesUpperCase|FSCalendarCaseOptionsWeekdayUsesSingleUpperCase;
        _calendar.scrollDirection = UICollectionViewScrollDirectionHorizontal;
        //_calendar.firstWeekday = 2;//设置周首日
        if ([kNSUserDefaults objectForKey:@"weekDay"]) {
            _calendar.firstWeekday = [[kNSUserDefaults objectForKey:@"weekDay"] intValue];
        }
        _calendar.headerHeight = 0;
        _calendar.weekdayHeight = 30.0;
        self.minimumDate = [self.dateFormatter dateFromString:@"1991-01-01"];
        self.maximumDate = [self.dateFormatter dateFromString:@"2099-01-01"];
        self.dateFormatter2 = [[NSDateFormatter alloc] init];
        self.dateFormatter2.dateFormat = @"yyyy-MM-dd";
        _calendar.backgroundColor = [UIColor whiteColor];
        [_calendar selectDate:[NSDate date]];
    }
    return _calendar;
}

- (UIView *)calendarView {
    if (!_calendarView) {
        _calendarView = [UIView new];
        _calendarView.backgroundColor = [UIColor orangeColor];
        _calendarView.layer.shadowColor = dt_line_color.CGColor;
        _calendarView.layer.shadowOffset = CGSizeMake(0, 2);
        _calendarView.layer.shadowOpacity = 0.4;
        
        [_calendarView addSubview:self.calendar];
        [self.calendar mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(UIEdgeInsetsMake(NMWCalendarTop, 0, NMWCalendarBottom, 0));
            _calendarViewHeight = make.height.mas_equalTo(300);
        }];
        
    }
    return _calendarView;
}


- (NSCalendar *)cldar {
    if (!_cldar) {
        _cldar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    }
    return _cldar;
}

-(UIView *)titleView
{
    if (!_titleView) {
        _titleView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenWidth-128, 44)];
        self.navigationItem.titleView = _titleView;
    }
    return _titleView;
}

- (UILabel *)titleLabel
{
    if (!_titleLabel) {
        
        CGSize titleLabelSize = [Utils setWidthForText:_yearMouth.length>0?_yearMouth:@""fontSize:17 labelSize:17 isGetHight:NO];
        _titleLabel = [[UILabel alloc]initWithFrame:CGRectMake((kScreenWidth-128-titleLabelSize.width-20)/2, 0, titleLabelSize.width, 44)];
        
        _titleLabel.textColor = [UIColor whiteColor];
        _titleLabel.font = [UIFont systemFontOfSize:17];
        _titleLabel.userInteractionEnabled = YES;
        [_titleLabel addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(chooseYearMouth)]];
        [self.titleView addSubview:_titleLabel];
    }
    
    return _titleLabel;
}
- (UIButton *)downArrow
{
    if (!_downArrow) {
        _downArrow = [Utils createButtonFrame:CGRectMake(CGRectGetMaxX(self.titleLabel.frame)+5,0, 15, 44) normalImage:@"icon-downArrow" selectImage:nil isBackgroundImage:NO title:nil target:self action:@selector(chooseYearMouth)];
        [self.titleView addSubview:_downArrow];
    }
    
    return _downArrow;
}

#pragma mark - 刷新头
- (void)refreshTitleViewFrame:(NSString *)text
{
    _yearMouth = text;
    CGSize titleLabelSize = [Utils setWidthForText:text fontSize:17 labelSize:17 isGetHight:NO];
    CGFloat with = kScreenWidth-128;
    self.titleLabel.frame = CGRectMake((with-titleLabelSize.width-20)/2, 0, titleLabelSize.width, 44);
    self.downArrow.frame = CGRectMake(CGRectGetMaxX(self.titleLabel.frame)+5, 0, 15, 44);
    self.titleLabel.text = text;
}

#pragma mark - 选择时间
- (void)chooseYearMouth{
    
    LZPickViewManager * shareManager = [LZPickViewManager initLZPickerViewManager];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    
    NSDate *maxDate = [Utils getDataString:@"2099-12-31" formatter:@"yyyy-MM-dd"];
    NSDate *minDate = [Utils getDataString:@"1979-12-31" formatter:@"yyyy-MM-dd"];
    
    //设置最大、最小、当前
    [shareManager setDatePickerWithMaxDate:maxDate withMinDate:minDate withFixedValueDate:self.selectedDate];
    
    [shareManager setPickViewTopWithLeftButtonColor:nil withRightButtonColor:nil withTitleColor:nil withTopBgColor:dt_text_F8F8F8_color];
    [shareManager setPickViewTopWithLeftButtonString:@"" withRightButtonString:@"" withTitleString:@""];
    [shareManager setPickViewTopWithLeftButtonImage:[UIImage imageNamed:@"chooseTime_cancel"] rightButtonImage:[UIImage imageNamed:@"chooseTime_sure"]];
    @KKWeak(self)
    [shareManager showWithDatePickerMode:UIDatePickerModeDate compltedBlock:^(NSDate *choseDate) {
        @KKStrong(self)
        [self refreshTitleViewFrame:[Utils transformDateWithFormatter:@"yyyy年MM月" date:choseDate]];
        self.selectedDate = choseDate;
        [self.calendar selectDate:choseDate];
        [self.calendar reloadData];
        
        [self getTodayReminderDataWithDate:choseDate];
        
    } cancelBlock:^{
        
    }];
}


#pragma mark - Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of rows in the section.
    return 3;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    
    if (section==0) {
        return self.gatasksArray.count;
    }else if (section==1){
        return self.remindArray.count;
    }else{
        return 1;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    
    if (section==0) {
        if (self.gatasksArray.count==0) {
            return 0.5;
        }
    }else if (section==1){
        if (self.remindArray.count==0) {
            return 0.5;
        }
    }
    return 10;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger section = indexPath.section;
    CGFloat height = 0.;
    if (section==0) {
        if (self.gatasksArray.count==0) {
            height =  0.;
        }else{
            height = 55;
        }
        
    }else if(section==1){
        if (self.remindArray.count==0) {
            height =  0.;
        }else{
            height = 55;
        }
    }else if(section==2){
        height = CGRectGetHeight(self.view.frame)-kTopBarHeight;
    }
    return height;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 0.5;
}


//- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
//    
//    UIView *view = [[UIView alloc]init];
//    view.backgroundColor = dt_F2F2F2_color;
//    
//    return view;
//}



- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:nil];
    
    if (indexPath.section==0) {
        
        
        GtaskListTableViewCell *gtaskCell = [tableView dequeueReusableCellWithIdentifier:@"gtaskCell"];
        
        if (!gtaskCell) {
            
            gtaskCell  = [[[NSBundle mainBundle]loadNibNamed:@"GtaskListTableViewCell" owner:self options:nil]firstObject];
        }
        gtaskCell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        GtasksModel *gModel = self.gatasksArray[indexPath.row];
        
        [gtaskCell loadDataWith:gModel didSelectBtn:^(int buttonType,BOOL buttonStatus) {
            
//            RKLog(@"%@",gModel.content);
            
            if (buttonType==1) {
                gModel.isComplete = buttonStatus?1:0;
                gModel.completeDate = [Utils getCurrentTime];
                if (gModel.isComplete) {
                    
                    dispatch_async(dispatch_get_global_queue(0,0), ^{
                        
                        BOOL ret =  [gModel update];
                        
                        dispatch_async(dispatch_get_main_queue(), ^{
                            
                            if (ret) {
                                
                                NSLog(@"indexPath is = %ld",indexPath.row);
                                
                                [self.gatasksArray removeObjectAtIndex:indexPath.row];
                                [self.tableView reloadData];
                                
                            }
                            
                        });
                        
                    });
                    
                }
                
            }else{
                gModel.isImportant = buttonStatus?1:0;
                dispatch_async(dispatch_get_global_queue(0,0), ^{
                    [gModel update];
                    
                });
            }
        }];
        
        return gtaskCell;
        
        
    }else if (indexPath.section==1){
        RemindListTableViewCell *remindCell = [tableView dequeueReusableCellWithIdentifier:@"remindCell"];
        
        if (!remindCell) {
            
            remindCell  = [[[NSBundle mainBundle]loadNibNamed:@"RemindListTableViewCell" owner:self options:nil]firstObject];
        }
        remindCell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        ReminderModel *rModel = self.remindArray[indexPath.row];
        
        [remindCell loadHomeDataWithReminderModel:rModel];
        
        
        return remindCell;
    }
    else{
        ReportListTableView *reportView = [[ReportListTableView alloc]initWithFrame:CGRectMake(0, 0, 0, 0) dataArray:self.reportArray];
        reportView.delegate = self;
        [cell.contentView addSubview:reportView];
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
}

#pragma mark - ReportListTableViewDelegate(查看报表详情)
- (void)didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    ReportModel *rModel = self.reportArray[indexPath.section];
    ReportlistModel *listModel = rModel.result[indexPath.row+1];
    NSDictionary *dict = [NSDictionary dictionaryWithObject:listModel.reportId forKey:@"id"];
    [self requestWithHTTPMethod:POST urlString:requestUrl(showReportDetail) parm:dict isHaveAlert:NO waitTitle:nil success:^(id dictData) {
        
        WebViewController *webVC = [[WebViewController alloc]init];
        webVC.Url = (NSString *)dictData;
        webVC.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:webVC animated:YES];
        
    } failure:^(NSString *message) {
        
    }];
    
}


#pragma mark - FSCalendarDelegate
- (NSString *)calendar:(FSCalendar *)calendar subtitleForDate:(NSDate *)date
{
    NSInteger day = [_lunarCalendar component:NSCalendarUnitDay fromDate:date];
    return _lunarChars[day-1];
    
}

- (NSInteger)calendar:(FSCalendar *)calendar numberOfEventsForDate:(NSDate *)date
{
    NSString *dateString = [self.dateFormatter2 stringFromDate:date];
    if ([_datesWithEvent containsObject:dateString]) {
        return 1;
    }
    return 0;
}



- (NSDate *)minimumDateForCalendar:(FSCalendar *)calendar
{
    return self.minimumDate;
}

- (NSDate *)maximumDateForCalendar:(FSCalendar *)calendar
{
    return self.maximumDate;
}

- (void)calendar:(FSCalendar *)calendar boundingRectWillChange:(CGRect)bounds animated:(BOOL)animated {
    
    RKLog(@"%f",CGRectGetHeight(bounds));
    self.calendarHight = CGRectGetHeight(bounds);
    _calendarViewHeight.mas_equalTo(CGRectGetHeight(bounds));
    [self.view setNeedsLayout];
    [self.view layoutIfNeeded];
}

- (nullable UIColor *)calendar:(FSCalendar *)calendar appearance:(FSCalendarAppearance *)appearance titleDefaultColorForDate:(NSDate *)date {
    
    NSDate *today = [NSDate date];
    if ([self.cldar yearOfDate:date] <= [self.cldar yearOfDate:today] && [self.cldar monthOfDate:date] <= [self.cldar monthOfDate:today] && [self.cldar dayOfDate:date] < [self.cldar dayOfDate:today]) {
        return [UIColor blackColor];
    } else if ([self.cldar yearOfDate:date] == [self.cldar yearOfDate:today] && [self.cldar monthOfDate:date] == [self.cldar monthOfDate:today] && [self.cldar dayOfDate:date] == [self.cldar dayOfDate:today]) {
        return [UIColor hex_colorWithHex:@"#29a1f7"];
    } else {
        return [UIColor blackColor];
    }
    
    return [UIColor blackColor];
}

- (UIColor *)calendar:(FSCalendar *)calendar appearance:(FSCalendarAppearance *)appearance subtitleDefaultColorForDate:(NSDate *)date{
    
    NSDate *today = [NSDate date];
    if ([self.cldar yearOfDate:date] == [self.cldar yearOfDate:today] && [self.cldar monthOfDate:date] == [self.cldar monthOfDate:today] && [self.cldar dayOfDate:date] == [self.cldar dayOfDate:today]) {
        return [UIColor hex_colorWithHex:@"#29a1f7"];
    }
    
    return [UIColor hex_colorWithHex:@"#000000"];
}
- (UIColor *)calendar:(FSCalendar *)calendar appearance:(FSCalendarAppearance *)appearance subtitleSelectionColorForDate:(NSDate *)date{
    return [UIColor hex_colorWithHex:@"#FFFFFF"];
}

- (UIColor *)calendar:(FSCalendar *)calendar appearance:(FSCalendarAppearance *)appearance fillSelectionColorForDate:(NSDate *)date {
    
    return [UIColor hex_colorWithHex:@"#29a1f7"];
}

- (nullable UIColor *)calendar:(FSCalendar *)calendar appearance:(FSCalendarAppearance *)appearance titleSelectionColorForDate:(NSDate *)date {
    return [UIColor hex_colorWithHex:@"#FFFFFF"];
}

- (UIColor *)calendar:(FSCalendar *)calendar appearance:(FSCalendarAppearance *)appearance fillDefaultColorForDate:(NSDate *)date {
    
    return self.calendar.backgroundColor;
}

//- (NSDate *)minimumDateForCalendar:(FSCalendar *)calendar {
//
//    NSDateComponents *components = [self.cldar components:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay|NSCalendarUnitHour fromDate:[NSDate date]];
//    components.day = 1;
//    return [self.cldar dateFromComponents:components];
//}

- (BOOL)calendar:(FSCalendar *)calendar shouldSelectDate:(NSDate *)date {
    //    NSDate *today = [NSDate date];
    //    return !([self.cldar yearOfDate:date] <= [self.cldar yearOfDate:today] && [self.cldar monthOfDate:date] <= [self.cldar monthOfDate:today] && [self.cldar dayOfDate:date] < [self.cldar dayOfDate:today]);
    
    
    return YES;
}

- (void)calendar:(FSCalendar *)calendar didSelectDate:(NSDate *)date {
    _selectedDate = date;
    [self getTodayReminderDataWithDate:date];
}

- (void)calendarCurrentPageDidChange:(FSCalendar *)calendar {
    
}



#pragma mark - 查询待办,提醒
- (void)getTodayReminderDataWithDate:(NSDate *)date{
    
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
        [self updateRemindAndGtasksWithDate:date];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            // 通知主线程刷新 神马的
            [self.tableView reloadData];
            
        });
        
    });
    
}

#pragma mark - 更新提醒和待办
- (void)updateRemindAndGtasksWithDate:(NSDate *)date{
    
    [self.remindArray removeAllObjects];
    
    [self.gatasksArray removeAllObjects];
    //提醒
    [self.remindArray addObjectsFromArray:[[RemindAndGtasksDBManager shareManager] getRemindArrayWithDate:date]];
    //待办
    [self.gatasksArray addObjectsFromArray:[[RemindAndGtasksDBManager shareManager] getAllGtasksArray]];
}


#pragma mark - 通知是否改变了周首日
- (void)isChangeWeekDay{
    
    _calendar.firstWeekday = [[kNSUserDefaults objectForKey:@"weekDay"] intValue];
    [_calendar reloadData];
    
}

- (NSMutableArray *)remindArray{
    
    if (!_remindArray) {
        _remindArray = [NSMutableArray array];
    }
    
    return _remindArray;
}
- (NSMutableArray *)gatasksArray{
    
    if (!_gatasksArray) {
        _gatasksArray = [NSMutableArray array];
    }
    
    return _gatasksArray;
}
- (NSMutableArray *)reportArray{
    
    if (!_reportArray) {
        _reportArray = [NSMutableArray array];
    }
    
    return _reportArray;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    
    if (scrollView != self.tableView) {
        return;
    }
    if (!_calendarViewHeight) {
        return;
    }
    CGFloat offsety = scrollView.contentOffset.y+scrollView.contentInset.top;
    if (offsety>20 && self.calendar.scope == FSCalendarScopeMonth) {
        [self.calendar setScope:FSCalendarScopeWeek animated:YES];
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            self.calendar.placeholderType = FSCalendarPlaceholderTypeFillHeadTail;
        });
    }
    
    if (offsety<0 && self.calendar.scope == FSCalendarScopeWeek) {
        [self.calendar setScope:FSCalendarScopeMonth animated:YES];
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            self.calendar.placeholderType = FSCalendarPlaceholderTypeNone;
        });
    }
    
    
    CGFloat tabOffsetY = [self.tableView rectForSection:2].origin.y-0;
    CGFloat offsetY = scrollView.contentOffset.y;
    _isTopIsCanNotMoveTabViewPre = _isTopIsCanNotMoveTabView;
    if (offsetY>=tabOffsetY) {
        scrollView.contentOffset = CGPointMake(0, tabOffsetY);
        _isTopIsCanNotMoveTabView = YES;
    }else{
        _isTopIsCanNotMoveTabView = NO;
    }
    if (_isTopIsCanNotMoveTabView != _isTopIsCanNotMoveTabViewPre) {
        if (!_isTopIsCanNotMoveTabViewPre && _isTopIsCanNotMoveTabView) {
            //NSLog(@"滑动到顶端");
            [[NSNotificationCenter defaultCenter] postNotificationName:kGoTopNotificationName object:nil userInfo:@{@"canScroll":@"1"}];
            _canScroll = NO;
        }
        if(_isTopIsCanNotMoveTabViewPre && !_isTopIsCanNotMoveTabView){
            //NSLog(@"离开顶端");
            if (!_canScroll) {
                scrollView.contentOffset = CGPointMake(0, tabOffsetY);
            }
        }
    }
    
}

-(void)acceptMsg:(NSNotification *)notification{
    NSDictionary *userInfo = notification.userInfo;
    NSString *canScroll = userInfo[@"canScroll"];
    if ([canScroll isEqualToString:@"1"]) {
        _canScroll = YES;
    }
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
