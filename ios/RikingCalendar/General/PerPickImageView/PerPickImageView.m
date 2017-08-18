//
//  PerPickImageView.m
//  LawApp_Test
//
//  Created by IOS8 on 15/12/1.
//  Copyright © 2015年 gyy. All rights reserved.
//

#import "PerPickImageView.h"
#import "AppDelegate.h"

@implementation PerPickImageView

/**
 *  头像，和性别view
 *
 *  @param ID 用来区分是编辑头像还是编辑性别
 *
 *  @return view
 */
-(id)initWithId:(int)ID{
    self = [super init];
    if (self) {

        self.partUD = ID;
        //初始化背景视图，添加手势
        self.frame = CGRectMake(0, 0, kScreenSize.width, kScreenSize.height);
        self.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:.4];
        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGesture)];
        [self addGestureRecognizer:tapGesture];
        
        self.backGroundView = [[UIView alloc] initWithFrame:CGRectMake(0, ([UIScreen mainScreen].bounds.size.height), kScreenSize.width, 0)];
        self.backGroundView.backgroundColor = [UIColor whiteColor];

        UIButton *cameraBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        cameraBtn.frame = CGRectMake(0, 0, kScreenSize.width, 49);
        cameraBtn.backgroundColor = [UIColor whiteColor];
        cameraBtn.tag = 1;
        [cameraBtn setTitle:ID==1?@"拍照":@"男" forState:UIControlStateNormal];
        if (ID==3) {
            [cameraBtn setTitle:@"周一" forState:UIControlStateNormal];
        }
        [cameraBtn setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
        [cameraBtn addTarget:self action:@selector(selectPerHeaderIcon:) forControlEvents:UIControlEventTouchUpInside];
        
        UIButton *photoBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        photoBtn.frame = CGRectMake(0, 50, kScreenSize.width, 49);
        photoBtn.tag = 2;
        photoBtn.backgroundColor = [UIColor whiteColor];
        [photoBtn setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
        [photoBtn setTitle:ID==1?@"从相册选择":@"女" forState:UIControlStateNormal];
        if (ID==3) {
            [photoBtn setTitle:@"周日" forState:UIControlStateNormal];
        }
        [photoBtn addTarget:self action:@selector(selectPerHeaderIcon:) forControlEvents:UIControlEventTouchUpInside];
        
        UIButton *cancelBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        cancelBtn.frame = CGRectMake(0, 100, kScreenSize.width, 49);
        cancelBtn.tag = 3;
        cancelBtn.backgroundColor = [UIColor whiteColor];
        [cancelBtn setTitleColor:RGBA(73, 154, 242, 1) forState:UIControlStateNormal];
        [cancelBtn setTitle:@"取消" forState:UIControlStateNormal];
        [cancelBtn addTarget:self action:@selector(selectPerHeaderIcon:) forControlEvents:UIControlEventTouchUpInside];
        
        UIView *lineVi1 = [[UIView alloc] initWithFrame:CGRectMake(10, 49, kScreenSize.width-20, 1)];
        lineVi1.backgroundColor = RGBA(230, 230, 230, 1);
        UIView *lineVi2 = [[UIView alloc] initWithFrame:CGRectMake(10, 99, kScreenSize.width-20, 1)];
        lineVi2.backgroundColor = RGBA(230, 230, 230, 1);
        
        
        [self addSubview:self.backGroundView];
        [self.backGroundView addSubview:lineVi1];
        [self.backGroundView addSubview:lineVi2];
        [self.backGroundView addSubview:photoBtn];
        [self.backGroundView addSubview:cancelBtn];
        [self.backGroundView addSubview:cameraBtn];
        [UIView animateWithDuration:0.25 animations:^{
            [self.backGroundView setFrame:CGRectMake(0, kScreenSize.height - 153, kScreenSize.width, 153)];
//            self.backGroundView = [UIColor redColor];
        } completion:^(BOOL finished) {
            
        }];
    }
    
    return self;
}

