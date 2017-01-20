//
//  EmailShare.h
//  RNShare
//
//  Created by Diseño Uno BBCL on 23-07-16.
//  Copyright © 2016 Facebook. All rights reserved.
//


#import <UIKit/UIKit.h>
#import "RCTConvert.h"
#import "RCTBridge.h"
#import "RCTUIManager.h"
#import "RCTLog.h"
#import "RCTUtils.h"
#import <MessageUI/MessageUI.h>
@interface EmailShare : NSObject

- (void)shareSingle:(NSDictionary *)options
 fromViewController:(UIViewController *)fromViewController
   composerDelegate:(id<NSObject>)composerDelegate
    failureCallback:(RCTResponseErrorBlock)failureCallback;
@end
