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
#import "ReminderAndGtasksDetailView.h"
#import "GtasksViewController.h"
#import "ReminderViewController.h"





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
ReportListTableViewDelegate,
UIGestureRecognizerDelegate
>
{
    NSMutableArray *_btnArray;
    
    dispatch_source_t _timer;
    
    BOOL _isRefreshRemind;
    BOOL _isRefreshGtsaks;
}
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
@property (strong, nonatomic) NSArray *monthArr;
@property (strong, nonatomic) NSMutableArray *datesWithEvent;
@property (strong, nonatomic) NSCache *cache;


@property (strong, nonatomic) UIView *titleView;
@property (strong, nonatomic) UILabel *titleLabel;
@property (copy,   nonatomic) NSString *yearMouth;
@property (strong, nonatomic) UIButton *downArrow;
@property (strong, nonatomic) NSDate *selectYearMouthDay;

@property (strong, nonatomic)NSMutableArray *remindArray;//提醒
@property (strong, nonatomic)NSMutableArray *gatasksArray;//待办
@property (strong, nonatomic)NSMutableArray *reportArray;//报表

@property (strong, nonatomic) UIPanGestureRecognizer *scopeGesture;
@property (assign, nonatomic)BOOL refreshCalendar;//刷新日历
@end

@implementation HomeViewController

- (instancetype)init
{
    self = [super init];
    if (self) {
        _lunarCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierChinese];
        _lunarCalendar.locale = [NSLocale localeWithLocaleIdentifier:@"zh-CN"];
        _lunarChars = @[@"初一",@"初二",@"初三",@"初四",@"初五",@"初六",@"初七",@"初八",@"初九",@"初十",@"十一",@"十二",@"十三",@"十四",@"十五",@"十六",@"十七",@"十八",@"十九",@"廿十",@"廿一",@"廿二",@"廿三",@"廿四",@"廿五",@"廿六",@"廿七",@"廿八",@"廿九",@"三十"];
        _monthArr = [NSArray arrayWithObjects:
                    @"正月", @"二月", @"三月", @"四月", @"五月", @"六月", @"七月", @"八月",
                    @"九月", @"十月", @"冬月", @"腊月", nil];
    }
    return self;
}


- (void)viewDidLoad {

    [super viewDidLoad];
    
    [self createMainView];
    
    [self getCalendarData];
    
    //请求报表数据
    //0.5s后请求报表数据,为什么的0.5s后呢,因为初始化网络配置需要时间,如果直接请求此时判断网络状态是无效的,因为网络状态还没有
    NSTimer *timer = [NSTimer timerWithTimeInterval:.5 target:self selector:@selector(getReport:) userInfo:nil repeats:NO];
    [[NSRunLoop currentRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
    
    
    //发送通知
    //周首日改变
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(isChangeWeekDay) name:@"changeWeekDay" object:nil];
    
    //tabview联动
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(acceptMsg:) name:kLeaveTopNotificationName object:nil];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(haveNetWork) name:@"HAVE_NETWORK" object:nil];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(refreshRemind) name:kRefreshRemindName object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(refreshGtasks) name:kRefreshGtasksName object:nil];
    
}



#pragma mark - 用户切换刷新本地数据库
- (void)userSwitch{
    
    [self.gatasksArray removeAllObjects];
    [self.remindArray removeAllObjects];
    [self.reportArray removeAllObjects];

    [self getCalendarData];
    //请求报表数据
    //0.5s后请求报表数据,为什么的0.5s后呢,因为初始化网络配置需要时间,如果直接请求此时判断网络状态是无效的,因为网络状态还没有
    NSTimer *timer = [NSTimer timerWithTimeInterval:.5 target:self selector:@selector(getReport:) userInfo:nil repeats:NO];
    [[NSRunLoop currentRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
    
}

#pragma mark - 是否要刷新提醒
- (void)refreshRemind{
    _isRefreshRemind = YES;
}

#pragma mark - 是否要刷新待办
- (void)refreshGtasks{
    _isRefreshGtsaks = YES;
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
    //编辑,新增待办提醒,首页需要重新刷新,由于可能新增频繁或者修改频繁,我们只在刚显示这个界面时再刷新UI
    if (_isRefreshRemind) {
        [self getAllRemindAndGtasksDate];
        [self getTodayReminderDataWithDate:self.selectedDate type:1];
        _isRefreshRemind = NO;
    }
    
    if (_isRefreshGtsaks) {
        [self getTodayReminderDataWithDate:self.selectedDate type:2];
        _isRefreshGtsaks = NO;
    }
}

#pragma mark - 创建主UI
- (void)createMainView{
    
    _btnArray = [NSMutableArray array];
    
    [self setRightButton:@[@"navigationBar_itemIcon_add"]];
    self.view.themeMap = @{kThemeMapKeyColorName:@"view_backgroundColor"};
    [self setLeftButton:NSLocalizedString(@"homeLeftBtnName",nil)];
    
    //设置日历
    [self refreshTitleViewFrame:[Utils getCurrentTimeWithDateStyle:DateFormatYearMonthWithChinese]];
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
        make.left.right.equalTo(self.view);
        make.bottom.equalTo(self.view).offset(-49);
    }];
}


- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[RKTableView alloc] initWithFrame:CGRectMake(0, 0, CGRectGetWidth(self.view.frame), CGRectGetHeight(self.view.frame)-kBottomBarHeight) style:UITableViewStyleGrouped];
        _tableView.backgroundColor = dt_F2F2F2_color;
        _tableView.showsVerticalScrollIndicator = NO;
        _tableView.showsHorizontalScrollIndicator = NO;
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        _tableView.delegate = self;
        _tableView.dataSource = self;
    }
    return _tableView;
}

- (FSCalendar *)calendar {
    if (!_calendar) {
        _calendar = [[FSCalendar alloc] init];
        _calendar.frame = CGRectMake(15, 2, kScreenWidth-30, 500);
        _calendar.dataSource = self;
        _calendar.delegate = self;
        _calendar.scope = FSCalendarScopeMonth;
        _calendar.appearance.weekdayTextColor = [UIColor hex_colorWithHex:@"#b6b6b6"];
        _calendar.appearance.workWeekdayTextColor = [UIColor hex_colorWithHex:@"#000000"];
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
        _calendar.appearance.separators = FSCalendarSeparatorNone;//分割线
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
        _titleView.backgroundColor = [UIColor clearColor];
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
    self.selectedDate = [NSDate date];
    [self.calendar setCurrentPage:[NSDate date] animated:NO];
    [self refreshData];
}

#pragma mark - 刷新提醒数据
- (void)refreshData{
    //日历
    [self.calendar selectDate:self.selectedDate];
    //提醒
    [self getTodayReminderDataWithDate:self.selectedDate type:1];
    //用户报表
    [self getAllreport];
}

#pragma mark - 获取日历表信息
- (void)getCalendarData{
    
    //先判断本地有无数据库,并且查看更新时间,每年的12月16号会从服务器更新日历表
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
        NSArray *oneData = [CalendarModel selectDataWithSql:@"SELECT * FROM CalendarModel LIMIT 1"];
        RKLog(@"%@",oneData);
        if ([CalendarModel isExistInTable] && oneData.count>0) {
            
            CalendarModel *model = [oneData lastObject];
            
            if ([[Utils getCurrentTimeWithDay] intValue]>[[model.dateId substringToIndex:8] intValue]) {
                //更新日历表
                [self UpdateCalendarSuccess:^(BOOL ret) {
                    //查询所有提醒的时间,用于标记在日历上
                    [self getAllRemindAndGtasksDate];
                }];
            }else{
                dispatch_async(dispatch_get_main_queue(), ^{
                    // 通知主线程刷新 神马的
                    //查询所有提醒的时间,用于标记在日历上
                    [self getAllRemindAndGtasksDate];
                    
                });
            }
            
        }else{
            //更新日历表
            [self UpdateCalendarSuccess:^(BOOL ret) {
                
                //查询所有提醒的时间,用于标记在日历上
                [self getAllRemindAndGtasksDate];
            }];
        }
        
        
        //查询当天的所有提醒和所有待办(待办不分时间,只要是未完成的都查询出来)
        [self getTodayReminderDataWithDate:[NSDate date] type:0];
    
    });

}

#pragma mark - 查询所有提醒的时间,用于标记在日历上

/**
 查询所有提醒的时间,用于标记在日历上
 */
- (void)getAllRemindAndGtasksDate{
    
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
        [self.datesWithEvent removeAllObjects];
        
        [self.datesWithEvent addObjectsFromArray:[[RemindAndGtasksDBManager shareManager] selectAllRemindAndGtasks]];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            // 通知主线程刷新 神马的
            [self.calendar reloadData];
            
        });
        
    });
    
}

#pragma mark - 更新日历表
- (void)UpdateCalendarSuccess:(void(^)(BOOL ret))success{
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
            
            dispatch_async(dispatch_get_main_queue(), ^{
                // 通知主线程刷新 神马的
                if (success) {
                    success(YES);
                }
                
            });
        });
        
    } failure:^(NSString *message) {
        
        if (success) {
            success(NO);
        }
    }];
}