- (void)selectPerHeaderIcon:(UIButton *)btn
{
    if (self.partUD == 1) {
        //     创建图片选择器
        self.picker = [[UIImagePickerController alloc] init];
        self.picker.delegate = self;
        // 打开图片选择器对象的可编辑模式;
//        self.picker.allowsEditing = YES;
        if (btn.tag == 1) {
            
            if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
                // 打开摄像机
                self.picker.sourceType = UIImagePickerControllerSourceTypeCamera;
                [self.delegate jumpOtherView:self.picker];
                [self hideBackView];
            }else{
                [Utils showMsg:@"相机不可用"];
            }
            
        }else if (btn.tag == 2){
            
            if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeSavedPhotosAlbum])
            {
                self.picker.sourceType = UIImagePickerControllerSourceTypeSavedPhotosAlbum;
                [self.delegate jumpOtherView:self.picker];
                [self hideBackView];
            }else{
                [Utils showMsg:@"相册不可用"];
            }
        }else{
            
            [self tapGesture];
        }

    }else if(self.partUD==2){
    
        if (btn.tag != 3) {
            [self.delegate selectImage:btn.titleLabel.text];
        }
        [self tapGesture];
    }else{
        if (btn.tag != 3) {
            [self.delegate selectImage:btn.titleLabel.text];
        }
        [self tapGesture];
    }
}

// 点击取消按钮
- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    // 模态回去
    [picker dismissViewControllerAnimated:YES completion:nil];
    [self tapGesture];
}

// 点击图片调的方法
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:
(NSDictionary *)info
{
    UIImage *icon = nil;
    if (picker.allowsEditing) {
        
        icon = [info objectForKey:UIImagePickerControllerEditedImage];
    }else
        // 返回原始的图片
    {
        icon = [info objectForKey:UIImagePickerControllerOriginalImage];
    }

    [self.delegate selectImage:icon];
    [picker dismissViewControllerAnimated:YES completion:nil];
    [self tapGesture];
}

- (void)hideBackView
{
    self.hidden = YES;
}

- (void)tapGesture
{
    [UIView animateWithDuration:0.25 animations:^{
        [self.backGroundView setFrame:CGRectMake(0, [UIScreen mainScreen].bounds.size.height, [UIScreen mainScreen].bounds.size.width, 0)];
        self.alpha = 0;
    } completion:^(BOOL finished) {
        if (finished) {
            [self removeFromSuperview];
        }
    }];

}

- (void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated

{
    
    viewController.navigationItem.leftBarButtonItem.tintColor = [UIColor whiteColor];
    viewController.navigationItem.rightBarButtonItem.tintColor = [UIColor whiteColor];
    
    int controllerIndex = (int)[navigationController.viewControllers indexOfObject:viewController];
    
    if (controllerIndex > 0)
    {
        [navigationController.navigationBar setHidden:YES];
    }else{
        [navigationController.navigationBar setHidden:NO];
    }
    
//    NSLog(@"%@",navigationController.view.subviews);
//    
//    
//    UIButton *cancelBtn = [[UIButton alloc]initWithFrame:CGRectMake(0,0,50,30)];
//    
//    [cancelBtn setTitle:@"取消" forState:(UIControlStateNormal)];
//    
//    [cancelBtn addTarget:self action:@selector(click) forControlEvents:(UIControlEventTouchUpInside)];
//    
//    UIBarButtonItem *btn = [[UIBarButtonItem alloc] initWithCustomView:cancelBtn];
//    
//    [viewController.navigationItem setRightBarButtonItem:btn animated:NO];
    
}

- (void)click{
    [_picker dismissViewControllerAnimated:YES completion:nil];
    [self tapGesture];
}

-(void)showInView
{
    AppDelegate *app = (AppDelegate*)[UIApplication sharedApplication].delegate;
    [app.window addSubview: self];
}

- (void)dealloc
{
    self.picker.delegate = nil;
    self.delegate = nil;

}

@end
