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

#define NMWCalendarTop 0.0
#define NMWCalendarBottom 0.0

#define NMWCalendarViewWeekHeight 35.0

@interface HomeViewController ()
<
UITableViewDelegate,
UITableViewDataSource,
FSCalendarDelegate,
FSCalendarDataSource,
FSCalendarDelegateAppearance
>

@property (nonatomic,strong) FSCalendar *calendar;
@property (strong, nonatomic) NSCalendar *cldar;


@property (strong, nonatomic) NSMutableArray *items;
@property (strong, nonatomic) UITableView *tableView;
@property (strong, nonatomic) NSDate *selectedDate;

@property (strong, nonatomic) UIView *calendarView;
@property (strong, nonatomic) MASConstraint *calendarViewHeight;
@property (assign, nonatomic) CGFloat calendarHeight;

@property (strong, nonatomic) NSDateFormatter *dateFormatter;
@property (strong, nonatomic) NSDateFormatter *dateFormatter2;
@property (strong, nonatomic) NSDate *minimumDate;
@property (strong, nonatomic) NSDate *maximumDate;


@property (strong, nonatomic) NSCalendar *lunarCalendar;

@property (strong, nonatomic) NSArray<NSString *> *lunarChars;
@property (strong, nonatomic) NSArray *datesWithEvent;
@property (strong, nonatomic) NSCache *cache;

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
    // Do any additional setup after loading the view.
    
    [self setRightButtonWithTitle:@[@"今天"]];
    
    
    self.view.themeMap = @{kThemeMapKeyColorName:@"view_backgroundColor"};
    self.dateFormatter = [[NSDateFormatter alloc] init];
    self.dateFormatter.dateFormat = @"yyyy-MM-dd";
    
    
    [self.view addSubview:self.calendarView];
    [self.calendarView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.view).offset(0);
        make.left.right.equalTo(self.view);
    }];
    
    [self.view insertSubview:self.tableView belowSubview:self.calendarView];
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.calendarView.mas_bottom);
        make.left.right.bottom.equalTo(self.view);
    }];
    
}

- (void)doRightAction:(UIButton *)sender{
    [self.calendar setCurrentPage:[NSDate date] animated:YES];
}



- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, kScreenHeight) style:UITableViewStylePlain];
        _tableView.dataSource = self;
        _tableView.delegate = self;
        _tableView.backgroundColor = [UIColor whiteColor];
        _tableView.keyboardDismissMode = UIScrollViewKeyboardDismissModeOnDrag;
        _tableView.tableFooterView = [UIView new];
        _tableView.separatorColor = [UIColor lightGrayColor];
    }
    return _tableView;
}

- (FSCalendar *)calendar {
    if (!_calendar) {
        _calendar = [[FSCalendar alloc] init];
        _calendar.frame = CGRectMake(15, 2, kScreenWidth-30, 325);
        _calendar.dataSource = self;
        _calendar.delegate = self;
        _calendar.appearance.weekdayTextColor = [UIColor hex_colorWithHex:@"#b6b6b6"];
        _calendar.appearance.weekdayFont = sixClassTextFont;
        _calendar.appearance.titleFont = threeClassTextFont;
        _calendar.appearance.subtitleFont = eightClassTextFont;
        _calendar.backgroundColor = [UIColor orangeColor];
        _calendar.placeholderType = FSCalendarPlaceholderTypeNone;
        _calendar.appearance.caseOptions = FSCalendarCaseOptionsHeaderUsesUpperCase|FSCalendarCaseOptionsWeekdayUsesSingleUpperCase;
        _calendar.scrollDirection = UICollectionViewScrollDirectionHorizontal;
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
        _calendarView.layer.shadowColor = RGBCOLOR(234, 237, 247).CGColor;
        _calendarView.layer.shadowOffset = CGSizeMake(0, 5);
        _calendarView.layer.shadowOpacity = 0.8;
        
        [_calendarView addSubview:self.calendar];
        [self.calendar mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(UIEdgeInsetsMake(NMWCalendarTop, 0, NMWCalendarBottom, 0));
            _calendarViewHeight = make.height.mas_equalTo(NMWCalendarViewWeekHeight*7);
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


#pragma mark - Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of rows in the section.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    // Return the number of rows in the section.
    return 20;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 50;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cellID"];
    if (!cell) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cellID"];
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
   
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
    
}

- (void)calendarCurrentPageDidChange:(FSCalendar *)calendar {
    
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