#pragma mark - 请求报表数据
- (void)getAllreport{
    
    if (self.networkStatus) {
        
        NSString *url = requestUrl(getAllReport);
        NSMutableDictionary *param = [NSMutableDictionary dictionary];
        if (isUser) {
            url = requestUrl(getUserRepor);;
            [param setObject:UserID forKey:@"appUserId"];
            [param setObject:[Utils transformDateWithFormatter:@"yyyyMMdd" date:self.selectedDate] forKey:@"completeDate"];
        }
        
        [self requestWithHTTPMethod:POST urlString:url parm:param isHaveAlert:NO waitTitle:nil success:^(id dictData) {
            
            NSArray *array = (NSArray *)dictData;
            [self parseReportData:array];
            
            //本地保存
            dispatch_async(dispatch_get_global_queue(0, 0), ^{
                
                //先判断本地有无报表,有则先删除后添加
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
        
            [self getLoactionAllReport];
            
        }];
        
    }else{
    
        [self getLoactionAllReport];
    }

}

#pragma mark - 取出本地的报表
- (void)getLoactionAllReport{
    
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

#pragma mark - 获取用户完成的报表情况
- (void)getUserCompleteReportWithDate
{
    if (isUser) {
        NSMutableDictionary *param = [NSMutableDictionary dictionary];
        [param setObject:@"000000005e0dcf52015e135fd2b30005" forKey:@"id"];
        [param setObject:[Utils transformDateWithFormatter:@"yyyyMMdd" date:self.selectedDate] forKey:@"date"];
        
        [self requestWithHTTPMethod:POST urlString:requestUrl(getUserAllCompleteReport) parm:nil isHaveAlert:NO waitTitle:nil success:^(id dictData) {
            
        
            
        } failure:^(NSString *message) {
            
        }];
    }
}


#pragma mark - 获取报表信息
- (void)getReport:(NSTimer *)timer
{
    [timer invalidate];
    
    [self getAllreport];
}

#pragma mark - 监测网络状态,同步本地数据
- (void)haveNetWork{
    
    [[RemindAndGtasksDBManager shareManager] selectLoactionNotSyncRemindModelSuccess:^(BOOL ret, NSString *message) {
        
        RKLog(@"%@",message);
        _isRefreshRemind = NO;
        if (ret) {
            [self getTodayReminderDataWithDate:self.selectedDate type:1];
            //防止有新增的数据
            [self getAllRemindAndGtasksDate];
        }
        
    }];

    
    
    [[RemindAndGtasksDBManager shareManager] selectLoactionNotSyncGtaskslSuccess:^(BOOL ret, NSString *message) {
        
       _isRefreshGtsaks = NO;
        RKLog(@"%@",message);
        if (ret) {
            [self getTodayReminderDataWithDate:self.selectedDate type:2];
        }
    
    }];
    
}

#pragma mark - 解析报表数据
- (void)parseReportData:(NSArray *)data{
    
    [self.reportArray removeAllObjects];
    [_btnArray removeAllObjects];
    
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

            [Rmodel.result addObject:listModel];
        }
        
        [self.reportArray addObject:Rmodel];
        [_btnArray addObject:@"0"];
    }
    
    [self.tableView reloadData];
}





#pragma mark - 头部选择时间
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
        
        if (self.selectedDate == choseDate) {
            return ;
        }
        [self refreshTitleViewFrame:[Utils transformDate:choseDate dateFormatStyle:DateFormatYearMonthWithChinese]];//[Utils transformDateWithFormatter:@"yyyy年MM月" date:choseDate]];
        self.selectedDate = choseDate;
        [self refreshData];
        [self.calendar reloadData];
        
    } cancelBlock:^{
        
    }];
}


