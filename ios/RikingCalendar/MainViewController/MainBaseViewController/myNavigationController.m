//
//  myNavigationController.m
//  KakaPersonal
//
//  Created by Aaron on 2017/3/16.
//  Copyright © 2017年 kakayun. All rights reserved.
//

#import "myNavigationController.h"
#import "MYNavigationControllerShouldPopProtocol.h"

static NSString *const kOriginDelegate = @"kOriginDelegate";

@interface UINavigationController (UINavigationControllerNeedshouldPopItem)

- (BOOL)navigationBar:(UINavigationBar *)navigationBar shouldPopItem:(UINavigationItem *)item;
- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer;
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch;
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer;
@end

#pragma clang diagnostic push
#pragma clang diagnostic ignored"-Wincomplete-implementation"
@implementation UINavigationController (UINavigationControllerNeedshouldPopItem)
@end
#pragma clang diagnostic pop


@interface myNavigationController ()<UINavigationBarDelegate,UIGestureRecognizerDelegate>

@end

@implementation myNavigationController



- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    objc_setAssociatedObject(self,
                             [kOriginDelegate UTF8String],
                             self.interactivePopGestureRecognizer.delegate,
                             OBJC_ASSOCIATION_ASSIGN);
    
    self.interactivePopGestureRecognizer.delegate = (id<UIGestureRecognizerDelegate>)self;
    
}

-(BOOL)navigationBar:(UINavigationBar *)navigationBar shouldPopItem:(UINavigationItem *)item
{
    UIViewController *vc = self.topViewController;
    
    if (item != vc.navigationItem) {
        return [super navigationBar:navigationBar shouldPopItem:item];
    }
    
    if ([vc conformsToProtocol:@protocol(MYNavigationControllerShouldPopProtocol)]) {
        
        if ([(id<MYNavigationControllerShouldPopProtocol>)vc my_navigationControllerShouldPopWhenSystemBackBtnSelected:self]) {
            
            return [super navigationBar:navigationBar shouldPopItem:item];
            
        }else
        {
            return NO;
        }
        
    }else
    {
        return [super navigationBar:navigationBar shouldPopItem:item];
    }
    
}


- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    
    if (gestureRecognizer == self.interactivePopGestureRecognizer) {
        
        id<UIGestureRecognizerDelegate> originDelegate = objc_getAssociatedObject(self, [kOriginDelegate UTF8String]);
        
        return  [originDelegate gestureRecognizer:gestureRecognizer shouldReceiveTouch:touch];
        
    }
    
    return YES;
}


- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer
{
   
    if (gestureRecognizer == self.interactivePopGestureRecognizer) {
        
        id<UIGestureRecognizerDelegate> originDelegate = objc_getAssociatedObject(self, [kOriginDelegate UTF8String]);
        
        return [originDelegate gestureRecognizer:gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:otherGestureRecognizer];
    }
    
    return YES;
}


- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer
{
    if (gestureRecognizer == self.interactivePopGestureRecognizer) {
        
        UIViewController *vc = self.topViewController;
        
        if ([vc conformsToProtocol:@protocol(MYNavigationControllerShouldPopProtocol)]) {
            if (![(id<MYNavigationControllerShouldPopProtocol>)vc my_navigationControllerShouldPopWhenGestureRecognizer:self]) {
                
                return NO;
            }
        }
        
        id<UIGestureRecognizerDelegate> originDelegate = objc_getAssociatedObject(self, [kOriginDelegate UTF8String]);
        
        return [originDelegate gestureRecognizerShouldBegin:gestureRecognizer];
        
    }
    
    return YES;
}




-(BOOL)shouldAutorotate{
    return self.topViewController.shouldAutorotate;
}
//支持的方向
- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return self.topViewController.supportedInterfaceOrientations;
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
