//
//  PerPickImageView.h
//  LawApp_Test
//
//  Created by IOS8 on 15/12/1.
//  Copyright © 2015年 gyy. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol PerPickViewDelegate <NSObject>

- (void)jumpOtherView:(UIImagePickerController *)object;
- (void)selectImage:(id)icon;
@end

@interface PerPickImageView : UIView<UIImagePickerControllerDelegate,UINavigationControllerDelegate>
{
    UIToolbar* toolBar;
}
@property (nonatomic,unsafe_unretained) id<PerPickViewDelegate>delegate;
@property (nonatomic,strong) UIView *backGroundView;
@property (nonatomic,assign) int partUD;
@property (nonatomic,strong) UIImagePickerController *picker;
-(id)initWithId:(int)ID;
-(void)showInView;
//- (void)removeSelfView;
@end