#pragma mark - Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    if (self.remindArray.count==0 && self.gatasksArray.count==0) {
        return self.reportArray.count;
    }else if (self.reportArray.count==0){
        return 2;
    }
    return 3;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    if (self.remindArray.count==0 && self.gatasksArray.count==0) {
        
        ReportModel *rModel = self.reportArray[section];
        ReportlistModel *listModel = rModel.result[0];
        // 如果为“1”则返回相应的行数，否则返回0
        if (rModel.result.count>1) {
            if ([listModel.reportId isEqualToString:@"1"]) {
                // 因为数组的第一位为标志位所以减1
                return ([rModel.result count] - 1);
            }
        }
        return 0;
    }else{
        if (section==0) {
            return self.gatasksArray.count;
        }else if (section==1){
            return self.remindArray.count;
        }else{
            return 1;
        }
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    
    if (self.remindArray.count==0 && self.gatasksArray.count==0) {
        return 45;
    }else{
        if (section==0) {
            if (self.gatasksArray.count==0) {
                return 1;
            }else{
                return 10;
            }
        }else if (section==1){
            if (self.remindArray.count==0) {
                return 1;
            }else{
                if (self.gatasksArray.count==0) {
                    return 8;
                }else{
                    return 9;
                }
            }
        }
        return 9;
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    if (self.remindArray.count==0 && self.gatasksArray.count==0) {
        return 0.0001;
    }
    
    return 1;
}


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    if (self.remindArray.count==0 && self.gatasksArray.count==0) {
    
        if (self.reportArray.count>0) {
            ReportModel *rModel = self.reportArray[section];
            UIView *view = [[UIView alloc]init];
            view.userInteractionEnabled = YES;
            view.tag = section;
            view.backgroundColor = [UIColor whiteColor];
            UILabel *titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, 0, kScreenWidth-30, 40)];
            titleLabel.themeMap = @{kThemeMapKeyColorName : normalText_main_color};
            titleLabel.font = threeClassTextFont;
            titleLabel.textAlignment = NSTextAlignmentLeft;
            titleLabel.text=[NSString stringWithFormat:@"%@",rModel.title];
            [view addSubview:titleLabel];
            
            UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(15, 44.5, kScreenWidth, 0.5)];
            lineView.backgroundColor = dt_line_color;
            [view addSubview:lineView];
            titleLabel.tag = section;
            titleLabel.userInteractionEnabled = YES;
            [view addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapHeader:)]];
            
            
            
            UIButton *tapBtn = [[UIButton alloc]initWithFrame:CGRectMake(kScreenWidth-15-17, 0, 17, 45)];
            [tapBtn setTitleColor:dt_text_main_color forState:UIControlStateNormal];
            [tapBtn setImage:[UIImage imageNamed:@"home_more_icon"] forState:UIControlStateNormal];
            [tapBtn setImage:[UIImage imageNamed:@"home_down_icon"] forState:UIControlStateSelected];
            tapBtn.tag = section;
            if ([_btnArray[section] isEqualToString:@"1"]) {
                tapBtn.selected = YES;
            }
            else{
                tapBtn.selected = NO;
            }
            tapBtn.userInteractionEnabled = NO;
            [view addSubview:tapBtn];
            return view;
        }else{
            return nil;
        }
        
        
    }else{
        UIView *view = [[UIView alloc]init];
        view.backgroundColor = dt_F2F2F2_color;
        return view;
    }
    
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    UIView *view = [[UIView alloc]init];
    view.backgroundColor = dt_F2F2F2_color;
    return view;
}


- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{
    
//    CGFloat cornerRadius = 10.f;
//    
//    cell.backgroundColor = UIColor.whiteColor;
//    
//    CAShapeLayer *layer = [[CAShapeLayer alloc] init];
//    
//    CGMutablePathRef pathRef = CGPathCreateMutable();
//    
//    CGRect bounds = CGRectInset(cell.bounds, 10, 0);
//    
//    BOOL addLine = NO;
//    
//    if (indexPath.row == 0 && indexPath.row == [tableView numberOfRowsInSection:indexPath.section]-1) {
//        
//        CGPathAddRoundedRect(pathRef, nil, bounds, cornerRadius, cornerRadius);
//        
//    } else if (indexPath.row == 0) {
//        
//        CGPathMoveToPoint(pathRef, nil, CGRectGetMinX(bounds), CGRectGetMaxY(bounds));
//        
//        CGPathAddArcToPoint(pathRef, nil, CGRectGetMinX(bounds), CGRectGetMinY(bounds), CGRectGetMidX(bounds), CGRectGetMinY(bounds), cornerRadius);
//        
//        CGPathAddArcToPoint(pathRef, nil, CGRectGetMaxX(bounds), CGRectGetMinY(bounds), CGRectGetMaxX(bounds), CGRectGetMidY(bounds), cornerRadius);
//        
//        CGPathAddLineToPoint(pathRef, nil, CGRectGetMaxX(bounds), CGRectGetMaxY(bounds));
//        
//        addLine = YES;
//        
//    } else if (indexPath.row == [tableView numberOfRowsInSection:indexPath.section]-1) {
//        
//        CGPathMoveToPoint(pathRef, nil, CGRectGetMinX(bounds), CGRectGetMinY(bounds));
//        
//        CGPathAddArcToPoint(pathRef, nil, CGRectGetMinX(bounds), CGRectGetMaxY(bounds), CGRectGetMidX(bounds), CGRectGetMaxY(bounds), cornerRadius);
//        
//        CGPathAddArcToPoint(pathRef, nil, CGRectGetMaxX(bounds), CGRectGetMaxY(bounds), CGRectGetMaxX(bounds), CGRectGetMidY(bounds), cornerRadius);
//        
//        CGPathAddLineToPoint(pathRef, nil, CGRectGetMaxX(bounds), CGRectGetMinY(bounds));
//        
//    } else {
//        
//        CGPathAddRect(pathRef, nil, bounds);
//        
//        addLine = YES;
//        
//    }
//    
//    layer.path = pathRef;
//    
//    CFRelease(pathRef);
//    
//    //颜色修改
//    
//    layer.fillColor = [UIColor colorWithWhite:1.f alpha:0.5f].CGColor;
//    
//    layer.strokeColor=[UIColor whiteColor].CGColor;
//    
//    if (addLine == YES) {
//        
//        CALayer *lineLayer = [[CALayer alloc] init];
//        
//        CGFloat lineHeight = (1.f / [UIScreen mainScreen].scale);
//        
//        lineLayer.frame = CGRectMake(CGRectGetMinX(bounds)+10, bounds.size.height-lineHeight, bounds.size.width-10, lineHeight);
//        
//        lineLayer.backgroundColor = tableView.separatorColor.CGColor;
//        
//        [layer addSublayer:lineLayer];
//        
//    }
//    
//    UIView *testView = [[UIView alloc] initWithFrame:bounds];
//    
//    [testView.layer insertSublayer:layer atIndex:0];
//    
//    testView.backgroundColor = UIColor.whiteColor;
//    
//    cell.backgroundView = testView;
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger section = indexPath.section;
    
    if (self.remindArray.count==0 && self.gatasksArray.count==0) {
        return 50;
    }else{
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
            RKLog(@"%f",kScreenHeight);
            
            RKLog(@"%f",CGRectGetHeight(self.view.frame));
            height = CGRectGetHeight(self.view.frame)-kTopBarHeight;
        }
        return height;
    }
}



- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    if (self.remindArray.count==0 && self.gatasksArray.count==0) {
        
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"reportID"];
        if (!cell) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"reportID"];
        }
        //为了防止重用
        for (UIView *view in cell.contentView.subviews) {
            [view removeFromSuperview];
        }
        
        if (self.reportArray.count>0) {
            ReportModel *Rmodel = self.reportArray[indexPath.section];
            ReportlistModel *listModel = Rmodel.result[indexPath.row+1];
            
            //背景
            
            UIView *bgView = [[UIView alloc]init];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell.contentView addSubview:bgView];
            [bgView mas_makeConstraints:^(MASConstraintMaker *make) {
                make.edges.mas_equalTo(UIEdgeInsetsMake(0, 0, 0, 0));
            }];
            
            UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(30, 0, 0, 0)];
            label.themeMap = @{kThemeMapKeyColorName : normalText_main_color};
            label.font = threeClassTextFont;
            label.textAlignment = NSTextAlignmentLeft;
            label.text = listModel.reportName;
            [bgView addSubview:label];
            
            [label mas_makeConstraints:^(MASConstraintMaker *make) {
                make.edges.mas_equalTo(UIEdgeInsetsMake(0, 30, 0.5, 15));
            }];
            
            //分割线
            UIView *lineView = [[UIView alloc]init];
            lineView.backgroundColor = dt_line_color;
            [bgView addSubview:lineView];
            
            [lineView mas_makeConstraints:^(MASConstraintMaker *make) {
                make.edges.mas_equalTo(UIEdgeInsetsMake(49.5, 30, 0, 0));
            }];
            
            if (indexPath.section==self.reportArray.count-1) {
                
                if (indexPath.row == Rmodel.result.count-2) {
                    lineView.hidden = YES;
                }
            }
        }
    
        return cell;
    }else{
        UITableViewCell *cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:nil];
        
        //为了防止重用
        for (UIView *view in cell.contentView.subviews) {
            
            [view removeFromSuperview];
        }
        
        if (indexPath.section==0) {
            
            GtaskListTableViewCell *gtaskCell = [tableView dequeueReusableCellWithIdentifier:@"gtaskCell"];
            
            if (!gtaskCell) {
                
                gtaskCell  = [[[NSBundle mainBundle]loadNibNamed:@"GtaskListTableViewCell" owner:self options:nil]firstObject];
            }
            gtaskCell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            if (indexPath.row==self.gatasksArray.count-1) {
                gtaskCell.lineViewLeftLayout.constant=0;
            }
            
            if (self.gatasksArray.count>0 && indexPath.row<self.gatasksArray.count) {
//                RKLog(@"%@",self.gatasksArray);
//                RKLog(@"%ld",self.gatasksArray.count);
//                RKLog(@"%ld",(long)indexPath.row);
                
                GtasksModel *gModel = self.gatasksArray[indexPath.row];
                
                [gtaskCell loadDataWith:gModel didSelectBtn:^(int buttonType,BOOL buttonStatus) {
                    
                    if (buttonType==1) {
                        
                        [self.gatasksArray removeObjectAtIndex:indexPath.row];
                        [self.tableView reloadData];
                        
                    }else{
                        
                    }
                }];
            }
            
            
            
            return gtaskCell;
            
            
        }else if (indexPath.section==1){
            
            RemindListTableViewCell *remindCell = [tableView dequeueReusableCellWithIdentifier:@"remindCell"];
            
            if (!remindCell) {
                
                remindCell  = [[[NSBundle mainBundle]loadNibNamed:@"RemindListTableViewCell" owner:self options:nil]firstObject];
            }
            remindCell.selectionStyle = UITableViewCellSelectionStyleNone;
            if (self.remindArray.count>0 && indexPath.row<self.remindArray.count) {
                
                ReminderModel *rModel = self.remindArray[indexPath.row];
                [remindCell loadDataWithReminderModel:rModel indexPath:indexPath remindArray:self.remindArray];
            }
           
            
            return remindCell;
        }
        else{
            
            if (self.reportArray.count>0) {
                ReportListTableView *reportView = [[ReportListTableView alloc]initWithFrame:CGRectMake(0, 0, 0, 0) dataArray:self.reportArray];
                reportView.delegate = self;
                [cell.contentView addSubview:reportView];
            }
        
        }
        return cell;
    }
}


