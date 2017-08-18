//
//  GtasksModel.m
//  RikingCalendar
//
//  Created by jinglun liang on 2017/8/2.
//  Copyright © 2017年 liangjinglun. All rights reserved.
//

#import "GtasksModel.h"
#import "LKDBTool.h"
@implementation GtasksModel

+ (NSDictionary *)describeColumnDict{
   
    LKDBColumnDes *account = [LKDBColumnDes new];
    account.primaryKey = YES;
    account.columnName = @"todo_id";
    
    LKDBColumnDes *name = [[LKDBColumnDes alloc] initWithgeneralFieldWithAuto:NO unique:NO isNotNull:YES check:nil defaultVa:nil];
    
    LKDBColumnDes *noField = [LKDBColumnDes new];
    noField.useless = YES;
    
    return @{@"todoId":account,@"name":name,@"noField":noField};
}



- (void)setValue:(id)value forUndefinedKey:(NSString *)key  {
    if([key isEqualToString:@"todo_id"]){
        
        self.todoId = value;
        NSLog(@"这是一个id关键字");
    }
    
}

@end