#pragma mark - 编辑提醒,删除提醒,编辑待办,删除待办
#pragma mark - 查看报表详情
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (self.remindArray.count==0 && self.gatasksArray.count==0) {
        
        [self didSelectRowAtIndexPath:indexPath];
        
    }else{
       
        if (indexPath.section==0) {
            
            GtasksModel *gModel = self.gatasksArray[indexPath.row];
            
            ReminderAndGtasksDetailView *detailVC = [[ReminderAndGtasksDetailView alloc]init];
            
            @KKWeak(self);
            [detailVC setDetailViewWithModel:gModel type:2 clickBtnType:^(int buttonType) {
                @KKStrong(self);
                RKLog(@"%d",buttonType);
                
                if (buttonType==2) {
                    
                    GtasksViewController *vc = [[GtasksViewController alloc]init];
                    vc.gtaskModel = gModel;
                    vc.isEdit = YES;
                    vc.editGtask = ^(){
                        
//                        [self getTodayReminderDataWithDate:self.selectedDate type:2];
                        
                    };
                    vc.hidesBottomBarWhenPushed = YES;
                    [self.navigationController pushViewController:vc animated:YES];
                    
                }else{
                    
                    //删除待办
                    @KKWeak(self);
                    [[RemindAndGtasksDBManager shareManager] doSaveGtasksNetWorkWithGtasksModel:gModel editType:3 success:^(BOOL ret) {
                        @KKStrong(self);
                        
                        [self.gatasksArray removeObject:gModel];
                        [self refreshMainUI];
                    
                    }];
                    
                }
                
            }];
            
            [detailVC show];
        }
        
        if (indexPath.section==1) {

            ReminderModel *rModel = self.remindArray[indexPath.row];
            ReminderAndGtasksDetailView *detailVC = [[ReminderAndGtasksDetailView alloc]init];
            
            @KKWeak(self);
            [detailVC setDetailViewWithModel:rModel type:1 clickBtnType:^(int buttonType) {
                @KKStrong(self);
                RKLog(@"%d",buttonType);
                if (buttonType==2) {
                    ReminderViewController *vc = [[ReminderViewController alloc]init];
                    vc.reminderMode = rModel;
                    vc.isEdit = YES;
                    vc.updateReminder = ^(){
//                        //查询所有提醒的时间,用于标记在日历上
//                        [self getAllRemindAndGtasksDate];
//                        [self getTodayReminderDataWithDate:self.selectedDate type:1];
                    };
                    vc.hidesBottomBarWhenPushed = YES;
                    [self.navigationController pushViewController:vc animated:YES];
                    
                }else{
                    @KKWeak(self);
                    [[RemindAndGtasksDBManager shareManager] doSaveRemindWithRemindModel:rModel editType:3 success:^(BOOL ret) {
                        @KKStrong(self);
                        [self getAllRemindAndGtasksDate];
                        [self.remindArray removeObject:rModel];
                        [self refreshMainUI];
                    }];
                    
                }
                
            }];
            
            [detailVC show];
            
        }
        
    }
}


#pragma mark - 点击头部,展开子报表
- (void)tapHeader:(UITapGestureRecognizer *)tap{
    
    UIView *label = (UIView *)tap.view;
    long section = label.tag;
    ReportModel *rModel = self.reportArray[section];
    ReportlistModel *listModel = rModel.result[0];
    if ([listModel.reportId isEqualToString:@"0"]) {
        listModel.reportId = @"1";
        _btnArray[section] = @"1";
    }else{
        listModel.reportId = @"0";
        _btnArray[section] = @"0";
    }
    
    [self.tableView reloadData];
    
}

#pragma mark - ReportListTableViewDelegate(查看报表详情)
- (void)didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    ReportModel *rModel = self.reportArray[indexPath.section];
    ReportlistModel *listModel = rModel.result[indexPath.row+1];
    NSDictionary *dict = [NSDictionary dictionaryWithObject:listModel.reportId forKey:@"id"];
    [self requestWithHTTPMethod:POST urlString:requestUrl(showReportDetail) parm:dict isHaveAlert:NO waitTitle:nil success:^(id dictData) {
        
        WebViewController *webVC = [[WebViewController alloc]init];
        webVC.htmlUrl = (NSString *)dictData;
        webVC.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:webVC animated:YES];
        
    } failure:^(NSString *message) {
        
    }];
    
}

- (void)editActionsForRowAtIndexPath:(NSIndexPath *)indexPath btnType:(NSInteger)btnType{
    
    
    ReportModel *rModel = self.reportArray[indexPath.section];
    ReportlistModel *listModel = rModel.result[indexPath.row+1];
    //完成
    if (btnType==0) {
        
        listModel.completeId = [Utils getCurrentTimeWithDateStyle:DateFormatYearMonthDayHourMintesecondMillisecond];
        listModel.appUserId = UserID;
        listModel.isComplete = 1;
        listModel.completeDate = [Utils getCurrentTimeWithTimeFormat:@"yyyyMMddHHmm"];
        NSDictionary *param = [listModel mj_keyValues];
        [self requestWithHTTPMethod:POST urlString:requestUrl(userReportSave) parm:param isHaveAlert:NO waitTitle:nil success:^(id dictData) {
            
            
            
        } failure:^(NSString *message) {
           
            
            
        }];
        
        
    }else{
        
    }
    
}




#pragma mark - FSCalendarDelegate
- (NSString *)calendar:(FSCalendar *)calendar subtitleForDate:(NSDate *)date
{
    NSInteger day = [_lunarCalendar component:NSCalendarUnitDay fromDate:date];
    
    if ([_lunarChars[day-1] isEqualToString:@"初一"]) {
        NSInteger month = [_lunarCalendar component:NSCalendarUnitMonth fromDate:date];
        return _monthArr[month-1];
    }
    
    return _lunarChars[day-1];
    
}

//标记日历事件
- (NSInteger)calendar:(FSCalendar *)calendar numberOfEventsForDate:(NSDate *)date
{
    NSString *dateString = [self.dateFormatter2 stringFromDate:date];
    if ([self.datesWithEvent containsObject:dateString]) {
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
    
    UIColor *color = nil;
    
    //今天之前的日期
    if ([self.cldar yearOfDate:date] <= [self.cldar yearOfDate:today] && [self.cldar monthOfDate:date] <= [self.cldar monthOfDate:today] && [self.cldar dayOfDate:date] < [self.cldar dayOfDate:today]) {
        color =  [UIColor blackColor];
        
    }
    //今天的日期
    else if ([self.cldar yearOfDate:date] == [self.cldar yearOfDate:today] && [self.cldar monthOfDate:date] == [self.cldar monthOfDate:today] && [self.cldar dayOfDate:date] == [self.cldar dayOfDate:today]) {
        color =  [UIColor hex_colorWithHex:@"#29a1f7"];
    }
    //今天之后的日期
    else {
        color =  [UIColor blackColor];
    }
    
    //今天是否周末
    if ([self.cldar weekdayOfDate:date]==1 || [self.cldar weekdayOfDate:date] == 7){
        color =  dt_textLightgrey_color;
    }
    
    return color;
}

- (UIColor *)calendar:(FSCalendar *)calendar appearance:(FSCalendarAppearance *)appearance subtitleDefaultColorForDate:(NSDate *)date{
    
    
    NSDate *today = [NSDate date];
    if ([self.cldar weekdayOfDate:date]==1 || [self.cldar weekdayOfDate:date] == 7){
        return dt_textLightgrey_color;
    }
    
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

#pragma mark - 日历选择时间
- (void)calendar:(FSCalendar *)calendar didSelectDate:(NSDate *)date {
    
    if (self.selectedDate==date) {
        
        return;
    }
    
    self.selectedDate = date;
    //待办提醒
    [self getTodayReminderDataWithDate:self.selectedDate type:1];
    //用户报表
    [self getAllreport];
}

- (void)calendarCurrentPageDidChange:(FSCalendar *)calendar {
    
    RKLog(@"翻页了%@",calendar.currentPage);
    [self refreshTitleViewFrame:[Utils transformDateWithFormatter:@"yyyy年MM月" date:calendar.currentPage]];
}



#pragma mark - 查询待办,提醒
- (void)getTodayReminderDataWithDate:(NSDate *)date type:(int)type{
    
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        
        [self updateRemindAndGtasksWithDate:date type:type];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            // 通知主线程刷新 神马的
            [self refreshMainUI];
        });
        
    });
    
}

#pragma mark - 刷新主UI
- (void)refreshMainUI{
    
    if (self.remindArray.count==0 && self.gatasksArray.count==0) {
        
        [self.tableView mas_updateConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(self.calendarView.mas_bottom).offset(10);
        }];
    }else{
        [self.tableView mas_updateConstraints:^(MASConstraintMaker *make) {
            
            make.top.equalTo(self.calendarView.mas_bottom);
        }];
    }
    [self.view layoutIfNeeded];
    [self.tableView reloadData];
}


#pragma mark - 更新提醒和待办1:提醒2:待办
- (void)updateRemindAndGtasksWithDate:(NSDate *)date type:(int)type{
    
    if (type==1) {
        [self refreshRemindWithDate:date];
        
    }
    else if (type==2){
        [self refreshGtaskaWithDate:date];
    }
    else{
        [self refreshRemindWithDate:date];
        [self refreshGtaskaWithDate:date];
    }
    
}

#pragma mark - 刷新提醒列表
- (void)refreshRemindWithDate:(NSDate *)date{
    [self.remindArray removeAllObjects];
    //提醒
    [self.remindArray addObjectsFromArray:[[RemindAndGtasksDBManager shareManager] getRemindArrayWithDate:date]];
}

#pragma mark - 刷新待办列表
- (void)refreshGtaskaWithDate:(NSDate *)date{
    
    [self.gatasksArray removeAllObjects];
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

- (NSMutableArray *)datesWithEvent{
    if (!_datesWithEvent) {
        
        _datesWithEvent = [NSMutableArray array];
    }
    
    return _datesWithEvent;
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
    
    
    if ((self.remindArray.count>0 || self.gatasksArray.count>0) && self.reportArray.count>0) {
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
                NSLog(@"滑动到顶端");
                [[NSNotificationCenter defaultCenter] postNotificationName:kGoTopNotificationName object:nil userInfo:@{@"canScroll":@"1"}];
                _canScroll = NO;
            }
            if(_isTopIsCanNotMoveTabViewPre && !_isTopIsCanNotMoveTabView){
                NSLog(@"离开顶端");
                if (!_canScroll) {
                    scrollView.contentOffset = CGPointMake(0, tabOffsetY);
                }
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




- (void)appgotoBackground{
    
    NSTimeInterval period = 1.0; //设置时间间隔
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    _timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    dispatch_source_set_timer(_timer, dispatch_walltime(NULL, 0), period * NSEC_PER_SEC, 0); //每秒执行
    // 事件回调
    dispatch_source_set_event_handler(_timer, ^{
        
        NSLog(@"Count");
        
        //初始化
        UILocalNotification * localNotification = [[UILocalNotification alloc] init];
        
        //设置开火时间(演示为当前时间5秒后)
        localNotification.fireDate = [NSDate dateWithTimeIntervalSinceNow:20];
        
        //设置时区，取手机系统默认时区
        localNotification.timeZone = [NSTimeZone defaultTimeZone];
        
        //重复次数 kCFCalendarUnitEra为不重复
        localNotification.repeatInterval = 0;
        
        //通知的主要内容
        localNotification.alertBody = @"人生苦短，我用Objcetive-C";
        
        //小提示
        localNotification.alertAction = @"查看详情";
        
        //设置音效，系统默认为电子音，在系统音效中标号为1015
        localNotification.soundName = UILocalNotificationDefaultSoundName;
        
        //or localNotification.soundName = @"send.caf" 自己的音频文件
        
        //localNotification.applicationIconBadgeNumber = 1; Icon上的红点和数字
        
        //查找本地系统通知的标识
        localNotification.userInfo = @{@"notificationID": [Utils getCurrentTimeWithTimeFormat:@"yyyyMMddHHmmssSSS"]};
        
        //提交到系统服务中，系统限制一个APP只能注册64条通知，已经提醒过的通知可以清除掉
        /**
         *64条是重点，必需mark一下
         */
        [[UIApplication sharedApplication] scheduleLocalNotification:localNotification];
        
        RKLog(@"%@",[[LocalNotificationManager shareManager] queryAllSystemNotifications]);
        
        
        //        dispatch_async(dispatch_get_main_queue(), ^{
        //
        //        });
    });
    
    // 开启定时器
    dispatch_resume(_timer);
    
}

- (void)appgotoBecomeActive{
    
    if (_timer) {
        dispatch_source_cancel(_timer);
        _timer = nil;
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
